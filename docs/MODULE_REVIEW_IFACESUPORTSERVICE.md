# IFaceSuportService — module review

Scope: `IFaceSuportService` only (287 Java sources under `src/main`, 21 controllers,
251 `@RequestMapping` declarations, 6 test sources). Findings below were read from the
code at the commit this file was added; line numbers are from that revision.

This is a maintainer's review, not a penetration test. Nothing here was exercised against
a running instance — every item is a code-reading result. Items marked **decision** change
the HTTP contract or the deployment model and need the owner's call before anyone touches
them; items marked **local** can be fixed without affecting callers.

Cross-reference: `docs/ROADMAP.md` already tracks some of this. Where an item is already
on the roadmap it is labelled with the phase, so this file does not silently duplicate it.

---

## Summary

| # | Item | Severity | Kind |
| --- | --- | --- | --- |
| 1 | OTP returned in the response body; `/iface/user` is outside the auth filter | **P0** | decision |
| 2 | Photo path built from a request field — directory traversal, no size cap | **P0** | decision |
| 3 | AES in ECB mode, key bytes read with the platform charset | P0 | decision |
| 4 | `AESUtil.main()` prints the encryption key; class ships in the SDK jar | P0 | local |
| 5 | Two dead classes that contain dangerous sinks | P1 | local |
| 6 | Unbounded thread pool, double lazy init | P1 | local |
| 7 | `contextDestroyed()` is empty — pools leak on redeploy | P1 | local |
| 8 | Redis pool: unvalidated config, no auth, no close | P1 | local |
| 9 | `Config` singleton race; missing file throws NPE | P1 | local |
| 10 | 8 datasources, 7 transaction managers, default manager commented out | P1 | local |
| 11 | Client jar ships 3 classes whose dependencies are not in the jar | P1 | decision |
| 12 | `client/Test.java` ships in the SDK with a public IP | P2 | local |
| 13 | Internal addresses hardcoded in `Constants` | P2 | decision |
| 14 | Example config is missing 2 keys used by code | P2 | local |
| 15 | README contradicts the code in two places | P2 | local |
| 16 | 198 `printStackTrace`, 93 `System.out/err` | P2 | local |
| 17 | 40 of 43 same-named classes have diverged from `_hn` | P2 | roadmap P5 |
| 18 | 3 unit-test classes for 287 sources | P2 | roadmap P3 |
| 19 | Dependency versions and one wrong artifact | P2 | decision |

---

## P0 — fix before calling this deployable

### 1. The SMS code is handed back to whoever asked for it

`web.xml` registers `BusinessFilter` on five prefixes only:

```
/iface/rest/*   /iface/cardInfo/*   /iface/bank/*   /iface/job/*   /iface/his/hangtianhis/*
```

`UserController` is mapped at `/iface/user` (`UserController.java:43`), which matches none
of them. Its two code endpoints then put the code into the response map:

| Line | Statement |
| --- | --- |
| `UserController.java:419` | `String random = CommUtil.getRandomNum(4);` |
| `UserController.java:427` | `JedisUtil.setValue(vo.getMobile(), random, 2);` |
| `UserController.java:429` | `map.put("random", random);` |
| `UserController.java:446` | `return this.result(result, message, map);` |

`getMobileCodeByNew` (`:458–487`) does the same at `:467`, `:475`, `:477`, and its
`newPhoneNo` is supplied by the caller rather than looked up from a user record.

So an unauthenticated caller can post any phone number to `/iface/user/getMobileCodeByNew`,
read the code out of the JSON body, and complete a phone-number change. The SMS is a
decoration, not a control.

Secondary problem: `CommUtil.getRandomNum` (`CommUtil.java:210–220`) uses `java.util.Random`
and removes each drawn digit from the pool, so the space is 4 distinct digits (5040)
rather than 10000, on a predictable generator.

**Why decision:** removing `random` from the response breaks every client that currently
reads it. That may be all of them — the field is in the published response shape. The fix
needs a coordinated client change, or a feature flag, not a silent edit.

**Options, cheapest first**

1. Delete `map.put("random", ...)` and add `/iface/user/getMobileCode*` to `BusinessFilter`.
   Only viable if no deployed client reads the field.
2. Keep the field but stop returning it for `getMobileCodeByNew` (the change-phone path) —
   narrows the hole without touching the login flow.
3. Return it only when a `debug` flag is on, default off.

Whichever is chosen, `getRandomNum` should use `SecureRandom` and keep the full digit pool.

### 2. Photo upload writes to a caller-controlled path

Chain:

| Step | Location |
| --- | --- |
| Endpoint takes the ID number from the request body | `JobQueryController.java:431–438` |
| Filename built from it, unvalidated | `JobServiceImpl.java:422–424` |
| Absolute path assembled | `JobServiceImpl.java:436` |
| Bytes written to that path | `JobServiceImpl.java:392` → `PicUtil.GenerateImage` |
| `PicUtil` caps neither size nor format | `PicUtil.java:29–51` |

`zjhm` reaches the filename verbatim, so `zjhm = "../../../opt/tomcat/webapps/ROOT/shell"`
escapes the configured directory. `PicUtil.GenerateImage` decodes the base64 with no upper
bound, so the same request can also fill the disk or the heap.

This module's `PicUtil` is the **pre-hardening** version. `IFaceSuperService_hn`'s copy was
already fixed: 5 MiB cap, JPEG SOI/EOI check, `idCard.matches("[A-Za-z0-9_-]{1,32}")`, and
an atomic temp-file move. The fix exists in the repository — it just was not applied here.
That asymmetry is the strongest argument for item 17.

**Why decision:** adding validation may reject ID numbers that real terminals currently
send (spaces, lowercase `x`, 15-digit forms). The allowlist needs to match observed traffic.

### 3. AES is ECB, and the key bytes depend on the platform charset

`AESUtil.java:120` and `:148` call `Cipher.getInstance("AES")`, which resolves to
`AES/ECB/PKCS5Padding`. ECB encrypts identical plaintext blocks identically, so the
ciphertext leaks structure. `AESUtil.java:116` and `:146` call `encryptKey.getBytes()`
without a charset, so the same key produces different bytes on a host with a non-UTF-8
default — the classic "decrypts on the build box, not in production" failure.

Already tracked as item 2.3 of issue #2 and not yet started. Changing the mode changes
every stored ciphertext, so it needs a re-encryption or dual-read migration.

### 4. A demo `main()` prints the encryption key, and it is packaged for customers

`AESUtil.java:29–42` reads `hr_encryption_key` and prints it:

```java
String key = Config.getInstance().get("hr_encryption_key");
System.out.println("加密密钥和解密密钥：" + key);
```

The client jar includes `**/common/**/**.class`, so `AESUtil.class` — with this `main()` —
is inside the artifact handed to SDK consumers. Verified: the built
`IFaceSuportService-V1.0.0.150630-client.jar` contains
`com/tecsun/sisp/iface/common/util/AESUtil.class`.

Local fix: delete the `main()` method. No caller, no contract.

---

## P1 — resource and startup correctness

### 5. Two dead classes that contain dangerous sinks

| Class | Why it is dangerous | Proof it is dead |
| --- | --- | --- |
| `CommUtil.getMACAddress` (`:91–133`) | `Runtime.getRuntime().exec("nbtstat -A " + ip)` — the IP is concatenated into a shell command | No caller in the workspace |
| `DBConnection` (whole class) | Static `Connection`/`Statement`/`ResultSet` shared across threads; `initConn()` builds a second `ClassPathXmlApplicationContext` per call; `closeConn()` has the connection close commented out (`:76–82`), so every call leaks a pooled connection; `executeQuery(String sql)` is a raw `Statement` | Its only call site, `StartupListener.java:43`, is commented out |

Neither can be reached, so neither can be exploited today. They are dead weight that will
be re-used by the next person who greps for "a way to run SQL". Deleting both removes the
sink and ~120 lines.

### 6. The thread pool has no upper bound

`ThreadPoolUtil.java:29–31`:

```java
threadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
        60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
```

`SynchronousQueue` plus `max = Integer.MAX_VALUE` means every submitted task gets a new
thread and nothing ever queues. The class's own Javadoc (`:24–27`) admits this. Six live
call sites submit work: `BankController:57`, `CardInfoController:489`,
`JobQueryController:692`, `HangTianHisController:1359` and `:1368`, `BusinessHandler:46`.

Two more defects in the same file: `isInit` (`:14`) is not `volatile` and `init()` (`:16`)
is an unsynchronised check-then-act; and `getThreadPool()` (`:34–39`) has its own lazy
init that bypasses `isInit` entirely, so a race can construct two pools and abandon one.

Local fix: bounded queue, a real maximum, named threads, and a single
`static synchronized` initialiser.

### 7. Shutdown does nothing

`StartupListener.java:49–51` is an empty `contextDestroyed`. The class Javadoc already says
so: "pools are not closed on undeploy, so a container reload leaks connections until the
JVM exits." A redeploy therefore strands the `JedisPool` and the executor.

The same class also `extends ContextLoader` (`:31`) for no reason — it overrides
`contextInitialized` without calling `super`, and `ContextLoaderListener` is registered
separately in `web.xml`. Dropping the superclass removes a confusing second identity.

Local fix: call `DataBase.close()` / `ThreadPoolUtil.shutdown()` (both need adding), and
drop `extends ContextLoader`.

### 8. Redis pool: unvalidated config, no credentials, no close

`DataBase.java:43–49`:

```java
String[] jedisInfo = jedisInfoStr.split("_");
...
jedisPublicPool = new JedisPool(config, jedisInfo[0], Integer.valueOf(jedisInfo[1]).intValue(), 10000);
```

- A value without an underscore (`redis_core_database=10.0.0.5`) throws
  `ArrayIndexOutOfBoundsException` during `StartupListener`, i.e. the deployment dies with
  an unhelpful stack trace instead of "malformed redis_core_database".
- No password and no TLS are passed. Jedis 2.6.2 is old enough that the constructor
  overload used here predates TLS support, so this is a library constraint, not an oversight.
- There is no `close()`, which is what item 7 needs.

### 9. `Config` races on first access and fails with the wrong exception

`Config.java:64–70` is a lazy singleton with no synchronisation and no `volatile` on
`instance`, so two threads arriving together can build two instances and one is discarded.
The Javadoc (`:22–24`) acknowledges this and argues it is fine because the class is first
touched during servlet initialisation — true for the current wiring, but it makes the class
unsafe to use from anything started earlier.

The failure mode is worse than the race. `Config.java:45–46`:

```java
in = Config.class.getResourceAsStream("/config.properties");
this.props.load(in);
```

`getResourceAsStream` returns `null` when the file is absent, and `Properties.load(null)`
throws `NullPointerException` — which the surrounding `catch (IOException)` does not
handle. A deployment missing its properties file therefore dies with an NPE from inside
`Properties`, not with "config.properties not found".

`IFaceSuperService_hn`'s copy already throws `IllegalStateException` with a clear message
when the file is missing or empty. Same fix applies here; no caller is affected.

### 10. Transaction managers do not cover the datasources

`applicationContext.xml` wires 8 datasources and 7 `DataSourceTransactionManager` beans
(`trans_1`…`trans_7`). `dryldyDataSource` has none.

Line 243 declares `<tx:annotation-driven/>` with no `transaction-manager`, so it looks for a
bean named `transactionManager` — which is commented out at `:279–281`.

This works today only by accident: the two `@Transactional` usages
(`NetUserServiceImpl.java:44` and `:49`) are written `@Transactional("net")`, and `"net"`
matches the `<qualifier>` on `trans_2`. Any new unqualified `@Transactional` will fail at
first invocation with `NoSuchBeanDefinitionException: transactionManager`.

Local fix: name one manager `transactionManager` explicitly, or pass
`transaction-manager` to `<tx:annotation-driven>`; document that per-datasource work must
carry a qualifier; add a manager for `dryldy` or state that it is intentionally
non-transactional.

---

## P2 — packaging, docs, hygiene

### 11. The client jar contains three classes it cannot load

`pom.xml` builds the SDK with `**/client/**.class` and `**/common/**/**.class`. Three
classes under `common/` import `server/` types, which are excluded:

| Class in jar | Imports | Present in jar? |
| --- | --- | --- |
| `common/util/AESUtil` | `server.util.Config` | no |
| `common/util/ArchiveUtil` | `server.outerface.card.CardInfoBus` | no |
| `common/util/ZxHisUtil` | `server.outerface.card.CardInfoBus` | no |

Verified against the built artifact: 188 classes, **zero** under `server/`. So the SDK
advertises `common/**` while shipping three classes that throw `NoClassDefFoundError` the
moment they are touched. `instruction.txt` is also absent from the jar even though the
module README lists it as part of the SDK.

**Why decision:** the clean fix is to break the `common → server` dependency (move the
config lookup behind an interface, move `CardInfoBus` usage into the server tree), which
changes those three classes' signatures. The cheap fix is to exclude them from the jar and
say so in `docs/CLIENT_SDK.md`.

### 12. A demo class with a public IP ships in the SDK

`client/Test.java` has a `main()` hardcoded to `http://61.28.113.182:8080/...` and
`https://61.28.113.182:9443/...`. It matches `**/client/**.class`, so it is inside the
published jar. A public, routable address of what looks like a real deployment does not
belong in a reference repository, and a `main()` does not belong in an SDK.

Local fix: delete, or move to `src/test/java`.

### 13. Internal addresses are compile-time constants

`Constants.java` hardcodes at least eight internal endpoints — `10.132.1.85:7002` (`:274`),
`10.132.1.78:8081` (`:712`), `10.132.1.85:3388` (`:716`), `10.132.1.85/OrderTreat` (`:720`),
`10.128.8.17:8080` (`:722`), and four more in the same block. They are `static final`, so
they cannot be overridden per environment without a rebuild. This also publishes the
internal topology. `docs/CONFIGURATION.md` already claims configuration is properties-driven;
these constants are the exception.

**Why decision:** moving them to `config.properties` adds keys that every deployment must
supply, which is a deployment-model change.

### 14. The example config is missing two keys the code reads

Code references 20 keys through `Config.getInstance().get(...)`; `config.properties.example`
defines 56. Two are referenced but undefined:

| Key | Read at | Consequence |
| --- | --- | --- |
| `jiuye_job_idcard_picture_dir` | `JobServiceImpl.java:427` | `isBlank` guard at `:428` returns a message, so the employment photo feature silently does nothing on a fresh deployment |
| `hr_encryption_key` | `AESUtil.java:34` (only the demo `main()`) | none once item 4 is fixed |

### 15. The module README contradicts the code

| README | Reality |
| --- | --- |
| `:162` "`BusinessFilter` (XSS) is disabled in `web.xml`. Re-enabling it is on the roadmap." | Registered on five prefixes at `web.xml:66–89`. It is a session-authorization filter, not an XSS filter — it checks a Redis-backed token and device id. `docs/ROADMAP.md:75` already marks re-enabling it `[x]`. |
| `:39` `SSLClient.java # permissive HTTPS client` | `SSLClient.java:27` sets `SSLSocketFactory.STRICT_HOSTNAME_VERIFIER` and uses the default trust store. `docs/ROADMAP.md:76` already marks this `[x]`. |

Both statements were true at some point and were not updated when the code changed. They
are the kind of stale claim that costs a reviewer's trust in the rest of the document.

### 16. Logging

- 198 `printStackTrace` calls, concentrated in `ZhongxinHisController` (40),
  `SecQueryController` (35), `ZhongxinHisSceRegController` (16), `CsspBusController` (16).
  These write to `catalina.out` with no logger, no level and no correlation id, so a failed
  request cannot be traced from the access log to the cause.
- 93 `System.out` / `System.err` calls in `src/main`.
- `Dom4JUtil.java:200` prints every column of every row while mapping archive results:
  `System.out.println(j+":"+columns.get(j).getName()+":"+columns.get(j).getText());`
  Leftover debugging on a per-row path.

### 17. Utility classes have forked between modules

`IFaceSuportService` and `IFaceSuperService_hn` share 43 same-named Java files. Only 3 are
byte-identical; **40 have diverged**. The security-relevant ones:

`AESUtil`, `BusinessFilter`, `CommUtil`, `Config`, `DataBase`, `JedisUtil`, `PicUtil`,
`DBConnection`, `Dom4JUtil`, `Constants`, `BaseController`.

This is why item 2 exists: `PicUtil` was hardened in `_hn` and left alone here. Every future
fix has to be applied twice and verified twice. `docs/ROADMAP.md` Phase 5 already plans a
shared `sisp-common` artifact; this measurement is the argument for prioritising it.

### 18. Test coverage

Three unit-test classes cover utility corners: `PageTest` (paging arithmetic),
`BusinessFilterTest` (`hasPermission` parsing), `IFaceHttpsClientTest`
(`requireSuccessfulPage`). Two further tests are HTTP smoke tests against a live instance
and are excluded from the build by `pom.xml` (`CsspControllerTest`,
`JobQueryControllerTest`).

Nothing exercises a controller, a service, a mapper or the Spring context. Item 1 and
item 2 would both have been caught by a single `MockMvc` test posting a hostile `zjhm` or
asserting that `getMobileCode` does not echo the code. Already `[~]` in roadmap Phase 3.

### 19. Dependencies

Versions worth flagging — all pinned in `pom.xml`, all with published advisories:

| Artifact | Version | Note |
| --- | --- | --- |
| `com.alibaba:fastjson` | 1.2.12 | known deserialization RCEs; later 1.2.x are drop-in |
| `log4j:log4j` | 1.2.12 | EOL; CVE-2019-17571, CVE-2021-4104 |
| `commons-fileupload` | 1.2.2 | CVE-2014-0050, CVE-2016-3092 |
| `io.netty:netty` | 3.6.6.Final | CVE-2019-20444/20445/20446 |
| `org.apache.axis:axis` | 1.4 | EOL since 2006; needed by the generated stubs |
| `org.apache.pdfbox:pdfbox` | 1.8.4 | EOL |
| `c3p0:c3p0` | 0.9.1.2 | CVE-2019-5427 |
| `org.apache.httpcomponents:httpclient` | 4.1.2 | 2011 |
| `com.google.code.gson:gson` | 1.4 | 2009 |
| `javax.servlet:servlet-api` | 2.4 | matches the `web.xml` 2.5 schema |

One dependency is simply wrong:

```xml
<groupId>org.apache.servicemix.examples</groupId>
<artifactId>org.apache.servicemix.examples.itests.cxf-soap-handler-osgi</artifactId>
<version>4.3.0</version>
```

That is an integration-test *example* project pulled in as a runtime dependency. Nothing in
the module imports anything it provides. It should be removed and the build re-verified.

**Why decision:** the Axis 1.4 and servlet 2.4 pins are structural — the generated SOAP
stubs and `web.xml` 2.5 depend on them. Upgrading those is a rewrite, not a bump.

---

## Suggested order of work

1. **Items 4, 5, 12** — pure deletion, no contract change, shrinks the attack surface.
2. **Items 6, 7, 8, 9, 10** — startup and shutdown correctness, no API change.
3. **Items 14, 15, 16** — config and documentation accuracy; cheap and visible.
4. **Items 1, 2** — the two real holes. Both need a decision on client compatibility
   before a diff exists.
5. **Items 3, 11, 13, 19** — need a migration plan (re-encryption, SDK refactor,
   deployment-model change, dependency upgrade).

Items 17 and 18 are structural and already on the roadmap; they get cheaper the earlier
they start, because every fix applied before the extraction has to be applied twice.
