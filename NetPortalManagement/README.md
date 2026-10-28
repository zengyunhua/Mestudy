# NetPortalManagement

Lightweight administration for the citizen portal: portal user accounts, published policy
documents, and the registry of external web connections the portal links out to.

| | |
| --- | --- |
| Artifact | `com.tecsun.sisp:NetPortalManagement` |
| Packaging | `war` |
| Version | `V1.0.0.150630` |
| Java files | 11 |
| Endpoints | 0 (framework-dispatched) |
| Datasources | Oracle + Redis, via `ds.xml` |

---

## Why this module looks different

Every other module in this workspace is Spring MVC. This one is built on the internal
**Bee** framework (`bee.cloud.*`), which provides its own request dispatch, persistence
and entity layer on top of JAX-RS (Jersey 1.15).

Consequences you will notice immediately:

- There are **no `@Controller` classes** and no `@RequestMapping`. Entry points are
  `*Application` classes annotated with `@Application`.
- Persistence is not MyBatis. Entities extend Bee's `Table`; services extend
  `SuperApplication` and get query capability from the framework.
- Configuration is split across `ds.xml` (datasource) and `bee_application.xml`
  (framework bootstrap), not `applicationContext.xml`.

## Layout

```
com/tecsun/sisp/npm/
├── bean/
│   ├── NetUserInfoVO.java        # extends bee Table — portal user
│   ├── PolicyVO.java             # extends bee Table — policy document
│   └── WebConnectionVO.java      # extends bee Table — external link registry
├── service/
│   ├── NetSuperApplication.java  # base: binds the sisp_source datasource
│   ├── NetUserInfoApplication.java
│   ├── PolicyApplication.java
│   └── WebConnectionApplication.java
└── util/
    ├── LogUtil.java              # writes operation logs to the platform
    ├── MD5Generator.java         # password hashing
    ├── PublicMethod.java
    └── StaticBean.java           # configuration accessor
```

Resources:

```
src/main/resources/
├── bee_application.xml   # points the framework at ds.xml
├── ds.xml                # datasource: Oracle + Redis
├── config.properties     # platform_context_path, inner_proxy_ip/port
└── log4j.xml
```

## Domain model

| Entity | Purpose | Notable fields |
| --- | --- | --- |
| `NetUserInfoVO` | Portal user account | Login name, MD5 password hash, status |
| `PolicyVO` | Policy document | Title, publish date, content/attachment |
| `WebConnectionVO` | External site registry | Name, URL, sort order, enabled flag |

## Configuration

### `ds.xml`

```xml
<ds>
  <source name="sisp_source" type="oracle">
    <store url="jdbc:oracle:thin:@HOST:1521:orcl"
           driver="oracle.jdbc.driver.OracleDriver"
           database="orcl" encode="UTF-8"
           max="100" min="1"
           user="CHANGE_ME" pwd="CHANGE_ME"
           readonly="false" type="oracle"/>
    <store url="REDIS_HOST" port="6379" min="1" max="100"
           type="redis" readonly="false"/>
  </source>
</ds>
```

> The current tree contains only `ds.xml.example` placeholders. Historical versions of
> `ds.xml` contain credential-shaped values whose real-world use remains unverified; see
> [SECURITY.md](../SECURITY.md). Never commit a local `ds.xml`.

### `bee_application.xml`

```xml
<app>
  <config type="datasource">/ds.xml</config>
</app>
```

### `config.properties`

```properties
platform_context_path=/sisp
inner_proxy_ip=127.0.0.1
inner_proxy_port=80
```

## Build and dependency boundary

From the repository root, `mvn -B verify` builds the four default public modules.
This module remains opt-in: `mvn -B -Pprivate-deps -pl NetPortalManagement -am verify`
requires authorized access to the private artifacts. The maintainer reports it compiles
and deploys in their environment; that is not evidence of a public-dependency
build or independently verified deployment. A fork user has reported a local project
build, but has not specified whether it included this module. Never add proprietary
jars or credentials to the repository.

| Artifact | Evidence in this module | Decision |
| --- | --- | --- |
| `bee:bee.cloud:1.0.4` | Java imports `Doe`, `Table`, annotations, `CndFactory`, `Cnd`, `Select` in entities and services. | Compile-time requirement; keep unless replacing the Bee persistence APIs. |
| `bee:bee.cloud.service:1.0.4` | `NetSuperApplication` extends `SuperApplication`; active `web.xml` names `StartupListener` and `bee.cloud.service.rest`. | Compile- and startup-time requirement; keep. |
| `bee:bee.cloud.impl:1.0.0` | No direct Java import; framework startup and `CndFactory`/query execution may need its implementation. | Runtime role is plausible, not verified without private artifacts; do not remove on import search alone. |
| `bee:bee.cloud.entity:1.0.0` | `@Application(entityPkg=...)` explicitly lists `bee.cloud.entity`; no direct Java import. | Runtime entity discovery may need it; verify with authorized startup/query tests before considering removal. |
| `bee:xx.tool:1.0.1` | `LogUtil` and `NetUserInfoApplication` import `com.xx.util.property.Config`. | Compile-time requirement; keep unless replacing configuration access. |
| `com.tecsun.sisp_public:aamanagement-client:1.0.5` | Active `web.xml` registers `AAMClient4Jersey` for `/*`; no Java import. | Startup/authentication requirement; never remove without a tested equivalent security filter. |

No other module declares these private coordinates. Previously commented-out AAM filters
in `InterfaceFunction` and `NetworkOfficeService` were inert and have been removed.
Removing a direct Bee dependency would break compilation; dropping a runtime-only
artifact based on imports alone risks broken startup or unauthorized endpoints. To
reduce the private dependency surface, first obtain its actual artifact contents and
runtime contract in an authorized environment, test startup, protected/unauthorized
requests and representative database queries, then remove one artifact at a time with
before/after WAR and smoke-test evidence. A public replacement of Bee or the AAM
filter is a separate, security-sensitive migration, not a POM-only cleanup.

## Notes for contributors

- **Do not port this to Spring MVC as a drive-by change.** It is a distinct framework
  integration and deserves its own discussion.
- **`MD5Generator` is not a password hashing function.** MD5 without a salt is broken for
  credential storage. Any new code should use bcrypt/argon2; migrating the existing
  hashes needs a compatibility path.
- **`LogUtil` writes operation logs through the platform proxy** (`inner_proxy_ip`), so
  logging silently degrades if that proxy is unreachable.
