
package com.tecsun.sisp.iface.server.controller.security;

import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.*;
import com.tecsun.sisp.iface.common.vo.card.CardAllInfo;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.NetUserServiceImpl;
import com.tecsun.sisp.iface.server.outerface.card.CardInfoBus;
import com.tecsun.sisp.iface.server.util.CommUtil;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import com.tecsun.sisp.iface.server.util.JedisUtil;
import com.tecsun.sisp.iface.server.util.SmsSendUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录
 *
 */

@Controller
@RequestMapping("/iface/user")
public class UserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private NetUserServiceImpl netUserService;

/**
     * 登录验证
     *
     * @param bean
     * @param request
     * @return
     */

    private static boolean channelSecretMatches(String channel, String provided) {
        String expected = System.getenv("SISP_" + channel.toUpperCase(java.util.Locale.ROOT) + "_SECRET");
        if (expected == null || expected.length() < 32 || provided == null) {
            return false; // Fail closed until an independent, high-entropy secret is configured.
        }
        return java.security.MessageDigest.isEqual(
                expected.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                provided.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
    @RequestMapping(value = "/checkLogin", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result checkLogin(@RequestBody NetUserInfoVO bean, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        NetUserInfoVO vo = new NetUserInfoVO();
        String pwd = bean.getNetpassword();
        if(CommUtil.isEmptyStr(pwd)){
            pwd = bean.getNet_password();
        }

        String type = bean.getType();
        String userName = bean.getUserName();
        String name = bean.getName();
        String mobile=bean.getMobile();
        String cernum=bean.getUserName().toUpperCase();
        String result = Constants.RESULT_MESSAGE_ERROR;
        String message = "登录失败";
        String token = "";
        String ip = "";
       // System.out.println("checkLogin身份证号：+"+cernum);
        try {
            if (type.equals("NetPortal")) {
                ip = CommUtil.getIpAddr(request);
                Result result2 = this.getUserInfo(cernum,pwd,mobile,name);
                if (result2.getStatusCode().equals("200")) {
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    JedisUtil.setValue(token, ip);
                    JedisUtil.setValue(Constants.CHANNELKEY + token, "NetPortal");
                    vo = (NetUserInfoVO)result2.getData();
                    vo.setToken(token);
                    message = type + " 登陆成功!";
                    return result2;
                } else {
                    logger.error("网办登录：用户名或密码错误！");
                    return result2;
                }
            } else if (type.equals("SelfService")) {//由于无用户登录操作，需前台定义定时更新任务
                if ("SelfService".equals(userName) && channelSecretMatches("SelfService", pwd)) {
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    ip = CommUtil.getIpAddr(request);
                    JedisUtil.setValue(token, ip, Constants.SELFSERVICETIMEOUT);
                    JedisUtil.setValue(Constants.CHANNELKEY + token, "SelfService", Constants.SELFSERVICETIMEOUT);
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                    message = type + " 登陆成功!";
                    return result(result, message, token);
                } else {
                    logger.error("自助终端登录：用户名或密码错误！");
                }
            } else if (type.equals("TSB")) {
                if ("TSB".equals(userName) && channelSecretMatches("TSB", pwd)) {
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    ip = CommUtil.getIpAddr(request);
                    JedisUtil.setValue(token,ip);
                    JedisUtil.setValue(Constants.CHANNELKEY + token, "TSB");
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                    message = type + " 登陆成功!";
                    return result(result, message, token);
                } else {
                    logger.error("德生宝登录：用户名或密码错误！");
                }
            } else if (type.equals("WeChat")) {
                ip = CommUtil.getIpAddr(request);
                Result result2 = this.getUserInfo(cernum,pwd,mobile,name);
                if (result2.getStatusCode().equals("200")) {
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    JedisUtil.setValue(token, ip);
                    JedisUtil.setValue(Constants.CHANNELKEY + token, "WeChat");
                    vo = (NetUserInfoVO)result2.getData();
                    vo.setToken(token);
                    message = type + " 登陆成功!";
                    return result2;
                } else {
                    logger.error("微信登录：用户名或密码错误！");
                    return result2;
                }
            }
            else if(type.equals("ThirdSystem")){  //第三方公司签到
                ThirdUserBean userbean = userLogin(userName, pwd);
                if(userbean == null){
                    logger.error("第三方公司登录：用户名或密码错误！");
                    message =  " 登陆失败,用户名或密码错误！";
                }else{
                    token = Constants.TOKENNAME + CommUtil.getUUID();
                    ip = CommUtil.getIpAddr(request);
                    JedisUtil.setValue(token,ip);
                    JedisUtil.setValue(Constants.CHANNELKEY + token, "ThirdSystem");
                    //向缓存中添加用户id，以及区域
                    String userkey = Constants.USERKEY + token;
                    String userAreaid = Constants.USERAREAID + token;
                    JedisUtil.setValue(userkey,Long.toString(userbean.getId()));
                    JedisUtil.setValue(userAreaid,Long.toString(userbean.getAreaid()));
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                    message = type + " 登陆成功!";
                    return result(result, message, token);
                }
            }
        } catch (Exception e) {
            logger.error("登录失败，渠道：" + type, e);
        }
        logger.info(message);
        return result(result, message, vo);
    }

/**
     * 登录验证规则：
     * 使用身份证号码作为登录帐号；
     * 若首次登录网办，则初始密码为身份证号码后六位，否则使用已有密码登录
     * @param
     * @return
     */

       // System.out.println("getUserInfo身份证号：+"+cernum);
    public Result getUserInfo(String cernum,String pwd,String mobile,String name) {
        String result = Constants.RESULT_MESSAGE_ERROR;
        String message = "";
        if(StringUtils.isBlank(cernum)||StringUtils.isBlank(pwd)){
            message = "请输入账号、密码等信息";
            return result(result, message,null);
        }
        NetUserInfoVO netUserInfoVO=null;
        NetUserInfoVO vo=new NetUserInfoVO();
        NetUserInfoVO netUserInfoVO1=new NetUserInfoVO();
        vo.setCernum(cernum);
        vo.setNet_password(pwd);
        vo.setName(name);
        try{
            netUserInfoVO = netUserService.userLogin(vo);
            if(netUserInfoVO!=null){
                if(!StringUtils.isEmpty(mobile)){
                    SecQueryBean bean=new SecQueryBean();
                    bean.setId(cernum);
                    bean.setName(name);
                    bean.setNewPhoneNo(mobile);
                    netUserService.updateMobile(bean);
                }
                String password = netUserInfoVO.getCernum();
                String net_password = netUserInfoVO.getNetpassword();
                String isUpdate=netUserInfoVO.getIsUpdate();
                if(!CommUtil.isEmptyStr(isUpdate) && isUpdate.equals("1")){//若用户修改过密码
                    if(!CommUtil.isEmptyStr(net_password) && net_password.equals(pwd)){
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                        message = "登录成功";
                        return result(result, message, netUserInfoVO);
                    }
                    else{
                        result = Constants.RESULT_MESSAGE_ERROR;
                        message = "您的密码输入错误，请重新输入";
                        return result(result, message,null);
                    }
                }else{
                    if(!CommUtil.isEmptyStr(password)){  //截取身份证号码后六位并进行加密
                        password = password.substring(password.length()-6, password.length());
                        password = CommUtil.generateValue(password);
                    }
                    if(password.equals(pwd)){
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                        message = "登录成功";
                        return result(result, message, netUserInfoVO);
                    }
                    else{
                        result = Constants.RESULT_MESSAGE_ERROR;
                        message = "您的密码输入错误，请重新输入";
                        return result(result, message,null);
                    }
                }
            } else{//若该用户在本地数据库不存在，则去省厅查找该用户

                CardInfoBus bus = new CardInfoBus();
                String cardno="";
                CardAllInfo allInfo=bus.getCardAllInfo(cardno,cernum);
                if(StringUtils.isEmpty(allInfo.getSbkh())){//省厅也不存在该用户
                    result = Constants.RESULT_MESSAGE_ERROR;
                    message = "您的用户名输入错误，请重新输入";
                    logger.error("省厅不存在该用户");
                    return result(result, message);
                }else{//省厅存在该用户，将该用户加入本地数据库，进行同步
                    //省厅数据加入本地数据库
                   // System.out.println("通过getCardAllInfo获取卡数据不为空，allinfo："+allInfo.getSfzh());
                    netUserInfoVO1=new NetUserInfoVO();
                    long id;
                    try {
                        id = netUserService.getMaxRetime();
                        id=id+1;
                    }
                    catch(Exception e){
                        id=1;
                    }
                    String  net_password = cernum.substring(cernum.length()-6, cernum.length());
                    net_password = CommUtil.generateValue(net_password);
                    netUserInfoVO1.setRetime(id);
                    String sbkh="";
                    String username="";
                    String bank="";
                    String bankno="";
                    String address="";
                    String mailAddress="";
                    String dwmc="";
                    String dwbh="";
                    String birthday="";
                    String ethnic="";
                    String sex="";
                    String email="";
                    if(!StringUtils.isEmpty(allInfo.getSbkh())){
                        sbkh=allInfo.getSbkh();
                    }
                    if(!StringUtils.isEmpty(allInfo.getName())){
                        username=allInfo.getName();
                    }
                    if(!StringUtils.isEmpty(allInfo.getBank())){
                        bank=allInfo.getBank();
                    }
                    if(!StringUtils.isEmpty(allInfo.getBankno())){
                        bankno=allInfo.getBankno();
                    }
                    if(!StringUtils.isEmpty(allInfo.getMailaddr())){
                        mailAddress=allInfo.getMailaddr();
                    }
                    if(!StringUtils.isEmpty(allInfo.getRegaddr())){
                        address=allInfo.getRegaddr();
                    }
                    if(!StringUtils.isEmpty(allInfo.getDwmc())){
                        dwmc=allInfo.getDwmc();
                    }
                    if(!StringUtils.isEmpty(allInfo.getDwbh())){
                        dwbh=allInfo.getDwbh();
                    }
                    if(!StringUtils.isEmpty(allInfo.getBirthday())){
                        birthday=allInfo.getBirthday();
                    }
                    if(!StringUtils.isEmpty(allInfo.getEthnic())){
                        ethnic=allInfo.getEthnic();
                    }
                    if(!StringUtils.isEmpty(allInfo.getSex())){
                        sex=allInfo.getSex();
                    }
                    if(!StringUtils.isEmpty(allInfo.getEmail())){
                        email=allInfo.getEmail();
                    }
                    if(!StringUtils.isEmpty(mobile)){
                        netUserInfoVO1.setMobile(mobile);
                    }
                    else if(!StringUtils.isEmpty(allInfo.getTelno())){
                        netUserInfoVO1.setMobile(allInfo.getTelno());
                    }
                    else{
                        netUserInfoVO1.setMobile(allInfo.getPhoneno());
                    }
                    netUserInfoVO1.setCardno(sbkh);
                    netUserInfoVO1.setCernum(cernum);
                    netUserInfoVO1.setName(username);
                    netUserInfoVO1.setNet_password(net_password);
                    netUserInfoVO1.setBankid(bank);
                    netUserInfoVO1.setBankno(bankno);
                    netUserInfoVO1.setMobile_address(mailAddress);
                    netUserInfoVO1.setAddress(address);
                    netUserInfoVO1.setUnitname(dwmc);
                    netUserInfoVO1.setUnitno(dwbh);
                    netUserInfoVO1.setBirthday(birthday);
                    if(!StringUtils.isEmpty(ethnic)){
                        String nation=allInfo.getEthnic().substring(0,ethnic.length()-1);
                        netUserInfoVO1.setNation(Constants.NATION_Code.get(nation));
                    }if(!StringUtils.isEmpty(sex)){
                        if(sex.contains("1")||sex.contains("2"))
                        { netUserInfoVO1.setSex(sex);}
                        else if(sex.contains("男")||sex.contains("女")){
                        sex=Constants.SEX.get(sex);
                        if(!StringUtils.isEmpty(sex)) {
                            netUserInfoVO1.setSex(sex.replace("0", ""));
                        }}
                    }
                    netUserInfoVO1.setEmail(email);
                    netUserInfoVO1.setIsUpdate("0");
                    try {
                        netUserService.insertUser(netUserInfoVO1);
                    } catch (Exception e) {
                        logger.error("登录异常： " + e.getMessage());
                        result = Constants.RESULT_MESSAGE_ERROR;
                        message = "登录异常---将省厅用户数据同步到本地时出错";
                        return result(result, message, netUserInfoVO1);
                    }
                    if(pwd.equals(net_password)){//如果用户输入的是默认密码
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                        message = "该用户数据从省厅中提取加入本地成功";
                        logger.info("登录成功");
                        return result(result, message,netUserInfoVO1);
                    }else{//用户输入密码错误
                        result = Constants.RESULT_MESSAGE_ERROR;
                        message = "您为首次登录该系统，请输入正确的默认密码";
                        logger.info("登录失败");
                        return result(result, message,null);
                    }
                }
            }
        }catch (Exception e){
            logger.error("登录异常： ",e);
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "登录异常";
            return result(result, message, netUserInfoVO1);
        }
    }

/**
         * 修改密码
         *
         * @param bean
         * @return
         */

        @RequestMapping(value = "/updatePwd", method = RequestMethod.POST, produces = "application/json")
        @ResponseBody
        public Result updatePwd(@RequestBody NetUserInfoVO bean) {
            String result = Constants.RESULT_MESSAGE_ERROR;
            String message = "密码修改失败";
            try {
                if(StringUtils.isNotBlank(bean.getUserName()) && StringUtils.isNotBlank(bean.getNetpassword())){
                    String pwd=bean.getNetpassword();
                    bean.setNetpassword(pwd);
                    netUserService.updateNetpwd(bean);
                    message="密码修改成功";
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                }else{
                    message="用户名和新密码不能为空！";
                    result = Constants.RESULT_MESSAGE_ERROR;
                }

            } catch (Exception e) {
                e.printStackTrace();
                logger.error(" 密码修改失败！ /n 异常信息：" + e.getMessage());
            }
            return result(result, message, null);
        }

/**
         * 获取验证码   && 
         * @param bean
         * @return
         */

    @RequestMapping(value = "/getMobileCode",method = RequestMethod.POST)
    @ResponseBody
    public Result checkMobile(@RequestBody SecQueryBean bean){
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        SmsRequstBean smsRequstBean=null;
        Map<String,String> map=new HashMap<String,String>();
        if(StringUtils.isNotBlank(bean.getCernum()) &&StringUtils.isNotBlank(bean.getName())){
            try {
                NetUserInfoVO vo=netUserService.getPhoneNo(bean);
                if(vo!=null && StringUtils.isNotBlank(vo.getMobile())){
                    smsRequstBean=new SmsRequstBean();
                    String random=CommUtil.getRandomNum(4);
                    smsRequstBean.setRandom(random);
                    smsRequstBean.setName(bean.getName());
                    smsRequstBean.setNewPhoneNo(vo.getMobile());
                    smsRequstBean.setType("3");
                    smsRequstBean.setCernum(vo.getCernum());
                    smsRequstBean.setREGTIME("");
                    SmsSendUtil.SmsSend(smsRequstBean);
                    JedisUtil.setValue(vo.getMobile(), random, 2);
                    message="获取验证码成功";
                    map.put("random",random);
                    map.put("name",smsRequstBean.getName());
                    map.put("newPhoneNo",smsRequstBean.getNewPhoneNo());
                    map.put("cernum",smsRequstBean.getCernum());
                }
                else{
                    result=Constants.RESULT_MESSAGE_ERROR;
                    message="不存在该用户手机号码等信息";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            result=Constants.RESULT_MESSAGE_ERROR;
            message="获取手机验证码失败,姓名不能为空：";
        }
        return this.result(result, message, map);
    }

    
    
    /**
     * 获取验证码
     * @param bean
     * @return
     * @throws Exception 
     */

@RequestMapping(value = "/getMobileCodeByNew",method = RequestMethod.POST)
@ResponseBody
public Result getMobileCodeByNew(@RequestBody SecQueryBean bean) throws Exception{
    String result = Constants.RESULT_MESSAGE_SUCCESS;
    String message = "";
    SmsRequstBean smsRequstBean=null;
    Map<String,String> map=new HashMap<String,String>();
            if(bean!=null && StringUtils.isNotBlank(bean.getNewPhoneNo())){
                smsRequstBean=new SmsRequstBean();
                String random=CommUtil.getRandomNum(4);
                smsRequstBean.setRandom(random);
                smsRequstBean.setName(bean.getName());
                smsRequstBean.setNewPhoneNo(bean.getNewPhoneNo());
                smsRequstBean.setType("3");
                smsRequstBean.setCernum("");
                smsRequstBean.setREGTIME("");
                SmsSendUtil.SmsSend(smsRequstBean);
                JedisUtil.setValue(bean.getNewPhoneNo(), random, 2);
                message="获取验证码成功";
                map.put("random",random);
                map.put("name",smsRequstBean.getName());
                map.put("newPhoneNo",smsRequstBean.getNewPhoneNo());
                map.put("cernum",smsRequstBean.getCernum());
            }
            else{
                result=Constants.RESULT_MESSAGE_ERROR;
                message="不存在该用户手机号码等信息";
            }
    return this.result(result, message, map);
}
    
    
    private ThirdUserBean userLogin(String userName , String pwd) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("userName", userName);
        json.addProperty("userPwd", pwd);
        String url = DictionaryUtil.getHost(Constants.INTERFACE_USERLOGIN);
        String result = (String) getWebClient(url, json, String.class);
        Map map = JsonMapper.buildNormalMapper().fromJson(result, Map.class);
        Map data = (Map) map.get("result");
        if(data == null) return null;
        else {
            ThirdUserBean bean = new ThirdUserBean();
            bean.setId(Long.parseLong(data.get("id").toString()));
            bean.setAreaid(Long.parseLong(data.get("areaId").toString()));
            return bean;
        }
    }
    
    @RequestMapping(value = "/queryByCardPerson", method = RequestMethod.POST, produces = "application/json")
   	@ResponseBody
   	public Result queryByCardPerson(@RequestBody ApersonVO bean) {
    	
    	String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if(StringUtils.isBlank(bean.getCernum())){
			return result(Constants.RESULT_MESSAGE_PARAM, "参数不能为空!");
		}
		try {
			
			List<ApersonVO> queryByCardPerson = netUserService.queryByCardPerson(bean);
			if (queryByCardPerson .size()>0) {
				return result(statusCode, message, queryByCardPerson);
			} else {
				return result(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return result(statusCode, message);

    }		
   		
   

}

