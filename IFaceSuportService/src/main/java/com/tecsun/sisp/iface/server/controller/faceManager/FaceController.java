package com.tecsun.sisp.iface.server.controller.faceManager;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import sun.misc.BASE64Decoder;

import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.client.WebClient;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.Page;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.card.CardAllInfo;
import com.tecsun.sisp.iface.common.vo.card.TsgrcbxxVO;
import com.tecsun.sisp.iface.common.vo.card.TsjbxxVO;
import com.tecsun.sisp.iface.common.vo.faceverify.BusinessInfoPO;
import com.tecsun.sisp.iface.common.vo.faceverify.DRPersonPO;
import com.tecsun.sisp.iface.common.vo.faceverify.FafangInfo;
import com.tecsun.sisp.iface.common.vo.faceverify.HistoryMsgBean;
import com.tecsun.sisp.iface.common.vo.faceverify.HistoryResult;
import com.tecsun.sisp.iface.common.vo.faceverify.IC09PO;
import com.tecsun.sisp.iface.common.vo.faceverify.IsCheckBean;
import com.tecsun.sisp.iface.common.vo.faceverify.KeyValueVO;
import com.tecsun.sisp.iface.common.vo.faceverify.PersonLoginBean;
import com.tecsun.sisp.iface.common.vo.faceverify.RegistBean;
import com.tecsun.sisp.iface.common.vo.faceverify.ResultVerify;
import com.tecsun.sisp.iface.common.vo.faceverify.XGPersonMsg;
import com.tecsun.sisp.iface.common.vo.faceverify.XGPersonPO;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.faceVerify.impl.PersonInfoServiceImpl;
import com.tecsun.sisp.iface.server.util.CommUtil;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import com.tecsun.sisp.iface.server.util.Dom4JUtil;
import com.tecsun.sisp.iface.server.util.FaceVerifyUtil;
import com.tecsun.sisp.iface.server.util.JsonHelp;

/**
 * 孝感人脸验证接口管理
 *
 */
@Controller
@RequestMapping(value = "/iface/face")
public class FaceController extends BaseController{

	public static final Logger logger = Logger.getLogger(FaceController.class);
	
	@Autowired
    public  PersonInfoServiceImpl personInfoService;
	/**
	 * APP-- 生存验证登陆
	 * @return
	 */
	@RequestMapping(value = "/userLogin" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
	public Result login(@RequestBody PersonLoginBean bean){
		try{
			String token = FaceVerifyUtil.createToken();
			int loginType = bean.getLoginType();
			String idCard = bean.getIdCard() == null?"":bean.getIdCard();
			String name = bean.getName() == null?"":bean.getName();
			if(idCard.isEmpty() || name.isEmpty()) return result("300", "验证登陆信息不能为空");
			Map<String,String> map = new HashMap<String, String>();
	        map.put("idCard",idCard);
	        map.put("name",name);
	        map.put("isCheck", Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_05"));
	        //1.以姓名和身份证号，查找东软参保人数据库,获取人员编号和企业编号
	        List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
	        if(list_dr.isEmpty()) return result("302","该参保人不是离退休或遗嘱人员");
	        String aac001 = list_dr.get(0).getGRBH();
	        String aab001 = list_dr.get(0).getDWBH();
	        //2.获取人脸验证平台中参保人的图片
	        String photoURL = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("getPhoto"));
	        String photoStr = FaceVerifyUtil.getPhoto(idCard, photoURL);
			if(loginType == Constants.TECSUN_VERIFY_LOGIN_TYPE.get("loginType_1") || //使用身份证+姓名+密码
					loginType == Constants.TECSUN_VERIFY_LOGIN_TYPE.get("loginType_3")){ //使用身份证+姓名
				if(loginType == Constants.TECSUN_VERIFY_LOGIN_TYPE.get("loginType_1")){
					String password = bean.getPassword() == null?"":bean.getPassword();
			        map.put("password",CommUtil.generateValue(password));
				}
		        //3.验证登陆
		        List<XGPersonPO>  list = personInfoService.getXGPersonInfo(map);
		        if(list.isEmpty()) return result("303", "用户名登陆失败，请核对登陆信息或注册不成功！");
		        map.put("token", token);
		        //4.更新token至人员表
		        if (!personInfoService.updateToken(map)) return this.result("301","token 更新不成功！");
		        //5.向客户端返回信息
		        XGPersonMsg personMsg = new XGPersonMsg();
		        personMsg.setPhoto(photoStr);
		        personMsg.setIdCard(idCard);
		        personMsg.setSex(list.get(0).getSex());
		        personMsg.setName(list.get(0).getPerson_name());
		        personMsg.setSys_token(token);
		        personMsg.setAddr(list.get(0).getAddress());
		        personMsg.setCompany(list.get(0).getCompany());
		        personMsg.setAac001(aac001);
		        personMsg.setAab001(aab001);
		        return ok("登录成功", personMsg);
			}else if(loginType == Constants.TECSUN_VERIFY_LOGIN_TYPE.get("loginType_2")){//使用身份证+姓名+照片
				//3.登陆验证
				String photo = bean.getPhoto() == null?"":bean.getPhoto();
				if(photo.isEmpty()) return result("303", "用户名登陆失败，请核对登陆信息或注册不成功！");
				List<XGPersonPO>  list = personInfoService.getXGPersonInfo(map);
		        if(list.isEmpty()) return result("303", "用户名登陆失败，请核对登陆信息！");
		        map.put("token", token);
		        //4.更新token至人员表
		        if (!personInfoService.updateToken(map)) return this.result("301","token 更新不成功！");
		        //5.比对照片
		        String cpURL = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("comparePhoto"));
		        if(!FaceVerifyUtil.comparePhoto(cpURL, photo, photoStr)) return result("999","照片比对机器审核失败");
		        //6.向客户端返回信息
		        XGPersonMsg personMsg = new XGPersonMsg();
		        personMsg.setPhoto(photoStr);
		        personMsg.setIdCard(idCard);
		        personMsg.setSex(list.get(0).getSex());
		        personMsg.setName(list.get(0).getPerson_name());
		        personMsg.setSys_token(token);
		        personMsg.setAddr(list.get(0).getAddress());
		        personMsg.setCompany(list.get(0).getCompany());
		        personMsg.setAac001(aac001);
		        personMsg.setAab001(aab001);
		        return ok("登录成功", personMsg);
			}else{
				logger.error("--生存验证登陆类别出错--");
				return result("304", "生存验证登陆类别出错");
			}
		}catch(Exception e){
			logger.error("--登陆失败--"+e);
			return error("登陆失败", null);
		}
	}

	/**
	 * APP注册--第一步
	 * 1.传一张照片进行比对，保存照片至认证平台
	 * 2.传三张照片进行验证
	 * @return
	 */
	@RequestMapping(value = "/registOne" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
	public Result registOne(@RequestBody RegistBean bean){
		try{
			long registtype = bean.getRegisttype();
			if(registtype == 0) return result("303","注册类型不符");
			String idCard = bean.getIdCard() == null?"":bean.getIdCard();
			String name = bean.getName() == null?"":bean.getName();
			String photo = bean.getPhoto();
			String photo1 = bean.getBase64String1();
	        String photo2 = bean.getBase64String2();
	        String photo3 = bean.getBase64String3();
			if(idCard.isEmpty() || name.isEmpty()) return result("301", "注册信息不能为空");
			Map<String,String> map = new HashMap<String, String>();
	        map.put("name",name);
	        map.put("idCard",idCard);
	        //1.向孝感数据库获取信息
	        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
	        if(!list_xg.isEmpty()) {
	        	String isCheck = list_xg.get(0).getIsCheck();
	        	if(isCheck.equals("05")){
	        		return result("201","用户已注册");
	        	}
	        	personInfoService.delPersonInfo(idCard);
	        }
	        //2.判断是否是离退休或遗嘱人员
	        List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
	        if(list_dr.isEmpty()) return result("302","该参保人不是离退休或遗嘱人员");
	        String rylb = list_dr.get(0).getRYLB();//离退休或遗嘱
	        String isLx;//0离休，1遗嘱
	        if (rylb.equals("离退休"))isLx = "0";
            else if (rylb.equals("遗嘱"))isLx = "1";
            else return result("304","不符合认证标准");
            
	        //3.获取省卡管照片数据
	        Result cardInfoResult = FaceVerifyUtil.getCardAllInfo(bean);
	        if(cardInfoResult != null && cardInfoResult.getStatusCode().equals("0")) {
	        	if(isLx.equals("1"))return result("306", "遗嘱人员请到服务点采集照片");
	        	return result("305","获取省卡管照片数据失败"); 
	        }
	        
/*	        String loginUrl = "https://219.138.205.201:443/sisp/iface/user/checkLogin";
	        XGLoginBean beans = new XGLoginBean();
            beans.setType("TSB");
            beans.setNetpassword(MD5("TSB").toLowerCase());
            beans.setUserName(MD5("TSB").toLowerCase());
	        String loginRe = HttpClientUtil.getData(loginUrl, beans, true, true, 443).toString();
	        String xg_token = JsonHelp.jsonToMap(loginRe).get("data").toString();
	        String loginStatus = JsonHelp.jsonToMap(loginRe).get("statusCode").toString();
	        String loginMsg = JsonHelp.jsonToMap(loginRe).get("message").toString();
            if (!loginStatus.equals("200")){//孝感登陆失败
                logger.error(">>>>>>>login xg happen error ,msg："+loginMsg+",statusCode is:"+loginStatus+"<<<<<<<");
                return result("305","获取省卡管照片数据失败");
            }
            //向省卡管获取
            String url1 = "https://219.138.205.201/sisp/iface/cardInfo/getCardAllInfo?tokenId="+xg_token;
            XGPhotoBean photoBean = new XGPhotoBean();
            photoBean.setId(idCard);
            photoBean.setChannelcode("TSB");
            String result = HttpClientUtil.getData(url1, photoBean, true, true, 443).toString();
            String statusCode = JsonHelp.jsonToMap(result).get("statusCode").toString();
            //状态码为0则是不存在该用户，且为遗嘱，则提示到服务点采集照片
            if (statusCode.equals("0") && isLx.equals("1")) {
                return this.result("306", "遗嘱人员请到服务点采集照片");
            }
            //存在则向孝感数据库插入参保人员信息，isCheck为等待未审核，不通过，再删除
            Map<String,String> data = (LinkedHashMap<String,String>)JsonHelp.jsonToMap(result).get("data");
            String base64Str = data.get("photo").toString();
            String sex = data.get("sex").toString();
            name = data.get("name").toString();
            String company = data.get("dwmc").toString();
            String tel = data.get("telno").toString();
            String address = data.get("regaddr").toString();
            String sexs =(sex.equals("男")?"01":"02");
            CardAllInfo cardInfo =  new CardAllInfo();
            cardInfo.setSfzh(idCard);
            cardInfo.setSex(sexs);
            cardInfo.setName(name);
            cardInfo.setRegaddr(address);
            cardInfo.setTelno(tel);
            cardInfo.setDwmc(company);
            cardInfo.setPhoto(base64Str);*/
//            GenerateImage(base64Str, "E:/"+idCard+".jpg");
            
	        //4.调用认证平台认证
	        String uuid = FaceVerifyUtil.createToken();
	        String aac001 = list_dr.get(0).getGRBH();//个人编号
	        String aab001 = list_dr.get(0).getDWBH();//企业编号
	        String identifyId = uuid+"-"+idCard+"-"+aab001+"-"+aac001;
	        String url = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("ImgFilter"));
	        CardAllInfo cardInfo = (CardAllInfo) cardInfoResult.getData();
	        String imgResult = null;
	        if(registtype == 1){ //一张照片
	        	String token = createToken();
	        	//插入人员资料
	        	FaceVerifyUtil.insertPersonInfo(personInfoService, cardInfo, "", token);
	        	imgResult = FaceVerifyUtil.ImgFilter(url, idCard, cardInfo.getPhoto(), photo , photo , photo , identifyId , "01");
	        }else if(registtype == 2) //3张
	        	imgResult = FaceVerifyUtil.ImgFilter(url, idCard, cardInfo.getPhoto(), photo1 , photo2 , photo3 , identifyId , "01");
	        else
	        	return result("303", "注册类型不符");
	        Map<String,Object> reMap = JsonHelp.jsonToMap(imgResult);
            String code = reMap.get("statusCode").toString();
            if (code.equals("01")){//比对通过，定时器获取人工审核结果
                 getResigt(registtype , photo, idCard,identifyId);//执行定时器，获取审核结果
                 return this.result("200", "注册图片上传成功，请稍后！");
            }else{//比对不通过，删除记录
                 personInfoService.delPersonInfo(idCard);
                 return result("307","用户注册图片不通过");
            }
		}catch(Exception e){
			logger.error("注册失败"+e);
			return error("注册失败", null);
		}
	}
	
	/**
	 * APP注册--输入密码
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/registTwo" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
	public Result registTwo(@RequestBody RegistBean bean){
		try{
			String password = bean.getPassword() == null?"":bean.getPassword();
			String idCard = bean.getIdCard() == null?"":bean.getIdCard();
			String name = bean.getName() == null?"":bean.getName();
			if(idCard.isEmpty() || name.isEmpty() || password.isEmpty()) return result("301", "注册信息不能为空");
			Map<String,String> map = new HashMap<String, String>();
	        map.put("name",name);
	        map.put("idCard",idCard);
	        map.put("isCheck", "04");
	        //1.向孝感数据库获取信息
	        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
	        if(list_xg.isEmpty()) return result("202","用户注册未通过或已注册");
	        //2.更新密码
	        password = CommUtil.generateValue(password);
	        boolean flag = personInfoService.updateXGPersonPOToPassword(password, idCard);
	        if(flag){
	        	personInfoService.updateXGPersonFPStatus(Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_05"),idCard);
	        	return ok("注册成功", null); 
	        }else return error("密码更新失败", null);
		}catch(Exception e){
			logger.error("--注册第二步失败，更新密码出错--");
			return error("更新密码出错", null);
		}
	}
	
	/**
	 * 验证
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verify1", method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result verify1(@RequestBody RegistBean bean) throws Exception {
		try{
			String idCard = bean.getIdCard();
	        String photo1 = bean.getBase64String1();
	        String photo2 = bean.getBase64String2();
	        String photo3 = bean.getBase64String3();
	        String justCheck = bean.getJustCheck();
	        String name = bean.getName();
	        String aac001 = bean.getAac001();
	        String aab001 = bean.getAab001();
	        String sys_token = bean.getToken();
	        
	        String aae279 = bean.getAae279();
	        String devicetype = bean.getDevicetype();
	        if(devicetype!=null && devicetype != ""){
	        	devicetype = DictionaryUtil.getDictName(Constants.CHANNELCODE,devicetype);

	        }
	        
	         
	         Calendar acl = Calendar.getInstance();
	         int year = acl.get(Calendar.YEAR);
	        
	        Map<String,String> map = new HashMap<String, String>();
	        map.put("name",name);
	        map.put("idCard",idCard);
	        map.put("isCheck", Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_05"));
	        
	        //1.判断是否是离退休或遗嘱人员
	        List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
	        if(list_dr.isEmpty()) return result("302","该参保人不是离退休或遗嘱人员");
	        
	        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
	        if (list_xg.isEmpty()) return this.result("302", "用户尚未注册或注册不通过");
	        
	        if(!sys_token.equals(list_xg.get(0).getToken())) return result("300", "登陆超时");
	        String personId = list_xg.get(0).getId();
	     
	        
	        //2.获取人脸验证平台中参保人的图片
	        String photoURL = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("getPhoto"));
	        String photoStr = FaceVerifyUtil.getPhoto(idCard, photoURL);
	        
	        String uuid = FaceVerifyUtil.createToken();
	        String identifyId = uuid+"-"+idCard+"-"+aab001+"-"+aac001;
	        String url = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("ImgFilter"));
	        String imgResult = FaceVerifyUtil.ImgFilter(url, idCard, photoStr, photo1 , photo2 , photo3 , identifyId , justCheck);
	        Map<String,Object> reMap = JsonHelp.jsonToMap(imgResult);
            String code = reMap.get("statusCode").toString();
            
            
            int icSize = 0;
            List<IC09PO> icList = personInfoService.getIC09Info(aab001, aac001 , 
            		Integer.toString(year) , Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"));//IC09
            if(!icList.isEmpty()){
            	icSize = icList.size();
            }
             
            
            //认证失败
            int failSize = 0;
            List<IC09PO> failList = personInfoService.getIC09Info(aab001, aac001 , 
            		Integer.toString(year) , Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"));//IC09
            if(!failList.isEmpty()){
            	failSize = failList.size();
            }
            
            if (code.equals("01")){//比对通过，等待人工审核
              //  getResult(identifyId,personId,idCard,icSize,failSize,aae279,devicetype,"");//执行定时器，获取审核结果,并将数据写入东软视图
                return this.result("200", "图片上传成功",identifyId);
            }
            //不为01则比对失败
           // personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"), "人脸比对失败", identifyId,personcheckstatus,machincheckstatus,devicetype,aae279);//业务表新增认证记录
            return this.result("307", "图片比对失败");
		}catch(Exception e){
			logger.error("验证失败"+e);
			return error("验证失败", null);
		}
	}
	
   
    /**
     * APP轮询照片是否审核
     * @param bean
     * @return
     */
    @RequestMapping(value="/isCheck", method=RequestMethod.POST , produces = "application/json")
    @ResponseBody
    public Result isCheck(@RequestBody IsCheckBean bean) {
        String idCard = bean.getIdCard();
        String token = bean.getToken();
        long type = bean.getType();
        long isCheckType = bean.getIsCheckType();
        Map<String,String> map = new HashMap<String, String>();
        map.put("idCard",idCard);
        if(type == 1){ //注册
        	if(isCheckType == 1){ //一张照片
        		map.put("isCheck", Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_03"));
        	}else if(isCheckType == 2){ //三张
        		map.put("isCheck", Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_04"));
        	}else if(isCheckType == 3){//德生宝采集
        		map.put("isCheck", Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_04"));
        	}else return result("300", "输入参数有误");
        	List<XGPersonPO>  list = personInfoService.getXGPersonInfo(map);
        	if(list.isEmpty()) return result("302","用户尚未注册或注册未审核或照片审核不通过");//不存在该条记录
//        	if(!list.get(0).getToken().equals(token)) return this.result("301", "身份认证失败，请重新登录");
        	return ok("注册通过",null);
        }else if(type == 2){//验证
        	String identifyId = bean.getIdentifyId();
        	map.put("identifyId", identifyId);
        	BusinessInfoPO info = personInfoService.getBusinessInfoByStatus(map);
        	if(info == null) return result("302","用户尚未验证");
        	if(info.getStatus().equals(Constants.TECSUN_VERIFY_BUSSINESS_STATUS.get("bussiness_success"))) 
        		return ok("验证成功", null); 
        	else if(info.getStatus().equals(Constants.TECSUN_VERIFY_BUSSINESS_STATUS.get("bussiness_fail")))
        		return result("303", info.getStatement());
        	else return result("304", "验证失败");
        }else return result("300", "输入参数有误");
    }
    
    /**
     *  离退休或遗嘱 养老金发放信息查询接口
     */
    @RequestMapping(value="/getFafangInfo", method=RequestMethod.POST , produces = "application/json")
    @ResponseBody
    public ResultVerify getFafangInfo(@RequestBody PersonLoginBean bean){
    	try{
    		String idCard = bean.getIdCard();
    		String name = bean.getName();
    	    Map<String,Object> map = new HashMap<String, Object>();
            map.put("idCard",idCard);
            map.put("name",name);
            //1. 先获取个人编号
            List<DRPersonPO>  list_xg = personInfoService.getDRPersonInfo(map);
            if(list_xg.isEmpty()){
            	return resultVerify("303", "个人信息查询失败，查无此人");
            }
            String grbh = list_xg.get(0).getGRBH();
            //2.查询养老金发放信息
            bean.setGrbh(grbh);
            Page<FafangInfo> page =  new Page<FafangInfo>(bean.getPageno(),bean.getPagesize());
            page = personInfoService.getFafangInfo(page,bean);
            return ok(page.getCount(), "", page.getData());
    	}catch(Exception e){
    		return error("养老金发放信息查询接口失败", null);
    	}
    }
	
    /**
     * 修改密码
     * @return
     */
    @RequestMapping(value = "/updatePwd" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result updatePwd(@RequestBody PersonLoginBean bean){
    	String idCard = bean.getIdCard() == null?"":bean.getIdCard();
		String name = bean.getName() == null?"":bean.getName();
    	try{
    		String password = bean.getPassword() == null?"":bean.getPassword();
    		String oldpassword = bean.getOldpassword() == null?"":bean.getOldpassword();
    		if(idCard.isEmpty() || name.isEmpty() || password.isEmpty() || oldpassword.isEmpty()) return result("300", "修改密码信息不能为空");
    		Map<String,String> map = new HashMap<String, String>();
	        map.put("idCard",idCard);
	        map.put("name",name);
	        //map.put("password",CommUtil.generateValue(oldpassword));
	        List<XGPersonPO> list = personInfoService.getXGPersonInfo(map);
	        if(list.isEmpty()) return result("301", "用户不存在");
	        XGPersonPO po = list.get(0);
	        if(!po.getPassword().equals(CommUtil.generateValue(oldpassword))) return result("302", "用户验证密码错误");
    		boolean flag = personInfoService.updateXGPersonPOToPassword(CommUtil.generateValue(password), idCard);
    		if(flag) return ok("修改成功", null);else return result("201", "修改失败");
    	}catch(Exception e){
    		logger.error(idCard+"--修改密码接口异常--"+e);
    		return error("修改密码接口异常", null);
    	}
    	
    }
    
    /**
     * TSB采集
     * @param bean
     * @return
     */
    @RequestMapping(value = "/tsbCollect1" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result tsbCollect1(@RequestBody RegistBean bean){
    	try{
    		String idCard = bean.getIdCard() == null?"":bean.getIdCard();
    		String name = bean.getName() == null?"":bean.getName();
    		Map<String,String> map = new HashMap<String, String>();
    	    map.put("idCard",idCard);
    	    map.put("name",name);
    	    //1.判断是不是已经采集过
    	    map.put("isCheck", Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_05"));
    	    List<XGPersonPO> list_xg = personInfoService.getXGPersonInfo(map);
    	    if(!list_xg.isEmpty()) return result("301", "用户已经采集");
    	    personInfoService.delPersonInfo(idCard);
    	    //2.判断是否是离退休或遗嘱人员
    	    List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
	        if(list_dr.isEmpty()) return result("302","该参保人不是离退休或遗嘱人员");
	        //3.验证照片
	    	String photo = bean.getPhoto();
			String photo1 = bean.getBase64String1();
	        String photo2 = bean.getBase64String2();
	        String photo3 = bean.getBase64String3();
	        String uuid = FaceVerifyUtil.createToken();
	        String aac001 = list_dr.get(0).getGRBH();//个人编号
	        String aab001 = list_dr.get(0).getDWBH();//企业编号
	        String identifyId = uuid+"-"+idCard+"-"+aab001+"-"+aac001;
	        String url = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("ImgFilter"));
	        
//	        String loginUrl = "https://219.138.205.201:443/sisp/iface/user/checkLogin";
//	        XGLoginBean beans = new XGLoginBean();
//            beans.setType("TSB");
//            beans.setNetpassword(MD5("TSB").toLowerCase());
//            beans.setUserName(MD5("TSB").toLowerCase());
//	        String loginRe = HttpClientUtil.getData(loginUrl, beans, true, true, 443).toString();
//	        String xg_token = JsonHelp.jsonToMap(loginRe).get("data").toString();
//	        String loginStatus = JsonHelp.jsonToMap(loginRe).get("statusCode").toString();
//	        String loginMsg = JsonHelp.jsonToMap(loginRe).get("message").toString();
//            if (!loginStatus.equals("200")){//孝感登陆失败
//                logger.error(">>>>>>>login xg happen error ,msg："+loginMsg+",statusCode is:"+loginStatus+"<<<<<<<");
//                return result("305","登陆孝感平台失败");
//            }
//            //向省卡管获取
//            String url1 = "https://219.138.205.201/sisp/iface/cardInfo/getCardAllInfo?tokenId="+xg_token;
//            XGPhotoBean photoBean = new XGPhotoBean();
//            photoBean.setId(idCard);
//            photoBean.setChannelcode("TSB");
//            String result = HttpClientUtil.getData(url1, photoBean, true, true, 443).toString();
//            String statusCode = JsonHelp.jsonToMap(result).get("statusCode").toString();
//            //状态码为0则是不存在该用户，且为遗嘱，则提示到服务点采集照片
//            if (statusCode.equals("0")) {
//                return this.result("306", "获取省卡管照片数据失败");
//            }
//            //存在则向孝感数据库插入参保人员信息，isCheck为等待未审核，不通过，再删除
//            Map<String,String> data = (LinkedHashMap<String,String>)JsonHelp.jsonToMap(result).get("data");
//            String base64Str = data.get("photo").toString();
//            String sex = data.get("sex").toString();
//            name = data.get("name").toString();
//            String company = data.get("dwmc").toString();
//            String tel = data.get("telno").toString();
//            String address = data.get("regaddr").toString();
//            String sexs =(sex.equals("男")?"01":"02");
	        
            CardAllInfo cardInfo =  new CardAllInfo();
            cardInfo.setSfzh(idCard);
            cardInfo.setSex(list_dr.get(0).getXB());
            cardInfo.setName(name);
            cardInfo.setRegaddr("");
            cardInfo.setTelno("");
            cardInfo.setDwmc("");
//            cardInfo.setPhoto(base64Str);
	        
	        //插入人员资料
        	String token = createToken();
        	FaceVerifyUtil.insertPersonInfo(personInfoService, cardInfo, "", token);
	        String imgResult = FaceVerifyUtil.ImgFilter(url, idCard, photo, photo1 , photo2 , photo3 , identifyId , "01");
	        Map<String,Object> reMap = JsonHelp.jsonToMap(imgResult);
            String code = reMap.get("statusCode").toString();
            if (code.equals("01")){//比对通过，定时器获取人工审核结果
                 getResigt(3 , photo, idCard,identifyId);//执行定时器，获取审核结果
                 return this.result("200", "采集图片上传成功，请稍后！");
            }else{//比对不通过，删除记录
                 personInfoService.delPersonInfo(idCard);
                 return result("307","采集图片不通过");
            }
    	}catch(Exception e){
    		logger.error("采集图片失败"+e);
			return error("采集失败", null);
    	}
    }
    
    /**
     * 重置密码
     * @return
     */
    @RequestMapping(value = "/resetPwd" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result resetPwd(@RequestBody PersonLoginBean bean){
    	String idCard = bean.getIdCard() == null?"":bean.getIdCard();
		String name = bean.getName() == null?"":bean.getName();
		String password = bean.getPassword() == null?"":bean.getPassword();
		try{
			if(idCard.isEmpty() || name.isEmpty() || password.isEmpty() ) return result("300", "重置密码信息不能为空");
			Map<String,String> map = new HashMap<String, String>();
	        map.put("idCard",idCard);
	        map.put("name",name);
	        List<XGPersonPO> list = personInfoService.getXGPersonInfo(map);
	        if(list.isEmpty()) return result("301", "用户不存在");
	        boolean flag = personInfoService.updateXGPersonPOToPassword(CommUtil.generateValue(password), idCard);
    		if(flag) return ok("重置成功", null);else return result("201", "重置失败");
		}catch(Exception e){
			logger.error(idCard+"--重置密码异常--"+e);
    		return error("重置密码接口异常", null);
		}
    }
    
    /**
     *  获取审核结果
     * @param identifyId
     * @param personId
     * @param idCard
     * @param size
     * @param failSize
     * @param aae279
     * @param aae011
     * @param type 轮循获取审核结果的类型，
     * insertBusinessFaceRe //t_business_info
     * insertView/updateView    //IC09
     * 如果机检成功
     */
    public void getResult(final String identifyId,final String personId, final String idCard, final int size,final int failSize,final String aae279,final String aae011,final String type,final String personcheckstatus,final String machincheckstatus,final String devicetype,final String dataValue){
        //定时器每2秒扫描一次数据库
        final  java.util.Timer timer;
        timer = new Timer(true);
        String []str = identifyId.split("-");
        final String aab001 = str[2];//企业编号
        final String aac001 = str[3];//个人编号
        timer.schedule(
                new java.util.TimerTask() {
                    public void run() {
                        JsonObject json = new JsonObject();
                        json.addProperty("businessid", identifyId);
                        //调用人检接口
                        String wsResult = (String) WebClient.getWebClient(FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("getVerifyResult")), json, String.class);
                        Map<String, Object> map = JsonHelp.jsonToMap(wsResult);
                        String verifyStatus = map.get("statusCode").toString();
                        //200则已经审核,有记录
                        if (verifyStatus.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                            Map<String,String> data = (LinkedHashMap<String,String>)map.get("data");
                            String verify = data.get("verifystate").toString();//人工审核结果
                            String failreason = data.get("failreason").toString();//失败原因
                            Calendar acl = Calendar.getInstance();
                            int year = acl.get(Calendar.YEAR);
                            //System.out.println("type:"+type+"=="+identifyId+"=="+failreason+"=="+verify+"==size:"+size+"==failSize:"+failSize);
                            //向t_business_info业务表中写数据；
                            //走机检的分支
                            if(type.equals("nrtPicCheck")){
                            	//人检成功 向 t_business_info 业务表中写数据;
                            	if(verify.equals("00")){
                            		//System.out.println("人检成功时间："+dataValue);
                            		if (size == 0 && failSize==0){//不存在该记录，则新增一条记录
                                        personInfoService.insertView(aac001,aab001,Integer.toString(year),dataValue,"","",Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"),"","","",personId,identifyId,aae279,aae011);
                                    }else{
                                    	personInfoService.updateView(aac001, aab001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"),dataValue);
                                    }
                            		personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_STATUS.get("bussiness_success"), failreason, identifyId,Constants.FACESTATUS.get("personcheck_success"),Constants.FACESTATUS.get("machincheck_success"),devicetype,aae279,dataValue);//业务表新增认证记录
                            	}else{//人检失败 向 t_business_info 业务表中写数据;
                            		//System.out.println("人检时间失败："+dataValue);
                            		if(failSize ==0  && size == 0  ){
	                               		 personInfoService.insertView(aac001,aab001,Integer.toString(year),dataValue,"","",Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"),"","","",personId,identifyId,aae279,aae011);
	                               	}/*else if(failSize >0  && size== 0 ){
	                               		personInfoService.updateView(aac001, aab001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"));
	                               	}*/
                            		
                            		personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_STATUS.get("bussiness_fail"), failreason, identifyId,Constants.FACESTATUS.get("personcheck_fail"),Constants.FACESTATUS.get("machincheck_success"),devicetype,aae279,dataValue);//业务表新增认证记录
                            	}
                            }else{
                            	//人检是会修改ICO9表中的数据，并向t_business_info表中写条数据；
                            	//走人检分支
                            	if (verify.equals("00")) {
                            		/*SimpleDateFormat sm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            		String dataValue= sm.format(new Date());*/
                            		//System.out.println("人检时间成功："+dataValue);
                                	if (size == 0 && failSize==0){//不存在该记录，则新增一条记录
                                        personInfoService.insertView(aac001,aab001,Integer.toString(year),dataValue,"","",Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"),"","","",personId,identifyId,aae279,aae011);
                                    } else if(size == 0 && failSize>0){
                                    	personInfoService.updateView(aac001, aab001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"),dataValue);
                                    } else if(size > 0 && failSize ==0){
                                    	personInfoService.updateView(aac001, aab001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"),dataValue);
                                    }
                                    personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_STATUS.get("bussiness_success"), failreason, identifyId,Constants.FACESTATUS.get("personcheck_success"),Constants.FACESTATUS.get("machincheck_success"),devicetype,aae279,dataValue);//业务表新增认证记录
                                } else {
                                	//System.out.println("人检时间失败："+dataValue);
                                	if(failSize ==0  && size == 0  ){
                                		 personInfoService.insertView(aac001,aab001,Integer.toString(year),dataValue,"","",
                                         		Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"),"","","",personId,identifyId,aae279,aae011);
                                	}/*else{
                                		personInfoService.updateView(aac001, aab001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"));
                                	}*//*else if(failSize ==0  && size> 0 ){
                                		personInfoService.updateView(aac001, aab001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"));
                                	}*/
                                    failreason = data.get("failreason").toString();//失败原因
                                    personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_STATUS.get("bussiness_fail"), failreason, identifyId,Constants.FACESTATUS.get("personcheck_fail"),Constants.FACESTATUS.get("machincheck_success"),devicetype,aae279,dataValue);//业务表新增认证记录
                                }
                            }
                            
                            timer.cancel();//审核完，则定时器取消
                        }
                        logger.info(">>>>>>Timer is running ,idCard is "+idCard+",identifyId is "+identifyId+"<<<<<");
                    }
                }, 0, 2 * 1000);
    }
	
/*	public static void main(String[] args) throws Exception {
		FaceController  ff = new FaceController();
		RegistBean bean = new RegistBean();
		bean.setIdCard("422201194610050829");
		bean.setName("王润娥");
		bean.setAac001("0010158444");
		bean.setAab001("4209011004694");
		bean.setAae279("101");
		bean.setDevicetype("01");
		
		ff.verify(bean);
		
		JsonObject jsonPic = new JsonObject();
        jsonPic.addProperty("idCard", "422201195309171343");
        jsonPic.addProperty("name", "李望珍");
        jsonPic.addProperty("aac001", "0010158443");
        jsonPic.addProperty("aab001", "4209011004694");
        jsonPic.addProperty("aae279", "101");
        jsonPic.addProperty("devicetype", "01");
        
        
        String picResult = (String) WebClient.getWebClient("http://localhost:8080/ifacesuportservice/iface/face/verify", jsonPic, String.class);
		
		//ff.verify(bean);
        
        
        JsonObject verify = new JsonObject();
		verify.addProperty("idCard", "422201195309171343");
		verify.addProperty("justCheck", "00");
		verify.addProperty("name", "李望珍");
		verify.addProperty("token", "81b4d12c18ed42bc879955c4bc46ff0c");
		verify.addProperty("aac001", "0010158443");
		verify.addProperty("aab001", "4209011004694");
		
		verify.addProperty("aae279", "101");
		verify.addProperty("devicetype", "01");
		
		
		String verifyUrl = "http://localhost:8080/ifacesuportservice/iface/face/verify";
		String verifyResult = (String)WebClient.getWebClient(verifyUrl, verify, String.class);
		System.out.println(verifyResult);
	}*/
    
    /**
     * 获取注册结果
     * @param idCard
     * @param str
     */
    public void getResigt(final long registtype,final String str, final String idCard ,final String identifyId){
        //定时器每2秒扫描一次数据库
        final  java.util.Timer timer;
        timer = new Timer(true);
        timer.schedule(
	        new java.util.TimerTask() {
	            public void run() {
	                JsonObject json = new JsonObject();
	                json.addProperty("businessid", identifyId);
	                String wsResult = (String) WebClient.getWebClient(FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("getVerifyResult")), 
	                		json, String.class);
	                Map<String, Object> map = JsonHelp.jsonToMap(wsResult);
	                String verifyStatus = map.get("statusCode").toString();
	                //200则已经审核,有记录
	                if (verifyStatus.equals("200")) {
	                    Map<String,String> data = (LinkedHashMap<String,String>)map.get("data");
	                    String verify = data.get("verifystate").toString();//人工审核结果
	                    if (verify.equals("00")) {//审核通过则新增人员信息表
	                    	if(registtype == 1 || registtype == 3){
	                    		personInfoService.updateXGPersonFPStatus(Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_02"),idCard);
		                        //保存照片
		                        //add图片上传接口，调用认证平台
		                        JsonObject jsonPic = new JsonObject();
		                        jsonPic.addProperty("idCard",idCard);
		                        jsonPic.addProperty("district", "xg");
		                        jsonPic.addProperty("base64Str", str);
		                        try {
		                            String picResult = (String) WebClient.getWebClient(FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("uploadPhoto")), jsonPic, String.class);
		                            Map<String, Object> picMap = JsonHelp.jsonToMap(picResult);
		                            String picStatus = map.get("statusCode").toString();
		                            if(picStatus.equals("200")){
		                            	String status = Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_03");
		                            	if(registtype == 3) status = Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_04");
		                            	personInfoService.updateXGPersonFPStatus(status,idCard);
		                            }
		                        }catch (Exception e){
		                            logger.error(">>>>save photo happen error<<<<<"+idCard);
		                        }
	                    	}else if(registtype == 2){
	                    		personInfoService.updateXGPersonFPStatus(Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_04"),idCard);
	                    	}else{
	                    		timer.cancel();
	                    	}
	                    	timer.cancel();//审核完，则定时器取消timer.cancel();//审核完，则定时器取消
	                    } 
	                }
	                logger.info(">>>>>>Timer is running ,idCard is "+idCard+",identifyId is "+identifyId+"<<<<<");
	            }
	        }, 0, 2 * 1000);
    }

    /**
     *  base64Str to pic
     * @param imgStr base64 字符串
     * @param path 存储路径
     * @return
     */
    public static boolean GenerateImage(String imgStr,String path) { // 对字节数组字符串进行Base64解码并生成voice
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
//            String imgFilePath = "C:/Users/sandwich/Desktop/base64/12.jpg";// 新生成的voice
            OutputStream out = new FileOutputStream(path);//imgFilePath
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    //------------------孝感生存认证新版本start--------------------------------------------
    //1、采集功能: 判断是否为离退休人员或遗嘱-checkPersonInfo、采集-tsbCollect；
    //2、生存认证功能：verify-认证、
    
    
	// 判断是否为离退休人员或遗嘱-拆分出来的方法；
	public ResultVerify checkPersonStatus(RegistBean bean) {
		String result = Constants.RESULT_MESSAGE_RETIREMENT;
		String message = "";
		Map<String, String> map = new HashMap<String, String>();
		String idCard = bean.getIdCard() == null ? "" : bean.getIdCard();
		String name = bean.getName() == null ? "" : bean.getName();
		map.put("idCard", idCard);
		map.put("name", name);
		//根据身份证和名称在东软表中查询是否为离退休人员或遗嘱
		//用前台传进来的身份证查询是否为离退休人员或遗嘱
		List<DRPersonPO> list_dr = personInfoService.getDRPersonInfo(map);
		if (list_dr.isEmpty()) {
			//处理身证号18位15位
			if (bean.getIdCard() != null && !"".equals(bean.getIdCard())&& bean.getIdCard().length() > 0) {
				String idCard15To18 = Dom4JUtil.get15Ic(bean.getIdCard());
				map.put("idCard", idCard15To18);
			}
			//-用15位身份证查询是否为离退休人员或遗嘱；如果没有数据说不是 离退休人员或遗嘱
			List<DRPersonPO> idCard15To18List = personInfoService.getDRPersonInfo(map);
			if (list_dr.isEmpty() && idCard15To18List.isEmpty()) {
				result =  Constants.RESULT_MESSAGE_NORETIREMENT;
				message = "经查询，您非离退休人员或遗嘱，无法执行些操作！";
			} else {  //用15位身份证查询是否为离退休人员或遗嘱；如果没有数据说是  离退休人员或遗嘱
				result =  Constants.RESULT_MESSAGE_RETIREMENT;
				message = "操作成功！";
			}
		} else {//用前台传进来的身份证查询 是否为离退休人员或遗嘱；如果有数据说是  离退休人员或遗嘱
			result =  Constants.RESULT_MESSAGE_RETIREMENT;
			message = "操作成功！";
		}
		return resultVerify(result, message);
	}
	
	
	//  判断是否采集过照片-拆分出来的方法；
	public ResultVerify checkPersonCollect(RegistBean bean) {
		String result = Constants.RESULT_MESSAGE_COLLECT;
		String message = "";
		Map<String, String> map = new HashMap<String, String>();
		String idCard = bean.getIdCard() == null ? "" : bean.getIdCard();
		String name = bean.getName() == null ? "" : bean.getName();
		map.put("idCard", idCard);
		map.put("name", name);
		//根据前台传的身份证号和名称在采集表中查询些是否进行过照片采集
		List<XGPersonPO> photoList = personInfoService.getXGPersonInfo(map);
		//如果查询的有数据并且照片字段不为空说明已进行过采集操作；
		if (!photoList.isEmpty() && photoList.size() > 0) {
			String photoPath = photoList.get(0).getCj_sbPhotoPath();
			if (!photoPath.isEmpty() && photoPath.length() > 0) {
				result = Constants.RESULT_MESSAGE_COLLECT;
				message = "经查询，您已经采集过照片！";
			} else {
				result = Constants.RESULT_MESSAGE_NOCOLLECT;
				message = "经查询，您未采集过照片！";
			}
		} else {
			//如果查询的无数据说明暂未进行采集过；
			result = Constants.RESULT_MESSAGE_NOCOLLECT;
			message = "经查询，您未采集过照片！";
		}
		return resultVerify(result, message);
	}
	
	
	
	// 判断当年是否认证通过-拆分出来的方法
	public ResultVerify checkPersonVerifyInfo(RegistBean bean) {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		String aac001 = "";// 个人编号
		String aab001 = "";// 单位编号
		Calendar acl = Calendar.getInstance();
		int year = acl.get(Calendar.YEAR);
		Map<String, String> map = new HashMap<String, String>();
		String idCard = bean.getIdCard() == null ? "" : bean.getIdCard();
		String name = bean.getName() == null ? "" : bean.getName();
		map.put("idCard", idCard);
		map.put("name", name);
		try {
			List<DRPersonPO> personList = personInfoService.getDRPersonInfo(map);
			if (personList.isEmpty()) {
				if (bean.getIdCard() != null && !"".equals(bean.getIdCard())&& bean.getIdCard().length() > 0) {
					String idCard15To18 = Dom4JUtil.get15Ic(bean.getIdCard());
					map.put("idCard", idCard15To18);
				}

				List<DRPersonPO> idCard15To18List = personInfoService.getDRPersonInfo(map);
				if (personList.isEmpty() && idCard15To18List.isEmpty()) {
					message = "暂无数据";
					return resultVerify(result,message);
				} else {
					aac001 = idCard15To18List.get(0).getGRBH();
					aab001 = idCard15To18List.get(0).getDWBH();
					List<IC09PO> verifyList = personInfoService.getIC09Info(aab001, aac001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"));
					if (!verifyList.isEmpty() && verifyList.size() > 0) {
						result = Constants.RESULT_MESSAGE_SUCCESS;
						message = "您于" + verifyList.get(0).getAAE047()+ "已认证通过，请确定是否重新认证！";
						return resultVerify(result, message);
					} else {
						result = Constants.RESULT_MESSAGE_ERROR;
						message = "暂无数据！";
						return resultVerify(result, message);
					}
				}
			} else {
				aac001 = personList.get(0).getGRBH();
				aab001 = personList.get(0).getDWBH();
				List<IC09PO> verifyList = personInfoService.getIC09Info(aab001,aac001, Integer.toString(year),Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"));
				if (!verifyList.isEmpty() && verifyList.size() > 0) {
					result = Constants.RESULT_MESSAGE_SUCCESS;
					message = "您于" + verifyList.get(0).getAAE047()+ "已认证通过，请确定是否重新认证";
					return resultVerify(result, message);
				} else {
					result = Constants.RESULT_MESSAGE_ERROR;
					message = "暂无数据！";
					return resultVerify(result, message);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = Constants.RESULT_MESSAGE_ERROR;
			message = "判断当年是否认证通过接口异常：" + e.getMessage();
		}
		return resultVerify(result, message);
	}
	
	 /*@author   zengyunhua
	   @date     2016-7-26
	   @param bean 
	   @return
	   @throws
	   @判断当年是否认证通过
	   @说明：此方法是共用，type值类型：1、采集，2、认证，3、查年个人中心；
	                       采集：只需要判断 是否为离退休人员或遗嘱和是否采集过照片；
	                       认证：只需要判断  是否为离退休人员或遗嘱、是否采集过照片和是否认证成功过；
	         查看个人中心：只需要判断  是否为离退休人员或遗嘱和是否认证成功过
	   */
	@RequestMapping(value = "/getVerifyInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public ResultVerify getVerifyInfo(@RequestBody RegistBean bean) {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		Map<String, String> map = new HashMap<String, String>();
		String idCard = bean.getIdCard() == null ? "" : bean.getIdCard();
		String name = bean.getName() == null ? "" : bean.getName();
		String type = bean.getType() == null ? "" : bean.getType();
		map.put("idCard", idCard);
		map.put("name", name);
		// 1、采集，2、认证，3、查年个人中心；
		if (type != null && type.length() > 0) {
			if ("2".equals(type)) {//认证业务
				// 判断是否为离退休人员或遗嘱 返回 301-RESULT_MESSAGE_RETIREMENT  是离退休人员或遗嘱；302-RESULT_MESSAGE_NORETIREMENT 非离退休人员或遗嘱
				ResultVerify resultVerify = this.checkPersonStatus(bean);
				//System.out.println("认证-是否离退状态："+resultVerify.getStatusCode());
				// 只有 是离退休人员或遗嘱301-RESULT_MESSAGE_RETIREMENT 才能判断是否进行过照片采集；
				if(resultVerify.getStatusCode().equals(Constants.RESULT_MESSAGE_RETIREMENT)){
					//判断是否采集过照片  已采集照片:201:RESULT_MESSAGE_COLLECT;未采集过照片-202:RESULT_MESSAGE_NOCOLLECT
					ResultVerify collectStatus = this.checkPersonCollect(bean);	
					//System.out.println("认证-采集结果："+collectStatus.getStatusCode());
					//只有采集过照片的状态：201：RESULT_MESSAGE_COLLECT 才能判断是否进行认证过；
					if(collectStatus.getStatusCode().equals(Constants.RESULT_MESSAGE_COLLECT)){
						ResultVerify verifyStatus = this.checkPersonVerifyInfo(bean);
						//System.out.println("认证-个人认证结果："+verifyStatus.getStatusCode()+"=="+verifyStatus.getMessage());
						result = verifyStatus.getStatusCode();
						message = verifyStatus.getMessage();
					}else{
						result = collectStatus.getStatusCode();
						message = "经查询，您未采集过照片！";
					}
				}else{
					result = resultVerify.getStatusCode();
					message = "经查询，您非离退休人员或遗嘱，无法执行些操作！";
				}
			}else if("1".equals(type)){ //采集业务
				// 判断是否为离退休人员或遗嘱 返回 301-RESULT_MESSAGE_RETIREMENT  是离退休人员或遗嘱；302-RESULT_MESSAGE_NORETIREMENT 非离退休人员或遗嘱
				ResultVerify resultVerify = this.checkPersonStatus(bean);
				//System.out.println("采集-是否离退状态："+resultVerify.getStatusCode());
				// 只有 是离退休人员或遗嘱301-RESULT_MESSAGE_RETIREMENT 才能判断是否进行过照片采集；
				if(resultVerify.getStatusCode().equals(Constants.RESULT_MESSAGE_RETIREMENT)){
					//判断是否采集过照片  已采集照片:201:RESULT_MESSAGE_COLLECT;未采集过照片-202:RESULT_MESSAGE_NOCOLLECT
					ResultVerify collectStatus = this.checkPersonCollect(bean);	
					//System.out.println("采集-采集结果："+collectStatus.getStatusCode());
					result = collectStatus.getStatusCode();
					message = collectStatus.getMessage();
					//System.out.println("采集结果："+result+"=="+message);
				}else{
					result = resultVerify.getStatusCode();
					message = "经查询，您非离退休人员或遗嘱，无法执行些操作！";
				}
			}else if("3".equals(type)){//查看个人中心；
				// 判断是否为离退休人员或遗嘱 返回 301-RESULT_MESSAGE_RETIREMENT  是离退休人员或遗嘱；302-RESULT_MESSAGE_NORETIREMENT 非离退休人员或遗嘱
				ResultVerify resultVerify = this.checkPersonStatus(bean);
				//System.out.println("查看个人认证中心-是否离退状态："+resultVerify.getStatusCode());
				// 只有 是离退休人员或遗嘱301-RESULT_MESSAGE_RETIREMENT 才能判断是否进行认证过；
				if(resultVerify.getStatusCode().equals(Constants.RESULT_MESSAGE_RETIREMENT)){
					ResultVerify verifyStatus = this.checkPersonVerifyInfo(bean);
					//System.out.println("查看个人认证中心-个人认证结果："+verifyStatus.getStatusCode()+"=="+verifyStatus.getMessage());
					result = verifyStatus.getStatusCode();
					message = verifyStatus.getMessage();
				}else{
					result = resultVerify.getStatusCode();
					message = "经查询，您非离退休人员或遗嘱，无法执行些操作！";
				}
			}
		}else{
			result = Constants.RESULT_MESSAGE_ERROR;
			return resultVerify(result, "操作类型不能为空！");
		}
		return resultVerify(result, message);
	}
    /**
     * 认证历史纪录
     * @param bean
     * @return
     */
    @RequestMapping(value="/verifyHistory", method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public ResultVerify verifyHistory(@RequestBody HistoryMsgBean bean) {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		String idCard = bean.getIdCard();
		Page<BusinessInfoPO> page = new Page<BusinessInfoPO>(bean.getPageno(),bean.getPagesize());
			Map<String, String> map = new HashMap<String, String>();
			map.put("idCard", idCard);
			List<XGPersonPO> list_xg = personInfoService.getXGPersonInfo(map);
			if(!list_xg.isEmpty() && list_xg.size()>0){
					try {String personId = list_xg.get(0).getId();
					String personName = list_xg.get(0).getPerson_name();
					bean.setPersonId(personId);
					bean.setPage(page);
					List<BusinessInfoPO> bussinessList = personInfoService.verifyHistory(bean);
					if (!bussinessList.isEmpty() && bussinessList.size() > 0) {
						for (BusinessInfoPO vo : bussinessList) {
							try {
								vo.setAuthway(DictionaryUtil.getDictName(Constants.CHANNELCODE, vo.getAuthway()));// 认证渠道；
								vo.setAuthtype(DictionaryUtil.getDictName(Constants.FACESTATUSAUTHTYPE, vo.getAuthtype()));// 认证方式；
								vo.setPersoncheckstatus(DictionaryUtil.getDictName(Constants.FACESTATUSVALUE, vo.getPersoncheckstatus()));// 人检状态；
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						result = Constants.RESULT_MESSAGE_SUCCESS;
						page.setData(bussinessList);
						return resultVerify(result, "操作成功！", page);
					} else {
						result = Constants.RESULT_MESSAGE_ERROR;
						return resultVerify(result, "暂无数据！");
					}
				} catch (Exception e) {
					result = Constants.RESULT_MESSAGE_ERROR;
					message = "认证历史纪录：" + e.getMessage();
					e.printStackTrace();
				}
				return resultVerify(result, message, page);
			}else{
				result = Constants.RESULT_MESSAGE_ERROR;
				return resultVerify(result, "暂无数据！");
			}
    }
    
    
    
    
    /**
     * 根据身份证号码修改采集信息；
     * @param bean
     * @return
     */
    
	@RequestMapping(value = "/upPersonInfoByIdCard", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result upPersonInfoByIdCard(@RequestBody RegistBean bean) {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		if (bean.getIdCard() != null && !"".equals(bean.getIdCard())
				&& bean.getIdCard().length() > 0) {
			String tel = bean.getTel();
			String address = bean.getAddress();
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("idCard", bean.getIdCard());
			map.put("tel", tel);
			map.put("address",address);
			boolean falge = personInfoService.upPersonInfoByIdCard(map);
			if (falge) {
				result = Constants.RESULT_MESSAGE_SUCCESS;
				return this.result(result, "操作成功！");
			} else {
				result = Constants.RESULT_MESSAGE_ERROR;
				return this.result(result, "操作失败！");
			}
		} else {
			result = Constants.RESULT_MESSAGE_ERROR;
			return this.result(result, "身份证号不能为空！");
		}
	}
    
	
	/**
     * 根据身份证号码取采集人的信息及照片信息；
     * @param bean
     * @return
     */
	 @RequestMapping(value = "/getPersonPhoto" , method=RequestMethod.POST , produces ="application/json")
	 @ResponseBody
	 public Result getPersonPhoto(@RequestBody RegistBean bean){
		 String result = Constants.RESULT_MESSAGE_SUCCESS;
		   Map<String,String> map = new HashMap<String, String>();
	  	  String idCard = bean.getIdCard() == null?"":bean.getIdCard();
	  	  String name = bean.getName() == null?"":bean.getName();
	  	  map.put("idCard",idCard);
	  	  map.put("name", name);
	  	  
	  	 //2.获取人脸验证平台中参保人的图片
	        String photoURL = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("getPhoto"));
	        String photoStr = FaceVerifyUtil.getPhoto(idCard, photoURL);
	  	  
	      List<XGPersonPO> photoList = personInfoService.getXGPersonInfo(map);
	      
	      
	      if(!photoList.isEmpty() && photoList.size()>0){
	    	 // System.out.println(photoList.get(0).getAddress());
	    	  XGPersonPO  xgpersonVO  = new XGPersonPO();
	    	   if((photoList.get(0).getAddress()==null || photoList.get(0).getAddress().equals("")) || (photoList.get(0).getTel()==null || photoList.get(0).getTel().equals(""))){
	    		   List<DRPersonPO>  personList = personInfoService.getPersonInfoByIdCard(bean);
	    		   if(!personList.isEmpty() && personList.size()>0){
	    			    xgpersonVO.setAddress(personList.get(0).getAAE006());
	    		   }
	    	   }
	    	   xgpersonVO.setCj_sbPhotoPath(photoStr);
	    	   photoList.get(0).setCj_sbPhotoPath(photoStr);
	    	  result = Constants.RESULT_MESSAGE_SUCCESS;
	    	  return resultVerify(result, "操作成功！",photoList);
	      }else{
	    	  result = Constants.RESULT_MESSAGE_ERROR;
	    	  return this.result(result, "暂未采集过照片，请先采集照片！");
	      }
	 }
	
	
  
    
    /**
     * 根据身份证号码、姓名、性别查询需要采集人的信息
     * @param bean
     * @return
     */
    
    @RequestMapping(value = "/getPersonInfoByIdCard" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result getPersonInfoByIdCard(@RequestBody RegistBean bean){
  	  String result = Constants.RESULT_MESSAGE_SUCCESS;
  	  String idCard = bean.getIdCard() == null?"":bean.getIdCard();
      String name = bean.getName() == null?"":bean.getName();
      String xb = bean.getXb() == null ?"":bean.getXb();
      String idCard15To18 ="";
      String message = "操作成功！";
      //2.获取人脸验证平台中参保人的图片
      String photoURL = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("getPhoto"));
      String photoStr = FaceVerifyUtil.getPhoto(idCard, photoURL);
      Page<DRPersonPO> page = new Page(bean.getPageno(), bean.getPagesize());
      bean.setName(name);
      bean.setIdCard(idCard);
      bean.setXb(xb);
      bean.setPage(page);
      List<DRPersonPO>  psersonList = personInfoService.getPersonInfoByIdCard(bean);
      if(psersonList.isEmpty()){
    	  if(bean.getIdCard()!= null && !"".equals( bean.getIdCard()) &&  bean.getIdCard().length()> 0){
    		  idCard15To18= Dom4JUtil.get15Ic(bean.getIdCard());
    		  if(idCard15To18!=null && idCard15To18.length()>0){
    			  bean.setIdCard(idCard15To18);
    			  List<DRPersonPO>  idCard15To18List = personInfoService.getPersonInfoByIdCard(bean);
    			  if(!idCard15To18List.isEmpty() && idCard15To18List.size()>0){
    				  for (DRPersonPO vo : idCard15To18List) {
    	    	    		 try {
    	    					vo.setXB(DictionaryUtil.getDictName(Constants.SEX_GROUP, vo.getXB()));
    	    				} catch (Exception e) {
    	    					e.printStackTrace();
    	    				}
    	    	    	  }
    	    	    	  result = Constants.RESULT_MESSAGE_SUCCESS;
    	    	    	  message= "查询成功！";
    	    	    	  page.setData(idCard15To18List);
    			  }else{
    				  result = Constants.RESULT_MESSAGE_ERROR;
    				  message = "换后身份证查询暂无数据！";
    			  }
    		  }else{
    			  result = Constants.RESULT_MESSAGE_ERROR;
    			  message = "身份证转换后不能为空！";
    		  }
    	  }
    	  return this.result(result, message,page);
      }else{
    	    if(!psersonList.isEmpty() && psersonList.size()>0){
    	    	 for (DRPersonPO vo : psersonList) {
    	    		 try {
    					vo.setXB(DictionaryUtil.getDictName(Constants.SEX_GROUP, vo.getXB()));
    				} catch (Exception e) {
    					e.printStackTrace();
    				}
    	    	  }
    	    	  result = Constants.RESULT_MESSAGE_SUCCESS;
    	    	  page.setData(psersonList);
    	    	  return this.result(result, "操作成功！",page);
    	      }else{
    	    	  result = Constants.RESULT_MESSAGE_ERROR;
    	    	  return this.result(result, "暂无数据！");
    	      }
      }
  
    }
    
    
    
    /**
     * 判断是否为离退休人员或遗嘱
     * @param bean
     * @return
     */
    @RequestMapping(value = "/checkPersonInfo" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result checkPersonInfo(@RequestBody RegistBean bean){
    	  String result = Constants.RESULT_MESSAGE_SUCCESS;
    	  String idCard = bean.getIdCard() == null?"":bean.getIdCard();
  		  String name = bean.getName() == null?"":bean.getName();
  		  Map<String,String> map = new HashMap<String, String>();
  		  map.put("idCard",idCard);
 	      map.put("name",name);
 	      //判断是否为离退休人员或遗嘱
  	      List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
          if(list_dr.isEmpty()){
        	  if(bean.getIdCard()!= null && !"".equals( bean.getIdCard()) &&  bean.getIdCard().length()> 0){
        		  String idCard15To18= Dom4JUtil.get15Ic(bean.getIdCard());
    			  map.put("idCard",idCard15To18);
        		}
        	  List<DRPersonPO>  idCard15To18List = personInfoService.getDRPersonInfo(map);
        	  if(list_dr.isEmpty()&&idCard15To18List.isEmpty() ){
        		  return result("302","经查询，您非离退休人员或遗嘱，无法执行些操作！");
        	  }
          }else{
        	  return result(result,"操作成功！");
          }
          
          //判断是否采集过照片；
        List<XGPersonPO> photoList = personInfoService.getXGPersonInfo(map);
		if (!photoList.isEmpty() && photoList.size() > 0) {
			String photoPath = photoList.get(0).getCj_sbPhotoPath();
			if (!photoPath.isEmpty() && photoPath.length() > 0) {
				return result("201", "经查询，您已经采集过照片！");
			}else{
				return result("202", "经查询，您未采集过照片！");
			}
		}else{
			return result("202", "经查询，您未采集过照片！");
		}
    }
    
    
    /**
     * TSB采集
	   @version   对类的说明 标明该类模块的版本
       @param    RegistBean ：idCard身份证;name姓名
       @return   Result 结果
       @exception  Exception
       @see   此接口原流程是：
                        1、判断是否为离退休人员或遗嘱
                        2、返回否，终止流程；返回否流程继续；
                        3、上传照片；
                        4、上传照片成功后保存采集信息；
                        
          2016-10-31 新需求，流程更改如下：
                        
       @see   此接口现流程是：
                    1、判断是否为离退休人员或遗嘱
                    2、返回否，上传照片，保存数据；返回是，上传照片，保存数据；
                        
     */
    
    @RequestMapping(value = "/tsbCollect" , method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result tsbCollect(@RequestBody RegistBean bean){
    	 String result = Constants.RESULT_MESSAGE_SUCCESS;
    	
    	 
    	try{
    		//身份证
    		String idCard = bean.getIdCard() == null?"":bean.getIdCard();
    		//姓名
    		String name = bean.getName() == null?"":bean.getName();
    		//名族
    		String ethnicValue = bean.getEthnic() == null?"":bean.getEthnic();
    		 //出生日期
    		String birthday = bean.getBirthday() == null?"":bean.getBirthday();
    		 //性别
    		String sexValue = bean.getXb() == null?"":bean.getXb();
    	    //取出民族的代码；
    	    ethnicValue = Dom4JUtil.getKeyByValue(ethnicValue);
    	    //取出性别的代码；
    	    sexValue = Dom4JUtil.getKeyByValue(sexValue);
    	    //插入人员资料 start
            CardAllInfo cardInfo =  new CardAllInfo();
            //出生日期
            cardInfo.setBirthday(birthday);
            //民族代码
            cardInfo.setEthnic(ethnicValue);
            //身份证号码
            cardInfo.setSfzh(idCard);
            //性别代码
            cardInfo.setSex(sexValue);
            //姓名 
            cardInfo.setName(name);
            cardInfo.setRegaddr("");//
            cardInfo.setTelno("");
            cardInfo.setDwmc("");
            //判断是否为离退休人员或遗嘱 的查询条件
            Map<String,String> map = new HashMap<String, String>();
    	    map.put("idCard",idCard);
    	    map.put("name",name);
    	    //判断是否为离退休人员或遗嘱
			List<DRPersonPO> list_dr = personInfoService.getDRPersonInfo(map);
			//list_dr大于0，说明是退休人员，否为非退休人员；
			if (list_dr.isEmpty()) {
				if (bean.getIdCard() != null && !"".equals(bean.getIdCard())&& bean.getIdCard().length() > 0) {
					//将18位身份证号转化为15位返回,非18位身份证号原值
					String idCard15To18 = Dom4JUtil.get15Ic(bean.getIdCard());
					map.put("idCard", idCard15To18);
				}
				//用15位身份证号码来判断是否为退休人员
				List<DRPersonPO> idCard15To18List = personInfoService.getDRPersonInfo(map);
				if (list_dr.isEmpty() && idCard15To18List.isEmpty()) {
					// return result("302", "经查询，您非离退休人员或遗嘱，无法执行些操作！");
					//非退休人员，采集类型设值为2；
					cardInfo.setCollType(Constants.COLL_TYPE2);
				} else {
					//退休人员，采集类型设值为1；
					cardInfo.setCollType(Constants.COLL_TYPE1);
				}
			} else {
				//退休人员，采集类型设值为1；
				cardInfo.setCollType(Constants.COLL_TYPE1);
			}
    	    
			String token = createToken();
			FaceVerifyUtil.insertPersonInfo(personInfoService, cardInfo, "", token);
	        //3.验证照片
	    	String photo = bean.getPhoto();
	    	//图片上传接口，调用认证平台,图片上传后，把图片保存到CardAllInfo表中；
            JsonObject jsonPic = new JsonObject();
            jsonPic.addProperty("idCard",idCard);
            jsonPic.addProperty("district", "xg");
            jsonPic.addProperty("base64Str", photo);
            String picResult = (String) WebClient.getWebClient(FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("uploadPhoto")), jsonPic, String.class);
            Map<String, Object> picMap = JsonHelp.jsonToMap(picResult);
            if(picMap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_SUCCESS)){
            	result = Constants.RESULT_MESSAGE_SUCCESS;
            	//String token = createToken();
            	
            	//1.判断是不是已经采集过
            	List<XGPersonPO> list_xg = personInfoService.getXGPersonInfo(map);
            	if(!list_xg.isEmpty()){
        	    	if(list_xg.size()>0){
        	    		String cjPath = "/home/disk/voicePrint/xg_sbPic/"+cardInfo.getSfzh()+".jpg";//采集照片路径
        	    		map.put("cj_sbPhotoPath", cjPath);
        	    		personInfoService.upPersonInfoByIdCard(map);
        	    	}
        	    }else{
        	    	//FaceVerifyUtil.insertPersonInfo(personInfoService, cardInfo, "", token);
                	//插入人员资料 end 
        	    }
            }else{
            	result = Constants.RESULT_MESSAGE_ERROR;
            }
            return this.result(result, "图片采集成功！");
    	}catch(Exception e){
    		logger.error("图片采集失败"+e);
			return error("图片采集", null);
    	}
    }
    
    /**
	 * 人工检测认证-复审（机检三次失败后，德生宝直接调用人检接口）
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/verify", method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result verify(@RequestBody RegistBean bean) throws Exception {
		try{
			String idCard = bean.getIdCard();
	        String photo1 = bean.getBase64String1();
	        String photo2 = bean.getBase64String2();
	        String photo3 = bean.getBase64String3();
	        String justCheck = bean.getJustCheck();
	        String name = bean.getName();
	        String aac001 = "";
	        String aab001 = "";
	        
	        String aae279 = bean.getAae279();//认证方式 
	        String authway = bean.getDevicetype();//认证渠道-代码；
	        
	        String devicetype = bean.getDevicetype();//认证渠道-中文；
	        /*if(bean.getDevicetype()!= null && !"".equals(bean.getDevicetype()) &&  bean.getDevicetype().length()> 0){
	        	devicetype = DictionaryUtil.getDictName(Constants.CHANNELCODE,devicetype);
	        }
	        */
	        
	        
	        Map<String,String> psersonMap = new HashMap<String, String>();
	        psersonMap.put("idCard",idCard);
	        psersonMap.put("name",name);
	        List<DRPersonPO> personList = personInfoService.getDRPersonInfo(psersonMap);
	        if(!personList.isEmpty() && personList.size()>0){
	        	aac001 = personList.get(0).getGRBH();
	        	aab001 = personList.get(0).getDWBH();
	        }else{
				if (bean.getIdCard() != null  && bean.getIdCard().length() > 0) {
					String idCard15To18 = Dom4JUtil.get15Ic(bean.getIdCard());
					psersonMap.put("idCard",idCard15To18);
					List<DRPersonPO>  idCard15To18List = personInfoService.getDRPersonInfo(psersonMap);
					if(!idCard15To18List.isEmpty() && idCard15To18List.size()>0){
						aac001 = idCard15To18List.get(0).getGRBH();
			        	aab001 = idCard15To18List.get(0).getDWBH();
					}
					
				}
	        }
	        
	        
	        String personcheckstatus="";//人检状态;
	        String machincheckstatus="";//机检状态；
	         
	         Calendar acl = Calendar.getInstance();
	         int year = acl.get(Calendar.YEAR);
	        
	        Map<String,String> map = new HashMap<String, String>();
	        map.put("name",name);
	        map.put("idCard",idCard);
	        
	        //1.判断是否是离退休或遗嘱人员
	       /* List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
	        if(list_dr.isEmpty()) return result("302","经查询，您非离退休人员或遗嘱，无法执行些操作");*/
	        
	    	List<DRPersonPO> list_dr = personInfoService.getDRPersonInfo(map);
			if (list_dr.isEmpty()) {
				if (bean.getIdCard() != null && !"".equals(bean.getIdCard())
						&& bean.getIdCard().length() > 0) {
					String idCard15To18 = Dom4JUtil.get15Ic(bean.getIdCard());
					map.put("idCard", idCard15To18);
				}
				List<DRPersonPO> idCard15To18List = personInfoService.getDRPersonInfo(map);
				if (list_dr.isEmpty() && idCard15To18List.isEmpty()) {
					return result("302", "经查询，您非离退休人员或遗嘱，无法执行些操作！");
				}
			} /*else {
				return result(Constants.RESULT_MESSAGE_SUCCESS, "操作成功！");
			}*/
	        
	        
	        
	        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
	        if (list_xg.isEmpty()) return this.result("302", "经查询，您已经采集过照片！");
	        
	        String personId = list_xg.get(0).getId();
	     
	        
	        //2.获取人脸验证平台中参保人的图片
	        String photoURL = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("getPhoto"));
	        String photoStr = FaceVerifyUtil.getPhoto(idCard, photoURL);
	        
	        String uuid = FaceVerifyUtil.createToken();
	        String identifyId = uuid+"-"+idCard+"-"+aab001+"-"+aac001;
	        String url = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("ImgFilter"));
	        String imgResult = FaceVerifyUtil.ImgFilter(url, idCard, photoStr, photo1 , photo2 , photo3 , identifyId , justCheck);
	        Map<String,Object> reMap = JsonHelp.jsonToMap(imgResult);
            String code = reMap.get("statusCode").toString();
            String checkMessage = reMap.get("message").toString();
           // System.out.println("人检结果："+code+",人检返回信息："+checkMessage);
            
            int icSize = 0;
            List<IC09PO> icList = personInfoService.getIC09Info(aab001, aac001 , 
            		Integer.toString(year) , Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"));//IC09
            if(!icList.isEmpty()){
            	icSize = icList.size();
            }
             
            
            //认证失败
            int failSize = 0;
            List<IC09PO> failList = personInfoService.getIC09Info(aab001, aac001 , 
            		Integer.toString(year) , Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"));//IC09
            if(!failList.isEmpty()){
            	failSize = failList.size();
            }
            
            SimpleDateFormat sm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		String dataValue = sm.format(new Date());
    		//System.out.println("人检verify时间："+dataValue);
            
            if (code.equals("01")){//比对通过，等待人工审核 
                getResult(identifyId,personId,idCard,icSize,failSize,aae279,devicetype,"verify",personcheckstatus,machincheckstatus,authway,dataValue);//执行定时器，认证数据写到IC09表中，获取审核结果,并将数据写入东软视图
                return this.result("200", checkMessage,identifyId);
            }
            //不为01则比对失败
            personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"), "人脸比对失败", identifyId,Constants.FACESTATUS.get("personcheck_fail"),Constants.FACESTATUS.get("machincheck_success"),devicetype,aae279,dataValue);//业务表新增认证记录
            return this.result("307", checkMessage);
		}catch(Exception e){
			logger.error("生存认证人工检测认证失败：FaceController类中verify方法"+e);
			return error("生存认证人工检测认证失败", null);
		}
	}
	
    
    
    /**
	 * 非实时认证-机检
	 * @param bean
	 *  idCard	String	是	身份证号
		base64String1	string	否	待验照片1
		base64String2	String	是	待验照片2
		base64String3	String	否	待验照片3
		identifyId	String	是	认证id（业务id）
		userid	String 	是	（不知干嘛用的，填身份证号吧）
		distinct	String 	否	区域编码
		businesstype	String 	否	业务类型
		area	String	是	区域简称 如 孝感：xg
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/nrtPicCheck", method=RequestMethod.POST , produces ="application/json")
    @ResponseBody
    public Result nrtPicCheck(@RequestBody RegistBean bean) throws Exception {
		try{
			String userid = bean.getIdCard(); //身份证号
			String idCard = bean.getIdCard(); //身份证号
	        String photo1 = bean.getBase64String1(); //待验照片1
	        String photo2 = bean.getBase64String2(); //待验照片2
	        String photo3 = bean.getBase64String3(); //待验照片3
	        String aac001 = ""; //个人编号
	        String aab001 = "";//单位编号
	        String sys_token = bean.getToken();
	        String aae279 = bean.getAae279();//认证方式
	        String name = bean.getName();
	        String devicetype = bean.getDevicetype();//业务类型-认证渠道
	        /*if(bean.getDevicetype()!= null && !"".equals(bean.getDevicetype()) &&  bean.getDevicetype().length()> 0){
	        	devicetype = DictionaryUtil.getDictName(Constants.CHANNELCODE,devicetype);
	        }*/
	        
	        
	        Map<String,String> psersonMap = new HashMap<String, String>();
	        psersonMap.put("idCard",idCard);
	        psersonMap.put("name",name);
	        List<DRPersonPO> personList = personInfoService.getDRPersonInfo(psersonMap);
	        if(!personList.isEmpty() && personList.size()>0){
	        	aac001 = personList.get(0).getGRBH();
	        	aab001 = personList.get(0).getDWBH();
	        }
	        
	        String uuid = FaceVerifyUtil.createToken();
	        String identifyId = uuid+"-"+idCard+"-"+aab001+"-"+aac001;//认证id（业务id）
	        String area =  Dom4JUtil.getConfigUrl("face_area");//区域简称 如 孝感：xg
	        
	        
	        String personcheckstatus="";//人检状态;
	        String machincheckstatus="";//机检状态；
	        
	        
	        Map<String,String> map = new HashMap<String, String>();
	        map.put("name",name);
	        map.put("idCard",idCard);
	        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
	        if (list_xg.isEmpty())
	        return this.result("302", "用户尚未采集过");
	        
	       /* if(!sys_token.equals(list_xg.get(0).getToken()))
            return result("300", "登陆超时");*/
	        String personId = list_xg.get(0).getId();
	        
	        
	        Calendar acl = Calendar.getInstance();
	        int year = acl.get(Calendar.YEAR);
	        
           //根据个人编号和单位编码当前年度判断此人是否进行过认证；如果有认证返回前台，否则进行机检认证；
            String url = FaceVerifyUtil.getHost(Constants.TECSUN_VERIFY_URL.get("nrtPicCheck"));
	        //人脸比对与人工审核nrtPicCheck接口（非实时）
	        String imgResult = FaceVerifyUtil.getNrtPicCheck(url, idCard, photo1 , photo2 , photo3 , identifyId , area,devicetype,userid);
	        Map<String,Object> reMap = JsonHelp.jsonToMap(imgResult);
	        //返回人脸比对与人工审核nrtPicCheck接口的结果
            String code = reMap.get("statusCode").toString();
            //返回人脸比对与人工审核nrtPicCheck接口的提示信息
            String checkMessage = reMap.get("message").toString();
            
           // System.out.println("人检加机检结果："+code+",人检加机检返回值："+checkMessage);
            
            SimpleDateFormat sm=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		String dataValue = "";
            
	            //如果机检成功就进行轮询操作；否刚就终止；
			if (code.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
				// 从要通知东软的表中查询认证成功的数据
				int icSize = 0;
				// 从要通知东软的表中查询认证失败的数据
				int failSize = 0;

				List<IC09PO> icList = personInfoService.getIC09Info(aab001,
						aac001, Integer.toString(year),
						Constants.TECSUN_VERIFY_BUSSINESS_AAE038
								.get("bussiness_success"));// IC09
				if (!icList.isEmpty()) {
					icSize = icList.size();
				}

				// 认证失败
				List<IC09PO> failList = personInfoService.getIC09Info(aab001,
						aac001, Integer.toString(year),
						Constants.TECSUN_VERIFY_BUSSINESS_AAE038
								.get("bussiness_fail"));// IC09
				if (!failList.isEmpty()) {
					failSize = failList.size();
				}
   	            
   	            //机检成功 - 向t_business_info 业务表添加数据； 机检成功，机检状态保存为-机检成功，人检状态保存为-待审核；\
				 dataValue = sm.format(new Date());
				// System.out.println("非实时的机检成功结果："+dataValue);
   	            personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_success"), checkMessage, identifyId,Constants.FACESTATUS.get("machincheck_no_success"),Constants.FACESTATUS.get("machincheck_success"),devicetype,aae279,dataValue);
   	            //轮循人检结果；
                getResult(identifyId,personId,idCard,icSize,failSize,aae279,devicetype,"nrtPicCheck",Constants.FACESTATUS.get("machincheck_no_success"),Constants.FACESTATUS.get("machincheck_success"),devicetype,dataValue);//执行定时器，获取审核结果,并将数据写入东软视图
                return this.result(Constants.RESULT_MESSAGE_SUCCESS, checkMessage,identifyId);
	         }else{
	        	//机检失败 - 向t_business_info 业务表添加数据； 机检失败，机检状态保存为-机检失败，人检状态不保存；  personcheck_fail
	   	        // personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"), checkMessage, identifyId,Constants.FACESTATUS.get("personcheck_fail"),Constants.FACESTATUS.get("personcheck_fail"),devicetype,aae279); //加个机检失败的状态
	        	 dataValue = sm.format(new Date());
				// System.out.println("非实时的机检失败结果："+dataValue);
	        	 dataValue = sm.format(new Date());
	        	 personInfoService.insertBusinessFaceRe(personId, Constants.TECSUN_VERIFY_BUSSINESS_AAE038.get("bussiness_fail"), checkMessage, identifyId,Constants.FACESTATUS.get("machincheck_fail"),Constants.FACESTATUS.get("machincheck_fail"),devicetype,aae279,
	    	        	 dataValue); //加个机检失败的状态
	        	 return this.result(Constants.RESULT_MESSAGE_ERROR, checkMessage);
	         }
		}catch(Exception e){
			logger.error("生存认证机检失败：FaceController类中nrtPicCheck方法"+e);
			return error("生存认证机检失败", null);
		}
	}
    
    
  //------------------孝感生存认证新版本end--------------------------------------------
}
