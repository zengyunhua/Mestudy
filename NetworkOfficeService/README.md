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
sbdt.file_savepath=/data/upload/sbdt
```

`sbdt.file_savepath` is the root for government-notice attachments, read by
`GovernmentController`. It is read through `Config.getInstance().get(...)`, which returns
`null` for a missing key rather than failing — so leaving it unset turns the download path
into the literal string `null/...` and attachment downloads fail at request time, not at
startup.

> `config.properties` in this module defines `iinner_proxy_ip` (double *i*). No code reads
> it, so it is inert — but do not copy the spelling.

## Build

```bash
cd NetworkOfficeService
cp src/main/resources/config.properties.example src/main/resources/config.properties
mvn clean package
```

`SpringContextLoadTest` runs as part of `package`. It loads the real
`applicationContext.xml` and asserts that each datasource reads its own `jdbc.<schema>.*`
keys and that every scanned mapper statement is bound. No database is needed — the
datasources connect lazily and MyBatis parses mapper XML without a server. Values come
from `src/test/resources/config.properties`, which shadows the production file on the test
classpath. That one file is committed — an explicit exception in `.gitignore` — because the
test cannot run without it; every password in it is `CHANGE_ME`, and it gives each
datasource a different url so a bean wired to the wrong key cannot pass by coincidence.

On Windows, if the surefire fork fails with *"The forked VM terminated without properly
saying goodbye"*, your `%TEMP%` path contains non-ASCII characters. Point Maven at an ASCII
temp directory — see [docs/TROUBLESHOOTING.md](../docs/TROUBLESHOOTING.md):

```bash
mvn -Djava.io.tmpdir=D:\mvn-tmp clean package
```

```bash
cp target/NetworkOfficeService-*.war $CATALINA_HOME/webapps/sisp.war
```

## Notes for contributors

- **Each `MapperScannerConfigurer` is paired with one `SqlSessionFactory`, and that
  factory's `mapperLocations` must list the XML for *every* `@MyBatisDao` interface in the
  package it scans.** Adding a DAO interface without adding its mapper to the right
  factory still compiles and still creates the bean; the failure shows up only when a
  method is called, as `Invalid bound statement (not found)`.
- **Never point `<mvc:resources>` at `WEB-INF/` itself.** Both mappings in
  `springMVC-servlet.xml` used `location="WEB-INF/"`, which serves any path beneath it —
  including `classes/config.properties` (datasource credentials) and `lib/*.jar` — to
  anyone who can reach `/views/**`. They are now scoped to `/WEB-INF/views/` and
  `/WEB-INF/ftl/`. Keep that shape when adding an asset directory: a resource handler
  bypasses the container's own `WEB-INF` protection.
- **`CommonExceptionHandler` converts unhandled exceptions into a `Result`.** A 500 from
  this module usually means the handler itself failed.
- **Signature is an outbound dependency.** Signing requires the external service, the
  template directory and the font to all be present. Test the failure path, not just the
  happy path.
- **Draft files are on local disk**, not object storage. Horizontal scaling requires a
  shared mount, otherwise a draft attached on node A is invisible on node B.
- **`com.tecsun.sisp.net.modules.dao.yth.QueryByDao`** has no mapper XML and no scanner, so
  no bean is created for it. Nothing injects it today; wiring it up needs a mapper and a
  scanner entry, not just the interface.
- **`src/main/webapp/WEB-INF/lib/print-2.1.1.jar`** is vendored into the WAR but every
  reference to its single class (`Tecsun`) is commented out. It is dead weight in the
  artifact.
