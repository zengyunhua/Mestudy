# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this
project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) for module
versions going forward.

Because this repository is an archive of independently versioned modules, entries are
grouped by scope rather than by a single release number.

## [Unreleased]

### Added

- **Context-load tests for the other three public modules.** `IFaceSuportService`,
  `IFaceSuperService_hn` and `InterfaceFunction` now each have a `SpringContextLoadTest`,
  so all four modules in the default reactor fail the build on broken wiring. Each test
  loads the real `applicationContext.xml` with no database. Where a module has more than
  one datasource, the test gives each one a distinct url in
  `src/test/resources/config.properties` and asserts that every bean reads its own key.
  c3p0 pools are built with `initialPoolSize=0`, so they acquire no connection.
- **A reflection check for unbound mapper methods, in all four modules.** The test walks
  every `MapperFactoryBean` and asserts that each interface method resolves to a
  statement in the factory its scanner uses. The lookup is the same one MyBatis'
  `MapperMethod.SqlCommand` makes. Adding a DAO method without its SQL now fails at build
  time, where before it failed on the first call. Hand-picked statement ids could not
  catch this. `IFaceSuperService_hn/.../SecQueryDAO.xml` looks like it defines
  `selectReceive` and `insertSend`, but its whole body is commented out. A separate
  assertion stops the check from passing vacuously when the scan finds no mappers.
- **A context-load test for `NetworkOfficeService`.** `SpringContextLoadTest` loads the
  real `applicationContext.xml` with no database and asserts the two things a successful
  `mvn package` cannot see: that each `DriverManagerDataSource` reads its own
  `jdbc.<schema>.url` key, and that every statement exposed by a scanned `@MyBatisDao`
  interface is bound in the matching `SqlSessionFactory`. It runs during `package` and
  needs no Oracle instance — the datasources connect lazily and MyBatis parses mapper XML
  while building its `Configuration`. Values come from
  `src/test/resources/config.properties`, which shadows the production file on the test
  classpath. This is the first test in the repository that fails on broken wiring rather
  than on a broken helper.
- **A CI gate on what the build actually produced.** `scripts/verify_public_build.py`
  inspects every module after `mvn verify`: each needs exactly one nonempty WAR
  and at least one Surefire XML report, with no failures or errors and at least one test
  that actually ran. Maven exits 0 when `skipTests` is set or when forking is disabled and
  nothing is collected, so the WAR and the reports are checked instead of trusting the
  exit code. `scripts/test_verify_public_build.py` covers the empty-WAR, failing,
  all-skipped, missing-report and malformed-report cases.
- **A local history inventory.** `scripts/audit_git_history.py` walks
  `git rev-list --objects --all` and counts historical blob/path pairs by the same
  categories `check_sensitive.py` applies to the working tree, printing reason labels and
  counts but never a matched value. It is triage for the owner, not remediation: it cannot
  distinguish a synthetic fixture from a provisioned credential, and it runs outside CI
  on purpose, where the checkout is shallow. The counts it produced at `d8a301d` are
  recorded in `docs/SECURITY_REVIEW.md`.
- **Unit tests for the portal module's configuration and token filter.** Fourteen tests
  that need no database: six for `AppConfig` (a system property wins, a blank value counts
  as absent, `required` throws naming the key) and eight for `ApiTokenFilter` (startup
  refuses a missing, blank or `CHANGE_ME` token; a missing or wrong token gets a 401 and
  never reaches the application; `X-API-Token` and `Authorization: Bearer` are both
  accepted; CORS preflight is not authenticated). These are what let the module join
  `scripts/verify_public_build.py`, which now gates all five modules — 45 tests in total.

### Known issues

- **`POST /userNet/drafts/insertUserRol` and `/userNet/drafts/queryUserRloe` in
  `NetworkOfficeService` always fail** with `Invalid bound statement (not found)`. The
  reflection check above found them. `AreaDAO.insertUserRol` and `AreaDAO.queryUserRloe`
  have no SQL in `areaDAO.xml` or anywhere in git history, and writing it needs the real
  role-table schema. The test lists them by name in `BROKEN_ENDPOINT_UNBOUND`, and the
  module README documents them.
- **Five more unbound mapper methods are dead code.** They have no SQL and no reachable
  caller: `NetPersonDAO.findAllByPersonState` and `DRPersonInfoDao.insertView` in
  `IFaceSuportService`, and `BusinessHandleDao.getBusinessHandlePageMapByCondition`,
  `DraftsDAO.deleteInformation` and `PersonDAO.updateDraftsMessage` in
  `NetworkOfficeService`. Each module's test lists them by name with the reason. They are
  harmless until someone calls one.
- `Database.open()` returns a fresh `DriverManager` connection per call and closes it in
  the caller; there is no pool. Fine for the traffic this module sees, but it is the
  first thing to change under load.
- The JDBC repository has no automated tests. The module is now in the gate, but what it
  asserts is `AppConfig`'s resolution order and `ApiTokenFilter`'s rejection behaviour —
  both stop at the boundary and need no database. `NetPortalRepository` itself is still
  uncovered and needs an Oracle-compatible fixture.
- `ApiTokenFilter` is one shared secret, not per-user authentication. It replaces a
  filter that was never exercised in this repository, so it should be treated as a
  boundary to build on, not as equivalent to the platform's real auth.
- `bee_application.xml` and `ds.xml.example` are kept in the tree as historical
  artifacts. Nothing reads them.

### Fixed

- **`NetworkOfficeService` no longer packages `servlet-api` and `spring-test` into the
  WAR.** `javax.servlet:servlet-api` was at compile scope, so it landed in
  `WEB-INF/lib`; Tomcat logs a validation warning and skips the jar, and other containers
  can fail at startup with a `ClassCastException` on `javax.servlet` types. It is now
  `provided`, matching the sibling modules. `spring-test` was likewise at compile scope
  and is now `test`. Verified against the built WAR: 61 jars under `WEB-INF/lib`, neither
  artifact among them.
- **`sispYthdataSource` read the wrong URL key.** It declared
  `${jdbc.sisp_public_yth.url}` while every other property on the bean used
  `jdbc.sisp_yth.*`, so the bean pointed at a different schema than its name and its
  siblings imply. Corrected to `${jdbc.sisp_yth.url}`. This is the class of defect that
  surfaces as missing rows rather than as an error, which is why the new test asserts
  every datasource URL explicitly instead of trusting that the context started.
- **Two mapper XML files were not loaded.** `sispSqlSessionFactory` listed only
  `mappings/draftsDAO.xml`, but the package its `MapperScannerConfigurer` covers also
  holds `PersonDAO` and `BusinessHandleDao`. Their beans were still created, and every
  call failed at runtime with `Invalid bound statement (not found)`. All three are now in
  `mapperLocations`.
- **`/views/**` and `/ftl/**` served everything under `WEB-INF`.** Both
  `<mvc:resources>` mappings used `location="WEB-INF/"` rather than a specific asset
  directory, so `/views/classes/config.properties` returned the datasource credentials and
  `/views/lib/*.jar` returned the module's jars. Scoped to `/WEB-INF/views/` and
  `/WEB-INF/ftl/`. Anyone who deployed this module from an earlier commit should treat the
  credentials in its `config.properties` as disclosed to whoever could reach `/views/**`.
- **`web.xml` declared a welcome file the container cannot serve.**
  `/WEB-INF/views/index.html` sits under `WEB-INF`, which a container never serves
  directly, and the file does not exist either — so `/` returned an error instead of
  falling through to the `DispatcherServlet`. The list is removed.
- **`display-name` read `InterfaceFunction`.** Copy-paste from a sibling module; corrected
  to `NetworkOfficeService`.
- **`config.properties.example` was missing `sbdt.file_savepath`.** `GovernmentController`
  reads it through `Config.getInstance().get(...)`, which returns `null` for a missing key,
  so an unset value turns the download path into the literal string `null/...` and notice
  attachments fail at request time rather than at startup. Added, with a comment naming
  the symptom.
- **`InterfaceFunction` had the same three defects.** The review that produced the fixes
  above was then applied to the other modules, and this one carried all of them:
  `javax.servlet:servlet-api` and `spring-test` were at compile scope and both sat in
  `WEB-INF/lib`; and its `/views/**` mapping also used `location="WEB-INF/"`, so
  `/views/classes/config.properties` and `/views/lib/*.jar` were reachable. Both scopes are
  corrected and the mapping is scoped to `/WEB-INF/views/`, which is also the view
  resolver's prefix. The duplicate `WEB-INF/jquery.js` — byte-identical to
  `WEB-INF/views/jquery.js` and present only to satisfy the old location — is deleted, so
  `/views/jquery.js` still resolves and the WAR is 283 KB smaller.
- **CI invoked a script that was not in the repository.** The build job added in
  `d8a301d` calls `python3 scripts/verify_public_build.py`, but the file was left
  untracked, so that job stopped at `can't open file` on every push — after the Maven
  build had already passed, which is the point at which the check was supposed to speak.
  The script and its test are committed with the rest of this batch.

### Changed

- **All five modules now build from public dependencies; `NetPortalManagement` was
  rewritten off the proprietary Bee stack.** This was the last hold-out, and the review
  in this file previously called a port "a rewrite, not a patch" — it turned out to be
  correct, and this is that rewrite. Removed: `bee:bee.cloud`, `bee:bee.cloud.service`,
  `bee:bee.cloud.impl`, `bee:bee.cloud.entity`, `bee:xx.tool`,
  `com.tecsun.sisp_public:aamanagement-client`, and with them the `private-deps` profile
  in the root POM. Replaced by: plain DTOs instead of `Doe`/`Table` entities;
  `NetPortalRepository` instead of `Cnd`/`Select`; a standard Jersey `ServletContainer`
  instead of Bee startup dispatch; `AppConfig` instead of `com.xx.util.property.Config`;
  and `ApiTokenFilter` instead of `AAMClient4Jersey`. `mvn -B verify` at the root now
  produces five WARs.
- **`NetPortalManagement` configuration moved from `ds.xml` into `config.properties`.**
  `AppConfig` resolves each key from a JVM system property, then `config.properties`,
  then an `NPM_*` environment variable (`jdbc.url` → `NPM_JDBC_URL`), and
  `AppConfig.required(...)` throws at first use rather than returning `null`. That is a
  deliberate behaviour change: the old loader handed back `null` for a missing key and
  the failure surfaced later as a URL containing the string `null`. The template and
  both configuration guides were updated to match, because they still pointed at
  `ds.xml`.
- **`ApiTokenFilter` fails closed.** It reads `api.token` at startup and throws if the
  key is absent, blank or still `CHANGE_ME`, so a misconfigured deployment refuses to
  serve rather than serving unauthenticated. The comparison is constant-time and the
  token is accepted from either `X-API-Token` or `Authorization: Bearer`.
- **`NetPortalRepository` uses parameterized JDBC throughout.** The one place SQL is
  concatenated is the `to_char` date format in `connectionStats`, and its value comes
  from a whitelist selected by `searchType` (`yyyy` / `yyyy-MM` / `yyyy-MM-dd`), never
  from the request body.

- **Each document now states the boundary of its claim.** Several guides described all
  five modules as if they shared one architecture, one configuration mechanism and one
  response envelope. `NetPortalManagement` shared none of them at that point — it
  dispatched through Bee, read `ds.xml` instead of `config.properties`, and did not
  return `Result` — so the architecture, API, configuration and getting-started documents
  scope each statement to the Spring MVC modules and point at the module guide for the
  exception. The rewrite described above has since narrowed that gap, but the scoping is
  still the honest way to read those documents.
  Test counts and the fifth-module build are attributed to the run and the reporter that
  produced them rather than asserted flatly, because neither has been reproduced from a
  clean clone. The CI claims now cite a specific run — #27 on `352fbe8`, the first green
  one after the artefact gate was wired in ahead of the script it calls — instead of
  asking the reader to go and check.
- **The `NetPortalManagement` dependency boundary is an inventory, not a sentence.**
  Before the rewrite, `NetPortalManagement/README.md` listed all six private coordinates
  with the evidence that kept each one — Java imports for the compile-time ones,
  `web.xml` registrations and `@Application(entityPkg=...)` for the runtime-only ones.
  That distinction is the whole point: "no import references it" is exactly the reasoning
  that correctly removed the AAM client from the two public modules and would have broken
  startup here. It is also why the port replaced the runtime pieces instead of deleting
  them — a POM-only cleanup would have produced a WAR that started and then failed on the
  first query.
- **The commented-out AAM filter is gone from `InterfaceFunction` and
  `NetworkOfficeService`.** Both `web.xml` files carried the same
  `<!-- <filter> AAMClientCommon ... -->` block — inert, but read by reviewers as
  configuration. Removing it also removes the last mention of `com.tecsun.sisp_public`
  from both modules, which turns the earlier "nothing references it" claim from arguable
  into obvious.

- **The WAR plugin is pinned to 2.2.** Without an explicit version Maven takes it from the
  super-POM, so the packaged artifact depended on which Maven version ran the build. 2.2
  is what Maven 3.8.x selects, so the output does not change — it just stops drifting.

## [1.1.0] - 2026-10-26

### Added

- **A maintainer readiness checklist.** `docs/OPEN_SOURCE_APPLICATION_GUIDE.md` states
  what a reviewer can verify today, records the verification boundary (one
  maintainer-workstation run, not a clean-room clone), describes how an outside
  contributor can reproduce the build independently, and lists the owner-only blockers
  with the evidence each would need. It deliberately does not estimate eligibility for
  any programme.
- **CI rejects tracked sensitive values.** `scripts/check_sensitive.py` scans
  `git ls-files` and fails the `hygiene` job on PEM private-key headers, 18-digit
  ID-shaped values outside the documented placeholder, non-placeholder values assigned
  to a password/key/secret/token name, and a tracked `config.properties` or `ds.xml`
  under `src/main/resources`. It prints `path:line:reason` and never the value. Worth
  recording that `SECURITY.md` described such a check before it existed; this closes the
  gap between the claim and the code. The scan is a heuristic over the current checkout
  only - it says nothing about history or about provider state.

### Changed

- **`InterfaceFunction` moved into the default reactor.** It declared
  `com.tecsun.sisp_public:aamanagement-client:1.0.5` and `bee:xx.tool:1.0.0`, but nothing
  in the module references either: there is no import of `com.tecsun.sisp_public` or
  `bee.*`, no `Class.forName`, and no matching string in the resources. Removing the two
  declarations lets the module compile and package into a war against Maven Central
  alone. The declarations were dead weight copied from a sibling POM, not a real
  dependency.
- **`NetworkOfficeService` moved into the default reactor too, via a reconstructed
  class.** Its declared private coordinates were equally unused, but removing them
  exposed something different: four classes and `mappings/draftsDAO.xml` refer to
  `com.tecsun.sisp.net.modules.entity.response.BanjianVo`, which was never published
  with the rest of the module. Deleting a `<dependency>` does not remove a symbol the
  source uses, it only reveals it. `BanjianVo` has been rebuilt with 43 properties
  derived from evidence rather than guesswork — the sibling query `queryByOfficeStatus`
  maps the same three tables into `DraftsVo`, and the remaining columns are proven to
  exist by other statements in the same mapper. The provenance and the residual risk are
  recorded in the class Javadoc. This is the one place in the repository where the code
  is not the original.
- **What is left, precisely.** `NetPortalManagement` is the only hold-out and it is a
  real one: its beans carry `@TableName` / `@Column` and extend `Doe`, and its service
  layer queries through `Cnd` / `Select`, so it needs the `bee.cloud` persistence
  framework and stays behind the profile. `mvn -B package` now builds four modules and
  runs the test suite green.
- **The default reactor is now the publicly buildable modules.** `mvn package` at the
  repository root used to walk into `NetworkOfficeService` third and fail on
  `com.tecsun.sisp_public:aamanagement-client:1.0.5` / `bee:xx.tool:1.0.0`, which meant a
  fresh clone could not complete a build at all. The modules that need those jars now
  live behind the `private-deps` profile: `mvn -B package` builds the public modules and
  succeeds, `mvn -B -Pprivate-deps package` restores the five-module reactor for anyone
  who has the private jars installed. The modules were isolated rather than stripped,
  because the dependencies were assumed to be real — that assumption later turned out to
  be wrong for `InterfaceFunction`, see above.
- **Java 8 baseline.** `maven.compiler.source` / `maven.compiler.target` raised from
  `1.7` to `1.8` in the root POM, which is now the single source of truth for the
  language level. The per-module `maven-compiler-plugin` overrides in
  `IFaceSuportService` and `NetworkOfficeService` were removed so the five modules
  cannot drift apart again; the empty `<build>` block that was left in
  `NetworkOfficeService` is gone too.
- No source change was needed. The code is Java 7-compatible and compiles unchanged at
  source/target 1.8; Java 8 constructs (lambdas, method references, `java.time`,
  streams) are now allowed in new code but existing classes were deliberately left
  alone.
- Documentation aligned with the new baseline: README (badge, tech-stack row,
  prerequisites, roadmap), `docs/GETTING_STARTED.md` (JDK requirement),
  `docs/TROUBLESHOOTING.md` (the source/target warning entry now describes the 1.8
  "obsolete option" warning instead of the old 1.7 one), `docs/ROADMAP.md` and
  `CONTRIBUTING.md` (language level rule).
- **Build and deployment are documented as separate things.** A green `mvn -B verify`
  is not evidence of a running service, and the guides no longer imply otherwise: step 4
  became "Build and verify independently", step 5 became "Deploy and smoke-test
  (requires infrastructure)", `config.properties` is marked deployment-only, and the
  smoke test now describes an outcome to check instead of "a JSON body with statusCode
  means the stack is up". The README badge reads `Java 8` rather than `8+`, because JDK 9
  and later are not supported.
- **`SECURITY.md` describes the historical values by provenance, not by severity.** The
  audit can show what the values look like and where they live; it cannot show whether
  they were ever provisioned. The document now says so, makes rotation conditional on a
  value having been live, and treats history cleanup as a decision gated on provenance
  instead of an unconditional step.
- **The aggregator version is 1.1.0.** The five modules keep their original coordinates
  and versions; only the parent reference moved, so a build still produces
  `IFaceSuportService-V1.0.0.150630.war` and its siblings.

### Fixed

- **The credential exposure is now inventoried instead of asserted.** `SECURITY.md` used
  to say the history held "database passwords, provincial interface accounts, a shared
  signing key" — accurate but misleading by omission. An audit of the 21 files and 11
  commits that still carry them found ten distinct categories of credential-shaped value, of which the worst
  is `zfb.app_private_key`: a ~1.6 KB RSA **private key** for the payment-channel
  integration. A signing identity is not a password: if it was ever live, changing the
  password would not neutralise it. The history also holds 57 values matching the
  18-digit resident ID pattern, which `SECURITY.md` did not mention at all — that is a
  separate problem from credentials, because personal data cannot be rotated. Both are
  documented with a rotation order; no values are reproduced.
- **`NetPortalManagement`'s dependencies are declared again.** They had been deleted
  outright while the module was being brought into line with the others, which left it in
  a worse state than before: previously it failed to resolve, and the fix was to install
  the jars; with the declarations gone it failed to compile even with the jars present,
  and there was no way back. The six coordinates are back in the POM with a comment
  recording why each one exists, and `-Pprivate-deps` now fails at dependency resolution
  again — the honest, documented failure mode.
- **A dependency can be used without a single `import`.** Worth recording because it is
  what makes these artifacts look dead. `com.tecsun.sisp_public:aamanagement-client` is
  never imported by a Java file in `NetPortalManagement`, but
  `src/main/webapp/WEB-INF/web.xml` names
  `com.tecsun.sisp_public.aam.client.AAMClient4Jersey` as an active Jersey filter and the
  container resolves that class by string; `bee.cloud.service` is named the same way
  through `bee.cloud.service.listener.StartupListener`. The reverse is why
  `InterfaceFunction` and `NetworkOfficeService` could legitimately drop the same
  declaration: their equivalent filter is inside `<!-- -->`. Grep resources, not just
  sources, and read the comments.
- **`sun.misc.BASE64Encoder` / `BASE64Decoder` replaced with `java.util.Base64`** in
  both modules (14 files: 13 main sources plus one test class). The internal `sun.misc`
  classes were never public API, are unavailable on JDK 9+ without `--add-exports`, and
  produced a warning on every compile. Behaviour notes:
  - **Encode** now returns a single line. `BASE64Encoder` wrapped at 76 characters with
    a line break, so previously an encoded photo or token carried embedded CRLFs; the
    captcha endpoint even shipped them inside a JSON string. Anything that relied on
    the old wrapping should now strip/ignore whitespace itself.
  - **Decode** uses `Base64.getMimeDecoder()`, which ignores line breaks and other
    non-base64 characters — i.e. it still accepts both wrapped (old) and unwrapped
    (new) input, so existing stored values keep decoding.
  - Dead declarations were dropped along the way: an unused `encoder` field in
    `FaceVerifyController`, unused `encoder`/`decoder` statics in `CodeController`, and
    unused imports in `ArchiveUtil` and `ZXHisIface`. Commented-out legacy snippets
    were left untouched.
- **Three hardcoded credentials were still in the source tree.** `Constants` held
  `HBNBpassword = "xgscx"` and `SNBpassword = "123456"`, and `PolicyApplication` opened
  its JDBC connection with a literal user, a literal password and an internal Oracle host
  (`10.132.1.71`). The CI scan described above is what surfaced them, which is the
  argument for having written it. They now come from the environment -
  `HBNB_SERVICE_PASSWORD`, `SNB_SERVICE_PASSWORD`, `NPM_JDBC_URL`, `NPM_JDBC_USERNAME`,
  `NPM_JDBC_PASSWORD` - and the two constants became methods that throw
  `IllegalStateException` when the variable is missing. **This is a behaviour change**:
  those code paths now fail fast instead of falling back to a built-in value, so any
  deployment that relied on the old defaults must supply the variables first.

## [1.0.0] - 2026-10-24

First tagged release. Everything below landed in this release.

### Added

- **Root `pom.xml`** — aggregator so the five modules build as one reactor. It is also
  the parent, supplying `project.build.sourceEncoding` and the Java 7 compiler level
  uniformly.
- **`.github/workflows/ci.yml`** — compiles the two publicly buildable modules on JDK 8,
  and runs repository-hygiene checks (no committed `target/`, no committed credentials,
  no personal identity numbers).
- **`NOTICE`** — third-party attribution for the JeeSite-derived files, plus a note on
  the vendored `print-2.1.1.jar` whose provenance is unknown.
- **Root `README.md`** — project overview, module map, architecture diagram, tech stack,
  quick start, known limitations, roadmap.
- **Per-module READMEs** for `IFaceSuportService`, `NetworkOfficeService`,
  `IFaceSuperService_hn`, `InterfaceFunction` and `NetPortalManagement`.
- **`docs/`** — `ARCHITECTURE.md`, `GETTING_STARTED.md`, `CONFIGURATION.md`,
  `API_OVERVIEW.md`, `CLIENT_SDK.md`, `TROUBLESHOOTING.md`, `ROADMAP.md`.
- **`config.properties.example`** for every module, and `ds.xml.example` for
  `NetPortalManagement`. All credentials replaced with `CHANGE_ME`.
- **`LICENSE`** — Apache License 2.0.
- **`CONTRIBUTING.md`**, **`CODE_OF_CONDUCT.md`**, **`SECURITY.md`**.
- **`.gitignore`** covering `target/`, IDE metadata, build output and local configuration.
- **`.editorconfig`** for consistent whitespace across editors.

### Changed

- Documentation language is English from this point forward. Existing Chinese comments
  are being translated incrementally; both are acceptable in the tree during the
  transition.
- **Build is no longer tied to an internal Nexus.** The `<repositories>` and
  `<pluginRepositories>` blocks were removed from all five POMs. Mirrors now belong in
  `~/.m2/settings.xml`.
- **`com.oracle:ojdbc6:11.2.0` → `com.oracle.database.jdbc:ojdbc6:11.2.0.4`**, which is
  the coordinate Oracle publishes to Maven Central. Same driver, same class name.
- **`com.google.code:kaptcha:2.3.2` → `com.github.penggle:kaptcha:2.3.2`**, the
  Central-published packaging of the same release. `com.google.code` was never on
  Central.
- **Removed the `unieap-dataexchange-client` dependency.** No source file imports it; it
  was declared for a provincial rural-pension integration that is not present in this
  tree. Dropping it makes `IFaceSuportService` build against public artifacts.
- **`IFaceSuperService_hn` artifactId renamed** from `IFaceSuportService` (which
  collided with the main module, distinguished only by version) to
  `IFaceSuperService_hn`. The war file name changes accordingly.
- **`target/` is no longer tracked** (235 files, including a 26 MB war). The files remain
  on disk; only the index changed.
- `NetworkOfficeService` configuration template corrects the `iinner_proxy_ip` typo to
  `inner_proxy_ip`. The original key was never read, so no behaviour changes.

### Fixed

- **UTF-8 BOM in `IFaceSuperService_hn/.../InvokeUtil.java`** made the module
  uncompilable — `javac` reported `illegal character: '\ufeff'` at line 1. BOM stripped.
- **Real identity numbers replaced** with the placeholder `110101199001010015`
  (checksum-valid, reserved region code) across 11 files / 27 occurrences, along with the
  personal names that appeared beside them.
- **Author signatures removed** from source files. The only `@author` tags that remain are
  the twelve third-party ones in the JeeSite-derived files, which Apache-2.0 requires to
  be preserved.

### Security

- `config.properties` and `ds.xml` were **removed from the git index** in all modules —
  being listed in `.gitignore` is not enough for files that are already tracked. **The
  credentials remain in git history and must be treated as compromised** — see
  `SECURITY.md` for the rotation and history-scrub procedure.

### Deprecated

- `DBConnection` in `IFaceSuportService` and `IFaceSuperService_hn`. It is dead code with
  a shared static `Statement`; do not revive it.

## Historical commits

Changes before this file existed are recorded in the git log. Notable milestones, in
reverse chronological order:

| Commit | Summary |
| --- | --- |
| `697dea1` | Organise MOOC test classes |
| `b8b1eb6` | Move MyBatis reverse-generated artefacts |
| `2d6736f` | EasyPOI import/export examples |
| `319bd13` | Remove outdated microservice modules |
| `76921cf` | Remove the MOOC project |
| `6fba097` | Spring Cloud demos |
| `5a998f2` | Xiaogan public service / online government service |
| `72fac09` | WebService projects |
| `d6e1d0b` | Auto-generated controller / service / DAO / XML |
| `51d2153` | Spring Boot 2.0 financial reconciliation sample |
| `34dc30c` | Alibaba EasyExcel import/export |
| `6e964a3` | MyBatis reverse engineering, partial Netty |
| `f504bdd` | Dubbox 2.8.4 projects |

[Unreleased]: https://github.com/zengyunhua/Mestudy/compare/v1.1.0...master
[1.1.0]: https://github.com/zengyunhua/Mestudy/releases/tag/v1.1.0
[1.0.0]: https://github.com/zengyunhua/Mestudy/releases/tag/v1.0.0
