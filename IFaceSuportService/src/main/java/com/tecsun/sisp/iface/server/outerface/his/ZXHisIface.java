package com.tecsun.sisp.iface.server.outerface.his;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.HisZxRequstBean;
import com.tecsun.sisp.iface.server.util.Dom4JUtil;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import java.util.HashMap;
import java.util.Map;

/**
 * 中心医院接口  -- webservice接口
* @ClassName: HTHisIface 
*
 */
public class ZXHisIface {
	
	public static final Logger logger = Logger.getLogger(ZXHisIface.class);
	public static final String HTHIS_URL=Constants.ZXHOSWEBSERVICEURL;
	public static final String SOAPACTION = "http://tempuri.org/";
	public static final String SIGNCARD_URL= Constants.SIGNCARDURL;
	public static final String GETPATIENTINFO_URL= Constants.GETPATIENTINFOURL;
	
	/**
	 *  1.1 获取医院信息
	* @Title: getHospitalList
	* @return String    返回类型
	* @throws
	 */
	public Map<String, String> getHospitalList_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetHospitalList"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetHospitalList");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		/*	//wsResult = new String(new BASE64Decoder().decodeBuffer(wsResult.toString()));
			System.out.println("解密前" + wsResult + "===");
			//wsResult = new String(Base64.decodeBase64(wsResult.toString().getBytes()));
			wsResult = new String(new BASE64Decoder().decodeBuffer(wsResult.toString()));
			wsResult = new String(wsResult.getBytes(),"UTF8");
			System.out.println("解密后"+wsResult+"===");*/
		} catch (Exception e) {
			logger.error("---获取【中心医院】医院信息getdoctorlist接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】医院信息getdoctorlist接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;

	}

	/**
	 *  1.2 获取科室信息
	 * @Title: getDeptmentList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getDeptmentList_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetDeptmentList"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetDeptmentList");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】医院科室信息getDeptmentList接口失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】医院科室信息getDeptmentList接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.3 获取医生信息
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getDoctorList_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetDoctorList"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetDoctorList");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】医院医生信息GetDoctorList接口失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】医院医生信息GetDoctorList接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.4 获取指定医生的当前可约状
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getDoctorScheduleFlag_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetDoctorScheduleFlag"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetDoctorScheduleFlag");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】指定医生的当前可约状信息GetDoctorScheduleFlag接口失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】指定医生的当前可约状信息GetDoctorScheduleFlag接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	/**
	 *  1.5 取一个医生的排班列表
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getDoctorSchedule_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetDoctorSchedule"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetDoctorSchedule");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】取一个医生的排班列表接口GetDoctorSchedule失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】取一个医生的排班列表接口GetDoctorSchedule失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.6 获取具体日期门诊安排的号源情况(列表)
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getScheduleList_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetScheduleList"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetScheduleList");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】具体日期门诊安排的号源情况(列表)GetScheduleList失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】具体日期门诊安排的号源情况(列表)GetScheduleList失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.7 取一个日排班的分时信息
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getSchedulePartTime_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetSchedulePartTime"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetSchedulePartTime");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】一个日排班的分时信息GetSchedulePartTime失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】一个日排班的分时信息GetSchedulePartTime失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.8 生成挂号订单并申请锁号
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getNewOrder_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "NewOrder"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "NewOrder");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---生成【中心医院】挂号订单并申请锁号NewOrdere失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "生成【中心医院】挂号订单并申请锁号NewOrdere失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.9 查询锁号是否成功
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> checkLock_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "CheckLock"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "CheckLock");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("--- 查询【中心医院】锁号是否成功CheckLocke失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "查询【中心医院】锁号是否成功CheckLocke失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.10 更新订单信息
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> updateOrder_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "UpdateOrder"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "UpdateOrder");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---【中心医院】更新订单信息UpdateOrder失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "【中心医院】更新订单信息UpdateOrder失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.11 提交订单生效
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> confirmOrder_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "ConfirmOrder"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "ConfirmOrder");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---【中心医院】提交订单生效ConfirmOrder失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "【中心医院】提交订单生效ConfirmOrder失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.12 取历史订单列表
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getOrderInfo_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetOrderInfo"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetOrderInfo");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】历史订单列表GetOrderInfo失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】历史订单列表GetOrderInfo失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.13取消订单
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> cancelOrder_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "CancelOrder"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "CancelOrder");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---【中心医院】取消订单CancelOrder失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "【中心医院】取消订单CancelOrder失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.14确定退费
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> refund_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "Refund"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "Refund");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---【中心医院】确定退费CancelOrder失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "【中心医院】确定退费CancelOrder失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.15取短信文本
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getSms_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetSms"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetSms");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---【中心医院】取短信文本GetSms失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "【中心医院】取短信文本GetSms失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 *  1.16获取指定排班的当前可约状态
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getScheduleFlag_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetScheduleFlag"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetScheduleFlag");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】指定排班的当前可约状态GetScheduleFlag失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】指定排班的当前可约状态GetScheduleFlag失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	/**
	 @ 1.17   获取退费记录接口
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getRefundList_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetRefundList"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetRefundList");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】退费记录接口GetRefundList失败---  " , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】退费记录接口GetRefundList失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	

	/**
	 @ 1.18   提交订单生效（包含支付信息）
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> confirmOrderPay_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "ConfirmPayOrder"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "ConfirmPayOrder");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】退费记录接口GetRefundList失败---  " , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】退费记录接口GetRefundList失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/**
	 * 调用‘检查诊疗卡是否绑定’接口
	 * @param xml
	 * @param result
	 * @param message
	 * @return
	 */
	public Map<String, String> getCheckCardSign(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(SIGNCARD_URL);
			call.setOperationName(new QName(SOAPACTION, "CheckCardSign"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "CheckCardSign");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取检查诊疗卡是否绑定接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取检查诊疗卡是否绑定接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

	
	/**
	 * 获取'新患者HIS绑定接口'接口
	 * @param xml
	 * @param result
	 * @param message
	 * @return
	 */
	public Map<String, String> cardSignNewPatient(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(SIGNCARD_URL);
			call.setOperationName(new QName(SOAPACTION, "CardSignNewPatient"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "CardSignNewPatient");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("---获取新患者HIS绑定接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取新患者HIS绑定接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/**
	 * 获取患者信息
	 * @param xml
	 * @param result
	 * @param message
	 * @return
	 */
	public Map<String, String> getPatientInfo(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(GETPATIENTINFO_URL);
			call.setOperationName(new QName(SOAPACTION, "GetPatientInfo"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetPatientInfo");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("---获取新患者HIS绑定接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取新患者HIS绑定接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/**
	 * 【中心医院】现场挂号 -获取代缴费的单据
	 * @param xml
	 * @param result
	 * @param message
	 * @return
	 */
	public Map<String, String> getOutPatientPayBills_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetOutPatientPayBills"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetOutPatientPayBills");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】现场挂号代缴费的单据GetOutPatientPayBills接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】现场挂号代缴费的单据GetOutPatientPayBills接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/**
	 * 【中心医院】现场挂号-获取单据详情
	 * @param xml
	 * @param result
	 * @param message
	 * @return
	 */
	public Map<String, String> getOutPatientPayBillsDetial_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "GetOutPatientPayBillsDetial"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "GetOutPatientPayBillsDetial");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】现场挂号单据详情GetOutPatientPayBillsDetial接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】现场挂号单据详情GetOutPatientPayBillsDetial接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/**
	 * 【中心医院】现场挂号-单据缴费
	 * @param xml
	 * @param result
	 * @param message
	 * @return
	 */
	public Map<String, String> outpatientPayBills_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "OutpatientPayBills"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "OutpatientPayBills");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】现场挂号单据缴费OutpatientPayBills接口失败---"+e.getMessage(), e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】现场挂号单据缴费OutpatientPayBills接口失败"+e.getMessage();
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	
	/**
	 *  中心医院现场挂号-提交订单；
	 * @Title: getDoctorList
	 * @return String    返回类型
	 * @throws
	 */
	public Map<String, String> getRegisterConfirmOrder_zx(String xml , String result , String message){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(HTHIS_URL);
			call.setOperationName(new QName(SOAPACTION, "RegisterConfirmOrder"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "RegisterConfirmOrder");
			call.addParameter(new QName(SOAPACTION, "inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
			call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
		} catch (Exception e) {
			logger.error("---获取【中心医院】现场挂号提交订单getRegisterConfirmOrder_zx失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "获取【中心医院】现场挂号提交订单getRegisterConfirmOrder_zx失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/**
     * 中心医院现场挂号-取消订单
     * @param xml
     * @param result
     * @param message
     * @return
     */
     public Map<String, String> getRegisterCancelOrder_zx(String xml , String result , String message){
          Service service = new Service();
          Call call;
          String wsResult = "" ;
          Map<String , String > map = new HashMap<String , String>();
           try {
               call = (Call) service.createCall();
               call.setTargetEndpointAddress(HTHIS_URL);
               call.setOperationName( new QName(SOAPACTION, "RegisterCancelOrder" ));//设置要调用哪个方法
               call.setSOAPActionURI(SOAPACTION + "RegisterCancelOrder" );
               call.addParameter( new QName(SOAPACTION, "inputxml" ), XMLType.SOAP_STRING, ParameterMode.IN); //设置传入参数的属性
               call.setReturnType(XMLType.SOAP_STRING); //要返回的数据类型（自定义类型）
               wsResult = (String) call.invoke( new String[]{xml}) ;//调用方法并传递参数
          } catch (Exception e) {
               logger.error( "---获取【中心医院】现场挂号取消订单RegisterCancelOrder接口失败---" +e.getMessage(), e.getCause());
               result =  Constants.RESULT_MESSAGE_ERROR;
               message = "获取【中心医院】现场挂号取消订单RegisterCancelOrder接口失败" +e.getMessage();
          }
          map.put( "wsResult" , wsResult);
          map.put( "result" , result);
          map.put( "message" , message);
           return map;
    }

     /**
 	 * 中心医院现场挂号-创建挂号订单
 	 * @param xml
 	 * @param result
 	 * @param message
 	 * @return
 	 */	   
 		public Map<String, String> getRegisterNewOrder_zx(String xml , String result , String message){
 		Service service = new Service();
 		Call call;
 		String wsResult = "";
 		Map<String,String> map = new HashMap<String , String>();
 		try{
 			call = (Call)service.createCall();
 			call.setTargetEndpointAddress(HTHIS_URL);
 			call.setOperationName(new QName(SOAPACTION,"RegisterNewOrder"));
 			call.setSOAPActionURI(SOAPACTION + "RegisterNewOrder" );
 			call.addParameter(new QName(SOAPACTION,"inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);
 			call.setReturnType(XMLType.SOAP_STRING);
 			wsResult = (String)call.invoke(new String[]{xml});
 		}catch(Exception e){
 			e.printStackTrace();
 			 logger.error( "---获取【中心医院】现场挂号创建挂号订单getRegisterNewOrder_zx接口失败---" +e.getMessage(), e.getCause());
 			result = Constants.RESULT_MESSAGE_ERROR;
 			message = "获取【中心医院】现场挂号创建挂号订单getRegisterNewOrder_zx接口失败"+e.getMessage();		
 		}
 		map.put("wsResult", wsResult);
 		map.put("result", result);
 		map.put("message", message);
 		return map;
 	}
 		
 		
 		 /**
 	 	 * 中心医院现场挂号-获取科室列表
 	 	 * @param xml
 	 	 * @param result
 	 	 * @param message
 	 	 * @return
 	 	 */	   
 	 		public Map<String, String> getGetDeptmentList_zx(String xml , String result , String message){
 	 		Service service = new Service();
 	 		Call call;
 	 		String wsResult = "";
 	 		Map<String,String> map = new HashMap<String , String>();
 	 		try{
 	 			call = (Call)service.createCall();
 	 			call.setTargetEndpointAddress(HTHIS_URL);
 	 			call.setOperationName(new QName(SOAPACTION,"GetDeptmentList"));
 	 			call.setSOAPActionURI(SOAPACTION + "GetDeptmentList" );
 	 			call.addParameter(new QName(SOAPACTION,"inputxml"), XMLType.SOAP_STRING, ParameterMode.IN);
 	 			call.setReturnType(XMLType.SOAP_STRING);
 	 			wsResult = (String)call.invoke(new String[]{xml});
 	 		}catch(Exception e){
 	 			e.printStackTrace();
 	 			 logger.error( "---获取【中心医院】现场挂号获取科室列表getGetDeptmentList_zx接口失败---" +e.getMessage(), e.getCause());
 	 			result = Constants.RESULT_MESSAGE_ERROR;
 	 			message = "获取【中心医院】现场挂号获取科室列表getGetDeptmentList_zx接口失败"+e.getMessage();		
 	 		}
 	 		map.put("wsResult", wsResult);
 	 		map.put("result", result);
 	 		map.put("message", message);
 	 		return map;
 	 	}
 	
}
