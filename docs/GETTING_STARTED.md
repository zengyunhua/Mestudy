# Getting started

This guide separates a public build (JDK 8 and Maven, no Oracle or Redis) from an optional deployment (Tomcat, Oracle, Redis, schemas and external services). A successful build is not proof of a runnable deployment.
For deployment you additionally need Tomcat, Oracle, Redis, and working application configuration. For code exploration without a database, see [step 6](#6-optional-explore-without-a-database).

Start with [the four-module build](#4-build-and-verify-independently) to check that you can contribute independently; skip step 3 unless deploying. Deployment time depends on your external infrastructure.

---

## 1. Prerequisites

| Tool | Version | Check |
| --- | --- | --- |
| JDK | 8 (required; 11+ needs extra flags) | `java -version` |
| Maven | 3.2+ | `mvn -v` |
| Servlet container | Tomcat 7/8, or WebLogic 12c | — |
| Oracle | 10g / 11g | `sqlplus -v` |
| Redis | 2.8+ | `redis-cli ping` |

> **Why JDK 8 and not 11?** The stack relies on Axis 1.x, c3p0 and `javax.*` APIs that
> assume the pre-module classpath. On JDK 11+ you will hit
> `IllegalAccessError` from Axis reflection. See
> [TROUBLESHOOTING.md](TROUBLESHOOTING.md#java-11-illegalaccesserror-from-axis).
>
> Java 8 is also the **language level**: the root POM sets `maven.compiler.source` and
> `target` to `1.8`, and all five modules inherit it. Compiling on JDK 9+ works but
> prints "source value 8 is obsolete" warnings — see
> [TROUBLESHOOTING.md](TROUBLESHOOTING.md#mvn-warns-that-sourcetarget-18-is-obsolete).

## 2. Clone

```bash
git clone https://github.com/zengyunhua/Mestudy.git
cd Mestudy
```

## 3. Provide configuration (deployment only)

For the example IFaceSuportService deployment, copy its template and fill in your own values. No configuration is needed for the public build:

```bash
cd IFaceSuportService/src/main/resources
cp config.properties.example config.properties
```

Illustrative subset of settings (not sufficient by itself to deploy):

```properties
platform_context_path=/sisp
redis_core_database=127.0.0.1_6379
redis_iface_expx=60

jdbc.dongruan.driverClassName=oracle.jdbc.driver.OracleDriver
jdbc.dongruan.url=jdbc:oracle:thin:@127.0.0.1:1521:orcl
jdbc.dongruan.username=your_schema
jdbc.dongruan.password=your_password

jdbc.maxIdleTime=3000
jdbc.minPoolSize=10
jdbc.maxPoolSize=300
jdbc.initialPoolSize=10
```

Every datasource bean referenced by `applicationContext.xml` **must** have keys present,
even if you point several of them at the same schema — the context fails to load on an
unresolvable placeholder. The full key list is in
[CONFIGURATION.md](CONFIGURATION.md).

`config.properties` is git-ignored. Never commit it. If the listed keys or database schema are unavailable, stop after the public build; do not treat placeholders as a deployable environment.

## 4. Build and verify independently

From the repository root. The default reactor is the four modules that resolve from
Maven Central; the remaining one is opt-in:

```bash
# Build everything in the default reactor, tests included
mvn -B verify

# Tests only, no database and no deployed service
mvn -B test

# Optional: only after installing legitimately obtained private Bee jars
mvn -B -Pprivate-deps verify
```

For a clean dependency-resolution check on a fresh machine, point Maven at a new,
empty local repository (do not copy the maintainer's `~/.m2/repository`):

```bash
mvn -B -ntp -Dmaven.repo.local=/tmp/mestudy-m2 verify
python3 scripts/verify_public_build.py
```

On Windows use an empty writable directory such as `D:\temp\mestudy-m2` instead of
`/tmp/mestudy-m2`. The checker requires one nonempty WAR and at least one executed,
passing Surefire test per public module; it does **not** prove a deployed application
works. GitHub Actions uses an isolated Maven repository on each run; its actual
status must still be checked on the relevant commit.
Expected artefacts (paths are relative to the repository root; check the exact versions in your checkout):

```
IFaceSuportService/target/IFaceSuportService-V1.0.0.150630.war
IFaceSuportService/target/IFaceSuportService-V1.0.0.150630-client.jar
IFaceSuperService_hn/target/IFaceSuperService_hn-V1.0.0.190107.war
InterfaceFunction/target/InterfaceFunction-V1.0.0.150630.war
NetworkOfficeService/target/NetworkOfficeService-V1.0.0.180612.war
```

On Windows, if Surefire cannot fork because `%TEMP%` contains non-ASCII characters,
set `TEMP` and `TMP` to an existing, writable ASCII-only directory before running
Maven (in PowerShell, e.g. `New-Item -ItemType Directory -Force D:\temp\mestudy-surefire;
$env:TEMP='D:\temp\mestudy-surefire'; $env:TMP=$env:TEMP; mvn -B verify`).
This preserves forked test execution. See [Troubleshooting](TROUBLESHOOTING.md).
`-DforkCount=0` is a last-resort diagnostic and does not provide equivalent isolation.

The `-client` jar is produced by the `maven-jar-plugin` execution with
`classifier=client`, and contains only `**/client/**` and `**/common/**` classes. That is
the artefact a downstream terminal embeds.

### Which modules build

| Module | Builds from Maven Central | Reactor | Missing |
| --- | --- | --- | --- |
| `IFaceSuportService` | yes | default | — |
| `IFaceSuperService_hn` | yes | default | — |
| `InterfaceFunction` | yes | default | — |
| `NetworkOfficeService` | yes | default | — |
| `NetPortalManagement` | no | `-Pprivate-deps` | `bee:cloud*` |

`NetPortalManagement` is kept out of the default reactor on purpose: a build that stops
on an unresolvable artifact teaches a newcomer nothing. Its dependency is genuinely
referenced by the code, so it cannot simply be deleted — install the jars into your local
repository first, then build with the profile.

Two modules moved into the default reactor during this work, for different reasons.
`InterfaceFunction` declared two private coordinates that nothing used: no import of
`com.tecsun.sisp_public` or `bee.*`, no `Class.forName`, no string in the resources.
`NetworkOfficeService` had the same dead declarations, but removing them exposed a source
file that was never published — `BanjianVo`, referenced by four classes and by
`mappings/draftsDAO.xml`. It has been reconstructed from the columns the mapper proves
exist; the provenance is in the class Javadoc, and it is the one place in this repository
where the code is not the original. If you have the real schema, diff it against your
DDL before trusting the JSON payload.

### Using a Maven mirror

The POMs no longer declare any repository, so Maven uses Central by default. If your
network requires a proxy, declare it once in `~/.m2/settings.xml` — never in the project
POM:

```xml
<settings>
  <mirrors>
    <mirror>
      <id>internal</id>
      <mirrorOf>*</mirrorOf>
      <url>https://your-mirror.example.com/repository/maven-public/</url>
    </mirror>
  </mirrors>
</settings>
```

### If the build still fails to resolve dependencies

The default four-module build needs no private jars. Only optional `NetPortalManagement` requires the private Bee framework. Options:

1. **Use the default reactor** — all four public modules build without Bee.
2. **Install all required, legally obtained artifacts** into your local repository. The following command illustrates one coordinate, not a complete dependency set:
   ```bash
   mvn install:install-file -Dfile=bee.cloud-1.0.4.jar \
       -DgroupId=bee -DartifactId=bee.cloud -Dversion=1.0.4 -Dpackaging=jar
   ```
3. **Do not delete the Bee dependency**: the portal code actually uses it. Do not commit proprietary jars without redistribution rights.

This is a known limitation; see [ROADMAP.md](ROADMAP.md).

## 5. Deploy and smoke-test (requires infrastructure)

```bash
# Tomcat
cp IFaceSuportService/target/IFaceSuportService-*.war "$CATALINA_HOME/webapps/sisp.war"
$CATALINA_HOME/bin/startup.sh
```

Watch the log for the two startup stages:

```
StartupListener  -> Redis pool initialised
DispatcherServlet -> framework servlet 'springMVC3' initialised
```

If the application deploys but every request 500s, the root context failed silently —
check for `BeanCreationException` in `catalina.out`.

Verify the service answers:

```bash
curl -i "http://localhost:8080/sisp/iface/user/checkLogin?channelcode=TSB"
```

A response is only a diagnostic. Confirm Tomcat and Spring start without errors, then test a configured route with non-sensitive test data and verify the expected outcome. An HTTP 500 or a JSON error body does not prove a working business flow. Port 8080 is an example; use the Tomcat HTTP connector port.

## 6. Optional: explore without a database

If you do not have Oracle available, you can still work with the codebase:

```bash
# Read the architecture
less docs/ARCHITECTURE.md

# Inspect every HTTP endpoint without running anything
grep -rn "@RequestMapping" --include=*.java . | head -50

# Verify the four public modules - no datasource is contacted at build time
mvn -B verify
```

The four-module default reactor resolves without private Bee jars. Only `-Pprivate-deps` needs authorised artifacts.

## 7. Call the service from Java (requires a working deployment)

Add the client jar, then initialise once per JVM:

```java
import com.tecsun.sisp.iface.client.IFaceHttpsClient;
import com.tecsun.sisp.iface.common.vo.Page;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;

public class Demo {
    public static void main(String[] args) throws Exception {
        IFaceHttpsClient.ClientInit("127.0.0.1", 8080, 60, false, "TSB", "sisp");

        SecQueryBean bean = new SecQueryBean();
        bean.setId("110101199001010015");
        bean.setName("...");
        bean.setChannelcode("SelfService");

        Page<?> page = IFaceHttpsClient.getPersonPesionPay(bean);
        System.out.println(page.getData().size());
    }
}
```

`ClientInit` fetches a token synchronously, then starts a background renewal thread every
`tokenTimeOut` minutes. Set `SISP_TSB_SECRET` in both the server and SDK processes before starting, and call `IFaceHttpsClient.shutdown()` on shutdown. It throws if called twice in
the same JVM. Full reference: [CLIENT_SDK.md](CLIENT_SDK.md).

## 8. Where to go next

| If you want to… | Read |
| --- | --- |
| Understand the layering and contexts | [ARCHITECTURE.md](ARCHITECTURE.md) |
| Know what every config key does | [CONFIGURATION.md](CONFIGURATION.md) |
| Find an endpoint | [API_OVERVIEW.md](API_OVERVIEW.md) |
| Fix a build or runtime problem | [TROUBLESHOOTING.md](TROUBLESHOOTING.md) |
| Contribute | [../CONTRIBUTING.md](../CONTRIBUTING.md) |
