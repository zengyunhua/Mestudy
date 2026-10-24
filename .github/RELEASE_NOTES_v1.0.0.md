# Mestudy v1.0.0

First tagged release. This is the point at which the repository stops being a dump of
production code and starts being something another developer can clone, build and read.

## What this is

A multi-module Maven workspace containing the backend interface services of a Chinese
social-insurance / human-resources public service platform. Five modules cover
social-insurance queries, the social-security card lifecycle, employment services,
hospital registration, face-based survival verification, and draft-based online service
applications. Spring MVC 4 with XML configuration, MyBatis 3, Oracle, Redis, and Axis 1.x
for the provincial SOAP gateway.

It is a **reference archive**, not a product. The value is in the patterns and the domain
modelling, not in deploying it as-is.

## What builds

| Module | Builds from Maven Central | Endpoints | Blocking dependency |
| --- | --- | --- | --- |
| `IFaceSuportService` | yes | 251 | — |
| `IFaceSuperService_hn` | yes | 8 | — |
| `NetworkOfficeService` | no | 72 | `bee:xx.tool`, `com.tecsun.sisp_public:aamanagement-client` |
| `InterfaceFunction` | no | 27 | same |
| `NetPortalManagement` | no | 0 | `bee:cloud*` |

```bash
git clone https://github.com/zengyunhua/Mestudy.git
cd Mestudy
mvn -B -pl IFaceSuportService,IFaceSuperService_hn clean package -DskipTests
```

Verified on JDK 8 with Maven 3.8.7: both modules produce a war and, for
`IFaceSuportService`, the `-client.jar` SDK.

## Highlights

**The build is no longer tied to a corporate network.** Every POM hard-coded an internal
Nexus. Those declarations are gone — mirrors belong in `~/.m2/settings.xml`, not in a
project POM. Three dependencies that only existed behind that Nexus were replaced with
their public equivalents, and one was removed outright because nothing imported it.

**`IFaceSuperService_hn` had never compiled.** `InvokeUtil.java` was saved with a UTF-8
BOM, which `javac` rejects. That single byte sequence meant the module could not build
under any circumstances. Fixed.

**Documentation.** A root README, seven guides under `docs/`, a README per module, and the
usual community files. All of it is in English.

**CI.** GitHub Actions compiles the two buildable modules on JDK 8 and runs repository
hygiene checks that fail the build if build output, credentials, or personal identity
numbers are committed.

## Action required if you cloned before this release

**Rotate every credential that ever appeared in this repository.** The
`config.properties` and `ds.xml` files contained real database passwords and third-party
interface keys. They are no longer tracked, but they are still in the history of every
commit before this tag. Removing them from the working tree does not remove them from
your clone.

Treat as compromised: the Oracle schema passwords, the provincial gateway keys, and the
Redis credentials. See `SECURITY.md` for the rotation and history-scrub procedure.

## Breaking changes

- `IFaceSuperService_hn` artifactId changed from `IFaceSuportService` to
  `IFaceSuperService_hn`. It previously collided with the main module and was
  distinguished only by version. The war is now `IFaceSuperService_hn-V1.0.0.190107.war`.
- `config.properties` and `ds.xml` are no longer tracked. Copy the `.example` template and
  fill in your own values.
- The `unieap-dataexchange-client` dependency was removed. If you were relying on it
  transitively, declare it yourself.

## Known limitations

- Three modules cannot be built without the private `bee` framework and AA client.
- No assertion-based test suite. The four classes under `src/test` are manual `main()`
  entry points.
- The `common` layer is duplicated across modules: 54 class names appear in more than one
  module and no two copies are identical. Consolidating them into a shared artifact is
  the highest-value refactor available.
- `print-2.1.1.jar` is vendored into `NetworkOfficeService` with no licence metadata.
  Establish its provenance before redistributing commercially.

## What's next

Tagged versions of the individual modules, a shared `common` artifact, and JUnit coverage
for the parts that do not need an Oracle instance. See `docs/ROADMAP.md`.
