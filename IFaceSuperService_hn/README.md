# IFaceSuperService_hn

The Henan (河南) deployment variant of the interface service. Same layering as
`IFaceSuportService`, but scoped to the provincial gateway: card services and
login/token only, with a different datasource and a newer version string.

| | |
| --- | --- |
| Artifact | `com.tecsun.sisp:IFaceSuportService` ⚠️ |
| Packaging | `war` |
| Version | `V1.0.0.190107` |
| Java files | 61 |
| Endpoints | 8 |
| Datasources | 2 |

> **Artifact id collision.** This module declares the *same* `artifactId`
> (`IFaceSuportService`) as `IFaceSuportService/`, distinguished only by version. If you
> install both into the same local repository, one will shadow the other depending on
> which version you depend on. Build them into separate repositories, or rename one
> (tracked on the [roadmap](../docs/ROADMAP.md)).

---

## Responsibility

- **Card services** against the provincial card gateway (`/iface/card`, 6 endpoints).
- **Login / token** (`/iface/user`, 2 endpoints).
- **Provincial collection interface** — SOAP calls to the provincial biometric and
  collection services.

This is a *narrower* module than `IFaceSuportService`: it drops employment, hospital,
bank, fee, WeChat and policy domains, keeping only what the Henan terminal needs.

## Layout

```
com/tecsun/sisp/iface/
├── client/                     # client SDK (same shape as IFaceSuportService)
├── common/
│   ├── page/
│   ├── util/                   # AESUtil, PicUtil, ...
│   └── vo/
└── server/
    ├── controller/
    │   ├── BaseController.java
    │   ├── card/               # CardInfoController
    │   └── so/                 # UserController
    ├── entity/card, entity/requestBean
    ├── model/dao, model/service
    └── util/
```

## Endpoints

| Base path | Controller | Endpoints |
| --- | --- | :---: |
| `/iface/card` | `CardInfoController` | 6 |
| `/iface/user` | `UserController` | 2 |

## Configuration

Two datasources, both c3p0 pools:

| Bean | Key prefix | Purpose |
| --- | --- | --- |
| `dataSource` | `jdbc.sisp.*` | Core platform schema |
| `drDataSource` | `jdbc.ncCollect.*` | Provincial collection schema |

> **The historical `config.properties` is incomplete.** It defined only
> `jdbc.ncCollect.*`, while `dataSource` requires `jdbc.sisp.*`. The module appears to
> start anyway because the context sets `ignore-unresolvable="true"` and
> `default-lazy-init="true"` — the unresolved placeholder stays literal and the bean only
> fails when something first uses it. The `.example` template defines both key sets; use
> it rather than copying the old file.

Note that neither bean reads `jdbc.drface.*` despite the `dr` prefix in the bean name.

Additional keys specific to this deployment:

```properties
# Provincial biometric gateway
biometrics.endpoint=http://gateway:7001/hnScrzService/services/ScrzService?wsdl
biometrics.namespace=http://server.wei.com
biometrics.user=CHANGE_ME
biometrics.pwd=CHANGE_ME
biometrics.typeid=C00002
biometrics.key=CHANGE_ME

# Image archive directory
authentication=/data/asc
```

`biometrics.*` points at an external government gateway. It will not resolve outside the
target network — expect connection timeouts, not fast failures.

## Build

```bash
cd IFaceSuperService_hn
cp src/main/resources/config.properties.example src/main/resources/config.properties
mvn clean package
```

```bash
cp target/IFaceSuportService-V1.0.0.190107.war $CATALINA_HOME/webapps/sisp.war
```

## Notes for contributors

- **Divergence is intentional but unbounded.** This module is a fork of the core service,
  so a fix applied here usually needs the same fix upstream and vice versa. Check both.
- **Windows paths in the original config.** `authentication=E:\\ASC2019` is a Windows
  path. Normalise to a POSIX path if you deploy on Linux.
- **Keep it small.** The point of this module is the reduced surface. Adding a domain
  here that already exists upstream should be a conscious decision, not a copy-paste.
