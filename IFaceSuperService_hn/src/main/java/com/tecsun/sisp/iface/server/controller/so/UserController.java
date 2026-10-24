package com.tecsun.sisp.iface.server.controller.so;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JedisUtil;
import com.tecsun.sisp.iface.common.vo.NetUserInfoVO;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.util.CommUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

/**
 * 登录
 *
 */
@Controller
@RequestMapping("/iface/user")
public class UserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

 
    /**
     * 登录验证
     *
     * @param bean
     * @param request
     * @return
     */
    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result checkLogin(@RequestBody SecQueryBean bean, @Context HttpServletRequest request, @Context HttpServletResponse response) {

    	 String pwd = bean.getNetpassword();

         String userName = bean.getUserName();
    	
    	
    	
        String type = bean.getType();
        String message = "登录失败";
        String token = "";
        String ip = "";
        Result result = new Result(Constants.RESULT_MESSAGE_ERROR, message, null);
        try {
            if (type.equals("SelfService")) {//由于无用户登录操作，需前台定义定时更新任务
                if (userName.equals(CommUtil.generateValue("SelfService")) && pwd.equals(CommUtil.generateValue("SelfService"))) {
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    ip = CommUtil.getIpAddr(request);
                    JedisUtil.setValue(token, ip, Constants.SELFSERVICETIMEOUT);
                    message = type + " 登陆成功!";
                    result.setStatusCode(Constants.RESULT_MESSAGE_SUCCESS);
                } else {
                    logger.info("token is :" + token + "============ip is: " + ip);
                    logger.error("自助终端登录：用户名或密码错误！");
                }
            } else if (type.equals("TSB")) {
                if (userName.equals(CommUtil.generateValue("TSB")) && pwd.equals(CommUtil.generateValue("TSB"))) {
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    ip = CommUtil.getIpAddr(request);
                    JedisUtil.setValue(token, ip);
                    message = type + " 登陆成功!";
                    result.setStatusCode(Constants.RESULT_MESSAGE_SUCCESS);
                } else {
                    logger.info("token is :" + token + "============ip is: " + ip);
                    logger.error("德生宝登录：用户名或密码错误！");
                }
            } else if (type.equals("WeChat")) {
                if (userName.equals(CommUtil.generateValue("WeChat")) && pwd.equals(CommUtil.generateValue("WeChat"))) {
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    ip = CommUtil.getIpAddr(request);
                    JedisUtil.setValue(token, ip);
                    message = type + " 登陆成功!";
                    result.setStatusCode(Constants.RESULT_MESSAGE_SUCCESS);
                } else {
                    logger.info("token is :" + token + "============ip is: " + ip);
                    logger.error("微信登录：用户名或密码错误！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("token is :" + token + "============ip is: " + ip);
            logger.error(type + " 登录失败！ /n 异常信息：" + e.getMessage());
        }
        result.setMessage(message);
        result.setData(token);
        logger.info(message);
        logger.info("token is :" + token + "============ip is: " + ip);
        return result;
    }
}
