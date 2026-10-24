package com.tecsun.sisp.iface.server.util;

import com.tecsun.sisp.iface.common.util.Config;
import com.tecsun.sisp.iface.common.util.Constants;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hudanmeng on 2015/11/30.
 */
public class CardIface {
    public static final Logger logger = Logger.getLogger(CardIface.class);

	public CardIface(){}
	
	
	
	
	
	/**
	 *  1.1查询制卡进度
	* @Title: getHospitalList
	* @return String    返回类型
	* @throws
	 */
	public  static Map<String, String> getCardProgress(String result , String message,String idCard,String name){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
            call.setTargetEndpointAddress( new URL(Config.getInstance().get("BASE_CARDINFO_URL")));
            call.setOperationName( new QName( Config.getInstance().get("BASE_CARDINFO_NAMESPACE"),Config.getInstance().get("BASE_CARDINFO_PROGRESS")));// 设置要调用哪个方法
            call.setSOAPActionURI( Config.getInstance().get("BASE_CARDINFO_NAMESPACE") + Config.getInstance().get("BASE_CARDINFO_PROGRESS"));
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "user"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "pass"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "aac002"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "aac003"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "aab301"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.setReturnType(XMLType. SOAP_STRING); // 要返回的数据类型（自定义类型）
            wsResult = (String) call.invoke(new String[]{Config.getInstance().get("BASE_CARDINFO_USDER"),Config.getInstance().get("BASE_CARDINFO_PWD"), idCard, name,Config.getInstance().get("BASE_CARDINFO_AREA")});/// 调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取查询制卡进度getCardProgress接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取查询制卡进度getCardProgress接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;

	}
	
	
	
	/**
	 *  1.2卡基础数据
	* @Title: getHospitalList
	* @return String    返回类型
	* @throws
	 */
	public  static Map<String, String> cardInfo(String result , String message,String idCard,String name){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			  /*  call=(Call)(new Service()).createCall();
		        call.setTargetEndpointAddress(new URL(Config.getInstance().get("BASE_CARDINFO_URL")));
		        call.setOperationName(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"),Config.getInstance().get("BASE_CARDINFO_BASEINFO"))); 
		        call.setTimeout(60000);
		        wsResult = (String) call.invoke(new String[]{Config.getInstance().get("BASE_CARDINFO_USDER"),Config.getInstance().get("BASE_CARDINFO_PWD"), idCard, name,Config.getInstance().get("BASE_CARDINFO_AREA")});*/
			
			
			call = (Call) service.createCall();
            call.setTargetEndpointAddress( new URL(Config.getInstance().get("BASE_CARDINFO_URL")));
            call.setOperationName( new QName( Config.getInstance().get("BASE_CARDINFO_NAMESPACE"),Config.getInstance().get("BASE_CARDINFO_BASEINFO")));// 设置要调用哪个方法
            call.setSOAPActionURI( Config.getInstance().get("BASE_CARDINFO_NAMESPACE") + Config.getInstance().get("BASE_CARDINFO_BASEINFO"));
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "user"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "pass"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "aac002"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "aac003"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.addParameter(new QName(Config.getInstance().get("BASE_CARDINFO_NAMESPACE"), "aab301"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.setReturnType(XMLType. SOAP_STRING); // 要返回的数据类型（自定义类型）
            wsResult = (String) call.invoke(new String[]{Config.getInstance().get("BASE_CARDINFO_USDER"),Config.getInstance().get("BASE_CARDINFO_PWD"), idCard, name,Config.getInstance().get("BASE_CARDINFO_AREA")});/// 调用方法并传递参数
			
			
			
		} catch (Exception e) {
			logger.error("---获取卡基础数据cardInfo接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取卡基础数据cardInfo接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;

	}
	
	   /**
	    * 修改个人服务密码接口
	      @version   V.1.0.0
	      @see     getRpcServiceMethod
	      @param   contextXml-入参；result-返回结果；message-返回信息；method-接口方法；serverNmae--接口服务
	      @return   Result
	      @exception  Exception
	    */
	
	public static Map<String, String> getRpcServiceMethod(String contextXml,
			String result, String message,String method,String serverNmae) {
		Map<String, String> map = new HashMap<String, String>();
		String wsResult = "";
		try {
			RPCServiceClient serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();// 固定
			options.setTimeOutInMilliSeconds(60000L);// TODO:根据实际情况设置超时时间
			// TODO：根据实际情况修改服务地址。
			String serviceUrl = new URL(Config.getInstance().get("BASE_CARDSERVICE_URL")).toString();
			EndpointReference targetEPR = new EndpointReference(serviceUrl);
			options.setTo(targetEPR);//http://serviceproxy.dareway.com
			QName qName = new QName("http://uddi.dareway.com", "invokeService");// 固定
			Class<?>[] returnTypes = new Class[] { String.class };
			// TODO：根据实际情况修改参数，具体参数格式和内容参考文档。
			String xmlpara = contextXml;
			// TODO：根据实际情况修改服务名和操作名。ScmsCardService
			//服务名HsdepService  操作名test 
			Object[] paras = new Object[] {serverNmae, method, xmlpara };
			Object[] response;
			try {
				response = serviceClient.invokeBlocking(qName, paras, returnTypes);
				Object[] result1 = response.clone();
				serviceClient.cleanup();
				serviceClient.cleanupTransport();
				serviceClient = null;
				response = null;
				if (result1[0] instanceof String) {
					wsResult = (String) result1[0];
					result = Constants.RESULT_MESSAGE_SUCCESS;
				}else{
					result = Constants.RESULT_MESSAGE_ERROR;
				}
			} catch (AxisFault e) {
				e.printStackTrace();
				logger.error("调用个"+method+"接口失败"+e.getMessage());
				message = "调用"+method+"接口失败";
				result = Constants.RESULT_MESSAGE_ERROR;
			}
		} catch (AxisFault e1) {
			e1.printStackTrace();
			logger.error("调用个"+method+"接口，创建SiService失败"+e1.getMessage());
			message = "调用"+method+"接口，创建SiService失败";
			result = Constants.RESULT_MESSAGE_ERROR;
		} catch (MalformedURLException e1) {
			logger.error("调用个"+method+"接口，获取接口地址失败"+e1.getMessage());
			message = "调用"+method+"接口，获取接口地址失败";
			result = Constants.RESULT_MESSAGE_ERROR;
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	   
	   

}
