---
name: Bug report
about: Something does not build, start, or behave as documented
title: "[bug] "
labels: bug
---

## What happened

<!-- A clear description of the problem. -->

## What you expected

<!-- What you expected instead. -->

## Steps to reproduce

1.
2.
3.

## Environment

| | |
| --- | --- |
| Module | <!-- e.g. IFaceSuportService --> |
| JDK | <!-- java -version --> |
| Maven | <!-- mvn -v --> |
| Container | <!-- Tomcat 8.5 / WebLogic 12c / none --> |
| OS | |

## Does the module build for you?

<!--
The four default reactor modules should build with public dependencies using JDK 8
and `mvn -B verify`. NetPortalManagement is opt-in (`-Pprivate-deps`) and requires
private Bee dependencies. If the default build cannot resolve a dependency, please
report the artifact and repository URL (redact credentials).
-->

- [ ] Builds cleanly
- [ ] Fails on dependency resolution (include artifact and module)
- [ ] Fails for another reason (paste below)

## Logs / stack trace

```
paste here
```

## Anything else

<!-- Screenshots, related issues, whether it worked before, etc. -->
