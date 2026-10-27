package com.tecsun.sisp.iface.server.controller.so;

import com.tecsun.sisp.iface.common.util.Config;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JedisUtil;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;
import com.tecsun.sisp.iface.server.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Controller
@RequestMapping("/iface/user")
public class UserController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result checkLogin(@RequestBody SecQueryBean bean, HttpServletRequest request) {
        Result result = new Result(Constants.RESULT_MESSAGE_ERROR, "登录失败", null);
        if (bean == null || !validCredentials(bean.getType(), bean.getUserName(), bean.getNetpassword())) {
            logger.warn("Interface login rejected");
            return result;
        }
        try {
            String token = Constants.TOKENNAME + com.tecsun.sisp.iface.server.util.CommUtil.getUUID();
            int ttl = Constants.SELFSERVICE.equals(bean.getType())
                    ? Constants.SELFSERVICETIMEOUT : Integer.parseInt(Config.getInstance().get("redis_iface_expx"));
            // Bind to the actual peer. Untrusted X-Forwarded-For headers must not determine identity.
            JedisUtil.setValue(token, request.getRemoteAddr(), ttl);
            result.setStatusCode(Constants.RESULT_MESSAGE_SUCCESS);
            result.setMessage("登录成功");
            result.setData(token);
        } catch (Exception e) {
            logger.error("Interface login failed", e);
        }
        return result;
    }

    static boolean validCredentials(String type, String username, String password) {
        if (!Constants.SELFSERVICE.equals(type) && !Constants.TSB.equals(type)
                && !Constants.WECHAT.equals(type)) {
            return false;
        }
        return validCredentials(type, username, password, Config.getInstance()::get);
    }

    static boolean validCredentials(String type, String username, String password,
                                    java.util.function.Function<String, String> configuration) {
        if (!Constants.SELFSERVICE.equals(type) && !Constants.TSB.equals(type)
                && !Constants.WECHAT.equals(type)) {
            return false;
        }
        String expectedUser = configuration.apply("auth." + type + ".username");
        String expectedPassword = configuration.apply("auth." + type + ".password");
        return expectedUser != null && expectedPassword != null
                && !expectedUser.isEmpty() && !expectedPassword.isEmpty()
                && !"CHANGE_ME".equals(expectedUser) && !"CHANGE_ME".equals(expectedPassword)
                && username != null && password != null
                && MessageDigest.isEqual(expectedUser.getBytes(StandardCharsets.UTF_8), username.getBytes(StandardCharsets.UTF_8))
                && MessageDigest.isEqual(expectedPassword.getBytes(StandardCharsets.UTF_8), password.getBytes(StandardCharsets.UTF_8));
    }
}
