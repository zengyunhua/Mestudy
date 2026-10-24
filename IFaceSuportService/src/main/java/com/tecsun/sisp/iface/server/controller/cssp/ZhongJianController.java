package com.tecsun.sisp.iface.server.controller.cssp;


import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.PostData;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.ZhongJianServiceImpl;


@Controller
@RequestMapping(value = "/iface/zhongjian")
public class ZhongJianController extends BaseController{
	 private static Logger logger = LoggerFactory.getLogger(ZhongJianController.class);
	
	 	@Autowired
	    private ZhongJianServiceImpl zhongJianService;
	
	/**
	 * 
	 * @Title:基本养老获取
	 * @company :TECSUN
	 * @developer: ZENGYUNHUA
	 *	@version 1.0
	 */
	  	@RequestMapping(value = "/endowmentinsurance", method = RequestMethod.POST, produces = "application/json")
	    @ResponseBody
	public Result barcode(@RequestBody PostData bean)  throws Exception{
		logger.info("########正在调用方法#######");
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功!";
		if (StringUtils.isBlank(bean.getSxbm()))
			return result(Constants.RESULT_MESSAGE_ERROR, "参数不能为空!");
		try {
			
		JSONObject jsonObj = JSONObject.fromObject(bean.getSxbm());// 获取json对象
		Object b = JSONObject.toBean(jsonObj, PostData.class);
		PostData pdf = (PostData) b;
		int addBarcode = zhongJianService.addBarcode(pdf);	
		if(addBarcode>0){
			logger.info("SUCCESS__DB!");
			result= Constants.RESULT_MESSAGE_SUCCESS;
		}else {
			logger.error("ERROR!__NOT DB!");
			result =Constants.RESULT_MESSAGE_ERROR;
		}
		} catch (Exception e) {
			// TODO: handle exception
			message = "解析出现异常!";
			logger.error("****RUN TIME EXCEPTION****!");
			result = Constants.RESULT_MESSAGE_ERROR;
			e.printStackTrace();
		}
		return result(result, message);
		
		
	  	}
	  	
	  	
	  
}
