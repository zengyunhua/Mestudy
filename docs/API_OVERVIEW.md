# API overview

There are **358 `@RequestMapping` declarations** across the workspace. This document
describes the Spring MVC API contract (envelope, status codes, paging) and indexes
the controller endpoints. Bee-dispatched `NetPortalManagement` does not use the same
`@RequestMapping`/`Result` contract. This is a map, not a generated specification;
consult controller code and Javadoc for individual request and response bodies.

- [1. Conventions](#1-conventions)
- [2. Response envelope](#2-response-envelope)
- [3. Status codes](#3-status-codes)
- [4. Paging](#4-paging)
- [5. Authentication](#5-authentication)
- [6. Endpoint index](#6-endpoint-index)
- [7. Generating an OpenAPI description](#7-generating-an-openapi-description)

---

## 1. Conventions

| Aspect | Convention |
| --- | --- |
| Base path | `/{platform_context_path}` — `/sisp` by default |
| Content type | `application/json;charset=UTF-8` for `@ResponseBody` endpoints |
| Method | Mostly `POST` with a JSON body; some `GET` for idempotent queries |
| Parameter style | `@RequestBody` for JSON payloads, `@RequestParam` for scalars, `@PathVariable` for ids |
| Naming | Lower camel case, verb-first: `/addJobDirection`, `/cancelOrder_zx`, `/deleteRole/{id}` |
| Suffixes | `_zx` / `_ht` denote the vendor gateway the call is routed to (Zhongxin HIS, Hangtian HIS). They are part of the path — do not strip them. |

## 2. Response envelope

In the Spring MVC modules, JSON controller responses commonly use `Result`
(or `ResultVerify`, which extends it with paging fields):

```json
{
  "statusCode": "200",
  "message": "success",
  "data": { }
}
```

| Field | Type | Notes |
| --- | --- | --- |
| `statusCode` | `String` | `"200"` success, `"0"` failure. **A string, not a number** — compare with `.equals()`. |
| `message` | `String` | Human-readable message, usually Chinese. Safe to log, not to parse. |
| `data` | `Object` | Payload. A VO, a list, or `null` on failure. |

Built by the helpers on `BaseController`:

```java
return result("200", "查询成功", vo);      // Result
return ok(total, "查询成功", list);        // ResultVerify with count
```

## 3. Status codes

The envelope carries only two transport-level codes. Business outcomes are expressed in
`data` or in `message`:

| `statusCode` | Meaning | HTTP status |
| --- | --- | --- |
| `"200"` | Handled successfully | 200 |
| `"0"` | Business failure or handled error | 200 |

HTTP status is not used to signal business failure. A 200 with `statusCode: "0"` is a
normal, expected response — client code must check the envelope, not the HTTP status.

`NetworkOfficeService` additionally defines a `CommonExceptionHandler` that converts
unhandled exceptions into a `Result` with a failure code, so a 500 should be rare there.

## 4. Paging

`Page<T>` is the standard page payload:

```json
{
  "pageNo": 1,
  "pageSize": 20,
  "count": 137,
  "data": [ ]
}
```

| Field | Meaning |
| --- | --- |
| `pageNo` | 1-based page index. |
| `pageSize` | Rows per page. `-1` means "do not paginate". |
| `count` | Total row count. `-1` means "count not requested". |
| `data` | The rows. |

`ResultVerify` flattens these onto the envelope, so paged responses look like:

```json
{ "statusCode": "200", "message": "success", "data": [ ], "total": 137 }
```

Check which of the two shapes a given endpoint uses before writing a client — the
workspace is not consistent.

## 5. Authentication

1. The client calls `POST /iface/user/checkLogin` with a channel code.
2. The service returns a `tokenId`.
3. The SDK sends `X-Token-Id` in the request header; legacy callers may still use `?tokenId=...`.
4. The token expires after `redis_iface_expx` **minutes** of inactivity; the SDK renews it on a timer (`tokenTimeOut` minutes, minimum 60).

The `IFaceHttpsClient` SDK implements all four steps. See [CLIENT_SDK.md](CLIENT_SDK.md).

## 6. Endpoint index

Counts are `@RequestMapping` declarations per class, including the class-level mapping.

### IFaceSuportService — 251

| Base path | Controller | Endpoints | Domain |
| --- | --- | :---: | --- |
| `/iface/rest` | `SecQueryController` | 55 | Social-insurance queries (pension, medical, unemployment, maternity) |
| `/iface/job` | `JobQueryController` | 33 | Employment: job seeker profiles, job directions, vacancies |
| `/iface/his/zhongxinhis` | `ZhongxinHisController` | 24 | Hospital registration via Zhongxin HIS |
| `/iface/cssp` | `CsspBusController` | 19 | Card replacement hub business |
| `/iface/face` | `FaceController` | 19 | Face enrolment and comparison |
| `/iface/his/hangtianhis` | `HangTianHisController` | 18 | Hospital registration via Hangtian HIS |
| `/iface/bank` | `BankController` | 13 | Bank channel: card binding, payment |
| `/iface/cardInfo` | `CardInfoController` | 11 | Social-security card info |
| `/iface/emp` | `EmpQueryController` | 10 | Employment queries |
| `/iface/his/sce` | `ZhongxinHisSceRegController` | 9 | Zhongxin HIS registration (SCE variant) |
| `/iface/faceVerify` | `FaceVerifyController` | 7 | Face verification / liveness |
| `/iface/fee` | `FeeController` | 7 | Fee payment and reconciliation |
| `/iface/policy` | `PolicyController` | 6 | Policy documents |
| `/iface/user` | `UserController` | 6 | Login, token, user session |
| `/iface/archive` | `ArchiveController` | 2 | Archive lookup |
| `/iface/code` | `CodeController` | 2 | Captcha / verification code |
| `/iface/sms` | `SmsController` | 2 | SMS dispatch |
| `/iface/yldy` | `YldyController` | 2 | Pension benefit calculation |
| `/iface/zhongjian` | `ZhongJianController` | 2 | Intermediate/staging schema access |
| `/ifaceInner/cardInfo` | `CardInfoInnerController` | 2 | Internal card info (service-to-service) |
| `/iface/wechat` | `WechatController` | 2 | WeChat channel |

### NetworkOfficeService — 72

| Base path | Controller | Endpoints | Domain |
| --- | --- | :---: | --- |
| `/userNet/drafts` | `DraftsController` | 42 | Application drafts: create, edit, attach, submit, delete |
| `/net/query` | `QueryController` | 10 | Portal query endpoints |
| `/userNet/audit` | `AuditingController` | 8 | Back-office audit of submitted applications |
| `/adapter/notice` | `GovernmentController` | 6 | Government notice adapter |
| `/net/business` | `BusinessHandleController` | 3 | Business handling |
| `/adapter/sign` | `SignController` | 3 | Electronic signature (PDF stamping) |

### InterfaceFunction — 27

| Base path | Controller | Endpoints | Domain |
| --- | --- | :---: | --- |
| `/interfacefunction/user` | `UserController` | 11 | Platform user administration |
| `/interfacefunction/role` | `RoleController` | 7 | Role administration |
| `/interfacefunction/app` | `FuntionController` | 6 | Function / permission tree |
| `/ans/common` | `CommonController` | 3 | Shared lookups |

### IFaceSuperService_hn — 8

| Base path | Controller | Endpoints | Domain |
| --- | --- | :---: | --- |
| `/iface/card` | `CardInfoController` | 6 | Card services (Henan gateway) |
| `/iface/user` | `UserController` | 2 | Login / token (Henan gateway) |

### NetPortalManagement — 11

This module exposes standard Jersey resources under `/npmanagement`. The resources are
protected by `ApiTokenFilter` and use the same `statusCode`/`message`/`data` envelope as
the other public APIs. See the [module guide](../NetPortalManagement/README.md) for the
endpoint table, token configuration, Oracle assumptions and upload behavior.

| Base path | Resource | Endpoints | Domain |
| --- | --- | :---: | --- |
| `/npmanagement/netUserInfo` | `NetUserInfoApplication` | 5 | Portal users and passwords |
| `/npmanagement/anp` | `PolicyApplication` | 5 | Policy documents and uploads |
| `/npmanagement/web` | `WebConnectionApplication` | 1 | Web visit statistics |

## 7. Generating an OpenAPI description

There is no OpenAPI spec yet. Because every controller is annotation-driven, the
pragmatic path is:

1. Add `springfox` 2.x (Spring 4 compatible) or hand-write a spec from the table above.
2. Document request/response bodies from the VO classes in `common/vo/`.
3. Publish under `docs/openapi/`.

This is an open item on the [roadmap](ROADMAP.md). Contributions that document a single
controller in `docs/api/` are welcome and do not require the whole spec.
