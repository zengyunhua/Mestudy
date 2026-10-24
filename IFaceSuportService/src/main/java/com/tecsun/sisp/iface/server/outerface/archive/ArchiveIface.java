package com.tecsun.sisp.iface.server.outerface.archive;

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
 * 档案查询接口  -- webservice接口
* @ClassName: ArchiveIface
* @date 2016年9月13日
*
 */
public class ArchiveIface {
	
	public static final Logger logger = Logger.getLogger(ArchiveIface.class);
	public static final String Archive_URL=Constants.ArchiveURL;
	public static final String SOAPACTION = "http://webservice.apps.lemis.neusoft.com/";
	

	
	
	/**
	 *  查询档案
	* @Title: getArchive
	* @param  xml 入参的xml
     *            结构编号,
     *               aac002 身份证号 acc709 接收日期起始 acc709_1 接收日期截止
     *               acc70b 入库状态 aaef036 最后一次修改时间 aaf036_1 最后一次修改时间截止
     *               ace750 数据更新标识
	* @return String    返回类型
	* @throws
     * ,String aac002 , String acc709 , String acc709_1,String acc70b,String aaef036,String aaef036_1,String ace750
	 */
	public Map<String,String> getArchiveInfo(String xml ){
		Service service = new Service();
		Call call;
		String wsResult = "";
		Map<String , String > map = new HashMap<String , String>();
        String result=Constants.RESULT_MESSAGE_SUCCESS;
        String message="查询成功";
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(Archive_URL);
			call.setOperationName(new QName(SOAPACTION, "StorageQuery"));//设置要调用哪个方法
			call.setSOAPActionURI(SOAPACTION + "StorageQuery");
            call.addParameter(new QName(SOAPACTION, "xml"), XMLType.SOAP_STRING, ParameterMode.IN);//设置传入参数的属性
            call.setReturnType(XMLType.SOAP_STRING);//要返回的数据类型（自定义类型）
			wsResult = (String) call.invoke(new String[]{xml}) ;//调用方法并传递参数
        } catch (Exception e) {
			logger.error("---访问【档案查询】档案信息查询webservice接口失败---" , e.getCause());
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "访问【档案查询】档案信息查询接口失败";
		}
		map.put("wsResult", wsResult);
		map.put("result", result);
		map.put("message", message);
		return map;
	}

}
