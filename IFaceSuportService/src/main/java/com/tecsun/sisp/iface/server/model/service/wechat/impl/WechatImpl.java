package com.tecsun.sisp.iface.server.model.service.wechat.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.tecsun.sisp.iface.server.model.dao_net.WechatDao;
import com.tecsun.sisp.iface.server.model.service.wechat.WechatService;

/**
 * 微信职称查询业务接口实现
 *
 */
@Service
public class WechatImpl implements WechatService{

	@Autowired
	private WechatDao wechatDao;
	
	@Override
	public List<Map<String, Object>> getProfessionalTitle(String xm, String sfzh, String zsbh) {
		List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		//先判断是不是三个参数都非空
		int count = isValid(xm,sfzh,zsbh);
		if(count==3){
			//先来一波三个参数的查询
			mapList = wechatDao.getProfessionalTitle(xm, sfzh, zsbh);
			if(mapList == null || mapList.isEmpty()){
				//三个参数没有查询出来(因为有一批数据的身份证号是空的,数据库里面没有身份证号的数据)
				//将sfzh置空,再次去数据库查询
				mapList = wechatDao.getProfessionalTitle(xm, null, zsbh);
			}
			if(mapList == null || mapList.isEmpty()){
				//还是没有查询出来(可能是其他字段的数据没有,可能又这种情况...)
				//试着将xm置空,再次去数据库查询
				mapList = wechatDao.getProfessionalTitle(null, sfzh, zsbh);
			}
			if(mapList == null || mapList.isEmpty()){
				//还是没有查询出来(可能是其他字段的数据没有,可能又这种情况...)
				//试着将xm置空,再次去数据库查询
				mapList = wechatDao.getProfessionalTitle(xm, sfzh, null);
			}
		}else{
			//再来两个参数的(因为装载controller层已经判断了只有2个及2个以上的参数才能进行查询)
			mapList = wechatDao.getProfessionalTitle(xm, sfzh, zsbh);
		}
		
		return mapList;
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
