# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this
project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) for module
versions going forward.

Because this repository is an archive of independently versioned modules, entries are
grouped by scope rather than by a single release number.

## [Unreleased]

Nothing yet.

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

[Unreleased]: https://github.com/zengyunhua/Mestudy/compare/v1.0.0...master
[1.0.0]: https://github.com/zengyunhua/Mestudy/releases/tag/v1.0.0
