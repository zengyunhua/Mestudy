# IFaceSuportService

The core interface service of the platform. It exposes the social-insurance and
social-security-card API surface consumed by self-service terminals, the web portal and
the WeChat channel, and it also publishes the **client SDK** that terminals embed.

| | |
| --- | --- |
| Artifact | `com.tecsun.sisp:IFaceSuportService` |
| Packaging | `war` (plus a `client`-classified jar) |
| Version | `V1.0.0.150630` |
| Java files | 286 |
| Endpoints | 251 |
| Datasources | 8 |

---

## Responsibility

- **Social-insurance queries** — pension, medical, unemployment, maternity and
  work-injury contribution records, account balances and benefit payments.
- **Social-security card** — card info, status, loss reporting, suspension, activation,
  phone-number change, replacement hub (`cssp`).
- **Employment services** — job-seeker profiles, job directions, vacancy queries.
- **Hospital registration** — adapters for two HIS vendors (Zhongxin, Hangtian).
- **Face verification** — enrolment, comparison, liveness, backed by an internal service
  and a provincial biometric gateway.
- **Channel integrations** — bank, fee payment, SMS, WeChat, policy documents.
- **Client SDK** — `com.tecsun.sisp.iface.client.IFaceHttpsClient`, packaged separately.

## Layout

```
com/tecsun/sisp/iface/
├── client/                      # published SDK — no Spring, no servlet API
│   ├── IFaceHttpsClient.java    # 26 static operations
│   ├── LoginUtil.java           # token acquisition
│   ├── HttpClientUtil.java      # request execution
│   ├── SSLClient.java           # permissive HTTPS client
│   └── instruction.txt          # original (Chinese) integration note
├── common/
│   ├── vo/                      # transport objects: Result, Page, SecQueryBean, ...
│   │   ├── archive/  card/  cssp/  employment/  faceverify/
│   │   ├── his/  his/zx/  mess/  xgcard/
│   ├── page/                    # paging helpers
│   └── util/                    # AESUtil, JsonMapper, PicUtil, ...
└── server/
    ├── controller/              # 21 controllers, grouped by domain
    │   ├── archive/ bank/ card/ cssp/ employment/ faceManager/
    │   ├── fee/ his/ security/ sms/ so/ wechat/ yldy/
    ├── entity/                  # persistence-shaped classes (card, requestBean)
    ├── model/
    │   ├── dao/ dao_cssp/ dao_dr/ dao_net/ dao_sisp_public/
    │   ├── dao_xg/ dao_yldygxhcs/ dao_zj/     # one package per datasource
    │   └── service/ + service/impl/
    └── util/                    # Config, DataBase, JedisUtil, DictionaryUtil, ...
```

The `dao*` packages are split **by datasource**, not by domain. Each package is bound to
its own `MapperScannerConfigurer` in `applicationContext.xml`. Adding a DAO to the wrong
package is the most common cause of a "mapper not found" error.

## Endpoints

| Base path | Controller | Domain |
| --- | --- | --- |
| `/iface/rest` | `SecQueryController` | Social-insurance queries (largest controller, 55 mappings) |
| `/iface/job` | `JobQueryController` | Employment |
| `/iface/his/zhongxinhis` | `ZhongxinHisController` | Hospital registration (Zhongxin) |
| `/iface/his/hangtianhis` | `HangTianHisController` | Hospital registration (Hangtian) |
| `/iface/his/sce` | `ZhongxinHisSceRegController` | Hospital registration (SCE) |
| `/iface/cssp` | `CsspBusController` | Card replacement hub |
| `/iface/face` | `FaceController` | Face enrolment |
| `/iface/faceVerify` | `FaceVerifyController` | Face verification |
| `/iface/bank` | `BankController` | Bank channel |
| `/iface/cardInfo` | `CardInfoController` | Card info |
| `/iface/emp` | `EmpQueryController` | Employment queries |
| `/iface/fee` | `FeeController` | Fees |
| `/iface/policy` | `PolicyController` | Policy documents |
| `/iface/user` | `UserController` | Login / token |
| `/iface/archive`, `/iface/code`, `/iface/sms`, `/iface/yldy`, `/iface/zhongjian`, `/iface/wechat` | misc | 2 endpoints each |
| `/ifaceInner/cardInfo` | `CardInfoInnerController` | Service-to-service card info |

Full index with counts: [../docs/API_OVERVIEW.md](../docs/API_OVERVIEW.md).

## Configuration

Eight datasources are wired in `config/applicationContext.xml`:

| Bean | Key prefix |
| --- | --- |
| `dataSource` | `jdbc.dongruan.*` |
| `zhongjianDataSource` | `jdbc.zhongjian.*` |
| `drDataSource` | `jdbc.drface.*` |
| `faceDataSource` | `jdbc.face.*` |
| `csspDataSource` | `jdbc.cssp.*` |
| `sispPublicDataSource` | `jdbc.sisp_public.*` |
| `dryldyDataSource` | `jdbc.dryldy.*` |
| `netportalDataSource` | `jdbc.sisp.*` |

All eight must resolve or the context fails to load. See
[../docs/CONFIGURATION.md](../docs/CONFIGURATION.md).

## Build

```bash
cd IFaceSuportService
cp src/main/resources/config.properties.example src/main/resources/config.properties
mvn clean package
```

Outputs:

```
target/IFaceSuportService-V1.0.0.150630.war          # deployable
target/IFaceSuportService-V1.0.0.150630-client.jar   # embeddable SDK
```

The client jar is produced by a `maven-jar-plugin` execution with `classifier=client`
that includes only `**/client/**.class` and `**/common/**/**.class`. If you add a class
the SDK needs, make sure it lives under one of those two trees — otherwise it compiles
but is missing at runtime for SDK consumers.

## Deploy

```bash
cp target/IFaceSuportService-*.war $CATALINA_HOME/webapps/sisp.war
$CATALINA_HOME/bin/startup.sh
```

`web.xml` registers:

- `CharacterEncodingFilter` (`/*`, UTF-8, forced)
- `ContextLoaderListener` + `StartupListener` (Redis pool, thread pool)
- `DispatcherServlet` `springMVC3` mapped to `/`
- `HiddenHttpMethodFilter` (allows PUT/DELETE from HTML forms)

## Using the SDK

```java
IFaceHttpsClient.ClientInit("127.0.0.1", 8080, 60, false, "TSB", "sisp");

SecQueryBean bean = new SecQueryBean();
bean.setId("110101199001010015");
bean.setChannelcode("SelfService");

Page<TsgrjfxxVO> page = IFaceHttpsClient.getPersonPesionPay(bean);
```

`ClientInit` is one-shot per JVM and starts a heartbeat thread that renews the token.
Full reference: [../docs/CLIENT_SDK.md](../docs/CLIENT_SDK.md).

## Notes for contributors

- **Do not edit generated SOAP stubs.** `server/outerface/**/*SoapBindingStub.java` is
  generated from WSDL. Regenerate instead.
- **Keep the `Result` envelope.** New endpoints must return `Result` or `ResultVerify`
  via the `BaseController` helpers, not raw objects.
- **`statusCode` is a `String`.** `"200"` / `"0"`, never a number.
- **Transactions do not span datasources.** A flow touching `sisp` and `zhongjian` is
  eventually consistent — make it idempotent.
- **`BusinessFilter` (XSS) is disabled** in `web.xml`. Re-enabling it is on the roadmap.
