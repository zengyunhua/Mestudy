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

The repository has a `v1.0.0` Git tag but no maintained release branches. The
`master` branch is the only supported line; fixes land there, not on the tag.

## Known security issues in this tree

This repository is a **reference archive derived from production systems**, published for
study. It is not hardened and should not be deployed as-is. The issues below are
documented deliberately so nobody mistakes this code for production-ready.

### 1. Credentials committed to the repository

Twenty-one files that were once tracked carried real values: `config.properties` under
`src/main/resources` in every module that existed at the time (plus copies under
`target/`), and `NetPortalManagement/src/main/resources/ds.xml`. Eleven commits in the
history still contain them.

An audit of those files found the following classes of secret. Only key names are listed
— the values are in the history and must be assumed public:

| Category | Keys | Why it matters |
| --- | --- | --- |
| Payment channel key pair | `zfb.app_private_key` (~1.6 KB), `zfb.alipay_public_key` | An RSA **private key**, not a password. This is the most serious item in the list: it is a signing identity, so requests signed with it look legitimate. Revoking the account password does not revoke the key — the key pair has to be rotated at the channel. |
| WeChat | `wx.api.key` (32 chars) | Merchant API key |
| Speech / OCR vendors | `sinovoice.asr.appkey`, `sinovoice.asr.devkey`, `sinovoice.csr.appkey`, `ocr.url` | Third-party API credentials |
| Databases | `jdbc.*.user` / `jdbc.*.password`, `ds.xml` | Oracle accounts for the `sisp`, `sisp_public` and business schemas |
| Face verification | `biometrics.user`, `biometrics.pwd`, `biometrics.key` | Biometric gateway account |
| Social-security card | `card.user`, `card.pwd` | Card service account |
| SMS | `sms_username`, `sms_password` | SMS gateway account |
| Email | `commom.emil.myemailpassword`, `commom.emil.myemailsmtphost` | SMTP credential |
| Signing material | `rzy.privatekey_path`, `rzy.publickey_path` | Paths to a key pair. The key itself is not in the tree, but the paths name where it lives. |
| Internal topology | ~18 `bank.*.ip` keys, `tecsun_verify_ip`, and about 20 `*_url` keys | Bank and provincial-gateway endpoints, with ports |

**Rotate in the order listed.** The payment key pair comes first because it is the only
item that impersonates the deployment to a third party; a database password that was
public for two years is bad, a signing key that was public for two years is worse,
because nothing about a signed request looks wrong.

**Status.** The working tree now ships `config.properties.example` /
`ds.xml.example` with `CHANGE_ME` placeholders, and `.gitignore` excludes the real files.

**This does not fix the exposure.** The values remain in git history. If you cloned this
repository, treat every credential in its history as compromised.

**Owner action still required.** Removal from the current tree is verified, but
credential revocation/rotation and historical cleanup have not been verified.
Do not treat this repository as cleared merely because the current checkout has
placeholders. Do not paste historical secrets into issues, logs or pull requests.

**If you are the repository owner, coordinate the following before claiming remediation:**

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

3. Coordinate any force-push with contributors and fork owners; history rewriting
   changes commit IDs and does not revoke already-copied credentials. Ask affected
   clones/forks to re-clone, and review tags, release artifacts and cached copies.
4. Add a `.gitignore` rule (already present) so it cannot recur.
5. Consider a secret-scanning hook (`gitleaks`, `trufflehog`) in CI.

### 2. Personal identity numbers in the history

The history also contains **57 distinct values matching the 18-digit PRC resident ID
pattern**, outside the `110101199001010015` placeholder that the sources now use. Some
may be synthetic test data, but nothing in the repository distinguishes them from real
ones, so treat them as real.

The current tree is clean — the CI `hygiene` job fails the build if an ID number outside
the placeholder appears in a `.java`, `.xml`, `.properties` or `.txt` file. The exposure
is historical only.

**Why this is a separate item from §1.** A credential is something you rotate. Personal
data cannot be rotated: once an ID number has been in a public repository, it is out.
For a system derived from Chinese social-insurance services this falls under the Personal
Information Protection Law, and the obligation is to minimise and to stop further
processing, not merely to change a password.

**Owner action.** Decide deliberately whether the history should be rewritten (see §1
step 2) knowing that rewriting does not retract anything already cloned, and record that
decision somewhere. Do not add further real identifiers to fixtures; the placeholder
exists for that reason.

### 3. Session filter does not sanitize XSS

`BusinessFilter` is enabled on selected `/iface/*` paths and validates sessions,
channels, and third-party permissions. It does **not** escape or sanitize request
input. Other paths are not covered by this filter.

**Impact.** HTML endpoints that echo untrusted input remain vulnerable unless they
use context-appropriate output encoding. Audit template rendering and browser-facing
responses separately; do not mistake authorization for XSS prevention.

### 4. TLS trust configuration

`IFaceSuportService/src/main/java/com/tecsun/sisp/iface/client/SSLClient.java`
uses the JVM default trust store and strict hostname verification. Deployments
with a private CA must import it into the JVM trust store; do not disable
certificate or hostname verification to work around trust errors.
### 5. MD5 password hashing

`NetPortalManagement/src/main/java/com/tecsun/sisp/npm/util/MD5Generator.java` hashes
passwords with unsalted MD5.

**Remediation.** Use bcrypt or argon2 for new credentials, and migrate existing hashes on
next successful login (you cannot reverse MD5, but you can rehash after verifying).

### 6. Unauthenticated internal endpoints

`CardInfoInnerController` is mounted at `/ifaceInner/cardInfo` and is intended for
service-to-service calls. There is no authentication on that path beyond network
position. If the application is reachable from a broader network than intended, these two
endpoints are open.

### 7. Dead but dangerous JDBC helper

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
