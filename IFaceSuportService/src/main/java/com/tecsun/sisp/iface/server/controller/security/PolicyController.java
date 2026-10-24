package com.tecsun.sisp.iface.server.controller.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonHelper;
import com.tecsun.sisp.iface.common.vo.Page;
import com.tecsun.sisp.iface.common.vo.PolicyVO;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.Xgweather;
import com.tecsun.sisp.iface.common.vo.postDataRequest;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.NetUserServiceImpl;
import com.tecsun.sisp.iface.server.model.service.ZhongJianServiceImpl;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import com.tecsun.sisp.iface.server.util.Config;

import java.net.URLConnection;
import java.net.URL;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.dom4j.Attribute;
import org.dom4j.Element;

import com.tecsun.sisp.iface.common.vo.PostData;

@Controller
@RequestMapping("/iface/policy")
public class PolicyController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(PolicyController.class);

	@Autowired
	private NetUserServiceImpl netUserService;

	
	@Autowired
    private ZhongJianServiceImpl zhongJianService;
	
	/**
	 * 查询政策法规标题
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/getPolicyTitle", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getPolicyTitle(@RequestBody PolicyVO bean) {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		Page<PolicyVO> page = new Page(bean.getPageno(), bean.getPagesize());
		String type = bean.getType(); // 政策类型
		String channelcode = bean.getChannelcode(); // 渠道编码
		if (channelcode != null && channelcode.equals("NetPortal")) {
			if (type != null && !type.equals("")) { 
				String [] types= {type};
				bean.setTypes(types);
			}
		} else if(channelcode != null && channelcode.equals("SelfService") || channelcode.equals("TSB")){
			if (type != null && type.equals("1")) { //1:劳动就业政策
				String [] types= {"31","32","33","34","35","36","37","38","39","10"};
				bean.setTypes(types);
			}else if(type != null && type.equals("2")){ //社会保险政策
				String [] types= {"11,12,13,14,15,16"}; 
				bean.setTypes(types);
			}else if(type != null && type.equals("3")){ //人事人才政策
				String [] types= {"23"};
				bean.setTypes(types);
			}else if(type != null && type.equals("4")){ //公务员管理政策
				String [] types= {"22"};
				bean.setTypes(types);
			}else if(type != null && type.equals("5")){ //劳动仲裁政策
				String [] types= {"20"};
				bean.setTypes(types);
			}else if(type != null && type.equals("6")){ //劳动监察政策
				String [] types= {"21"};
				bean.setTypes(types);
			}
		}
		try {
		List<PolicyVO> list = netUserService.getPolicyTitleList(bean);
        if (!list.isEmpty()) {
            for (PolicyVO vo : list) {
            	vo.setDistrict(DictionaryUtil.getDictName(Constants.DISTRICT_GROUP, bean.getDistrict()));
            }
        }
        page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			result = Constants.RESULT_MESSAGE_ERROR;
			return result(result, "查询政策法规信息失败", page);
		}
		page.setCount(netUserService.getPolicyTitleCount(bean));
		return result(result, "查询政策法规信息成功", page);
	}

	/**
	 * 查询法规内容
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/getPolicyContext", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getPolicyContext(@RequestBody PolicyVO bean) {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "查询政策法规信息成功";
		PolicyVO policyVO = null;
		try {
			policyVO=netUserService.getPolicyContext(bean);
		} catch (Exception e) {
			e.printStackTrace();
			result=Constants.RESULT_MESSAGE_ERROR;
			message = "查询政策法规信息失败";
			logger.error("查询政策法规内容出错：" + e);
		}
		return result(result, message, policyVO);
	}
	
	

	/**
	   * 
	   * @Title:解析xml获取天气
	   * @company :TECSUN
	   * @developer: ZENGYUNHUA
	   *	@version 1.0
	   */
	  	@RequestMapping(value = "/queryweather", method = RequestMethod.POST, produces = "application/json")
	    @ResponseBody
	   public Result queryweather(@RequestBody Xgweather bean)throws Exception{
	  		
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "获取成功!";
		String url = Config.getInstance().get("XIAOGANWEATHER");
		StringBuffer temp = new StringBuffer();
		URLConnection uc = new URL(url).openConnection();
		uc.setConnectTimeout(30000);
		uc.setReadTimeout(30000);
		uc.setDoOutput(true);
		InputStream in = new BufferedInputStream(uc.getInputStream());
		Reader rd = new InputStreamReader(in, "UTF-8");
		int c = 0;
		while ((c = rd.read()) != -1) {
			temp.append((char) c);
		}
		in.close();
		String strHtml = temp.toString();
		List<Xgweather> listWeath = new ArrayList<Xgweather>();
		// 将xml格式字符串转化为DOM对象
		Document document = DocumentHelper.parseText(strHtml);
		// 获取根结点对象
		Element rootElement = document.getRootElement();
		// 循环根节点，获取其子节点
		for (Iterator iter = rootElement.elementIterator(); iter.hasNext();) {
			Map<String, String> map = new HashMap<String, String>();
			Element element = (Element) iter.next(); // 获取标签对象
			// 获取该标签对象的属性
			List<Attribute> listAttr = element.attributes();// 当前节点的所有属性的list
			for (Attribute attr : listAttr) {// 遍历当前节点的所有属性
				map.put(attr.getName(), attr.getValue());

			}
			bean = new Xgweather();
			bean = JsonHelper.mapToJavaBean(map, Xgweather.class);
			listWeath.add(bean);

		}
		return this.result(result, message, listWeath.get(4));

	}
	  	
	  	
		/**
	  	 * 
	  	 * @Title:获取
	  	 * @company :TECSUN
	  	 * @developer: ZENGYUNHUA
	  	 *	@version 1.0
	  	 */
		@RequestMapping(value = "/querybyAuditTable", method = RequestMethod.POST, produces = "application/json")
	    @ResponseBody
	    public Result querybyAuditTable(@RequestBody postDataRequest bean)  throws Exception{
			String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			String message = "操作成功";
			
			try {
				
				Page<PostData> list =zhongJianService.querybyAuditTable(bean);
				
				if (list.getData() != null && list.getData().size() > 0) {
					logger.info("querybyAuditTable接口出参:\t..........."
							+ JsonHelper.javaBeanToJson(list));
					return result(statusCode, message, list);
				} else {
					return result(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
				}

			} catch (Exception e) {
				statusCode = Constants.RESULT_MESSAGE_ERROR;
				message = "获取失败";
				logger.error("数据出现异常", e);
			}

			return result(statusCode, message);
			
			
		}
	  	
		@RequestMapping(value = "/updateBjztAndBz", method = RequestMethod.POST, produces = "application/json")
	    @ResponseBody
	    public Result updateBjztAndBz(@RequestBody PostData bean)  throws Exception{

			String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			String message = "操作成功";
			
			
			int rows = zhongJianService.updateBjztAndBz(bean);	
			if(rows >0){
				result(statusCode,message);
				logger.info("STATUS OR Remarks Update Success!");
			}else{
				statusCode = Constants.RESULT_MESSAGE_ERROR;
				message = "STATUS OR Remarks Update ERROR!";
				
			}
			
			return result(statusCode, message);
		}
	  	
		
		
		
}

