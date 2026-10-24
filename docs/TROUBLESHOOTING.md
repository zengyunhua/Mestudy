# Troubleshooting

Symptoms first, causes second. Each entry lists what you see, why it happens, and what to
do.

- [Build problems](#build-problems)
- [Startup problems](#startup-problems)
- [Runtime problems](#runtime-problems)
- [Data and encoding problems](#data-and-encoding-problems)
- [Known code traps](#known-code-traps)

---

## Build problems

### Build fails to resolve dependencies

```
Could not resolve dependencies for project com.tecsun.sisp:...
Failed to read artifact descriptor ... 192.168.1.8:8686
```

**Cause.** Every `pom.xml` declares an internal Nexus repository
(`http://192.168.1.8:8686/...` or `http://11.11.11.8:8686/...`). Those hosts are only
reachable inside the original corporate network.

**Fix.** Pick one:

1. Build only the modules whose dependencies resolve from Maven Central —
   `InterfaceFunction` and `NetPortalManagement` are the most self-contained.
2. Mirror the repository. In `~/.m2/settings.xml`:

   ```xml
   <mirrors>
     <mirror>
       <id>internal</id>
       <mirrorOf>*</mirrorOf>
       <url>https://your-nexus/repository/maven-public/</url>
     </mirror>
   </mirrors>
   ```

3. Vendor the jars into `src/main/webapp/WEB-INF/lib/` and remove the dependency
   declaration. Fine for local experiments; do not submit it as a patch.

### `mvn` compiles with warnings about source/target 1.7

`maven-compiler-plugin` pins `source`/`target` to `1.7` in some modules. On JDK 9+ the
build still works but emits a deprecation warning. Leave it alone unless you are
deliberately modernising the module.

### Java 11+ `IllegalAccessError` from Axis

```
java.lang.IllegalAccessError: class org.apache.axis... cannot access class com.sun...
```

**Cause.** Axis 1.x uses reflection into JDK internals that the module system blocks.

**Fix.** Run on JDK 8. If you must use JDK 11+, add to the container's startup options:

```
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
```

Expect further failures; JDK 8 remains the supported runtime.

---

## Startup problems

### Application deploys but every request returns 500

The root Spring context failed to load, but the container only logged it.

**Look for** `BeanCreationException` or `Could not resolve placeholder` in
`catalina.out`. The usual cause is a missing `jdbc.*` key: `applicationContext.xml`
declares eight datasources in `IFaceSuportService`, and **every one** must have
`driverClassName`, `url`, `username` and `password` present, even if you point several at
the same schema.

**Fix.** Copy `config.properties.example` and fill in all datasources, or comment out the
unused datasource beans *and* the `SqlSessionFactory`/`MapperScannerConfigurer` pairs
that reference them.

### Startup hangs or fails on Redis

`StartupListener.contextInitialized()` calls `DataBase.init()`. If Redis is unreachable,
the listener throws and deployment fails.

**Fix.** Start Redis, or check the `redis_core_database` format — it is `host_port`, not
a URL:

```properties
redis_core_database=127.0.0.1_6379
```

A value like `redis://127.0.0.1:6379` will not parse.

### `NoSuchBeanDefinitionException` for a service

**Cause.** `springMVC-servlet.xml` scans only `@Controller`:

```xml
<context:component-scan base-package="...controller" use-default-filters="false">
    <context:include-filter type="annotation"
        expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
```

`@Service` and `@Repository` beans come from `applicationContext.xml` instead. If a
service is missing, check whether it is in a package the root context scans.

**Fix.** Put services in the package scanned by the root context, or add an explicit
`<bean>` declaration.

---

## Runtime problems

### Paged query returns an empty list but the database has rows

Three candidates, in order of likelihood:

1. **Mapper namespace mismatch.** The `namespace` in `resources/mappings/*.xml` must equal
   the DAO interface's fully-qualified name. A mismatch makes the binding fail silently
   for that statement.
2. **Wrong datasource.** The DAO is bound to a `MapperScannerConfigurer` for a different
   `SqlSessionFactory` than the schema you populated.
3. **Oracle `NULL` in a filter.** `WHERE col != 'x'` excludes rows where `col IS NULL`.
   Use `WHERE (col IS NULL OR col != 'x')` when the column is nullable.

### Token rejected / intermittent 401

The token TTL is `redis_iface_expx` **minutes** (multiplied by 60 internally), while the
client renews every `tokenTimeOut` minutes. If TTL < renewal interval, the token expires
between renewals.

**Fix.** Keep `redis_iface_expx` at or above `tokenTimeOut`. Note that `tokenTimeOut`
values `<= 60` are silently raised to `60` by `ClientInit`, so the effective renewal
interval is at least one hour — `redis_iface_expx=60` matches that exactly and leaves no
margin. Give it headroom.

### Redis errors mask themselves

`JedisUtil` calls `returnBrokenResource(jedis)` in its `catch` block and then
`returnResource(jedis)` on the same instance in `finally`. Jedis 2.x rejects the second
return with `IllegalStateException("Returned object not currently part of this pool")`,
which is thrown from `finally` and **replaces** the real exception.

Worse, if `DataBase.init()` never ran, the pool reference is `null` and the `catch` block
throws `NullPointerException` before the original exception can propagate.

**How to diagnose.** Look at the first exception in the `cause` chain, not the one in the
stack trace header. If you see `NullPointerException` from `JedisUtil`, check whether
`StartupListener` completed.

**Fix.** Restructure the try/catch/finally so exactly one return happens, and guard the
pool reference:

```java
JedisPool pool = DataBase.getJedisPublicPool();
Jedis jedis = null;
boolean broken = false;
try {
    jedis = pool.getResource();
    jedis.setex(key, 60 * outTime, value);
} catch (Exception e) {
    broken = true;
    throw new Exception("redis set failed: key=" + key, e);
} finally {
    if (jedis != null) {
        if (broken) { pool.returnBrokenResource(jedis); }
        else        { pool.returnResource(jedis); }
    }
}
```

### `redis_iface_expx` missing causes a `NumberFormatException` on first write

`JedisUtil.setValue` does `Integer.parseInt(Config.get("redis_iface_expx"))` with no null
check. The application starts fine and fails on the first cached value. Always define the
key.

### Bean fails on first use, not at startup

`applicationContext.xml` sets `default-lazy-init="true"` and
`ignore-unresolvable="true"`. Together these mean an undefined property placeholder stays
as the literal string `${...}` and the bean is only created when something asks for it.

Symptom: the application deploys cleanly, then throws on the first request that touches
that bean.

**Fix.** Define every key the context references. `IFaceSuperService_hn` ships an
incomplete `config.properties` for exactly this reason — see
[CONFIGURATION.md](CONFIGURATION.md#datasources).

### c3p0 pool exhausted under load

`jdbc.maxPoolSize=300` applies **per datasource**, and `IFaceSuportService` declares
eight. Worst case is 2400 Oracle connections.

**Fix.** Lower `maxPoolSize` to something proportional to the Oracle session limit, and
reduce `minPoolSize` on rarely used schemas.

### Electronic signature fails at signing time

`signpdf.font.path` points at a TTF that must exist **on the application host**:

```properties
signpdf.font.path=/usr/share/fonts/winfonts/ARIALUNI.TTF
```

A missing font throws only when a document is signed, not at startup. Verify the path
inside the container, not on your workstation.

---

## Data and encoding problems

### Chinese characters come back as `???` or mojibake

Check all four layers — fixing one is not enough:

1. **Source files.** `pom.xml` sets `project.build.sourceEncoding=UTF8`; your editor must
   save as UTF-8 without BOM.
2. **Request encoding.** `CharacterEncodingFilter` with `forceEncoding=true` is declared
   in `web.xml`. If it is missing in a module, add it.
3. **Response encoding.** `@RequestMapping(produces = "application/json;charset=UTF-8")`,
   or set the content type explicitly.
4. **Database.** Oracle `NLS_CHARACTERSET` must support Chinese (usually `AL32UTF8` or
   `ZHS16GBK`). If the DB charset is GBK, do not send UTF-8 literals in SQL.

### Numbers arrive as strings

`statusCode` is a `String` (`"200"`, not `200`). Comparing with `==` or `200` will always
fail. Use `"200".equals(statusCode)`.

### Oracle connection string rejected

Two URL forms are in use and are **not** interchangeable:

```
jdbc:oracle:thin:@host:1521:orcl     # SID
jdbc:oracle:thin:@host:1521/orcl     # service name
```

Keep the form the original configuration used. Switching requires the matching listener
on the server side.

---

## Known code traps

These are real issues in the current tree, documented so you do not lose an afternoon to
them.

### `ResultVerify` shadows its parent's fields

`ResultVerify extends Result` but re-declares `statusCode`, `message` and `data`. Two
copies of each field exist. Depending on which static type serialises the object, JSON
output can contain duplicate keys, and a value set through the parent's setter may not be
read through the child's getter.

**Workaround.** Always construct `ResultVerify` with its own constructor, never by
upcasting a `Result`.

### `iinner_proxy_ip` typo

`NetworkOfficeService/config.properties` defines `iinner_proxy_ip` (double *i*). Nothing
in that module reads it today, so it is inert — but it will silently return `null` if
someone adds an internal call and copies the existing key name.

### `Config.get()` returns `null`, never throws

The per-module `Config` singleton does not fail on a missing key, while Spring's
`property-placeholder` does. A typo therefore produces an empty value at runtime instead
of a startup error. Grep the key name before assuming the properties file is wrong.

### Committed build output

`target/` directories were historically tracked (128 `.class` files, 83 `.jar` files).
`.gitignore` now excludes them, but existing clones still carry them:

```bash
git rm -r --cached */target
git commit -m "chore: stop tracking build output"
```

### XSS filter is disabled

`BusinessFilter` is declared in `web.xml` but the whole block is commented out. Do not
assume input is escaped. See [SECURITY.md](../SECURITY.md).
