package com.tecsun.sisp.iface.client;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.Result;


/** Obtains a channel token using the environment-provided channel secret. */
public class LoginUtil {

    public static String checkLogin(int port,String url, String type) throws Exception {
        LoginVO vo = new LoginVO();
        vo.setUserName(type);
        String secret = System.getenv("SISP_" + type.toUpperCase(java.util.Locale.ROOT) + "_SECRET");
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("Missing channel secret for " + type);
        }
        vo.setNet_password(secret);
        vo.setType(type);
        Result result = null;
        String msg = HttpClientUtil.getData(port,url, vo, true);
        if (msg != null && msg.length() > 0) {
            result = JsonMapper.buildNormalMapper().fromJson(msg, Result.class);
        }
        if (result == null || !Constants.RESULT_MESSAGE_SUCCESS.equals(result.getStatusCode()) || result.getData() == null) {
            throw new Exception("Interface authentication failed, channel: " + type);
        }
        return result.getData().toString();
    }

}
