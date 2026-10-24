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

> The committed `ds.xml` contains a real database password. Replace it before publishing
> or sharing the repository — see [SECURITY.md](../SECURITY.md).

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

## Build

```bash
cd NetPortalManagement
mvn clean package
```

**This module is the hardest to build publicly.** `@Application(entityPkg = {...})`
references `com.tecsun.sb.entity` and `bee.cloud.*`, which come from proprietary jars in
the internal Nexus. Without those artifacts the module will not compile. It is kept in
the workspace because the *pattern* — framework-driven dispatch with an XML datasource —
is worth seeing, and because it documents the original deployment topology.

If you want to experiment, the realistic path is to stub `SuperApplication` and `Table`
locally; a full port to Spring MVC would be a rewrite, not a patch.

## Notes for contributors

- **Do not port this to Spring MVC as a drive-by change.** It is a distinct framework
  integration and deserves its own discussion.
- **`MD5Generator` is not a password hashing function.** MD5 without a salt is broken for
  credential storage. Any new code should use bcrypt/argon2; migrating the existing
  hashes needs a compatibility path.
- **`LogUtil` writes operation logs through the platform proxy** (`inner_proxy_ip`), so
  logging silently degrades if that proxy is unreachable.
