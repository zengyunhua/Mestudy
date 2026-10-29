# Roadmap

The goal is not to modernise this codebase into Spring Boot. The goal is to make it a
**usable, trustworthy reference** for the architecture it represents: honest about its
limitations, reproducible by someone outside the original network, and documented well
enough that the domain modelling is legible.

Status key: `[ ]` not started · `[~]` in progress · `[x]` done

---

## Phase 1 — Make the repository trustworthy (current)

Four modules now build in the default public reactor; the fifth remains optional and
requires private artifacts. Security provenance and independent deployment evidence
remain open work.

- [x] Add a root README with architecture, module map and honest limitations
- [x] Add per-module READMEs
- [x] Add `docs/` (architecture, configuration, API, client SDK, troubleshooting)
- [x] Add `.gitignore` covering `target/`, IDE metadata and local configuration
- [x] Replace committed credentials with `config.properties.example` templates
- [x] Add `LICENSE`, `CONTRIBUTING`, `CODE_OF_CONDUCT`, `SECURITY`
- [x] Stop tracking `target/` in git (CI verifies no build outputs are tracked)
- [ ] Establish historical provenance and provisioning status, rotate any live values,
      then decide on coordinated history cleanup (see [SECURITY.md](../SECURITY.md))
- [~] Maintain `CHANGELOG.md` entries for future releases (v1.0.0 is documented)

## Phase 2 — Make it build

- [x] Audit every dependency against Maven Central; list the ones that are proprietary
- [x] Replace proprietary artifacts with public equivalents where a behavioural match
      exists (e.g. swap internal utility jars for Guava/Commons) — `InterfaceFunction`
      and `NetworkOfficeService` needed none of their private coordinates, so both now
      build publicly
- [x] Stub the remainder behind an optional Maven profile so `mvn package` succeeds
      without the internal Nexus (`-Pprivate-deps` opts the remaining one back in)
- [x] Reconstruct `BanjianVo` so `NetworkOfficeService` leaves the profile too
- [ ] Diff the reconstructed `BanjianVo` against a real `t_drafts` /
      `T_ITEM_MESSAGE` / `t_office_detailed` schema and correct any drift
- [ ] Replace `NetPortalManagement`'s `bee.cloud` persistence layer, or accept that it
      stays out of the default reactor
- [~] Root POM is the aggregator and parent with shared encoding/compiler settings;
      module versions remain separate and private dependencies are not yet replaced
- [x] Set the language level to Java 8 (`maven.compiler.source`/`target` = `1.8`) once,
      in the root POM, and drop the per-module overrides
- [ ] Verify a clean build on JDK 8 in a container with an empty `~/.m2`

## Phase 3 — Tests and CI

- [~] Unit tests for the `common` layer (`Page` covered; `Result`, `AESUtil`, `JsonMapper`,
      date/number helpers) — no database required
- [x] A context-load test per module that fails on an unbound mapper statement or a
      mis-typed `${jdbc.*}` key, without needing a database. All four public modules
      have one (`SpringContextLoadTest`), including a reflection check over every
      scanned mapper method
- [ ] Write the missing SQL for `AreaDAO.insertUserRol` / `queryUserRloe`
      (`NetworkOfficeService`; needs the role-table schema) or remove the two endpoints
- [ ] One controller test per module using `MockMvc` with mocked services
- [ ] H2-backed integration test for one mapper per module (Oracle-specific SQL will need
      dialect guards; start with a mapper that uses portable SQL)
- [~] GitHub Actions runs isolated `mvn -B verify` on the four-module default
      reactor on JDK 8 with test/WAR checks; confirm actual run status per commit,
      then expand to a JDK matrix and coverage reporting
- [ ] Coverage reporting, with a floor that only ratchets upward

## Phase 4 — Documentation depth

- [ ] Javadoc on every public class in `common/` and `client/`
- [ ] Per-endpoint request/response examples in `docs/api/`, one file per controller
- [ ] An OpenAPI 3 description for at least the `IFaceSuportService` surface
- [ ] Sequence diagrams for the three flagship flows: pension query, card replacement,
      online-service submission with signature
- [ ] A "domain primer" explaining the Chinese social-insurance vocabulary
      (五险, 社保卡, 补换卡, 生存认证) for readers outside the domain

## Phase 5 — Code health

- [ ] Extract a shared `sisp-common` artifact and remove the per-module copies of
      `Result`, `Page`, `CommUtil`, `AESUtil`, `JsonMapper`
- [ ] Fix `ResultVerify` field shadowing
- [ ] Replace `static` state in `IFaceHttpsClient` with an instance-based client
- [ ] Inject gateway clients instead of instantiating SOAP stubs at call sites
- [x] Re-enable `BusinessFilter` session authorization on selected interface routes (not XSS sanitization)
- [x] Use the JVM default trust store and strict hostname verification in `SSLClient`
- [ ] Move credentials out of properties files into environment variables or a secret
      store
- [ ] Rename `iinner_proxy_ip` and add a startup validation pass for required keys

## Non-goals

Stated explicitly so the scope stays honest:

- **No migration to Spring Boot.** The value of this repository is the XML-wired Spring 4
  style; converting it would erase the thing worth reading.
- **No rewrite of the SOAP layer.** The generated Axis stubs stay as generated code.
- **No feature development.** This is an archive with maintenance, not a product.

## How to help

Pick anything above that is not `[x]`. Phase 1 and Phase 3 items are the highest value
and the easiest to review. Open an issue first for anything in Phase 5 — those touch many
files and benefit from agreement before the diff exists.
