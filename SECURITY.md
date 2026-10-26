# Security policy

## Reporting a vulnerability

**Do not open a public issue for a security problem.**

Report privately through GitHub's [Security Advisories](https://github.com/zengyunhua/Mestudy/security/advisories/new)
for this repository. If that is unavailable, contact the maintainer through the details
on [github.com/zengyunhua](https://github.com/zengyunhua) and state only that you have a
security report — do not include details in a public channel.

Please include:

- The module and class affected
- A description of the impact and the conditions required to trigger it
- Reproduction steps or a proof of concept
- Any suggested fix

Expect an acknowledgement within a reasonable time. This is a best-effort, volunteer-run
archive, so there is no formal SLA — but security reports are treated as the highest
priority.

## Supported versions

There are no released versions and no maintenance branches. The `master` branch is the
only supported line. Fixes land there and nowhere else.

## Known security issues in this tree

This repository is a **reference archive derived from production systems**, published for
study. It is not hardened and should not be deployed as-is. The issues below are
documented deliberately so nobody mistakes this code for production-ready.

### 1. Credentials committed to the repository

The historical `config.properties` files (and `NetPortalManagement/src/main/resources/ds.xml`)
contained real values: database passwords, provincial interface accounts, a shared
signing key, and internal IP addresses and hostnames.

**Status.** The working tree now ships `config.properties.example` /
`ds.xml.example` with `CHANGE_ME` placeholders, and `.gitignore` excludes the real files.

**This does not fix the exposure.** The values remain in git history. If you cloned this
repository, treat every credential in its history as compromised.

**If you are the repository owner, do this before publishing:**

1. **Rotate every credential that ever appeared in the tree.** This is the only step that
   actually matters — history rewriting does not un-leak a password that has been
   public.
2. Remove the files from history. With `git-filter-repo`:

   ```bash
   git filter-repo --path-glob '*/src/main/resources/config.properties' \
                   --path 'NetPortalManagement/src/main/resources/ds.xml' \
                   --invert-paths
   ```

   Or with BFG:

   ```bash
   bfg --delete-files config.properties
   bfg --delete-files ds.xml
   git reflog expire --expire=now --all && git gc --prune=now --aggressive
   ```

3. Force-push, and ask anyone with a fork to re-clone.
4. Add a `.gitignore` rule (already present) so it cannot recur.
5. Consider a secret-scanning hook (`gitleaks`, `trufflehog`) in CI.

### 2. Session filter does not sanitize XSS

`BusinessFilter` is enabled on selected `/iface/*` paths and validates sessions,
channels, and third-party permissions. It does **not** escape or sanitize request
input. Other paths are not covered by this filter.

**Impact.** HTML endpoints that echo untrusted input remain vulnerable unless they
use context-appropriate output encoding. Audit template rendering and browser-facing
responses separately; do not mistake authorization for XSS prevention.

### 3. TLS trust configuration

`IFaceSuportService/src/main/java/com/tecsun/sisp/iface/client/SSLClient.java`
uses the JVM default trust store and strict hostname verification. Deployments
with a private CA must import it into the JVM trust store; do not disable
certificate or hostname verification to work around trust errors.
### 4. MD5 password hashing

`NetPortalManagement/src/main/java/com/tecsun/sisp/npm/util/MD5Generator.java` hashes
passwords with unsalted MD5.

**Remediation.** Use bcrypt or argon2 for new credentials, and migrate existing hashes on
next successful login (you cannot reverse MD5, but you can rehash after verifying).

### 5. Unauthenticated internal endpoints

`CardInfoInnerController` is mounted at `/ifaceInner/cardInfo` and is intended for
service-to-service calls. There is no authentication on that path beyond network
position. If the application is reachable from a broader network than intended, these two
endpoints are open.

### 6. Dead but dangerous JDBC helper

`DBConnection` (in `IFaceSuportService` and `IFaceSuperService_hn`) holds a **static**
`Statement` and exposes `executeQuery(String sql)` / `executeUpdate(String sql)` that
execute caller-supplied SQL.

It is currently dead code — `StartupListener` calls `DataBase.init()` and the
`DBConnection.initConn()` call is commented out. **Do not revive it.** If you need ad-hoc
SQL, use a MyBatis mapper with bound parameters.

## What is *not* a problem

Worth stating, because it is the first thing a reviewer checks:

- **MyBatis mappers do not use `${}` string substitution.** All 24 mapper XML files were
  checked; there are zero `${}` occurrences, so every mapper statement uses bound `#{}`
  parameters and is not injectable.
- **The SOAP clients use generated stubs** with typed parameters rather than string-built
  XML envelopes.

## Hardening checklist for anyone deploying this

- [ ] Rotate and externalise every credential; read them from environment variables or a
      secret store, not from `config.properties`
- [ ] Re-enable the XSS filter
- [ ] Replace the permissive TLS trust manager
- [ ] Put the application behind an authenticating gateway; do not expose
      `/ifaceInner/*` publicly
- [ ] Change the default `statusCode`-based error handling so internal exception messages
      are not returned to clients
- [ ] Disable the Kaptcha development defaults if the captcha is used as a security
      control
- [ ] Set `jdbc.maxPoolSize` to a value your Oracle instance can actually sustain
      (the default of 300 × 8 datasources is 2400 sessions)
