# NetworkOfficeService

The online government-service portal ("网办"). Citizens draft an application, attach
documents, submit it, and back-office staff audit it. Signed applications are stamped
into PDFs by an external signature service.

| | |
| --- | --- |
| Artifact | `com.tecsun.sisp:NetworkOfficeService` |
| Packaging | `war` |
| Version | `V1.0.0.180612` |
| Java files | 128 |
| Endpoints | 72 |
| Datasources | 5 |

---

## Responsibility

- **Application drafts** — create, edit, attach files, submit, delete, list by status
  (`DraftsController`, 42 mappings — the busiest controller in the workspace).
- **Audit** — back-office review of submitted applications (`AuditingController`).
- **Electronic signature** — renders a stamp onto a submitted PDF via an external service
  and exposes a download URL (`SignController`).
- **Government notices** — adapter for published notices (`GovernmentController`).
- **Area data** — administrative division lookup, used for cascading region selectors.
- **Excel export** — conditional export of application lists (`ExcelUtil`).

## Layout

```
com/tecsun/sisp/net/
├── common/
│   ├── page/                    # paging
│   └── vo/faceverify/           # verification VOs
├── exception/                   # shared exception types
├── export/excel/                # ExcelUtil — streamed export
├── interceptor/                 # request interceptors
├── modules/
│   ├── controller/
│   │   ├── business/            # BusinessHandleController
│   │   ├── common/              # CommonExceptionHandler
│   │   ├── gov/                 # GovernmentController
│   │   ├── sign/controller/     # SignController (@RestController)
│   │   └── userNet/             # DraftsController, AuditingController
│   ├── dao/                     # area_sisppublic/ common/ dr/ drafts_sisp/ gov/ yth/
│   ├── entity/                  # business/ request/ response/
│   ├── exception/
│   └── service/                 # business/ + impl/
└── util/                        # CommUtil, ...
```

`QueryController` lives under `controller/` and serves `/net/query`.

## Endpoints

| Base path | Controller | Endpoints |
| --- | --- | :---: |
| `/userNet/drafts` | `DraftsController` | 42 |
| `/net/query` | `QueryController` | 10 |
| `/userNet/audit` | `AuditingController` | 8 |
| `/adapter/notice` | `GovernmentController` | 6 |
| `/net/business` | `BusinessHandleController` | 3 |
| `/adapter/sign` | `SignController` | 3 |

### Draft lifecycle

```
create ──► edit ──► attach ──► submit ──► audit ──► signed PDF
  │          │                                  │
  └──────────┴──► delete (allowed until submit) └──► reject ──► back to edit
```

`DraftsController` exposes the whole cycle. State is held in the `drafts_sisp` schema;
audit records in `gov`. There is no workflow engine — the state machine is the `status`
column plus guards in the service layer.

## Configuration

| Bean | Key prefix |
| --- | --- |
| `sispDataSource` | `jdbc.sisp.*` |
| `sisppublicDataSource` | `jdbc.sisp_public.*` |
| `sispPublicYthDataSource` | `jdbc.sisp_public_yth.*` |
| `sispYthdataSource` | `jdbc.sisp_yth.*` |
| `drDataSource` | `jdbc.drface.*` |

> **No connection pooling.** All five datasources are Spring
> `DriverManagerDataSource`, which opens a new physical connection per request. The
> `jdbc.minPoolSize` / `maxPoolSize` / `maxIdleTime` / `initialPoolSize` keys are not read
> by this module. Under any real load this is the first thing to fix — swapping in c3p0 or
> HikariCP is a contained change.

Signature-related keys:

```properties
signpdf.data.url=http://sign-service:80
signpdf.font.path=/usr/share/fonts/winfonts/ARIALUNI.TTF
signpdf.save.path=/disk/deploy/pdfsign/pdfsign_temp
signpdf.template.path=/disk/deploy/pdfsign/pdfsign_template/
signpdf.download.url=/sisp/signdownload/sign/download/
```

The font path must exist **on the application host**; a missing font fails at signing
time, not at startup.

File storage:

```properties
SCFILEPATH=/data/upload/sc
MATTERSCFILEPATH=/data/upload/matter
```

> `config.properties` in this module defines `iinner_proxy_ip` (double *i*). No code reads
> it, so it is inert — but do not copy the spelling.

## Build

```bash
cd NetworkOfficeService
cp src/main/resources/config.properties.example src/main/resources/config.properties
mvn clean package
```

```bash
cp target/NetworkOfficeService-*.war $CATALINA_HOME/webapps/sisp.war
```

## Notes for contributors

- **`CommonExceptionHandler` converts unhandled exceptions into a `Result`.** A 500 from
  this module usually means the handler itself failed.
- **Signature is an outbound dependency.** Signing requires the external service, the
  template directory and the font to all be present. Test the failure path, not just the
  happy path.
- **Draft files are on local disk**, not object storage. Horizontal scaling requires a
  shared mount, otherwise a draft attached on node A is invisible on node B.
