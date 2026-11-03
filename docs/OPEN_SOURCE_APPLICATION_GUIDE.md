# Open-source application readiness guide

This is a project-readiness checklist, **not** an official Codex for Open Source eligibility checklist or acceptance estimate. Check the current application form and programme terms before submission. Do not claim production readiness or a complete security remediation.

## What a reviewer can verify today

- Source and Apache-2.0 license are visible in the repository; the five modules and their purpose are mapped in the [README](../README.md).
- The default Maven reactor builds all five WAR modules from Maven Central on JDK 8. GitHub Actions runs `mvn -B verify` against an isolated local repository on push and PR, then `scripts/verify_public_build.py` requires one nonempty WAR and at least one passing test per module.
- [Getting started](GETTING_STARTED.md), [configuration](CONFIGURATION.md), [troubleshooting](TROUBLESHOOTING.md), [contributing](../CONTRIBUTING.md), [roadmap](ROADMAP.md), and [security policy](../SECURITY.md) document the current scope and limitations.
- The repository is a **reference archive**, not a deployable demo. A Maven build does not verify Oracle schemas, Redis, Tomcat startup, external gateways, authentication, or end-to-end transactions.

## Current verification boundary

On November 3, 2026, the maintainer workstation (Windows 11, JDK 1.8.0_421, Maven 3.8.7)
ran `mvn -B -ntp -Dmaven.repo.local=<empty-directory> verify` on `70c0029`. All five modules
packaged, and `python scripts/verify_public_build.py` reported 46 passing tests
(12 `IFaceSuportService`, 8 `IFaceSuperService_hn`, 7 `InterfaceFunction`,
4 `NetworkOfficeService`, 15 `NetPortalManagement`), none skipped. The empty repository
run completed successfully; a contributor should still reproduce it independently because
a local run is not a substitute for a public CI result. On Windows, prefer an ASCII-only
Maven repository path when the user profile contains non-ASCII characters; see
[troubleshooting](TROUBLESHOOTING.md).

The CI workflow is the stronger evidence because it resolves into an empty repository.
Run #27 on `352fbe8` (October 28, 2026) was the first green run with the artefact gate,
when the gate covered four modules. `NetPortalManagement` joined the reactor and the gate
in `27f8ffe` and `6137391`; **cite the CI run for the exact commit you submit**, and do
not reuse the run #27 result for later commits.

`NetPortalManagement` was ported off the private Bee framework in `27f8ffe`. Its tests
stop at the configuration and authentication boundary (`AppConfig` resolution order,
`ApiTokenFilter` rejection). The JDBC repository has no automated test, and no one has
run the ported module against an Oracle schema in a recorded, reproducible way.

For historical risk, `python scripts/audit_git_history.py` provides a local,
category-only heuristic inventory; see [security review](SECURITY_REVIEW.md) for the
October 28 counts, limitations and owner-only evidence record. No historical
provenance, provider rotation or history-cleanup decision has yet been independently
verified; a clean current-tree scan is not closure.

## Independent reproduction (ask a contributor who has not set up this repo)

1. Start from a fresh clone in an empty working directory, without the maintainer's local Maven artifacts or private credentials. Record the exact commit SHA (`git rev-parse HEAD`), OS, `java -version` (JDK 8), and `mvn -v`.
2. From the repository root run `mvn -B -ntp -Dmaven.repo.local=<empty dir> verify`, then `python scripts/verify_public_build.py`. Confirm the reactor succeeds, note the per-module test counts and inspect the five `*/target/*.war` outputs.
3. Ask the contributor to report the actual time, confusing instructions, failed commands, and a redacted build log or a link to a public CI run. A screenshot can supplement this record; it is not by itself reproducible evidence. Do not expose secrets, personal data, internal addresses or full private configuration.
4. Have the contributor file a documentation PR or issue about any friction if they are willing. An issue is not required simply to prove activity; real PRs, fixes, CI runs, releases and responses are better evidence than artificially created tickets.
5. If demonstrating runtime behaviour, separately provide a safe, isolated Oracle schema/seed data, Redis, Tomcat and approved stubbed external integrations. Record startup logs, HTTP status, expected and actual response, and cleanup procedure. Until that exists, describe the claim as **buildable**, not **independently runnable**.

## Owner-only blockers and high-value work

| Priority | Gap | Completion evidence |
| --- | --- | --- |
| Critical | Historical credential-shaped values and a payment-signing-key-shaped value remain in Git history; the owner states that these are synthetic, but provenance, actual provisioning, and history cleanup have not been verified. | Owner documents that each sensitive-looking value was never provisioned, or confirms rotation/revocation of any live value with affected providers. Coordinate safe history cleanup and record a sanitized remediation summary. Follow [SECURITY.md](../SECURITY.md); never publish historical values. |
| High | No independently executable end-to-end deployment recipe with public test data. | A clean-room operator follows a versioned, non-sensitive test environment guide and reports a successful startup and expected smoke-test outcome. Do not commit proprietary Oracle/Redis jars or actual credentials; use publicly redistributable dependencies only. |
| High | `NetPortalManagement` builds publicly, but its ported JDBC repository has no automated test and no recorded run against Oracle. | An Oracle-compatible fixture (for example a Testcontainers Oracle XE image) exercising `NetPortalRepository`, or a redacted, reproducible smoke-test record from an authorized environment. |
| High | Automated tests are limited and do not establish runtime behaviour for every module. | Add focused tests for each module, negative/security cases and CI coverage evidence; do not overstate current counts. |
| Medium | `v1.1.0` predates the five-module public build and the portal port; that work is only in `[Unreleased]`. | After CI is green on the release commit, publish a new, accurately scoped release/tag with a changelog, verified build command, limitations and checksum/artifacts where applicable. Do not move existing tags. |
| Medium | Public adoption is hard to assess from stars alone. | Cite genuine external PRs, users, downstream projects, discussions or release downloads **with dates and links**; distinguish actual usage from stars/watches and do not fabricate metrics. |

## Submission narrative

Describe the specific technical value (the multi-module government-service integration patterns), explain why AI-assisted maintenance would help (tests, docs, dependency isolation and safe modernization), link to recent real PRs and green CI, and openly disclose the known security and runtime limitations. Include only claims you can substantiate. Before applying, independently review the current programme's official eligibility rules, application fields, account requirements and deadlines; this guide does not verify or replace them.
