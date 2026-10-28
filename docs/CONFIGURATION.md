# Configuration reference

The four Spring MVC modules read `src/main/resources/config.properties` from
the classpath root. `NetPortalManagement` ships one too, but reads it through the Bee
framework rather than Spring, and keeps its datasource in `ds.xml`; see its section
below and its [module guide](../NetPortalManagement/README.md).

Two access paths exist:

- **Spring beans** resolve placeholders through
  `<context:property-placeholder location="classpath:config.properties"/>`.
  This is how datasources, the view resolver and other XML-defined beans get values.
  Every module sets `ignore-unresolvable="true"`, so an unknown key does **not** fail
  the context — the literal `${key}` survives into whatever consumes it.
- **Java code** goes through a per-module `Config` singleton
  (`Config.getInstance().get("key")`). This returns `null` for a missing key — it does
  **not** throw. A typo therefore fails silently at runtime, not at startup.

That asymmetry is the single most common source of "it works in module A but not B"
confusion, and the two failure shapes differ: `${key}` in a Spring-injected value,
`null` in a `Config` lookup. When a value looks empty, grep the key name before
suspecting the property file.

A sanitised template ships as `config.properties.example` in every module. Copy it to
`config.properties` and edit. The real file is git-ignored and must never be committed.

---

## Web and context

| Key | Default | Meaning |
| --- | --- | --- |
| `platform_context_path` | `/sisp` | Context path the application is deployed under. Used to build absolute internal URLs. Must match the container's deployment name. |
| `web.view.prefix` | `/WEB-INF/views/` | View resolver prefix for JSP. |
| `web.view.suffix` | `.jsp` | View resolver suffix. |

## Internal proxy

Used to build URLs for calls between platform modules (`DictionaryUtil.getHost`).

| Key | Default | Meaning |
| --- | --- | --- |
| `inner_proxy_ip` | `127.0.0.1` | Host of the internal proxy / load balancer. |
| `inner_proxy_port` | `8080` | Port of the internal proxy. |

> `NetworkOfficeService/config.properties` contains `iinner_proxy_ip` (double *i*). No
> code in that module reads either key, so it is currently dead — but if you add an
> internal call there, use the correct spelling `inner_proxy_ip`.

## Redis

| Key | Default | Meaning |
| --- | --- | --- |
| `redis_core_database` | `127.0.0.1_6379` | Redis endpoint in `host_port` form. Parsed by splitting on `_`. |
| `redis_iface_expx` | `120` | Cache / token TTL in **minutes**. `JedisUtil` calls `setex(key, 60 * redis_iface_expx, value)`. Keep it strictly greater than the SDK refresh interval (minimum 60 minutes). |

> The key name suggests a unit that it does not use. It is minutes, not seconds. Also
> note `JedisUtil.setValue` does `Integer.parseInt(Config.get("redis_iface_expx"))` with
> no null check — omitting the key throws `NumberFormatException` on the first cache
> write, not at startup.

Redis is initialised in `StartupListener.contextInitialized()` via `DataBase.init()`. If
Redis is unreachable the application fails to deploy.

## JDBC pool

Shared by every c3p0 datasource in the module.

| Key | Default | Meaning |
| --- | --- | --- |
| `jdbc.type` | `oracle` | Database dialect hint used by some helpers. |
| `jdbc.maxIdleTime` | `3000` | Seconds a connection may sit idle before being retired. |
| `jdbc.minPoolSize` | `10` | Minimum pooled connections. |
| `jdbc.maxPoolSize` | `300` | Maximum pooled connections. |
| `jdbc.initialPoolSize` | `10` | Connections opened at startup. |

> `maxPoolSize=300` is high. Each datasource opens its own pool, and
> `IFaceSuportService` declares eight datasources — a worst case of 2400 connections
> against Oracle. Tune down for anything other than a large production deployment.

> **`NetworkOfficeService` ignores these four keys.** Its datasources are Spring
> `DriverManagerDataSource` instances, not c3p0 pools — a new physical connection is
> opened per request. See [ARCHITECTURE.md](ARCHITECTURE.md#datasource-inventory).

## Datasources

Every datasource needs four keys: `driverClassName`, `url`, `username`, `password`.
The prefix maps to a Spring bean in `applicationContext.xml`.

| Prefix | Bean | Schema / purpose | Used by |
| --- | --- | --- | --- |
| `jdbc.sisp` | `dataSource` / `sispDataSource` / `netportalDataSource` | Core platform schema | `IFaceSuportService`, `NetworkOfficeService`, `IFaceSuperService_hn` |
| `jdbc.sisp_public` | `sispPublicDataSource` / `sisppublicDataSource` | Public query schema | `IFaceSuportService`, `NetworkOfficeService` |
| `jdbc.dongruan` | `dataSource` | Neusoft (东软) five-insurance schema | `IFaceSuportService` |
| `jdbc.ncCollect` | `drDataSource` | Provincial collection schema | `IFaceSuperService_hn` |
| `jdbc.zhongjian` | `zhongjianDataSource` | Intermediate/staging schema | `IFaceSuportService` |
| `jdbc.cssp` | `csspDataSource` | Card replacement hub | `IFaceSuportService` |
| `jdbc.face` | `faceDataSource` | Face-verification base data | `IFaceSuportService` |
| `jdbc.drface` | `drDataSource` | Neusoft face-recognition schema | `IFaceSuportService`, `NetworkOfficeService` |
| `jdbc.dryldy` | `dryldyDataSource` | Pension benefit calculation | `IFaceSuportService` |
| `jdbc.sisp_yth` | `sispYthdataSource` | Integrated government-service schema | `NetworkOfficeService` |
| `jdbc.sisp_public_yth` | `sispPublicYthDataSource` | Integrated government-service public schema | `NetworkOfficeService` |
| `jdbc.*` (unprefixed) | `dataSource` | Single schema | `InterfaceFunction` |

Oracle URL forms in use — both are valid, do not "normalise" them blindly:

```
jdbc:oracle:thin:@host:1521:orcl        # SID form
jdbc:oracle:thin:@host:1521/orcl        # service-name form
```

## Face verification

| Key | Meaning |
| --- | --- |
| `biometrics.endpoint` | WSDL URL of the provincial biometric service. |
| `biometrics.namespace` | SOAP namespace of that service. |
| `biometrics.user` | Interface account. |
| `biometrics.pwd` | Interface password. |
| `biometrics.typeid` | Operation type identifier. |
| `biometrics.key` | Shared secret used to sign requests. |
| `tecsun_verify_ip` / `tecsun_verify_port` | Internal face-verification service host/port. |
| `tecsun_verify_projectName` | Context path of that service (default `/Verify`). |
| `face_area` | Region code for face data partitioning (e.g. `xg`). |

## Electronic signature (PDF stamping)

Used by `NetworkOfficeService` when a submitted application must be stamped.

| Key | Meaning |
| --- | --- |
| `signpdf.data.url` | Signature service endpoint. |
| `signpdf.font.path` | TTF used to render text onto the PDF. Must exist on the host — a missing font fails at signing time, not at startup. |
| `signpdf.save.path` | Directory where signed PDFs are written. |
| `signpdf.template.path` | Directory holding the stamp templates. |
| `signpdf.download.url` | Public path prefix for downloading a signed document. |

## External gateways

| Key | Meaning |
| --- | --- |
| `CARDBUESS_URL` | Provincial card-management SOAP endpoint. |
| `public_job_url` | Job-board service base URL. |
| `XIAOGANWEATHER` | Weather XML feed. |
| `PERSONFILENUMBER` | Electronic-licence API: list licences by person. |
| `XGFILENUMBER` | Electronic-licence API: image-info list. |
| `XGIMAGEFILEID` | Electronic-licence API: fetch a file stream. |
| `cityno` | Region code used when calling the provincial interface. |
| `user` / `password` | Provincial interface credentials. |
| `authentication` | Directory for authentication image archives. |

## File storage

| Key | Meaning |
| --- | --- |
| `SCFILEPATH` | Directory for uploaded scan files. |
| `MATTERSCFILEPATH` | Directory for uploaded application-matter files. |
| `cardmaxsize` / `carboxcount` | Card-box capacity limits (80 / 48). |

## NetPortalManagement

This module does not use `config.properties` for persistence. It uses the internal *Bee*
framework, configured by:

- `bee_application.xml` — framework bootstrap and package scanning
- `ds.xml` — datasource definition

Both are framework-specific; the connection settings live in `ds.xml`.

`config.properties` is still read here, but only for non-datasource keys —
`platform_context_path`, `inner_proxy_ip` and `inner_proxy_port`, through
`Config.getInstance().get(...)` in `LogUtil` and `NetUserInfoApplication`. Because that
is the `null`-returning path, a missing proxy key produces a URL containing the literal
`null` rather than a startup failure. There is no `<context:property-placeholder>` in
this module, so no `${...}` substitution happens at all.

## Conventions for new keys

1. Group with a comment header, matching the existing style.
2. Prefix by concern: `jdbc.`, `redis_`, `signpdf.`, `biometrics.`.
3. Add the key to `config.properties.example` in the same commit.
4. Document it in this table.
5. Never put a real credential in the example file — use `CHANGE_ME`.

## Interface authentication rollout

- Configure independent high-entropy values (at least 32 characters) for `SISP_SELFSERVICE_SECRET` and `SISP_TSB_SECRET` in the **IFaceSuportService process and each corresponding SDK process**. Do not put these values in Git or Java source. An unset/short secret fails login closed; the old MD5-of-channel login is intentionally no longer accepted.
- Coordinate deployment of the server and SDK; replace older SDK clients with a version that sends the secret at login. Existing tokens lack a server-side channel marker and must log in again after deployment. Do not expose the service publicly until the clients have been migrated.
- By default the server uses the immediate peer `getRemoteAddr()` for the token/IP binding. If behind a trusted reverse proxy, set `SISP_TRUSTED_PROXIES` to a comma-separated list of **exact IP addresses** of immediate proxy peers and ensure those proxies overwrite `X-Forwarded-For`. Never trust a client-supplied forwarded header. Keep the list unset if the service is directly exposed.
- For ThirdSystem, permission is checked server-side against the actual endpoint method; absent/invalid permission rejects the request. The filter covers `/iface/rest/*`, `/iface/cardInfo/*`, `/iface/bank/*`, `/iface/job/*`, and `/iface/his/hangtianhis/*`. Validate real channel use cases in staging before enabling traffic.
- Auth failures now use HTTP 401/403 (upstream unavailable: 503); SDK calls fail on non-2xx HTTP and paged business errors throw. Legacy query-string tokens remain accepted temporarily but may leak in proxy access logs; move callers to `X-Token-Id`.
