# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this
project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html) for module
versions going forward.

Because this repository is an archive of independently versioned modules, entries are
grouped by scope rather than by a single release number.

## [Unreleased]

### Added

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
- `NetworkOfficeService` configuration template corrects the `iinner_proxy_ip` typo to
  `inner_proxy_ip`. The original key was never read, so no behaviour changes.

### Security

- Local configuration files (`config.properties`, `ds.xml`) are now git-ignored. **The
  credentials remain in git history and must be treated as compromised** — see
  [SECURITY.md](SECURITY.md) for the rotation and history-scrub procedure.

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

[Unreleased]: https://github.com/zengyunhua/Mestudy/commits/master
