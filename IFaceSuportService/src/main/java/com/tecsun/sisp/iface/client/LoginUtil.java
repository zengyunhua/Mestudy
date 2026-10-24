package com.tecsun.sisp.iface.client;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.Result;

import java.security.MessageDigest;

/**
 * Acquires the interface token that every subsequent SDK call appends as
 * {@code ?tokenId=...}.
 *
 * <p>{@link #checkLogin} posts to {@code /iface/user/checkLogin} with the channel code as
 * both username and password, where each value is the MD5 hex digest of the channel code.
 * The service replies with the standard {@code Result} envelope; on success
 * {@code data} is the token, returned here as a String.
 *
 * <p><strong>Failure is an exception, not a return value.</strong> A non-{@code "200"}
 * status throws {@code Exception("接口鉴权失败！渠道：" + type)}. A {@code null} or empty
 * response body also throws — {@code result} is dereferenced without a null check, so an
 * empty body surfaces as {@code NullPointerException} rather than a clear message.
 *
 * <p>Called on a timer by {@code IFaceHttpsClient.TokenGetter}; not intended to be called
 * by application code directly.
 */
public class LoginUtil {

    public static String checkLogin(int port,String url, String type) throws Exception {
        LoginVO vo = new LoginVO();
        vo.setUserName(generateValue(type));
        vo.setNet_password(generateValue(type));
        vo.setType(type);
        Result result = null;
        String msg = HttpClientUtil.getData(port,url, vo, true);
        if (msg != null && msg.length() > 0) {
            result = JsonMapper.buildNormalMapper().fromJson(msg, Result.class);
        }
        if (!result.getStatusCode().equals(Constants.RESULT_MESSAGE_SUCCESS)) {
            throw new Exception("接口鉴权失败！渠道：" + type);
        }
        return result.getData().toString();
    }

    private static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        final char[] hexCode = "0123456789abcdef".toCharArray();
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
}
