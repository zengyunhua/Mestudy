# Contributing to Mestudy

Thanks for taking the time to look at this repository. It is an archive of
production-derived modules published as a reference, maintained on a best-effort basis.
Contributions that make it **easier to understand, build, or trust** are the most
valuable.

This guide tells you how to get a change accepted without a long review cycle.

---

## Before you start

| Change type | What to do first |
| --- | --- |
| Typo, broken link, comment clarification | Open a pull request directly |
| New test, new documentation | Open a pull request directly |
| Bug fix | Open an issue if the cause is not obvious, otherwise a PR |
| Refactor touching more than one module | **Open an issue first** |
| New dependency, framework upgrade, build change | **Open an issue first** |
| Anything in the `common` layer | **Open an issue first** — it is duplicated across modules |

The reason for the last two rows: `common` classes exist in five copies with slightly
different signatures. A change in one is a change in five, and reviewers need to agree on
the approach before the diff exists.

## Good first issues

If you want to contribute but are not sure where to start:

1. Add a unit test for `Page` or `Result` in `IFaceSuportService` — no database needed.
2. Translate one class-level Chinese comment into English Javadoc.
3. Document one controller in `docs/api/` (see [docs/API_OVERVIEW.md](docs/API_OVERVIEW.md)).
4. Fix the `ResultVerify` field-shadowing issue described in
   [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md#resultverify-shadows-its-parents-fields).
5. Replace a hard-coded constant with a configuration key and add it to
   `config.properties.example`.

## Development setup

```bash
git clone https://github.com/zengyunhua/Mestudy.git
cd Mestudy

# Pick the module you are changing
cd InterfaceFunction
cp src/main/resources/config.properties.example src/main/resources/config.properties
mvn clean package
```

JDK 8 is the supported runtime. JDK 11+ breaks Axis 1.x — see
[docs/GETTING_STARTED.md](docs/GETTING_STARTED.md#1-prerequisites).

### Build limitations

Some modules declare an internal Nexus repository that is not reachable from the public
internet, so a clean build fails on those modules. This is a known issue tracked in the
[roadmap](docs/ROADMAP.md). If your change requires a module that cannot be built
publicly, say so in the pull request and describe how you verified it.

## Coding conventions

These match the existing code. Consistency beats personal preference here — the value of
this repository is that a reader can move between modules without re-learning the style.

### Java

- **Language level**: Java 8. It is declared once in the root `pom.xml`
  (`maven.compiler.source`/`target` = `1.8`); do not override it in a module POM.
  Java 8 constructs — lambdas, method references, `java.time`, streams — are fine in
  new code. Do not rewrite existing classes purely to adopt them.
- **Indentation**: 4 spaces. No tabs.
- **Encoding**: UTF-8, no BOM.
- **Braces**: K&R — opening brace on the same line.
- **Naming**: `XxxController`, `XxxService` / `XxxServiceImpl`, `XxxDAO`, `XxxVO`.
- **Packages**: keep the existing domain grouping. Do not introduce a new top-level
  package without discussing it.
- **Javadoc**: required on new public classes and public methods. English. Describe
  *contract*, not implementation — say what the method guarantees, what it throws, and
  what it returns on failure.
- **No `System.out.println`** in new code. Use SLF4J: `private static final Logger
  logger = LoggerFactory.getLogger(Xxx.class);`
- **No new raw types.** The existing code has plenty of `Map` and `List` without
  generics; do not add more.

### Response envelope

Every new endpoint must return `Result` or `ResultVerify` through the `BaseController`
helpers:

```java
return result("200", "查询成功", vo);
return ok(total, "查询成功", list);
```

Never return a raw VO or a `Map` from a controller.

### Comments

Prefer explaining *why* over *what*. A comment that restates the code is noise:

```java
// BAD
// set the page number
page.setPageNo(1);

// GOOD
// The provincial gateway caps result sets at 500 rows; page 1 is the only
// page we ever request for the summary view.
page.setPageNo(1);
```

### SQL and mappers

- One statement per `<select>`/`<insert>` with an `id` matching the DAO method name.
- The XML `namespace` must be the DAO interface's fully-qualified name. In
  `InterfaceFunction` that is `...modules.dao.impl.RoleDAO`, **not** `...modules.dao`.
- Avoid `SELECT *`. List columns — several tables have `BLOB` columns that will
  surprise you.
- Do not introduce dialect-specific SQL into a mapper that is shared across datasources.

### Configuration

- New keys go into `config.properties.example` in the same commit, with a comment.
- Never commit `config.properties` or `ds.xml`.
- Use `CHANGE_ME` for values that must be supplied by the operator.

## Commit messages

Conventional Commits, with the module as scope:

```
feat(iface): add batch query for pension contributions
fix(network-office): correct draft status transition on resubmit
docs(readme): document the client SDK token lifecycle
test(function): add unit tests for Page
chore(build): stop tracking target/ directories
refactor(common): extract shared Result envelope
```

Body: explain **why**, not what. The diff already says what.

## Branching

`master` is the only long-lived branch, and it is the default branch on GitHub.

- **Small changes** — documentation, typo fixes, comment clarifications, a one-line bug
  fix — commit straight to `master`.
- **Anything larger** — a fix touching several files, a refactor, a new feature — work on
  a short-lived branch (`fix/...`, `feat/...`) and open a pull request against `master`.
  The branch is deleted after the merge.

There is no `develop` branch. With a single maintainer, a second long-lived branch only
means the default branch drifts behind the work that is actually happening, so visitors
see stale code and a stale "last commit" date.

## Pull requests

A good PR here is small and explains itself.

- **Target `master`.**
- **One concern per PR.** A bug fix plus a refactor is two PRs.
- **Describe verification.** "Built with `mvn clean package` on JDK 8, deployed to
  Tomcat 8, called `/iface/user/checkLogin` and got `statusCode: 200`." If you could not
  build, say what you did instead.
- **Link the issue** if one exists.
- **Do not reformat unrelated code.** A whitespace-only diff hides the real change.
- **Do not bump dependency versions** as a side effect.

Reviewers will check: does it build, does it follow the conventions above, does it
preserve the response envelope, and does it add a test where a test is feasible.

## Documentation contributions

Documentation is as valuable as code here. When writing:

- Use English for all new documentation.
- Prefer concrete commands over prose. A copy-pasteable `curl` beats a paragraph.
- When you find a bug while documenting it, **document the bug** in
  [docs/TROUBLESHOOTING.md](docs/TROUBLESHOOTING.md) rather than silently working around
  it.
- Keep the module map in the root README in sync if you add or rename a module.

## Reporting bugs

Include:

1. Module and version.
2. JDK, Maven and container versions.
3. The exact command that failed and the full stack trace.
4. Whether the module builds for you at all (many do not, publicly — that is useful
   information, not a reason to stay silent).

## Security issues

Do **not** open a public issue. See [SECURITY.md](SECURITY.md).

## Code of conduct

All participation is governed by the [Code of Conduct](CODE_OF_CONDUCT.md). Be
straightforward, assume good faith, and criticise code rather than people.

## License

By contributing you agree that your contribution is licensed under the
[Apache License 2.0](LICENSE), and you confirm you have the right to submit it.
