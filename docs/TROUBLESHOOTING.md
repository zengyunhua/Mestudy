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
Could not resolve dependencies for project com.tecsun.sisp:NetPortalManagement:...
The following artifacts could not be resolved:
  bee:bee.cloud:jar:1.0.4, bee:bee.cloud.service:jar:1.0.4,
  bee:bee.cloud.impl:jar:1.0.0, bee:bee.cloud.entity:jar:1.0.0,
  bee:xx.tool:jar:1.0.1, com.tecsun.sisp_public:aamanagement-client:jar:1.0.5
```

**Cause.** The default reactor no longer contains the one module that needs them, so this
only appears when you opt in with `-Pprivate-deps`. `NetPortalManagement` depends on
`bee.cloud`, the internal persistence framework, which was only ever published to an
internal Nexus. Nothing in this repository can replace it — the code really does call
into it, so deleting the `<dependency>` block just moves the failure to compile time.

> **A dependency can be used without a single `import`.** This is the trap that makes
> these artifacts look dead. `com.tecsun.sisp_public:aamanagement-client` is never
> imported by a Java file in `NetPortalManagement`, yet the module cannot start without
> it: `src/main/webapp/WEB-INF/web.xml` names
> `com.tecsun.sisp_public.aam.client.AAMClient4Jersey` as an active Jersey filter, and
> the container resolves that class by string. `bee.cloud.service` is referenced the same
> way through `bee.cloud.service.listener.StartupListener`. Before deleting a
> `<dependency>`, grep the resources as well as the sources — and check whether the
> `web.xml` block that mentions it is actually commented out. In `InterfaceFunction` and
> `NetworkOfficeService` the equivalent filters existed only in comments (now
> removed), which is why those modules could drop the declaration.

**Fix.** Pick one:

1. **Do nothing.** The default reactor already excludes that module, so `mvn package`
   at the root builds and passes. This is what CI does.
2. Install the jars you already have into your local repository, then build with the
   profile:

   ```bash
   mvn install:install-file -Dfile=bee.cloud-1.0.4.jar \
       -DgroupId=bee -DartifactId=bee.cloud -Dversion=1.0.4 -Dpackaging=jar

   mvn -B -Pprivate-deps package
   ```

   Repeat for `bee:bee.cloud.service`, `bee:bee.cloud.impl` and `bee:bee.cloud.entity`.
3. If nothing in the sources references the artifact, delete the `<dependency>` block.
   That is how `unieap-dataexchange-client` was removed, and how
   `com.tecsun.sisp_public:aamanagement-client` and `bee:xx.tool` were removed from
   `InterfaceFunction` and `NetworkOfficeService` — neither module imports them, and both
   now build in the default reactor. Check before deleting: `NetPortalManagement` really
   does use `bee.cloud` (`@TableName`, `@Column`, `Doe`, `Cnd`, `Select`).

> **A missing class is not a missing jar.** Removing the unused coordinates from
> `NetworkOfficeService` exposed `BanjianVo`, which four classes and
> `mappings/draftsDAO.xml` refer to but which was never published. Deleting a
> `<dependency>` does not remove a symbol the source uses — it only reveals it. That
> class has since been reconstructed from the columns the mapper proves exist; see its
> Javadoc for the provenance before trusting the JSON payload.

> **Historical note.** Earlier revisions of this repository hard-coded the Nexus URLs
> (`192.168.1.8:8686`, `11.11.11.8:8686`) directly in every POM, which made a public
> clone fail immediately. Mirrors belong in `~/.m2/settings.xml`, not in a project POM.

### `InvokeUtil.java` fails with "illegal character: '\ufeff'"

```
InvokeUtil.java:[1,1] illegal character: '\ufeff'
InvokeUtil.java:[1,10] class, interface or enum expected
```

**Cause.** The file was saved with a UTF-8 BOM. `javac` does not strip a BOM and treats
the three bytes as source characters.

**Fix.** Strip the BOM. On Linux/macOS:

```bash
sed -i '1s/^\xEF\xBB\xBF//' path/to/InvokeUtil.java
```

Or in most editors: "Save with encoding → UTF-8" (not "UTF-8 with BOM"). This affected
`IFaceSuperService_hn` and is fixed in the current tree.

### `mvn` warns that source/target 1.8 is obsolete

The root POM sets the language level to Java 8 (`maven.compiler.source`/`target` = `1.8`)
and every module inherits it. On JDK 9+ the build succeeds but prints:

```
[WARNING] source value 8 is obsolete and will be removed in a future release
[WARNING] target value 8 is obsolete and will be removed in a future release
[WARNING] To suppress warnings about obsolete options, use -Xlint:-options.
```

This is expected — Java 8 is the supported baseline, not an oversight. Do **not** raise
the level to silence the warning: the runtime is JDK 8 and Axis 1.x will not load on 11+.
If you compile on a newer JDK, also expect the "bootstrap classpath not set" warning,
which is equally harmless for this stack.

Do not re-declare `maven-compiler-plugin` in a module POM. The level lives in one place
so the five modules cannot drift apart.

### `mvn test` fails with `Connection refused`

```
com.sun.jersey.api.client.ClientHandlerException: java.net.ConnectException: Connection refused: connect
	at CsspControllerTest.registerTest(CsspControllerTest.java:61)
```

**Cause.** `CsspControllerTest` and `job/JobQueryControllerTest` are not unit tests — they
POST to a deployed instance at `http://127.0.0.1:8081/IFaceSuportService/iface/...`. With
nothing listening they fail on the first request, all 26 of them.

**Fix.** Nothing to fix: both are excluded from the default surefire run in
`IFaceSuportService/pom.xml`, so `mvn test` stays green without a server. To run them on
purpose, deploy the war first, then override the filter:

```bash
mvn -pl IFaceSuportService test -Dtest=CsspControllerTest,JobQueryControllerTest
```

`-Dtest` overrides the `excludes` block. If you add a test that needs a live server,
add it to that list rather than leaving the default build red.

### surefire: `The forked VM terminated without properly saying goodbye` (Windows)

```
[ERROR] The forked VM terminated without properly saying goodbye. VM crash or System.exit called?
[ERROR] Error occurred in starting fork, check output in log
```

**Cause.** Surefire forks a JVM through `cmd.exe` and passes the booter jar path from
`java.io.tmpdir`. If `%TEMP%` contains non-ASCII characters — which it does for any Windows
account with a Chinese user name, because the profile directory is used verbatim — the path
is mangled on the way through the console code page and the fork cannot start. Tests run: 0,
build fails, and no test report is written. The repository now sets Maven's temporary
directory to the ASCII-relative `.mvn-tmp` path through `.mvn/jvm.config`, so the current
public build avoids this path entirely.

**Fix for older checkouts or custom Maven configurations.** Point the temp directory at
an ASCII path:

```bash
mvn -Djava.io.tmpdir=D:\mvn-tmp -pl IFaceSuportService test
```

Creating `D:\mvn-tmp` first is enough. Alternatively add `-DforkCount=0` to run the tests
inside the Maven JVM; that works but loses per-test isolation. Linux and macOS are
unaffected, so CI never sees this.

Two things worth knowing before you spend time on this:

- The `.mvn/jvm.config` setting applies to normal Maven runs from this repository. The
  command-line flag remains useful for older checkouts and custom Maven configurations.
- It breaks `mvn package` and `mvn verify` too, not just `mvn test`, because both run the
  test phase. On an account whose name is not ASCII, an older checkout is red until you
  pass the flag or copy the repository's `.mvn/jvm.config` setting.

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

### Redis failures during session access

JedisUtil now returns each Redis connection exactly once and preserves the original
exception as the cause. If session operations fail, check the cause and confirm
StartupListener initialized the pool and Redis is reachable. Do not expose the
exception details in the HTTP response.

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

### BusinessFilter is not an XSS sanitizer

`BusinessFilter` is enabled for selected interface routes to enforce session authorization. It does not
escape input. Controllers rendering HTML must still apply context-appropriate output encoding. See [SECURITY.md](../SECURITY.md).
