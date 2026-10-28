# Historical data and secret review

This document separates the maintainer's assertion from independently verified facts.
Do not paste historical values, credentials, or personal identifiers into this file, issues, PRs, or CI logs.

## Verified in the current checkout

- Local `config.properties` and `ds.xml` files are not tracked; only placeholder templates are.
- `scripts/check_sensitive.py` checks tracked source files for ID-shaped values, private-key markers and common hardcoded sensitive assignments. Its tests run in CI. It does not inspect Git history or live providers.
- Two service-password literals and one JDBC connection triplet were moved to environment variables. The Redis key prefixes and a protocol key-name template are not authentication secrets.

## Maintainer assertion to document

The maintainer states that historical data is synthetic. This statement alone does not establish that no historical credential was ever provisioned or that every identity-shaped value was generated synthetically. No rotation is required for a fixture that has never been used with a real service.

| Historical category | Non-sensitive provenance (generator/test system) | Ever provisioned? (yes/no/unknown) | Reviewer and date | Action/evidence reference |
| --- | --- | --- | --- | --- |
| Payment signing material | Pending | Unknown | Pending | Verify with provider; rotate only if provisioned |
| Vendor and gateway credentials | Pending | Unknown | Pending | Verify account owners; rotate live values |
| Database and SMTP credentials | Pending | Unknown | Pending | Verify environments; rotate live values |
| Identity-number-shaped fixtures | Pending | Unknown | Pending | Record synthetic generation method; do not publish values |

## Historical inventory (October 28, 2026)

Run `python scripts/audit_git_history.py` on a full local clone. At baseline
commit `d8a301d`, the inventory reported
only category counts for reachable historical blob/path pairs: 4,135 inspected,
0 skipped as oversized/binary, 44 pairs with ID-shaped values, 38 pairs with
sensitive-assignment-shaped text, and 13 pairs with tracked local-configuration
findings. These are **not** counts of unique people, live secrets, or incidents.
The path listing and matching are heuristic; omissions and false positives are possible.
Counts may change as Git history changes.
No historical values should be copied into a ticket, pull request, CI log or this guide.
The current-tree `python scripts/check_sensitive.py` remains the CI gate; the
historical inventory is a local triage aid, not a remediation or proof of absence.

## Private review record (non-sensitive metadata only)

For each risk category, keep a restricted owner record with: category and affected
service/environment; accountable owner; synthetic generator or test-system provenance
(if applicable); whether ever provisioned (`yes`/`no`/`unknown`); provider-side
verification reference; revocation or rotation ticket and date when provisioned;
identity-fixture provenance without the identifier itself; reviewer and review date;
and a history-cleanup decision plus approval reference. Do not include credential
values, full identifiers, copies of sensitive files, or value hashes. An `unknown`
provisioning status is not evidence that a value was harmless.

**Order of operations:** identify the provider and owner privately; revoke or rotate
any live value (especially payment signing material) before repository cleanup; then
review all impacted paths and embedded data, tags, forks, releases and caches. Only
with owner approval, backup and a tested mirror-clone plan should history be rewritten
and coordinated with downstream consumers. Rewriting does not revoke a live key or
remove copies already fetched. Where values are proven never provisioned and synthetic,
record the decision not to rotate/rewrite explicitly. For identity-shaped data, verify
synthetic provenance privately; real identifiers cannot be rotated. Do not assert closure
until the responsible owner records the decision and independent reviewer signs off.

## Runtime configuration for modified paths

Set these in the process environment (not in Git or CI logs) before invoking the relevant operations:

- `HBNB_SERVICE_PASSWORD`: required by `IFaceSuportService` provincial service queries.
- `SNB_SERVICE_PASSWORD`: reserved for the legacy SNB service integration; not read by active request paths.
- `NPM_JDBC_URL`, `NPM_JDBC_USERNAME`, `NPM_JDBC_PASSWORD`: required by the `NetPortalManagement` `savePolicy` path. This module still needs its private Bee framework dependencies.

Missing required values fail the affected operation explicitly, rather than falling back to a committed example. These changes do not turn the archive into a deployable public demo.

## Closure decision

1. Fill in the provenance and service-use columns using non-sensitive records, not raw values.
2. For every real provisioned credential, record the provider revocation/rotation confirmation (ticket ID and date only).
3. Record the owner's decision on history rewriting. Coordinate with fork and tag owners before any rewrite; a rewrite does not retract prior clones. If all historical values are proven synthetic and never used, a documented decision not to rewrite may be appropriate.
4. Rerun the checks after changes and retain the CI run URL plus a clean-room contributor test result.

**Status: current-tree preventive controls implemented; historical provenance and owner decisions pending.** This is not a claim that the entire project is production-hardened; see `SECURITY.md` for separate runtime risks.
