# Mestudy

**A reference implementation of a Chinese social-insurance / e-government service platform, built on Spring MVC 4 + MyBatis 3.**

[![Java](https://img.shields.io/badge/Java-8%2B-orange.svg)](#tech-stack)
[![Spring](https://img.shields.io/badge/Spring-4.0.8-6db33f.svg)](#tech-stack)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.2.4-red.svg)](#tech-stack)
[![Build](https://img.shields.io/badge/build-Maven-c71a36.svg)](#build-from-source)
[![CI](https://github.com/zengyunhua/Mestudy/actions/workflows/ci.yml/badge.svg)](https://github.com/zengyunhua/Mestudy/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)

---

## What this is

Mestudy is a multi-module Maven workspace that collects the backend services behind a
provincial social-insurance and online government-service platform. It is not a toy
sample: the modules implement real business flows — social-insurance contribution
queries, social-security card lifecycle, employment/job-seeking services, hospital
registration, face verification, electronic signature, and draft-based online
service applications.

The code is published as a **reference architecture** for engineers who have to build
or maintain systems with the same shape: a servlet container, XML-wired Spring,
MyBatis mappers against Oracle, Redis-backed sessions, SOAP bridges to legacy
government interfaces, and a token-authenticated HTTP client SDK for downstream
terminals.

If you are looking for a modern Spring Boot starter, this is deliberately not one.
The value here is the *domain modelling* and the *integration patterns* that show up
when a Java system has to talk to a dozen heterogeneous government backends.

## Why it is worth reading

| Pattern | Where to look |
| --- | --- |
| Token-authenticated client SDK with self-renewing heartbeat | [`IFaceHttpsClient`](IFaceSuportService/src/main/java/com/tecsun/sisp/iface/client/IFaceHttpsClient.java) |
| Uniform response envelope shared by ~350 endpoints | [`Result`](IFaceSuportService/src/main/java/com/tecsun/sisp/iface/common/vo/Result.java) |
| Multi-datasource wiring with c3p0 + MyBatis | [`applicationContext.xml`](IFaceSuportService/src/main/resources/config/applicationContext.xml) |
| SOAP (Axis) → REST adapter for a legacy HIS system | [`HangTianHisController`](IFaceSuportService/src/main/java/com/tecsun/sisp/iface/server/controller/his/HangTianHisController.java) |
| Draft / submit / audit state machine for online services | [`DraftsController`](NetworkOfficeService/src/main/java/com/tecsun/sisp/net/modules/controller/userNet/DraftsController.java) |
| Startup-time resource bootstrapping | [`StartupListener`](IFaceSuportService/src/main/java/com/tecsun/sisp/iface/server/util/StartupListener.java) |

## Module map

| Module | Artifact | What it does | Size |
| --- | --- | --- | :---: |
| [`IFaceSuportService`](IFaceSuportService/README.md) | `sisp_IFaceSuportService` | Core interface service. Social-insurance queries, card services, employment, hospital, face verification, SMS, WeChat, bank/fee channels. Also ships the reusable **client SDK** jar (`classifier=client`). | 285 Java files |
| [`NetworkOfficeService`](NetworkOfficeService/README.md) | `sisp_NetworkOfficeService` | Online government-service portal. Application drafts, submission, audit trail, electronic signature (PDF stamping), area/administrative division data. | 127 Java files |
| [`IFaceSuperService_hn`](IFaceSuperService_hn/README.md) | `sisp_IFaceSuportService` (HN build) | Province-specific (Henan) variant of the interface service: card + social-insurance controllers against the provincial gateway. | 61 Java files |
| [`InterfaceFunction`](InterfaceFunction/README.md) | `sisp_InterfaceFunction` | Platform security administration: users, roles, function/permission trees, login. | 48 Java files |
| [`NetPortalManagement`](NetPortalManagement/README.md) | `sisp_NetPortalManagement` | Lightweight portal administration: portal users, policy documents, external web-connection registry. Uses the internal *Bee* persistence framework. | 11 Java files |

All modules share the group id `com.tecsun.sisp` and are packaged as `war` for
deployment into a servlet container.

## Architecture

Every module follows the same vertical slice. There is no shared `common` artifact —
each module carries its own copy of `common`/`util`, which keeps modules independently
deployable at the cost of some duplication.

```mermaid
flowchart TB
    subgraph Channel["Channel layer"]
        T["Self-service terminal<br/>(TSB client SDK)"]
        W["Web portal / JSP"]
        X["WeChat / SMS"]
    end

    subgraph Web["Servlet container (Tomcat / WebLogic)"]
        F["CharacterEncodingFilter<br/>HiddenHttpMethodFilter"]
        D["DispatcherServlet<br/>springMVC-servlet.xml"]
        L["StartupListener<br/>Redis + thread pool bootstrap"]
        C["@Controller<br/>BaseController"]
        S["Service / ServiceImpl"]
        M["MyBatis Mapper<br/>DAO + mappings/*.xml"]
    end

    subgraph Infra["Infrastructure"]
        R[("Redis<br/>token / session / dictionary")]
        O[("Oracle<br/>sisp · sisp_public · business schemas")]
        P["c3p0 pool<br/>multi-datasource"]
    end

    subgraph Ext["External systems"]
        G["Provincial social-insurance gateway<br/>(SOAP / Axis)"]
        H["Hospital HIS<br/>(SOAP)"]
        B["Biometric / face service"]
        K["Bank & fee channels"]
    end

    T --> F
    W --> F
    X --> F
    F --> D --> C --> S --> M --> P --> O
    C --> R
    S --> G
    S --> H
    S --> B
    S --> K
    L -.-> R
```

### Request lifecycle

1. `web.xml` loads `config/applicationContext.xml` into the root context and registers
   `StartupListener`, which initialises the Redis pool (`DataBase.init()`) and the
   shared thread pool (`ThreadPoolUtil.init()`) before the first request is served.
2. `DispatcherServlet` is mapped to `/` and scans only `@Controller` beans from
   `com.tecsun.sisp.iface.server.controller`.
3. Controllers extend `BaseController` and return a `Result` (`statusCode` / `message` /
   `data`) or a `ResultVerify` for paged payloads. `statusCode` is `"200"` on success and
   `"0"` on failure — see [API overview](docs/API_OVERVIEW.md).
4. Services delegate to MyBatis mappers; mapper XML lives in
   `src/main/resources/mappings/` and is bound in `applicationContext.xml`.
5. Calls to legacy government systems go through Axis-generated SOAP stubs or the
   Jersey/`HttpClient` helpers, and are wrapped so a downstream outage degrades into a
   `Result` with a failure code rather than a 500.

## Tech stack

| Concern | Choice | Notes |
| --- | --- | --- |
| Language / runtime | Java 8 (source/target 1.8) | Level is set once in the root POM via `maven.compiler.source`/`target`; modules must not override it |
| Web framework | Spring MVC 4.0.8.RELEASE | XML configuration, no Spring Boot |
| Persistence | MyBatis 3.2.4 | Mapper interfaces + XML, `SqlSessionTemplate` |
| Connection pool | c3p0 (`ComboPooledDataSource`) | One bean per datasource |
| Database | Oracle 10g/11g | Multiple schemas per module |
| Cache / session | Redis via Jedis | Dictionary cache, interface token, channel state |
| SOAP | Apache Axis 1.x | Generated stubs for provincial gateway / HIS |
| HTTP client | Apache HttpClient 4.1 + Jersey 1.15 | Client SDK and inter-service calls |
| JSON | Gson + `net.sf.json` | Both are in use; new code should prefer Gson |
| Logging | SLF4J + Log4j 1.2 | `log4j.properties` / `log4j.xml` per module |
| Packaging | `war` | Servlet 2.5 descriptor |

## Build from source

### Prerequisites

- JDK 8 required — it is both the build JDK and the language level. JDK 9+ compiles
  the sources but warns that source/target 8 is obsolete; JDK 11+ will need `--add-opens`
  workarounds for Axis and c3p0
- Maven 3.2+
- A servlet container (Tomcat 7/8, or WebLogic 12c)
- Oracle and Redis instances if you want the application to actually start

### Build

```bash
git clone https://github.com/zengyunhua/Mestudy.git
cd Mestudy

# Compile every module whose dependencies are public (see the table below)
mvn -B -pl IFaceSuportService,IFaceSuperService_hn compile

# Build one module into a deployable war
mvn -B -pl IFaceSuportService package
#   target/IFaceSuportService-V1.0.0.150630.war         -> deploy to the container
#   target/IFaceSuportService-V1.0.0.150630-client.jar  -> embeddable client SDK
```

The repository root holds an aggregator `pom.xml`, so Maven works from the top of the
tree. Each module keeps its own coordinates and version — the root POM supplies only
the module list, the source encoding and the Java 8 compiler level (1.8), which every
module inherits.

### Which modules build publicly

| Module | Public build | Blocking dependency |
| --- | --- | --- |
| `IFaceSuportService` | yes | — |
| `IFaceSuperService_hn` | yes | — |
| `NetworkOfficeService` | no | `bee:xx.tool`, `com.tecsun.sisp_public:aamanagement-client` |
| `InterfaceFunction` | no | `bee:xx.tool`, `com.tecsun.sisp_public:aamanagement-client` |
| `NetPortalManagement` | no | `bee:cloud`, `bee.cloud.service`, `bee.cloud.impl`, `bee.cloud.entity` |

The three blocked modules depend on an internal application framework (`bee`) and an
internal AA client that were never published to a public repository. Nothing in this
repository can substitute for them; they must be present in your local repository
before those modules will build. Everything else — Spring, MyBatis, Axis, Jersey,
kaptcha and the Oracle JDBC driver — resolves from Maven Central.

> **Private mirrors.** The POMs no longer hard-code an internal Nexus. If your
> organisation proxies Maven Central, declare the mirror in `~/.m2/settings.xml`
> instead of in the project POM. See
> [docs/GETTING_STARTED.md](docs/GETTING_STARTED.md#using-a-maven-mirror).

### Run

```bash
# 1. Copy the configuration template and fill in your own values
cp src/main/resources/config.properties.example src/main/resources/config.properties

# 2. Deploy the war (context path is /sisp by convention)
cp target/IFaceSuportService-*.war $CATALINA_HOME/webapps/sisp.war

# 3. Start the container
$CATALINA_HOME/bin/startup.sh
```

The service is then reachable at `http://localhost:8080/sisp/`.

## Using the client SDK

`IFaceSuportService` publishes a slim client jar containing only `**/client/**` and
`**/common/**` classes. Consumers initialise it once, then call typed methods.

```java
// Initialise once per JVM. Spawns a heartbeat thread that renews the token.
IFaceHttpsClient.ClientInit(
        "10.0.0.10",   // host
        8080,          // port
        60,            // token refresh interval, minutes (floor is 60)
        false,         // useHttps
        "TSB",         // channel code
        "sisp"         // platform context path
);

// Then call the typed API
SecQueryBean bean = new SecQueryBean();
bean.setId("110101199001010015");
bean.setName("...");
bean.setChannelcode("SelfService");

Page<TsgrjfxxVO> page = IFaceHttpsClient.getPersonPesionPay(bean);
System.out.println(page.getData().size());
```

The heartbeat calls `POST {context}/iface/user/checkLogin` and stores the returned
`tokenId`; subsequent SDK requests send `X-Token-Id` as a header. Because the token is held
in a `static` field and the heartbeat thread is a singleton, **the SDK is bound to a
single target server per JVM**. See [docs/CLIENT_SDK.md](docs/CLIENT_SDK.md) for the
full reference.

## Configuration

Configuration is a single `config.properties` per module, loaded through
`<context:property-placeholder>`. A sanitised template ships as
`config.properties.example`; the real file is git-ignored.

| Key group | Purpose |
| --- | --- |
| `jdbc.<name>.*` | Driver, URL, credentials and pool sizing per datasource |
| `redis_core_database` | Redis endpoint as `host_port` |
| `redis_iface_expx` | Token / cache TTL in **minutes** (multiplied by 60 internally) |
| `platform_context_path` | Deployed context path, default `/sisp` |
| `inner_proxy_ip` / `inner_proxy_port` | Internal proxy used for inter-service calls |
| `biometrics.*` | Face-verification gateway endpoint and credentials |
| `signpdf.*` | Electronic-signature service endpoint, font and template paths |

Full reference: [docs/CONFIGURATION.md](docs/CONFIGURATION.md).

## API surface

All HTTP endpoints answer with the same envelope:

```json
{
  "statusCode": "200",
  "message": "success",
  "data": { }
}
```

Paged endpoints add `pageNo`, `pageSize` and `count`:

```json
{
  "statusCode": "200",
  "message": "success",
  "data": [ ],
  "pageNo": 1,
  "pageSize": 20,
  "count": 137
}
```

Across the workspace there are **358 `@RequestMapping` declarations**, grouped by
domain prefix (`/iface/rest/`, `/iface/cardInfo/`, `/iface/user/`, ...). The
[endpoint index](docs/API_OVERVIEW.md) lists them by controller.

## Project status

This repository is a **curated archive of production-derived modules**, published for
reference. It is maintained on a best-effort basis.

- **Documentation**: in place — this README, a per-module README each, and the eight
  documents under `docs/`.
- **Build reproducibility**: partial. `IFaceSuportService` and `IFaceSuperService_hn`
  build from Maven Central alone and are compiled on every push; the other three need
  private jars, see below.
- **Tests**: thin. Four `src/test` entry points with `main()` methods, no assertions.
  CI compiles the buildable modules and runs hygiene checks, but it runs no tests.
- **Modernisation**: not planned in-place. The intent is to keep the original
  Spring 4 / XML style intact so the patterns stay readable. The language level is
  Java 8 (raised from 7) and that is as far as it goes — no framework upgrade.

### Known limitations

These are documented on purpose — they are the honest state of the code, and each one
is a good first contribution.

1. **Three modules need private jars.** `NetworkOfficeService`, `InterfaceFunction` and
   `NetPortalManagement` depend on an internal framework (`bee`) and an internal AA
   client that were never published publicly. They are excluded from CI for that
   reason. The other two modules build against Maven Central alone.
2. **Credentials are still in the git history.** The `config.properties` / `ds.xml`
   files are no longer tracked (they were replaced by `.example` templates), but they
   remain in every commit before that removal. Treat every credential that ever
   appeared in this repository as compromised and rotate it — see
   [SECURITY.md](SECURITY.md) for the procedure.
3. **Duplicated `common` layer.** Each module re-implements `Result`, `Page`,
   `CommUtil`, `AESUtil`. 54 class names exist in more than one module and none of
   the copies are identical — they have drifted. Consolidating them into a shared
   artifact is the single most valuable refactor available here.
4. **Thin test coverage.** There is no assertion-based test suite. The four classes
   under `src/test` are manual entry points with `main()` methods; one of them is not
   even on the test source path.
5. **One release only.** `v1.0.0` is tagged and described in
   [CHANGELOG.md](CHANGELOG.md); the per-module artifact versions in the POMs still
   carry their original internal numbers (`V1.0.0.150630`, `V1.0.0.190107`) and are not
   aligned with it.

## Roadmap

See [docs/ROADMAP.md](docs/ROADMAP.md). Near-term:

- [x] Add a root aggregator POM so the workspace builds as one reactor
- [x] Remove the hard-coded internal Nexus from every POM
- [x] Replace non-public dependency coordinates with their Maven Central equivalents
- [x] Stop tracking `target/` and the real `config.properties` files
- [x] GitHub Actions: compile the buildable modules on JDK 8, plus repository hygiene checks
- [x] Raise the language level to Java 8 (source/target 1.8, set once in the root POM)
- [x] Tag `v1.0.0`
- [ ] Make the remaining three modules build against public artifacts
- [ ] Scrub credentials and personal data from the git history
- [ ] Add JUnit + H2 tests for the `common` layer and one controller per module
- [ ] Generate an OpenAPI description from the existing controllers

## Documentation

| Document | Contents |
| --- | --- |
| [docs/GETTING_STARTED.md](docs/GETTING_STARTED.md) | Zero-to-running in ~15 minutes |
| [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Layering, contexts, request lifecycle, cross-cutting concerns |
| [docs/CONFIGURATION.md](docs/CONFIGURATION.md) | Every configuration key, with defaults |
| [docs/API_OVERVIEW.md](docs/API_OVERVIEW.md) | Response envelope, error codes, endpoint index |
| [docs/CLIENT_SDK.md](docs/CLIENT_SDK.md) | Using `IFaceHttpsClient` from a Java consumer |
| [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md) | Build failures, encoding issues, Oracle/Redis gotchas |
| [docs/ROADMAP.md](docs/ROADMAP.md) | Where this is going |
| Per-module READMEs | Linked from the [module map](#module-map) |

## Contributing

Issues and pull requests are welcome — especially the items in
[Known limitations](#known-limitations). Please read
[CONTRIBUTING.md](CONTRIBUTING.md) first, and note that all participation is governed
by the [Code of Conduct](CODE_OF_CONDUCT.md).

Good first issues: adding a test, replacing a hard-coded constant with a configuration
key, translating a class-level comment into English Javadoc, or removing one module's
dependency on the private Nexus.

## Security

Do not open a public issue for a vulnerability. See [SECURITY.md](SECURITY.md).

## License

[Apache License 2.0](LICENSE).

Third-party attribution is recorded in [NOTICE](NOTICE). A small number of files
(`Dialect`, `PreparePaginationInterceptor`, `Reflections`) are copied from
[JeeSite](https://github.com/thinkgem/jeesite) and keep their original `@author`
tags — please leave those intact if you fork this project.

## Disclaimer

This codebase is derived from systems built for Chinese public-service institutions.
It is published for **educational and reference purposes**. It is not affiliated with,
endorsed by, or supported by any government agency. Any data, endpoints, credentials or
identifiers present in the history are placeholders or historical artefacts and must not
be used against live systems. You are responsible for complying with local law —
including personal-information protection rules — before deploying anything derived from
this repository.
