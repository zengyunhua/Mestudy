# Open-source application readiness guide

This is a project-readiness checklist, **not** an official Codex for Open Source eligibility checklist or acceptance estimate. Check the current application form and programme terms before submission. Do not claim production readiness or a complete security remediation.

## What a reviewer can verify today

- Source and Apache-2.0 license are visible in the repository; the five modules and their purpose are mapped in the [README](../README.md).
- The default Maven reactor selects four publicly buildable WAR modules on JDK 8. GitHub Actions runs `mvn -B verify` on push and PR. `NetPortalManagement` is opt-in with `-Pprivate-deps` because its Bee persistence dependencies are not public.
- [Getting started](GETTING_STARTED.md), [configuration](CONFIGURATION.md), [troubleshooting](TROUBLESHOOTING.md), [contributing](../CONTRIBUTING.md), [roadmap](ROADMAP.md), and [security policy](../SECURITY.md) document the current scope and limitations.
- The repository is a **reference archive**, not a deployable demo. A Maven build does not verify Oracle schemas, Redis, Tomcat startup, external gateways, authentication, or end-to-end transactions.

## Current verification boundary

On October 26, 2026, the maintainer workstation ran `mvn -B -DforkCount=0 verify`: the four default modules packaged and eight tests passed, all in `IFaceSuportService`. This was not a clean external clone, used a populated local Maven cache, and disabled Surefire forking for the local Windows environment; it is **not** evidence of independent deployment or a clean-room public dependency resolution. The GitHub Actions workflow uses `mvn -B verify`, but check the actual run for the commit being submitted.

## Independent reproduction (ask a contributor who has not set up this repo)

1. Start from a fresh clone in an empty working directory, without the maintainer's local Maven artifacts or private credentials. Record the exact commit SHA (`git rev-parse HEAD`), OS, `java -version` (JDK 8), and `mvn -v`.
2. From the repository root run `mvn -B verify`. Confirm the reactor succeeds, note the test counts and inspect the four `*/target/*.war` outputs. Do not opt into `-Pprivate-deps` for this public build.
3. Ask the contributor to report the actual time, confusing instructions, failed commands, and a redacted build log or a link to a public CI run. A screenshot can supplement this record; it is not by itself reproducible evidence. Do not expose secrets, personal data, internal addresses or full private configuration.
4. Have the contributor file a documentation PR or issue about any friction if they are willing. An issue is not required simply to prove activity; real PRs, fixes, CI runs, releases and responses are better evidence than artificially created tickets.
5. If demonstrating runtime behaviour, separately provide a safe, isolated Oracle schema/seed data, Redis, Tomcat and approved stubbed external integrations. Record startup logs, HTTP status, expected and actual response, and cleanup procedure. Until that exists, describe the claim as **buildable**, not **independently runnable**.

## Owner-only blockers and high-value work

| Priority | Gap | Completion evidence |
| --- | --- | --- |
| Critical | Historical credential-shaped values and a payment-signing-key-shaped value remain in Git history; the owner states that these are synthetic, but provenance, actual provisioning, and history cleanup have not been verified. | Owner documents that each sensitive-looking value was never provisioned, or confirms rotation/revocation of any live value with affected providers. Coordinate safe history cleanup and record a sanitized remediation summary. Follow [SECURITY.md](../SECURITY.md); never publish historical values. |
| High | No independently executable end-to-end deployment recipe with public test data. | A clean-room operator follows a versioned, non-sensitive test environment guide and reports a successful startup and expected smoke-test outcome. Do not commit proprietary Oracle/Redis jars or actual credentials; use publicly redistributable dependencies only. |
| High | Only four of five modules build publicly; `NetPortalManagement` uses private Bee APIs. | Obtain redistribution rights and a lawful public dependency, or replace the persistence layer with tests; otherwise continue clearly marking the module private-only. |
| High | Automated tests are limited and do not establish runtime behaviour for every module. | Add focused tests for each public module, negative/security cases and CI coverage evidence; do not overstate current counts. |
| Medium | `v1.0.0` predates the four-module public-build improvements. | After CI and security review, publish a new, accurately scoped release/tag with a changelog, verified build command, limitations and checksum/artifacts where applicable. Do not move the existing tag. |
| Medium | Public adoption is hard to assess from stars alone. | Cite genuine external PRs, users, downstream projects, discussions or release downloads **with dates and links**; distinguish actual usage from stars/watches and do not fabricate metrics. |

## Submission narrative

Describe the specific technical value (the multi-module government-service integration patterns), explain why AI-assisted maintenance would help (tests, docs, dependency isolation and safe modernization), link to recent real PRs and green CI, and openly disclose the known security and runtime limitations. Include only claims you can substantiate. Before applying, independently review the current programme's official eligibility rules, application fields, account requirements and deadlines; this guide does not verify or replace them.