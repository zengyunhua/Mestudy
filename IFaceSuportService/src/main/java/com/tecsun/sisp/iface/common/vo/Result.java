package com.tecsun.sisp.iface.common.vo;

/**
 * The uniform response envelope returned by every controller in the workspace.
 *
 * <p>Clients must check {@link #getStatusCode()} rather than the HTTP status: the
 * service answers {@code 200 OK} for both success and handled business failure, and
 * signals the outcome in the payload.
 *
 * <pre>{@code
 * {
 *   "statusCode": "200",
 *   "message": "success",
 *   "data": { }
 * }
 * }</pre>
 *
 * <p><strong>{@code statusCode} is a String.</strong> Compare with
 * {@code "200".equals(result.getStatusCode())}, never with {@code ==} or an int literal.
 * The two values in use are {@code "200"} (success) and {@code "0"} (failure).
 *
 * <p>{@code message} is human-readable and usually Chinese; it is safe to log and unsafe
 * to parse. {@code data} may be {@code null} on failure.
 *
 * <p>Controllers should not construct this directly — use the {@code result(...)} and
 * {@code ok(...)} helpers inherited from
 * {@code com.tecsun.sisp.iface.server.controller.BaseController}.
 *
 * @see com.tecsun.sisp.iface.common.vo.faceverify.ResultVerify
 */
public class Result {
    private String statusCode;//200 成功；0 失败
    private String message;
    private Object data;


    public Result() {
    }

    public Result(String statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
