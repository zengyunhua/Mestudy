# Getting started

This guide takes you from a fresh clone to a running service and a working client call.
It assumes you have a servlet container, an Oracle instance and a Redis instance
available. If you only want to *read* the code, skip to
[step 6](#6-optional-explore-without-a-database).

Estimated time: 15–30 minutes, most of it waiting for Maven and for the container to
start.

---

## 1. Prerequisites

| Tool | Version | Check |
| --- | --- | --- |
| JDK | 8 (7 works; 11+ needs extra flags) | `java -version` |
| Maven | 3.2+ | `mvn -v` |
| Servlet container | Tomcat 7/8, or WebLogic 12c | — |
| Oracle | 10g / 11g | `sqlplus -v` |
| Redis | 2.8+ | `redis-cli ping` |

> **Why JDK 8 and not 11?** The stack relies on Axis 1.x, c3p0 and `javax.*` APIs that
> assume the pre-module classpath. On JDK 11+ you will hit
> `IllegalAccessError` from Axis reflection. See
> [TROUBLESHOOTING.md](TROUBLESHOOTING.md#java-11-illegalaccesserror-from-axis).

## 2. Clone

```bash
git clone https://github.com/zengyunhua/Mestudy.git
cd Mestudy
```

## 3. Provide configuration

Every module reads a single `config.properties` from its classpath. A template is
committed; copy it and fill in your own values:

```bash
cd IFaceSuportService/src/main/resources
cp config.properties.example config.properties
```

Minimum set to get the application to deploy:

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

`config.properties` is git-ignored. Never commit it.

## 4. Build

```bash
cd IFaceSuportService
mvn clean package -DskipTests
```

Expected artefacts:

```
target/IFaceSuportService-V1.0.0.150630.war
target/IFaceSuportService-V1.0.0.150630-client.jar
```

The `-client` jar is produced by the `maven-jar-plugin` execution with
`classifier=client`, and contains only `**/client/**` and `**/common/**` classes. That is
the artefact a downstream terminal embeds.

### If the build fails to resolve dependencies

The POMs declare internal Nexus repositories (`192.168.1.8`, `11.11.11.8`) that are not
reachable from the public internet. Options:

1. **Build only the modules that resolve.** `InterfaceFunction` and
   `NetPortalManagement` are the most self-contained.
2. **Point the repository at a mirror you control.** Edit the `<repositories>` block, or
   add a `~/.m2/settings.xml` mirror.
3. **Vendor the missing jars** into `src/main/webapp/WEB-INF/lib/` and drop the
   dependency declaration. Acceptable for local experiments, not for a contribution.

This is a known limitation; see [ROADMAP.md](ROADMAP.md).

## 5. Deploy and smoke-test

```bash
# Tomcat
cp target/IFaceSuportService-*.war $CATALINA_HOME/webapps/sisp.war
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

A JSON body with `statusCode` means the stack is up. The exact payload depends on your
data; a failure code with a readable `message` is still a healthy deployment.

## 6. Optional: explore without a database

If you do not have Oracle available, you can still work with the codebase:

```bash
# Read the architecture
less docs/ARCHITECTURE.md

# Inspect every HTTP endpoint without running anything
grep -rn "@RequestMapping" --include=*.java . | head -50

# Compile just the common layer, which has no datasource dependency
cd IFaceSuportService
mvn -q compile
```

`mvn compile` needs the third-party jars on the classpath, so it is subject to the same
Nexus limitation as the build. Reading and grepping always works.

## 7. Call the service from Java

Add the client jar, then initialise once per JVM:

```java
import com.tecsun.sisp.iface.client.IFaceHttpsClient;
import com.tecsun.sisp.iface.common.vo.Page;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;

public class Demo {
    public static void main(String[] args) throws Exception {
        IFaceHttpsClient.ClientInit("127.0.0.1", 8080, 60, false, "TSB", "sisp");

        SecQueryBean bean = new SecQueryBean();
        bean.setId("000000199001010000");
        bean.setName("...");
        bean.setChannelcode("SelfService");

        Page<?> page = IFaceHttpsClient.getPersonPesionPay(bean);
        System.out.println(page.getData().size());
    }
}
```

`ClientInit` starts a background thread that calls `/iface/user/checkLogin` every
`tokenTimeOut` minutes and refreshes the static `tokenId`. It throws if called twice in
the same JVM. Full reference: [CLIENT_SDK.md](CLIENT_SDK.md).

## 8. Where to go next

| If you want to… | Read |
| --- | --- |
| Understand the layering and contexts | [ARCHITECTURE.md](ARCHITECTURE.md) |
| Know what every config key does | [CONFIGURATION.md](CONFIGURATION.md) |
| Find an endpoint | [API_OVERVIEW.md](API_OVERVIEW.md) |
| Fix a build or runtime problem | [TROUBLESHOOTING.md](TROUBLESHOOTING.md) |
| Contribute | [../CONTRIBUTING.md](../CONTRIBUTING.md) |
