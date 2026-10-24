package com.tecsun.sisp.iface.server.controller.card;

import java.util.Map;

import com.tecsun.sisp.iface.common.util.AESUtil;
import com.tecsun.sisp.iface.common.util.Config;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.Dom4JUtil;
import com.tecsun.sisp.iface.common.util.InvokeUtil;
import com.tecsun.sisp.iface.common.util.JsonHelper;
import com.tecsun.sisp.iface.common.util.PicUtil;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.entity.requestBean.PersonParam;
import com.tecsun.sisp.iface.server.model.service.AuthenticationServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @title
 * @2018年12月28日-下午2:27:42
 * @version2018
 */
@RestController
@RequestMapping(value = "/iface/card")
public class CardInfoController extends BaseController {

	private String user = Config.getInstance().get("biometrics.user");
    private String pass = Config.getInstance().get("biometrics.pwd");
    private String typeid = Config.getInstance().get("biometrics.typeid");
    @Autowired
    private AuthenticationServiceImpl authenticationService;
	
	private static Logger logger = LoggerFactory
			.getLogger(CardInfoController.class);

	
	// 人员查询
	@RequestMapping(value = "/queryPersonnel", method = RequestMethod.POST, produces = "application/json")
	public Result queryPersonnel(@RequestBody PersonParam bean) throws Exception {
		 logger.info("---------------CardInfoController: queryPersonnel---------------");
	     logger.info("---------------bean:\t" + JsonHelper.javaBeanToJson(bean));
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			if(StringUtils.isNotEmpty(bean.getAac002())){
	
				String m = 
						 "<ID>" + Constants.PID + "</ID>" 
						+"<PASS>"+pass+"</PASS>"
						+ "<AAC002>"+ bean.getAac002()+"</AAC002>" 
						+"<TYPEID>"+typeid+"</TYPEID>";
				//加密		
				byte[] aesValue  = AESUtil.aesEncryptToBytes(m, Config.getInstance().get("biometrics.key"));
				String strValue = AESUtil.byteArrayToHexString(aesValue);
				//调用生物检测入参
				String val ="<USER>" + user + "</USER>"+"<m>"+strValue+ "</m>";
				// 调用生物检测接口
				  Map<String, String> res = InvokeUtil.invoke("allJK",
						new String[] { val });
				System.out.println(res);
				if(Constants.RETURN_SUCCESS_00.equals(res.get("ERROR"))){
				//接收r域的值
				String str=res.get("r");
				//转字节数组
				byte[] hexToByteArray = AESUtil.hexToByteArray(str);
				//AES解密
				String aesDecrypt = AESUtil.aesDecryptByBytes(hexToByteArray, Config.getInstance().get("biometrics.key"));
				//xml解析
				String re = "<root>" + aesDecrypt + "</root>";
				Map<String,String> map = Dom4JUtil.readXmlToMap(re);
					return result(statusCode, message, map);
				}else{
					return result(Constants.RESULT_MESSAGE_ERROR, res.get("ERROR"));
				}
			}else{
				return result(Constants.RESULT_MESSAGE_EMPTY, "社保卡号不能为空!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("获取数据异常",e);
		}
			return result(Constants.RESULT_MESSAGE_EXCEPTION, Constants.RESULT_MESSAGE_MSG);
	}
	
	    // 认证
		@RequestMapping(value = "/authentication", method = RequestMethod.POST, produces = "application/json")
		public Result authentication(@RequestBody PersonParam bean) throws Exception {
			 logger.info("---------------CardInfoController: authentication---------------");
		     logger.info("---------------bean:\t" + JsonHelper.javaBeanToJson(bean));
			String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			String message = "认证成功";
			try {
				if(StringUtils.isNotEmpty(bean.getAac002()) &&
						StringUtils.isNotEmpty(bean.getDeviceid()) &&
						StringUtils.isNotEmpty(bean.getAuthen()) && 
						StringUtils.isNotEmpty(bean.getCbdid()) &&
						StringUtils.isNotEmpty(bean.getRzyw())){
					
					String m = 
							 "<ID>" + Constants.AID + "</ID>" 
							+"<PASS>"+pass+"</PASS>"
							+ "<AAC002>"+bean.getAac002()+"</AAC002>" 
							+"<TYPEID>"+typeid+"</TYPEID>"
							+"<DEVICEID>"+bean.getDeviceid()+"</DEVICEID>"
							+"<FEATURES1>"+bean.getFeatures1()+"</FEATURES1>"
							+"<REMARK1>"+bean.getRemark1()+"</REMARK1>"
							+"<AUTHEN>"+bean.getAuthen()+"</AUTHEN>"
							+"<CBDID>"+bean.getCbdid()+"</CBDID>"
							+"<RZYW>"+bean.getRzyw()+"</RZYW>"
							+"<SIGN>"+Constants.SIGN+"</SIGN>";
							
					//加密		
					System.out.println("222222222"+Config.getInstance().get("biometrics.key"));
					byte[] aesValue  = AESUtil.aesEncryptToBytes(m, Config.getInstance().get("biometrics.key"));
					String strValue = AESUtil.byteArrayToHexString(aesValue);
					//调用生物检测入参
					String val ="<USER>" + user + "</USER>"+"<m>"+strValue+ "</m>";
					// 生物检测接口Method
					  Map<String, String> res = InvokeUtil.invoke("allJK",
							new String[] { val });
					  logger.info("res"+res);
					System.out.println(res);
					if(Constants.RETURN_SUCCESS_00.equals(res.get("ERROR"))){
						String path = Config.getInstance().get("authentication");
						//此处最好需要捕获异常 判断authen
						boolean authen = PicUtil.base64StrToImage(bean.getAuthen(), path,bean.getAac002());
						bean.setAuthen(path+bean.getAac002()+".jpg");
						authenticationService.saveAuthenticationDatas(bean);
						
						return result(statusCode, message);
					}else{
						return result(Constants.RESULT_MESSAGE_ERROR, res.get("ERROR"));
					}
				}else{
					return result(Constants.RESULT_MESSAGE_EMPTY, "缺失必要参数!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("获取数据异常",e);
			}
				return result(Constants.RESULT_MESSAGE_EXCEPTION, Constants.RESULT_MESSAGE_MSG);
		}
		
		
	//认证查询
	@RequestMapping(value = "/queryAuthentication", method = RequestMethod.POST, produces = "application/json")
	public Result queryAuthentication(@RequestBody PersonParam bean) throws Exception {
		 logger.info("---------------CardInfoController: queryPersonnel---------------");
	     logger.info("---------------bean:\t" + JsonHelper.javaBeanToJson(bean));
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			if(StringUtils.isNotEmpty(bean.getAac002())){
				String qAid = Constants.QAID;
				String m = 
						 "<ID>" + qAid + "</ID>" 
						+"<PASS>"+pass+"</PASS>"
						+ "<AAC002>"+bean.getAac002()+"</AAC002>";
				//加密		
				byte[] aesValue  = AESUtil.aesEncryptToBytes(m, Config.getInstance().get("biometrics.key"));
				String strValue = AESUtil.byteArrayToHexString(aesValue);
				//调用生物检测入参
				String val ="<USER>" + user + "</USER>"+"<m>"+strValue+ "</m>";
				// 调用生物检测接口
				  Map<String, String> res = InvokeUtil.invoke("allJK",
						new String[] { val });
				  if(Constants.RETURN_SUCCESS_00.equals(res.get("ERROR"))){
				//接收r域的值
				String str=res.get("r");
				//转字节数组
				byte[] hexToByteArray = AESUtil.hexToByteArray(str);
				//AES解密
				String aesDecrypt = AESUtil.aesDecryptByBytes(hexToByteArray, Config.getInstance().get("biometrics.key"));
				//xml解析
				String re = "<root>" + aesDecrypt + "</root>";
				Map<String,String> map = Dom4JUtil.readXmlToMap(re);
					return result(statusCode, message, map);
				}else{
					return result(Constants.RESULT_MESSAGE_ERROR, res.get("ERROR"));
				}
			}else{
				return result(Constants.RESULT_MESSAGE_EMPTY, "社保卡号或身份证号不可为空!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("接口内部异常",e);
		}
			return result(Constants.RESULT_MESSAGE_EXCEPTION, Constants.RESULT_MESSAGE_MSG);
	}
	
	
		//批量查询
		@RequestMapping(value = "/batchQuery", method = RequestMethod.POST, produces = "application/json")
		public Result batchQuery(@RequestBody PersonParam bean) throws Exception {
			 logger.info("---------------CardInfoController: batchQuery---------------");
		     logger.info("---------------bean:\t" + JsonHelper.javaBeanToJson(bean));
			String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			String message = "操作成功";
			try {
				if(StringUtils.isNotEmpty(bean.getAac002())){
					String bid = Constants.BID;
					String m = 
							 "<ID>" + bid + "</ID>" 
							+"<PASS>"+pass+"</PASS>"
							+ "<AAC002S>"+bean.getAac002()+"</AAC002S>";
					//加密		
					byte[] aesValue  = AESUtil.aesEncryptToBytes(m, Config.getInstance().get("biometrics.key"));
					String strValue = AESUtil.byteArrayToHexString(aesValue);
					//调用生物检测入参
					String val ="<USER>" + user + "</USER>"+"<m>"+strValue+ "</m>";
					// 调用生物检测接口
					  Map<String, String> res = InvokeUtil.invoke("allJK",
							new String[] { val });
					  if(Constants.RETURN_SUCCESS_00.equals(res.get("ERROR"))){
					//接收r域的值
					String str=res.get("r");
					//转字节数组
					byte[] hexToByteArray = AESUtil.hexToByteArray(str);
					//AES解密
					String aesDecrypt = AESUtil.aesDecryptByBytes(hexToByteArray, Config.getInstance().get("biometrics.key"));
					String batValue=aesDecrypt.substring(22);
					String bValue = AESUtil.replaceBlank(batValue);
						return result(statusCode, message, bValue);
					}else{
						return result(Constants.RESULT_MESSAGE_ERROR, res.get("ERROR"));
					}
				}else{
					return result(Constants.RESULT_MESSAGE_EMPTY, "社保卡号或身份证号不可为空!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("接口内部异常",e);
			}
				return result(Constants.RESULT_MESSAGE_EXCEPTION, Constants.RESULT_MESSAGE_MSG);
		}

		
		//认证流水
		@RequestMapping(value = "/flowAuthentication", method = RequestMethod.POST, produces = "application/json")
		public Result flowAuthentication(@RequestBody PersonParam bean) throws Exception {
			 logger.info("---------------CardInfoController: flowAuthentication---------------");
		     logger.info("---------------bean:\t" + JsonHelper.javaBeanToJson(bean));
			String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			String message = "操作成功";
			try {
				if(StringUtils.isNotEmpty(bean.getAac002())){
					String fid = Constants.FID;
					String m = 
							 "<ID>" + fid + "</ID>" 
							+"<PASS>"+pass+"</PASS>"
							+ "<AAC002>"+bean.getAac002()+"</AAC002>";
					//加密		
					byte[] aesValue  = AESUtil.aesEncryptToBytes(m, Config.getInstance().get("biometrics.key"));
					String strValue = AESUtil.byteArrayToHexString(aesValue);
					//调用生物检测入参
					String val ="<USER>" + user + "</USER>"+"<m>"+strValue+ "</m>";
					// 调用生物检测接口
					  Map<String, String> res = InvokeUtil.invoke("allJK",
							new String[] { val });
					  if(Constants.RETURN_SUCCESS_00.equals(res.get("ERROR"))){
					//接收r域的值
					String str=res.get("r");
					//转字节数组
					byte[] hexToByteArray = AESUtil.hexToByteArray(str);
					//AES解密
					String aesDecrypt = AESUtil.aesDecryptByBytes(hexToByteArray, Config.getInstance().get("biometrics.key"));
					String fValue=aesDecrypt.substring(22);
					String flowValue = AESUtil.replaceBlank(fValue);
					
						return result(statusCode, message, flowValue);
					}else{
						return result(Constants.RESULT_MESSAGE_ERROR, res.get("ERROR"));
					}
				}else{
					return result(Constants.RESULT_MESSAGE_EMPTY, "社保卡号或身份证号不可为空!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("接口内部异常",e);
			}
				return result(Constants.RESULT_MESSAGE_EXCEPTION, Constants.RESULT_MESSAGE_MSG);
		}

	

}
