# Client SDK

`IFaceSuportService` publishes an embeddable jar that lets a Java consumer call the
interface service without hand-rolling HTTP, JSON or token handling.

```
target/IFaceSuportService-V1.0.0.150630-client.jar
```

It contains only `com/tecsun/sisp/iface/client/**` and
`com/tecsun/sisp/iface/common/**` — the entry point class `IFaceHttpsClient` and the
transport VOs it returns. No Spring, no MyBatis, no servlet API.

---

## 1. Add the dependency

Build the module first (`mvn clean package` in `IFaceSuportService`), then either install
it locally or reference the jar directly.

```xml
<dependency>
    <groupId>com.tecsun.sisp</groupId>
    <artifactId>IFaceSuportService</artifactId>
    <version>V1.0.0.150630</version>
    <classifier>client</classifier>
</dependency>
```

The SDK is not self-contained. These transitive dependencies must be on the consumer's
classpath:

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.1.2</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient-cache</artifactId>
    <version>4.1.2</version>
</dependency>
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpmime</artifactId>
    <version>4.1.2</version>
</dependency>
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-mapper-asl</artifactId>
    <version>1.9.13</version>
</dependency>
<dependency>
    <groupId>org.codehaus.jackson</groupId>
    <artifactId>jackson-core-asl</artifactId>
    <version>1.9.13</version>
</dependency>
<dependency>
    <groupId>log4j</groupId>
    <artifactId>log4j</artifactId>
    <version>1.2.17</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.6.6</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.6.6</version>
</dependency>
```

## 2. Initialise once per JVM

```java
IFaceHttpsClient.ClientInit(
        "127.0.0.1",  // host of the interface service
        8080,         // port
        60,           // token refresh interval, minutes (values <= 60 are raised to 60)
        false,        // useHttps — false means plain HTTP
        "TSB",        // channel code
        "sisp"        // platform context path
);
```

| Parameter | Type | Rules |
| --- | --- | --- |
| `ip` | `String` | Required. Empty or `null` throws. |
| `port` | `int` | Required. `0` throws. |
| `tokenTimeOut` | `int` | Minutes between token renewals. Values `<= 60` are silently raised to `60`. |
| `useHttps` | `boolean` | Selects the `https://` or `http://` scheme for every call. |
| `channelcode` | `String` | Channel identity, sent on login. `TSB` = Deshunbao terminal. |
| `platformContext` | `String` | Context path. A leading `/` is added if missing. |

### What initialisation actually does

`ClientInit` stores the parameters in **static fields** and starts a single daemon-less
`TokenGetter` thread:

```
while (true) {
    tokenId = LoginUtil.checkLogin(port, url, channelcode);
    Thread.sleep(tokenTimeOut * 60 * 1000);
}
```

Two consequences you must plan for:

1. **Call it exactly once.** A second call throws
   `Exception("Error: Do not init again !!!!! ")` because the thread reference is
   non-null.
2. **One JVM talks to one server.** `ip`, `port` and `tokenId` are static. A process
   cannot target two interface services. If you need that, run two JVMs or fork the
   class into a non-static client.

The first token is fetched asynchronously, so an immediate call after `ClientInit` may
race with the first login. In practice the login completes in milliseconds; if you need
a hard guarantee, poll a cheap endpoint until it stops returning an auth failure.

## 3. Call an operation

All operations are `static` and return either `Page<T>` or `Result`.

```java
SecQueryBean bean = new SecQueryBean();
bean.setId("110101199001010015");
bean.setName("...");
bean.setChannelcode("SelfService");

Page<TsgrjfxxVO> page = IFaceHttpsClient.getPersonPesionPay(bean);
for (TsgrjfxxVO row : page.getData()) {
    // ...
}
```

> **Note the asymmetry.** `Page<T>` carries only `pageNo`, `pageSize`, `count` and
> `data` — it does **not** extend `Result`, and the SDK discards the envelope's
> `statusCode` and `message` when building it. Only the card operations, which return
> `Result`, expose a status code. See [error handling](#4-error-handling).

### Available operations

**Social-insurance queries** — return `Page<...VO>`

| Method | Returns |
| --- | --- |
| `getSelfInfo` | `Page<TsjbxxVO>` — personal basic info |
| `getPersonPesionPay` | `Page<TsgrjfxxVO>` — pension contribution detail |
| `getPersonPay` | `Page<TsgrjfjsxxVO>` — contribution settlement |
| `getPersonSureInfo` | `Page<TsgrcbxxVO>` — insured-person info |
| `getPesionInfo` | `Page<TsylbxjbxxVO>` — pension insurance basic info |
| `getPesionPay` | `Page<TsyldyffxxVO>` — pension benefit payments |
| `getPesionTreatment` | `Page<TsylbxgrjfmxVO>` — pension contribution detail |
| `getPesionYearAccount` | `Page<TsylgrzhxxVO>` — pension account by year |
| `getPesionMouthAccount` | `Page<TsylyzhxxVO>` — pension account by month |
| `getQuitPay` | `Page<TsylbxltxrydyxxVO>` — withdrawn-insured benefits |
| `getHealthAccount` | `Page<TsmtbxgrjbxxVO>` — medical account basic info |
| `getHealthPay` | `Page<TsmtbxgrjfmxVO>` — medical contribution detail |
| `getHealthFee` | `Page<TsmtbxfyjsxxVO>` — medical expense settlement |
| `getLoseFee` | `Page<TslybxjbxxVO>` — unemployment insurance basic info |
| `getLosePay` | `Page<TssydyffxxVO>` — unemployment benefit payments |
| `getBirthInfo` | `Page<TssybxjbxxVO>` — maternity insurance basic info |
| `getHurtInfo` | `Page<TsgsbxjbxxVO>` — work-injury insurance basic info |

**Card operations** — return `Result`

| Method | Purpose |
| --- | --- |
| `getCardInfo` | Card summary |
| `getCardAllInfo` | Full card record |
| `getCardStatus` | Card status |
| `setStart` | Unlock / activate |
| `setLoss` | Report lost |
| `setHanging` | Suspend |
| `changePhoneNo` | Change the bound mobile number |

## 4. Error handling

The SDK does not throw for business failures, and it does not give you a uniform way to
detect them. This is the most surprising part of the API:

| Return type | How failure surfaces |
| --- | --- |
| `Page<T>` (queries) | The envelope is unwrapped and `statusCode`/`message` are **dropped**. On failure the server returns `data: null`, so you get a `Page` with an empty `data` list and `count == 0`. |
| `Result` (card ops) | The envelope is returned intact — check `statusCode`. |

So a paged query cannot distinguish "no records" from "server rejected the request"
through the return value alone. Options:

- Log the raw response in your HTTP layer if you need the reason.
- Call the underlying endpoint directly for diagnostics.
- Treat empty results as "no data or failure" and confirm with a second call.

Transport failures (connection refused, timeout, non-200 HTTP) surface as exceptions
from the underlying Apache HttpClient and are **not** wrapped. Calling any operation
before `ClientInit` throws `Exception(" Please init   !!!!! ")`.

```java
try {
    Page<TsgrjfxxVO> page = IFaceHttpsClient.getPersonPesionPay(bean);
    if (page.getData().isEmpty()) {
        log.warn("no pension rows returned (empty result or rejected request)");
    }
} catch (Exception e) {
    log.error("transport failure or SDK not initialised", e);
}

// Card operations do expose the envelope
Result r = IFaceHttpsClient.getCardStatus(bean);
if (!"200".equals(r.getStatusCode())) {
    log.warn("card query failed: {}", r.getMessage());
}
```

## 5. HTTPS

Passing `useHttps = true` switches the scheme. The SDK ships `SSLClient`, which
installs a permissive trust manager. **That is acceptable for an internal network and
unacceptable for anything crossing the public internet.** If you deploy over HTTPS
publicly, replace `SSLClient` with a truststore-based `SSLContext` before relying on it.

## 6. Thread safety

| State | Shared? | Implication |
| --- | --- | --- |
| `ip`, `port`, `tokenId`, `channelcode` | `static` | Process-wide; set once at init |
| `TokenGetter` thread | single instance | Cannot re-init |
| Operation methods | stateless apart from the above | Safe to call from multiple threads |

## 7. Complete example

See [`Test.java`](../IFaceSuportService/src/main/java/com/tecsun/sisp/iface/client/Test.java)
in the client package for a runnable `main`.
