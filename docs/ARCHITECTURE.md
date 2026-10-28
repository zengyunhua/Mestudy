# Architecture

This document primarily explains request handling in `IFaceSuportService` and the
Spring MVC/MyBatis modules. `NetPortalManagement` instead uses Bee/Jersey with its own
datasource and authentication filter; do not apply the Spring request lifecycle to it.
Differences among the public modules are called out where relevant.

- [1. Workspace layout](#1-workspace-layout)
- [2. Layering](#2-layering)
- [3. Spring contexts](#3-spring-contexts)
- [4. Request lifecycle](#4-request-lifecycle)
- [5. Persistence](#5-persistence)
- [6. Cross-cutting concerns](#6-cross-cutting-concerns)
- [7. Integration with external systems](#7-integration-with-external-systems)
- [8. Design decisions and trade-offs](#8-design-decisions-and-trade-offs)

---

## 1. Workspace layout

The root `pom.xml` is the parent and aggregator. Its default reactor builds four
public modules; `-Pprivate-deps` opts in `NetPortalManagement` when its six private
artifacts are available. Each module still produces a separately deployable WAR:

```
Mestudy/
├── pom.xml                      # parent and default four-module reactor
├── IFaceSuportService/          # core interface service + client SDK
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/tecsun/sisp/iface/
│       │   │   ├── client/          # published client SDK (jar classifier=client)
│       │   │   ├── common/          # shared VOs, utilities, paging
│       │   │   └── server/          # controllers, services, DAOs, utils
│       │   ├── resources/
│       │   │   ├── config/          # applicationContext.xml, springMVC-servlet.xml
│       │   │   ├── mappings/        # MyBatis mapper XML
│       │   │   └── config.properties.example  # copy locally; do not commit secrets
│       │   └── webapp/WEB-INF/web.xml
│       └── test/java/               # focused automated tests; legacy smoke tests excluded
├── NetworkOfficeService/
├── IFaceSuperService_hn/
├── InterfaceFunction/
└── NetPortalManagement/
```

The consequence: **`common` classes are duplicated per module.** `Result`, `Page`,
`CommUtil`, `AESUtil`, `JsonMapper` exist in several copies with slightly different
signatures. This is a deliberate trade-off from the original delivery model — each
module had to be deployable into a different customer environment without coordinating
a shared release. Consolidating them is tracked in the [roadmap](ROADMAP.md).

## 2. Layering

```
┌──────────────────────────────────────────────────────────┐
│ Controller      @Controller, extends BaseController      │
│                 - binds request params / JSON bodies     │
│                 - returns Result / ResultVerify / Page   │
├──────────────────────────────────────────────────────────┤
│ Service         *Service / *ServiceImpl                  │
│                 - business rules, orchestration          │
│                 - calls external gateways                │
├──────────────────────────────────────────────────────────┤
│ DAO / Mapper    *DAO interfaces + mappings/*.xml         │
│                 - SQL only, no business logic            │
├──────────────────────────────────────────────────────────┤
│ Domain          entity/ and common/vo/                   │
│                 - persistence entities vs. transport VOs │
└──────────────────────────────────────────────────────────┘
```

Conventions worth knowing before you read the code:

- **Entities vs. VOs.** `server/entity/` holds classes shaped like database rows;
  `common/vo/` holds classes shaped like the wire format. Controllers return VOs.
  Do not return entities directly — some contain internal identifiers.
- **Service naming.** `XxxServiceImpl` implements `XxxService`; both live in
  `server/model/service/` (interface) and `server/model/service/impl/`.
- **DAO naming.** Interface in `server/model/dao/`, XML in `resources/mappings/`.
  The XML namespace must match the DAO interface's fully-qualified name.
- **`BaseController`** supplies `result(...)`, `ok(...)`, `resultVerify(...)` helpers and
  a shared Jersey client. Extend it; do not re-implement the envelope.

## 3. Spring contexts

Two contexts, classic pre-Boot arrangement:

**Root context** — `classpath*:/config/applicationContext.xml`, loaded by
`ContextLoaderListener`.

- `<context:property-placeholder location="classpath:config.properties"/>`
- One `ComboPooledDataSource` bean per database schema
- One `SqlSessionFactoryBean` + `SqlSessionTemplate` per datasource
- `MapperScannerConfigurer` per datasource, each scanning its own DAO package
  (`...model.dao`, `...model.dao_cssp`, `...model.dao_dr`, ...)
- `DataSourceTransactionManager` per datasource

**Servlet context** — `classpath:config/springMVC-servlet.xml`, loaded by
`DispatcherServlet`.

- `<mvc:annotation-driven/>`
- `<mvc:default-servlet-handler/>` so static assets are served
- `<context:component-scan base-package="...controller" use-default-filters="false">`
  with an include-filter for `@Controller` only — services and DAOs are *not* picked up
  here, they come from the root context
- `InternalResourceViewResolver` driven by `web.view.prefix` / `web.view.suffix`

Because component scanning is restricted to `@Controller`, a class annotated `@Service`
in the controller package will silently not be registered. If a bean is "missing",
check which context is supposed to own it first.

### Datasource inventory

| Module | Datasource beans | Backing keys |
| --- | --- | --- |
| `IFaceSuportService` | `dataSource`, `zhongjianDataSource`, `drDataSource`, `faceDataSource`, `csspDataSource`, `sispPublicDataSource`, `dryldyDataSource`, `netportalDataSource` | `jdbc.dongruan.*`, `jdbc.zhongjian.*`, `jdbc.drface.*`, `jdbc.face.*`, `jdbc.cssp.*`, `jdbc.sisp_public.*`, `jdbc.dryldy.*`, `jdbc.sisp.*` |
| `NetworkOfficeService` | `sispDataSource`, `sisppublicDataSource`, `sispPublicYthDataSource`, `sispYthdataSource`, `drDataSource` | `jdbc.sisp.*`, `jdbc.sisp_public.*`, `jdbc.sisp_public_yth.*`, `jdbc.sisp_yth.*`, `jdbc.drface.*` |
| `IFaceSuperService_hn` | `dataSource`, `drDataSource` | `jdbc.sisp.*` (`dataSource`), `jdbc.ncCollect.*` (`drDataSource`) |
| `InterfaceFunction` | `dataSource` | `jdbc.*` (single schema) |
| `NetPortalManagement` | managed by the *Bee* framework via `ds.xml` | none in `config.properties` |

Two inconsistencies worth knowing before you debug a connection problem:

- **`NetworkOfficeService` does not pool.** All five datasources are Spring
  `DriverManagerDataSource`, which opens a new physical connection per request. The
  `jdbc.minPoolSize` / `maxPoolSize` keys are not read there.
- **`IFaceSuperService_hn` ships an incomplete configuration.** `dataSource` needs
  `jdbc.sisp.*`, but the historical `config.properties` only defined
  `jdbc.ncCollect.*`. It "works" because the context sets
  `ignore-unresolvable="true"` and `default-lazy-init="true"`: the placeholder stays
  literal and the bean only fails on first use. The `.example` template defines both.

## 4. Request lifecycle

```
HTTP request
   │
   ├─ CharacterEncodingFilter        forces UTF-8 both ways
   ├─ HiddenHttpMethodFilter         allows PUT/DELETE from HTML forms
   │
   ▼
DispatcherServlet  (mapped to "/")
   │
   ├─ resolve handler  → @Controller method in ...server.controller
   ├─ bind params      → @RequestBody JSON / @RequestParam / path variables
   │
   ▼
Controller method
   │
   ├─ validate input, translate to VO
   ├─ call Service
   │     ├─ Service → DAO → SqlSessionTemplate → c3p0 → Oracle
   │     └─ Service → external gateway (Axis stub / HttpClient / Jersey)
   │
   └─ return Result / ResultVerify
   │
   ▼
@ResponseBody  → JSON (Gson or net.sf.json, per controller)
```

### Startup sequence

`StartupListener` implements `ServletContextListener` and extends `ContextLoader`. On
`contextInitialized`:

1. `DataBase.init()` — builds the Redis connection pool from `redis_core_database`.
2. `ThreadPoolUtil.init()` — creates the shared executor used by async work.
3. (Commented out) legacy direct JDBC bootstrap.

If Redis is unreachable, the listener throws and the application fails to deploy. This
is intentional: the token and dictionary caches are load-bearing.

## 5. Persistence

- **MyBatis, XML mappers.** No annotations, no JPA. SQL lives in `resources/mappings/`.
- **Multi-schema.** Each datasource has its own `SqlSessionFactory`; DAOs are separated
  into packages (`dao`, `dao_cssp`, `dao_dr`, `dao_yth`, ...) so the scanner can bind
  them to the right factory.
- **Transactions.** `DataSourceTransactionManager` per datasource. Cross-datasource
  writes are *not* atomic — if a flow writes to both `sisp` and `zhongjian`, there is no
  XA coordinator. Treat such flows as eventually consistent and make them idempotent.
- **Paging.** `Page<T>` carries `pageNo` / `pageSize` / `count` / `data`. Mapper XML uses
  Oracle `ROWNUM` ranges rather than a paging plugin.
- **Oracle specifics.** `jdbc:oracle:thin:@host:port:SID` and `@host:port/service`
  forms are both used — keep the form you were given, they are not interchangeable.

## 6. Cross-cutting concerns

| Concern | Implementation | Notes |
| --- | --- | --- |
| Encoding | `CharacterEncodingFilter` (`forceEncoding=true`) | `web.xml` |
| Auth | `tokenId` issued by `/iface/user/checkLogin` | Validated per request; the client SDK renews it on a timer |
| Response envelope | `Result`, `ResultVerify` | `statusCode` `"200"` / `"0"` |
| Caching | Redis via `JedisUtil`; dictionary cache in `DictionaryUtil` | TTL from `redis_iface_expx` |
| Logging | SLF4J + Log4j 1.2 | Per-module `log4j.properties` / `log4j.xml` |
| API session authorization | `BusinessFilter` | Enabled on selected `/iface/*` routes; not an XSS sanitizer — see [SECURITY.md](../SECURITY.md) |
| Captcha | Kaptcha `DefaultCaptcha` | Configured in `applicationContext.xml` |
| Excel export | `ExcelUtil` (`NetworkOfficeService`), EasyExcel/EasyPOI in older branches | Streams to the response |

## 7. Integration with external systems

Three integration styles coexist:

1. **SOAP via generated Axis stubs.** The provincial card gateway
   (`CARDBUESS_URL`) and hospital HIS systems are called through
   `*SoapBindingStub` classes. These are generated code — do not hand-edit; regenerate
   from the WSDL if the contract changes.
2. **SOAP via dynamic `org.apache.axis.client.Call`.** Used where the operation name
   varies at runtime, e.g. `SecQueryController`. Parameters are set by name and type
   (`XMLType.XSD_STRING`), so a typo fails at runtime rather than compile time.
3. **HTTP/JSON via Jersey or Apache HttpClient.** Face verification, job board,
   electronic licence library, weather. Wrapped by `HttpClientUtil` / `WebClient` /
   `SSLClient` (the last one uses the JVM default trust store and strict
   hostname verification; install a private CA in the JVM trust store if required).

Every outbound call is expected to be wrapped so that a downstream failure produces a
`Result` with a failure code instead of an unhandled exception. That contract is not
enforced by the framework; check each call site.

## 8. Design decisions and trade-offs

**XML over annotations.** Configuration is explicit and greppable, which matters when
you inherit a system with fifteen datasources. The cost is verbosity and no compile-time
safety.

**Per-module `common` copies over a shared library.** Independent deployability won.
The cost is drift: `Page` has a different constructor set in `InterfaceFunction` than in
`IFaceSuportService`.

**Static state in the client SDK.** `IFaceHttpsClient` keeps `ip`, `port`, `tokenId` in
static fields. It is simple and matches the "one terminal, one server" deployment, but
it means one JVM cannot talk to two servers, and `ClientInit` throws if called twice.

**No DI for gateway clients.** SOAP stubs are instantiated at call sites rather than
injected. This makes unit-testing those paths hard; it is the main reason test coverage
is low.

**Fail-fast on infrastructure.** The application refuses to start without Redis or a
usable datasource, rather than degrading. Appropriate for an internal service behind a
health check; hostile for local development.
