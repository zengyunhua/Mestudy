# NetPortalManagement

Lightweight administration for the citizen portal: portal user accounts, published policy
 documents, and external web-connection statistics.

| | |
| --- | --- |
| Artifact | `com.tecsun.sisp:NetPortalManagement` |
| Packaging | `war` |
| Version | `V1.0.0.150630` |
| Java files | 16 |
| Endpoints | 11 JAX-RS resources |
| Persistence | Parameterized JDBC against Oracle |
| Authentication | Required API token filter |

---

## Public migration

This module no longer depends on the proprietary Bee framework, `xx.tool`, or the
private AAM Jersey filter. The migration keeps the existing resource paths while replacing
framework-specific behavior with public components:

- Plain Java DTOs replace Bee `Table` entities.
- `NetPortalRepository` replaces Bee `Cnd`/`Select` queries with parameterized JDBC.
- `NetSuperApplication` provides pagination and the JSON response envelope.
- Standard Jersey `ServletContainer` replaces Bee startup dispatch.
- `ApiTokenFilter` provides a fail-closed authentication boundary.
- `AppConfig` reads classpath properties, system properties, or `NPM_*` environment variables.

The migration is intentionally Oracle-oriented because the historical schemas and sequence
names are Oracle-specific. It makes the module publicly buildable; it does not remove the
need for an authorized Oracle schema for meaningful end-to-end tests.

## Layout

```
com/tecsun/sisp/npm/
├── bean/                         # plain API/data-transfer objects
├── repository/
│   └── NetPortalRepository.java  # parameterized JDBC queries and updates
├── security/
│   └── ApiTokenFilter.java       # fail-closed X-API-Token/Bearer authentication
├── service/                      # JAX-RS resources; URL paths are preserved
└── util/
    ├── AppConfig.java            # public configuration loader
    ├── Database.java             # Oracle JDBC connection factory
    ├── JsonResponse.java         # statusCode/message/data envelope
    ├── LogUtil.java
    ├── MD5Generator.java
    ├── PublicMethod.java
    └── StaticBean.java
```

## Configuration

Copy the template before deployment:

```bash
cp NetPortalManagement/src/main/resources/config.properties.example \
   NetPortalManagement/src/main/resources/config.properties
```

Required settings are:

```properties
jdbc.driver=oracle.jdbc.driver.OracleDriver
jdbc.url=jdbc:oracle:thin:@HOST:1521:orcl
jdbc.username=CHANGE_ME
jdbc.password=CHANGE_ME
api.token=CHANGE_ME
inner_proxy_ip=127.0.0.1
inner_proxy_port=80
platform_context_path=/sisp
upload.dir=uploads
```

`jdbc.url`, `jdbc.username`, and `jdbc.password` may instead be provided as
`NPM_JDBC_URL`, `NPM_JDBC_USERNAME`, and `NPM_JDBC_PASSWORD`. The API token may be
provided as `NPM_API_TOKEN`. The application refuses to start if `api.token` is missing or
still equals `CHANGE_ME`.

The API token can be sent in either form:

```http
X-API-Token: replace-with-a-secret
```

or:

```http
Authorization: Bearer replace-with-a-secret
```

Never commit `config.properties`, real tokens, database credentials, or uploaded files.

The old `bee_application.xml` and `ds.xml.example` files remain only as historical
reference artifacts; they are not read by the public runtime. New deployments should use
`config.properties` or environment variables.

## Endpoints

| Method | Path | Purpose |
| --- | --- | --- |
| GET | `/npmanagement/netUserInfo/queryNetUserInfoList` | Find the first matching portal user |
| GET | `/npmanagement/netUserInfo/updateNetUserInfo` | Generate and store a new portal password |
| GET | `/npmanagement/netUserInfo/queryNetUserInfoById/{cernum}` | Load a portal user by identity number |
| GET | `/npmanagement/netUserInfo/queryUserPhoto/{cernum}/{cardno}` | Proxy card photo lookup |
| POST | `/npmanagement/netUserInfo/updateNetUserInfoPwd/{cernum}/{net_password}` | Reset a portal password |
| GET | `/npmanagement/anp/getPolicyInfo` | List policy documents with `title`, `pageno`, and `pagesize` |
| POST | `/npmanagement/anp/savePolicy/{userId}/{appId}` | Insert or update a policy document |
| GET | `/npmanagement/anp/uploadfile` | Upload an allowed policy attachment |
| GET | `/npmanagement/anp/getPolicyById/{id}/{userId}/{appId}` | Load one policy document |
| DELETE | `/npmanagement/anp/delPolicyById/{id}` | Delete one policy document |
| GET | `/npmanagement/web/getWebInfo` | Aggregate web visits by year, month, or day |

All successful responses use the following envelope:

```json
{
  "statusCode": "200",
  "message": "success",
  "data": {}
}
```

Errors use `statusCode: "0"`; unauthorized requests return HTTP 401. The policy and user
list endpoints use `pageno` starting at 1 and default `pagesize=5`.

## Build and test

From the repository root:

```bash
mvn -B -ntp -pl NetPortalManagement -am verify
```

The module is part of the default reactor, so a complete build is simply:

```bash
mvn -B -ntp verify
```

The build verifies compilation and packaging without requiring Oracle connectivity. The
repository does not yet include an Oracle-compatible integration fixture; add one before
changing SQL semantics or schema-specific behavior.

## Migration notes

- `NetPortalRepository` uses Oracle `ROWNUM` pagination and `T_POLICY_SEQ.NEXTVAL`, matching
the historical schema assumptions.
- Policy content is written as UTF-8 bytes to the existing `context` column and read back
through the JDBC `Blob` interface.
- File uploads are stored below `upload.dir`; the endpoint rejects unknown extensions,
path traversal components, and files over 1 MB.
- `MD5Generator` remains for compatibility with existing portal password hashes. It is not
suitable for new password storage; a future migration should support bcrypt/Argon2 with a
read-old/write-new compatibility path.
- `LogUtil` still calls the platform proxy and returns `false` when that integration is
unavailable.