# CSSP session protection and client migration

This documents the current `[Unreleased]` contract, not a production deployment or
an independent end-to-end demonstration.

## Scope

`IFaceSuportService` maps `BusinessFilter` to `/iface/cssp/*`. The exact
`POST /iface/cssp/login` credential-login route is the only public CSSP request.
Aliases (trailing slash, path parameters, suffixes, encoded spellings) do not bypass
the filter. Other HTTP methods do not bypass it either.

`register` and `registerNew` register social-security cards, not new accounts.
They require an interface session, as do photo writes/reads, audit and application
queries. Unknown future CSSP routes are protected by default.

This filter validates an existing interface session and its server-assigned channel.
It is **not** complete per-person or per-organization authorization. The old credential
login's shared Redis `username` key also remains a separate isolation risk. See
[SECURITY.md](../../SECURITY.md#8-cssp-interface-sessions-and-remaining-authorization-limitations).

## Two different login contracts

1. `/iface/user/checkLogin` obtains the existing **interface** `tokenId` for a channel;
   read [the SDK guide](../CLIENT_SDK.md) and the actual controller for its request contract.
2. `/iface/cssp/login` checks the legacy CSSP business credentials. It returns the
   business envelope, **not** an interface token. A successful response alone does not
   authorize subsequent protected CSSP requests.
3. Protected calls must carry `X-Token-Id`. A legacy `tokenId` parameter remains supported,
   but a nonblank header takes precedence. Prefer the header so tokens are not copied
   into URL logs. Use TLS and never publish a real token or personal request payload.

## Failure contract

| Condition | HTTP | Action |
| --- | --- | --- |
| Missing/expired session, wrong IP, missing server-assigned channel | 401 | Obtain a valid interface session; do not blindly replay a write |
| Unknown channel, rejected device, denied ThirdSystem method permission | 403 | Correct the authorization; do not retry as another channel |
| Session lookup/refresh fails | 503 | Restore the authentication backend; no anonymous fallback |
| Session valid but a photo name is unsafe | 200 + JSON `statusCode: "201"` for the illustrated upload | Correct the body; this legacy validation code is not HTTP authorization |

The filter checks identity before consuming the JSON body or invoking a controller.
A session does not mean the application payload is valid; clients must check both
HTTP status and the response envelope. ThirdSystem permission checks use the actual
URI operation, not a caller-provided alternative `method` parameter.

## Safe request examples

Run these only against an isolated, authorized test environment. The host, token and
identity below are placeholders/synthetic. These examples do not start or configure
a server, Redis, Oracle or the upstream gateways.

An unauthenticated upload fails before parsing the deliberately invalid body:

```bash
curl -i -X POST http://127.0.0.1:8080/sisp/iface/cssp/uploadPicInfoByTSB \
  -H 'Content-Type: application/json' --data 'not-json'
# Expected: HTTP 401; controller and file/database writes are not reached.
```

To check both session protection and existing path validation without a successful
file write, use a valid interface session for this test environment and an unsafe name:

```bash
curl -i -X POST http://127.0.0.1:8080/sisp/iface/cssp/uploadPicInfoByTSB \
  -H 'Content-Type: application/json' -H "X-Token-Id: $SISP_INTERFACE_SESSION" \
  --data '{"picName":"../escaped","base64String":"AQID","cernum":"110101199001010015","name":"Example"}'
# Expected for an authorized channel/device: HTTP 200, JSON statusCode "201".
# No successful file write or business database call should be performed.
```

Do not mistake the 401/path-rejection examples for proof of successful business
execution. They demonstrate negative/security behavior only.

## Deployment checklist

- Inventory all CSSP callers, including desktop audit tools and card terminals.
- Obtain and attach the existing interface token; verify channel/device/ThirdSystem
  method permissions and the configured trusted proxy/IP boundary.
- Use the canonical login URL and POST. Coordinate terminal changes before rollout.
- Test 401, 403 and backend 503 responses in an authorized staging environment.
- Keep private network controls. Do not remove the filter or add a blanket bypass for
  old terminals that do not yet send a token.
- Separately verify organization/person ownership, credential-login isolation and
  business audit identity. These are not closed by the current session patch.

## Regression tests

`CsspAuthorizationTest` uses the real filter with in-memory session/device seams and
Spring MockMvc; no Redis or Oracle is used. It also verifies the deployed `web.xml`
mapping, so removing the mapping fails a test.

From the repository root:

```bash
mvn -B -ntp -pl IFaceSuportService -Dtest=CsspAuthorizationTest test
```
