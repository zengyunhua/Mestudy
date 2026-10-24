# Pull request

## What does this change?

<!-- One or two sentences. Link the issue if there is one: Closes #123 -->

## Why?

<!--
The diff shows *what* changed. This section explains *why* — the bug, the missing
case, the confusion it removes.
-->

## Scope

- [ ] Single module (which one: ______)
- [ ] Build / dependencies
- [ ] Documentation only
- [ ] Cross-module (requires prior discussion — link it: ______)

## Type

- [ ] Bug fix (no behaviour change beyond the fix)
- [ ] Documentation
- [ ] Test
- [ ] Refactor (no behaviour change)
- [ ] Build / CI

## Verification

<!--
Be specific. "It works" is not verifiable. Examples:

  mvn clean package -DskipTests   -> BUILD SUCCESS
  Deployed to Tomcat 8.5, called GET /iface/user/checkLogin -> statusCode 200
  No database available; compiled with mvn compile only
-->

```
paste commands and results
```

## Checklist

- [ ] I read [CONTRIBUTING.md](../CONTRIBUTING.md)
- [ ] The change is limited to one concern
- [ ] No unrelated reformatting or dependency bumps
- [ ] New public classes/methods have English Javadoc
- [ ] New configuration keys are added to `config.properties.example` and documented
- [ ] No credentials, internal hostnames or real personal data are included
- [ ] Controllers still return `Result` / `ResultVerify` via `BaseController` helpers
- [ ] Documentation updated if behaviour or setup changed

## Notes for reviewers

<!-- Anything you are unsure about, or where you would like a second opinion. -->
