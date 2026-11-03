# Release checklist

This project is a reference archive with five independently versioned WAR modules. A
release is a reproducibility and communication event, not a claim that the runtime is
production-hardened.

## Before tagging

- [ ] Confirm the release diff adds no local configuration, build output, IDE metadata,
      credentials, private keys or real personal data. Existing legacy IDE metadata
      must not be changed incidentally; remove it only in a separate, intentional
      cleanup change after checking downstream users.
- [ ] Review [SECURITY.md](../SECURITY.md) and [the historical security review](SECURITY_REVIEW.md).
      Any live credential must be rotated or revoked before release work continues.
- [ ] Update `CHANGELOG.md` with the scope, verification boundary and known limitations.
- [ ] Update the README and per-module documentation when commands, endpoints or
      configuration keys changed.
- [ ] Verify the release version and tag name do not reuse an existing tag.

## Reproduce the release candidate

Use JDK 8 from a fresh clone. Prefer a path that contains only ASCII characters for the
Maven temporary directory on Windows.

```text
mvn -B -ntp -Dmaven.repo.local=<empty-directory> verify
python scripts/verify_public_build.py
python scripts/check_sensitive.py
python -m unittest discover -s scripts -p "test_*.py"
```

Record the exact commit SHA, Java version, Maven version, operating system, test totals,
per-module WAR names, and the CI run URL. The build proves compilation, packaging and
unit/context tests only; it does not prove Oracle, Redis, Tomcat or external-gateway
runtime behaviour.

## Tag and publish

1. Merge the release-preparation change after CI is green.
2. Create an annotated tag, for example `v1.2.0`, from the verified commit:
   `git tag -a v1.2.0 <commit> -m "Release v1.2.0"`.
3. Push the tag and create GitHub release notes from the matching changelog section.
4. Attach only the WARs produced by the verified build, if distributing binaries is
   useful. Include SHA-256 checksums and the exact JDK/Maven command used.
5. State the unsupported runtime dependencies and security limitations in the release
   notes. Never describe a green Maven build as an end-to-end production certification.

## After publishing

- [ ] Confirm the release page, tag, source archive and checksums point to the same commit.
- [ ] Record the CI run and release URL in the maintainer's non-sensitive project record.
- [ ] Keep the previous release immutable; publish a new tag for corrections.
