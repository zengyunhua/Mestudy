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
Many modules depend on an internal Nexus that is not publicly reachable, so a clean
build may fail on dependency resolution. That is a known limitation, not something you
did wrong — but knowing it helps us tell the two apart.
-->

- [ ] Builds cleanly
- [ ] Fails on dependency resolution (private Nexus)
- [ ] Fails for another reason (paste below)

## Logs / stack trace

```
paste here
```

## Anything else

<!-- Screenshots, related issues, whether it worked before, etc. -->
