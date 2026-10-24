package com.tecsun.sisp.iface.server.outerface.his;

import com.tecsun.sisp.iface.common.util.Constants;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import java.util.HashMap;
import java.util.Map;

/**
 * 航天医院接口  -- webservice接口
* @ClassName: HTHisIface 
*
 */
public class HTHisIface {
	
	public static final Logger logger = Logger.getLogger(HTHisIface.class);
	public static final String HTHIS_URL=Constants.HOSWEBSERVICEURL;

	//public static final String HTHIS_URL = "http://10.132.1.85:3388/SiCardWebService.asmx";
	//public static final String HTHIS_URL = "http://10.132.7.3:3388/SiCardWebService.asmx";
	public static final String SOAPACTION = "http://tempuri.org/";
	
	/**
	 *  获取医院门诊科室
	 * sjks 1.若传值为空时，WS接口会返回所有一级科室 
	 *  	2. 若有值，WS接口会返回的是这个科室编号下的子科室 
	* @Title: getHisKS 
	* @param  sjks  上级科室
	* @return String    返回类型 
	* @throws
	 */
	public Map<String, String> getHisKS(String sjks , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION,"Get_HISKS"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "Get_HISKS");
			call.addParameter(new QName(SOAPACTION,"SJKS"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{sjks}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【航天医院】门诊科室webservice接口失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【航天医院】门诊科室接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	
	/**
	 *  获取医院门诊挂号号表
	* @Title: getHisKS 
	* @param  deptNo  科室代号,registerTime 挂号日期；
	* @return String    返回类型 
	* @throws
	 */
	public Map<String, String> getHisRegister(String deptNo,String registerTime , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION,"Get_GHHB"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "Get_GHHB");
			call.addParameter(new QName(SOAPACTION,"HBRQ"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.addParameter(new QName(SOAPACTION,"SJKS"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{registerTime,deptNo}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【航天医院】门诊科室webservice接口失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【航天医院】门诊科室接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	
	/**
	 *  查询余额
	* @Title: getHisKS 
	* @param  deptNo  科室代号,registerTime 挂号日期；
	* @return String    返回类型 
	* @throws
	 */
	public Map<String, String> getHisBalance(String cardtype ,String cardno , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION,"Get_HIS_Balance"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "Get_HIS_Balance");
			call.addParameter(new QName(SOAPACTION,"cardtype"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.addParameter(new QName(SOAPACTION,"cardno"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{cardtype,cardno}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【航天医院】余额查询webservice接口失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【航天医院】余额查询接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/**
	 *  终端缴费
	* @Title: getHisKS 
	* @param  deptNo  科室代号,registerTime 挂号日期；
	* @return String    返回类型 
	* @throws
	 */
	public Map<String, String> getHisTerminalPay(String otherparams,String result , String message){
		//otherparams = "<XMLTable><XMLRec><OtherParams>10,K00000000,110101199001010015,张三,,,,,,,</OtherParams><CardType>02</CardType><YHC>K00000000|110101199001010015</YHC><UNM>张三</UNM><SEX>1</SEX><AGE>21</AGE><ZXL></ZXL><ZXD></ZXD><YYL>201507241911501400344248</YYL><YYD>20150724</YYD><JBS>1</JBS><TYPE>0</TYPE><TIM>AM</TIM><YYM>140034</YYM><ZDM>123</ZDM><QDM>1</QDM><FLOWID>8742</FLOWID><YYZF>20.00</YYZF><SJF>20.00</SJF><ZFLB>01</ZFLB><CHANNELTPYE>02</CHANNELTPYE><REGDATE>2015-07-29</REGDATE><DEPARTMENT>03</DEPARTMENT><JZH>6321022</JZH><SJF>0</SJF><XJZF>0</XJZF><ZHZF>0</ZHZF><TCZF>0</TCZF><ZPZF>0</ZPZF><SFLX>A001</SFLX><YHL></YHL><YHD></YHD><YHT></YHT><YHM></YHM></XMLRec></XMLTable>";
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION,"SendHisSal"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "SendHisSal");
			call.addParameter(new QName(SOAPACTION,"XmlData"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{otherparams}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【航天医院】终端缴费挂号webservice接口失败---SendHisSal" , e);
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【航天医院】终端缴费挂号接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	
	/**
	 *  终端取号（现场）
	* @Title: getHisGhbr 
	* @param  JZH  就诊号
	* @return String    返回类型 
	* @throws
	 */
	public Map<String, String> getHisGhbr(String JZH,String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION,"Get_HIS_GHBR"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "Get_HIS_GHBR");
			call.addParameter(new QName(SOAPACTION,"JZH"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{JZH}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【航天医院】 终端取号（现场）webservice接口失败---Get_HIS_GHBR"+e , e);
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【航天医院】终端取号（现场）接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
}
