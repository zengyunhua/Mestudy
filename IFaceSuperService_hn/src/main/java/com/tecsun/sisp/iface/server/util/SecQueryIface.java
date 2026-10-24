package com.tecsun.sisp.iface.server.util;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.log4j.Logger;

import javax.xml.namespace.QName;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import com.tecsun.sisp.iface.common.util.Config;
import com.tecsun.sisp.iface.common.util.Constants;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
public class SecQueryIface {
	
	public static final Logger logger = Logger.getLogger(SecQueryIface.class);
/*	public static final String JX_SNB_URL=Constants.INSUREURL;
	public static final String JX_SISERVICE_URL=Constants.SISERVICEURl;*/

	public static final String SOAPACTION = "http://xfire.codehaus.org/QueryService";
	
	/*@param bean
	 *@version 20151217
	 * 
	 * 个人历年缴费基数信息-queryPerPayBase
	 入参：
				参数名称	参数类别	      非空	 参数说明
				sfzhm	string(18)	Y	身份证号码
				xzbz	string(3)	Y	险种标志，code，XZBZ
				qsny	string(6)	N	起始年月，YYYYMM
				zzny	string(6)	N	终止年月，YYYYMM
				fwjss	string(20)	Y	服务接收商
				fwjsxtlx	string(3)	Y	服务接收系统类型

	 * */
	public static Map<String, String> queryPerPayBase(String contextXml,
			String result, String message,String method) {
		Map<String, String> map = new HashMap<String, String>();
		String wsResult = "";
	
		
		Config config = Config. getInstance();
        String urlStr = config.get("INSUREURL");
        
        
    	HttpClient httpClient = new HttpClient();
		//PostMethod postMethod = new PostMethod(JX_SNB_URL);
    	PostMethod postMethod = new PostMethod(urlStr);
    	
    	
    //	System.out.println("config.properites:"+urlStr+"====Constats:"+JX_SNB_URL);
		
		try {
			byte[] bytes = contextXml.getBytes("utf-8");

			InputStream inputStream = new ByteArrayInputStream(bytes, 0,bytes.length);
			RequestEntity requestEntity = new InputStreamRequestEntity(inputStream, bytes.length,"application/soap+xml; charset=utf-8");
			postMethod.setRequestEntity(requestEntity);
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(200000);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(900000);
			int statusCode = httpClient.executeMethod(postMethod);
			if (statusCode == 200) {// 结果报文
				wsResult = postMethod.getResponseBodyAsString();
				result = Constants.RESULT_MESSAGE_SUCCESS;
			}else{
				result = Constants.RESULT_MESSAGE_ERROR;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("调用个"+method+"接口失败"+e.getMessage());
			message = "调用"+method+"接口失败";
			result = Constants.RESULT_MESSAGE_ERROR;
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	/*@param bean
	 *@version 20151217
	 * 
	 * 医疗住院相关接口-querySiServicePerPayBase
	 入参：
	 * */
	public static Map<String, String> querySiServicePerPayBase(String contextXml,
			String result, String message,String method,String serverNmae) {
		Map<String, String> map = new HashMap<String, String>();
		String wsResult = "";
		try {
			RPCServiceClient serviceClient = new RPCServiceClient();
			Options options = serviceClient.getOptions();// 固定
			options.setTimeOutInMilliSeconds(60000L);// TODO:根据实际情况设置超时时间
			// TODO：根据实际情况修改服务地址。
			//EndpointReference targetEPR = new EndpointReference(JX_SISERVICE_URL);
			
			Config config = Config. getInstance();
	        String urlStr = config.get("AYHISTORYULR");
			EndpointReference targetEPR = new EndpointReference(urlStr);
			
			
	       // System.out.println("config.properites:"+urlStr+"====Constats:"+JX_SISERVICE_URL);
			
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			logger.error("调用个"+method+"接口，创建RPCServiceClient失败"+e1.getMessage());
			message = "调用"+method+"接口，创建RPCServiceClient失败";
			result = Constants.RESULT_MESSAGE_ERROR;
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}
	
	
}
