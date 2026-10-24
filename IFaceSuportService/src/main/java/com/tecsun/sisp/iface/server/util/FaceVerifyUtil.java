package com.tecsun.sisp.iface.server.util;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.client.WebClient;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.CardInfoBean;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.card.CardAllInfo;
import com.tecsun.sisp.iface.common.vo.faceverify.RegistBean;
import com.tecsun.sisp.iface.server.controller.card.CardInfoController;
import com.tecsun.sisp.iface.server.model.service.faceVerify.impl.PersonInfoServiceImpl;

/**
 * 人脸验证接口功能--工具类
 *
 */
public class FaceVerifyUtil {
	
	public static final Logger logger = Logger.getLogger(FaceVerifyUtil.class);
	/**
	 * 获取接口地址
	 * @param uri
	 * @return
	 */
	public static String getHost(String uri){
		 com.tecsun.sisp.iface.server.util.Config config = com.tecsun.sisp.iface.server.util.Config.getInstance();
	     return "http://" + config.get("tecsun_verify_ip") + ":" +
	                config.get("tecsun_verify_port") + config.get("tecsun_verify_projectName") + uri;
	}
	
	/**
	 * 通过参保人的身份证 获取 认证平台 照片信息
	 * @param idCard 身份证
	 * @param url    接口路径地址
	 */
	public static String getPhoto(String idCard , String url){
		String str = "";
		try{
			JsonObject jsonPic = new JsonObject();
	        jsonPic.addProperty("idCard", idCard);
	        jsonPic.addProperty("district", "xg");
	        String picResult = (String) WebClient.getWebClient(url, jsonPic, String.class);
	        Map<String,Object> picMap = JsonHelp.jsonToMap(picResult);
	        str = picMap.get("data").toString();//base64String
		}catch(Exception e){
			str = "";
			logger.error("--获取认证平台照片失败--"+e);
		}
		return str;
	}
	
	/**
	 * 机器比对照片
	 * @param url
	 * @param photo
	 * @param photoStr
	 * @return
	 */
	public static boolean comparePhoto(String url , String photo , String photoStr){
		boolean flag = false;
		try{
			JsonObject json = new JsonObject();
			json.addProperty("photo", photo);
			json.addProperty("sbPhoto", photoStr);
			String picResult = (String) WebClient.getWebClient(url, json, String.class);
	        Map<String,Object> picMap = JsonHelp.jsonToMap(picResult);
	        String statusCode = picMap.get("statusCode").toString();
	        if(statusCode.equals("200")) flag = true;
		}catch(Exception e){
			flag = false;
			logger.error("--机器审核失败--"+e);
		}
		return flag;
	}
	
	/**
	 * 获取省卡管照片
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static Result getCardAllInfo(RegistBean bean) throws Exception{
		CardInfoController cardInfo = new CardInfoController();
        CardInfoBean cardInfoBean = new CardInfoBean();
        cardInfoBean.setId(bean.getIdCard());
        cardInfoBean.setChannelcode(bean.getChannelcode());
        cardInfoBean.setDeviceid(bean.getDeviceid());
        Result cardInfoResult = cardInfo.getCardAllInfo(cardInfoBean);
		return cardInfoResult;
	}
	
	/**
	 * 比对照片
	 * @param url
	 * @param idCard
	 * @param base64Str
	 * @param base64String1
	 * @param base64String2
	 * @param base64String3
	 * @param identifyId
	 * @return
	 */
	public static String ImgFilter(String url , String idCard , String base64Str,
			String base64String1 , String base64String2 , String base64String3, String identifyId , String justCheck){
		JsonObject json = new JsonObject();
		json.addProperty("sbBase64String",base64Str);//省厅的照片
		json.addProperty("base64String1", base64String1);//注册照片
		json.addProperty("base64String2", base64String2);
		json.addProperty("base64String3", base64String3);
		json.addProperty("identifyId",identifyId);
		json.addProperty("userid",idCard);
		//json.addProperty("distinct", "XG");
		json.addProperty("businesstype", "SB");
		json.addProperty("justCheck",justCheck);//00只进行人工审核，01机器+人工
		String wsResult = (String)WebClient.getWebClient(url, json, String.class);
		return wsResult;
	}
	
	public static void insertPersonInfo(PersonInfoServiceImpl personInfoService , 
			CardAllInfo cardInfo , String password , String token){
        //新增用户信息
        Map<String, String> newMap = new HashMap<String, String>();
        String cjPath = "/home/disk/voicePrint/xg_sbPic/"+cardInfo.getSfzh()+".jpg";//采集照片路径
        newMap.put("password",password);
        newMap.put("person_name", cardInfo.getName());
        newMap.put("role","0");
        newMap.put("idCard", cardInfo.getSfzh());
        //newMap.put("sex",cardInfo.getSex().equals("男")?"01":"02");
        newMap.put("sex",cardInfo.getSex());
        newMap.put("company", cardInfo.getDwmc());
        newMap.put("tel", cardInfo.getTelno());
        newMap.put("address", cardInfo.getRegaddr());
        newMap.put("token", token);
        newMap.put("isCheck", Constants.TECSUN_VERIFY_PERSON_ISCHECK.get("PERSON_ISCHECK_01"));//注册信息未审核
        newMap.put("cj_sbPhotoPath",cjPath);
        
        newMap.put("birthday", cardInfo.getBirthday());//出生年月
        newMap.put("ethnic",cardInfo.getEthnic());//民族
        newMap.put("collType",cardInfo.getCollType());//采集类型
        
        personInfoService.insertPersonInfo(newMap);//向孝感数据库插入参保人员信息，isCheck为未审核
	}
    
    /**
     * 生成32位编码
     * @return string
     */
    public static String createToken() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * MD5加密
     * @param s
     * @return
     */
    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
	/**
	 * 人脸比对与人工审核nrtPicCheck接口（非实时）
	 *  idCard	String	是	身份证号
		base64String1	string	否	待验照片1
		base64String2	String	是	待验照片2
		base64String3	String	否	待验照片3
		identifyId	String	是	认证id（业务id）
		userid	String 	是	（不知干嘛用的，填身份证号吧）
		distinct	String 	否	区域编码
		businesstype	String 	否	业务类型
		area	String	是	区域简称 如 孝感：xg

	 */
	public static String getNrtPicCheck(String url , String idCard ,
			String base64String1 , String base64String2 , String base64String3, String identifyId , String area,String businesstype,String userid){
		JsonObject json = new JsonObject();
		json.addProperty("idCard",idCard);
		json.addProperty("base64String1", base64String1);//注册照片
		json.addProperty("base64String2", base64String2);
		json.addProperty("base64String3", base64String3);
		json.addProperty("identifyId",identifyId);
		json.addProperty("userid",userid);
		json.addProperty("businesstype", businesstype);
		json.addProperty("area",area);
		String wsResult = (String)WebClient.getWebClient(url, json, String.class);
		return wsResult;
	}
}
