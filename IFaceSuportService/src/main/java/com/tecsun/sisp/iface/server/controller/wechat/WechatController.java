package com.tecsun.sisp.iface.server.controller.wechat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.wechat.WechatService;

/**
 * <p>微信职称查询</p>
 */
@RestController
@RequestMapping("/iface/wechat")
public class WechatController extends BaseController {

	@Autowired
	private WechatService professionalTitleService;
	
	@RequestMapping(value="/getProfessionalTitle",method=RequestMethod.POST)
	public Result getProfessionalTitle(@RequestBody Zzz zzz) throws UnsupportedEncodingException{
		//判断参数是否为空,(至少又两个不为空才能查询,某一批数据没有身份证号)
		// 开始判断参数是否正确,至少要两个参数不为空,先去一波空格再说...
		
		String xm = zzz.getXm();
		String sfzh = zzz.getSfzh();
		String zsbh = zzz.getZsbh();
		
		xm  = xm ==null? xm:xm.trim();
		sfzh  = sfzh ==null? sfzh:sfzh.trim();
		zsbh  = zsbh ==null? zsbh:zsbh.trim();
		
		int count = isValid(xm,sfzh,zsbh);
		
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		String statusCode;
		String message;
		if(count>=2){
			mapList = professionalTitleService.getProfessionalTitle(xm,sfzh,zsbh);

			if(mapList==null || mapList.isEmpty()){
				statusCode = "0";
				message = "查无数据，如有问题请咨询0712-12333。" ;
			}else{
				statusCode = "200";
				message = "success" ;
			}
		}else{
			statusCode = "400";
			message = "参数错误,请至少输入两项!";
		}

		return new Result(statusCode,message,mapList);
	}
	
	
	/**
	 * 判断传过来的字符串有几个空
	 * @param values
	 * @return
	 */
	private int isValid(String ...values){
		int count = 0;
		for(String str : values){
			if(!StringUtils.isEmpty(str)){
				count ++;
			}
		}
		return count;
		
	}
	
}
