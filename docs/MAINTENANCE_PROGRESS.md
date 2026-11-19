# Maintenance hardening progress

Date: **November 19, 2026 (Asia/Shanghai)**. Working-tree baseline: `659af20`.
These changes are local until reviewed, committed and pushed. They are not a release,
a green public CI run, or a claim of production/security compliance.

## Batch 1: implemented

| Item | Implementation | Evidence |
| --- | --- | --- |
| CI identity-fixture mismatch | Keep the scanner unchanged; test only the documented synthetic ID and non-ID `exampleX` | Photo-name tests and sensitive scan |
| CSSP anonymous request boundary | Map `BusinessFilter` to `/iface/cssp/*`; only exact POST credential login public | 14 filter/descriptor/MVC regression tests |
| Known unbound role SQL | Preserve the two URLs as explicit HTTP/JSON 501 handlers; remove unbound DAO methods and test exemption | 5 controller/service/DAO regression tests |
| Build consistency | Inherit WAR plugin; keep direct/transitive container APIs provided; reject malformed WARs and container/test libraries | Clean reactor build, WAR gate and archive inspection |
| Documentation drift | Update module/API/security guides, roadmap, application guide and obsolete print-JAR NOTICE | Current source and documented boundaries |

Detailed client-visible changes: [CSSP migration](api/CSSP_AUTHENTICATION.md) and
[unsupported role operations](../NetworkOfficeService/README.md#explicitly-unsupported-role-endpoints).
Old review documents remain historical observations; the current implementation and
[SECURITY.md](../SECURITY.md) supersede obsolete descriptions of these specific paths.

## Validation

Run from the repository root with JDK 8 and Maven:

```bash
mvn -B -ntp clean verify
python -B scripts/verify_public_build.py
python -B -m unittest discover -s scripts -p 'test_*.py'
python -B scripts/check_sensitive.py
```

Final local validation completed on November 19, 2026 at 16:34 (Asia/Shanghai):

- `mvn -B -ntp clean verify`: all five WAR modules passed on JDK 8, using the workstation's
  existing Maven configuration/cache. This was not an empty-cache or container run.
- `verify_public_build.py`: **81 passing Java tests**, no failures/errors/skips, one valid
  WAR per module: 42 IFaceSuportService, 8 IFaceSuperService_hn, 7 InterfaceFunction,
  9 NetworkOfficeService and 15 NetPortalManagement; no forbidden container/test libraries.
- Python repository-check tests: **12 passed**, including 3 new archive/library tests.
- Current tracked-source sensitive scan: **0 findings over 706 tracked paths**.
- Additional tracked-plus-new-source scan: **0 findings over 634 relevant source files**;
  this supplements the unchanged tracked-only scanner before new Java tests are committed.
- Changed-document local file links and `git diff --check`: passed.
- WAR/SDK inspection: the IFaceSuportService WAR contains the CSSP filter mapping and its
  client JAR retains the client/common class boundary. Neither Servlet API coordinate
  appears in any module's WAR library directory.

Targeted tests also passed: 14 CSSP authorization tests, 2 existing photo-name tests,
and 5 unsupported-role tests. No private server or database was used. These results
establish local build/negative-contract coverage, not successful business deployment.

The sensitive scanner covers Git-tracked sources. Newly added source files must also
be reviewed/scanned before commit; an untracked file is not evidence of a clean index.
The Python build-gate unit tests intentionally construct failing fake reports; their
console output is not the count of the real Maven reactor. Read the real Surefire XML
through `verify_public_build.py` instead.

Public CI for baseline `659af20` packaged successfully but repository hygiene failed
on November 6. Fixing a local working tree does not alter that historical result. Use
the new commit's own public CI URL in an application or release. No remote CI run has
been triggered by this batch.

## Owner-only evidence: still pending

Do not insert secrets, identity numbers, private keys, value hashes or full private
configuration into these records. Keep detailed provider records privately; publish
only sanitized decisions and ticket references.

| Evidence needed | Responsible role | Current status |
| --- | --- | --- |
| Public-release authorization for original business code and third-party sources | Project/source owner | Not verified by this batch |
| Provenance and actual provisioning status of historical credential-shaped values | Owner/provider | Pending; see security review |
| Rotation/revocation of any live credential, if one existed | Owner/provider | Pending verification; a history rewrite is not rotation |
| Synthetic provenance of historical identity-shaped fixtures | Data owner | Pending; no claim that every match is a real person |
| Decision and approval for any coordinated history cleanup | Repository owner | Not performed or implicitly authorized |
| CSSP terminal migration, per-session business identity and object ownership | Deployment/business owner | Session filter is implemented; deployment/isolation remains unverified |
| Verified role-table schema/SQL | Authorized database owner | Unavailable; corresponding endpoints return 501 |

See [security provenance process](SECURITY_REVIEW.md). Do not mark these rows done
because a current-tree scanner or Maven test passes.

## Next batches, in order

1. **Independent demo path:** one representative module/flow with synthetic fixtures
   and stubbed external services, plus startup/smoke checks from a fresh environment.
   A lightweight database cannot establish Oracle-specific behavior.
2. **Successful business regression tests:** pension query, photo workflow and draft
   submit/audit, including failure rollback and duplicate submission. Add baseline
   coverage reporting that ratchets upward rather than an arbitrary global target.
3. **Configuration and observability:** required-key preflight, readiness diagnostics,
   trace IDs and redacted logs. Never make a health response expose infrastructure/secrets.
4. **Dependency and SDK maintenance:** dependency inventory/SBOM, compatible upgrades,
   isolated instance clients and resource-close tests while retaining legacy entry points.
5. **Release/application evidence:** review and commit the changes, wait for the exact
   commit's public green CI, obtain genuine external reproduction feedback, then publish
   an accurately scoped new version. Do not move old tags or fabricate adoption metrics.

The pre-existing IDE module-file deletion was not changed by this batch. The standalone
illustration page is unrelated to this project hardening. Do not include unrelated
artifacts or user changes in the maintenance PR.
