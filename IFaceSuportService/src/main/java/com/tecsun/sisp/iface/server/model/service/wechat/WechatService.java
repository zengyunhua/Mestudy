package com.tecsun.sisp.iface.server.model.service.wechat;

import java.util.List;
import java.util.Map;

/**
 * 微信职称查询业务接口
 *
 */
public interface WechatService {

	/**
	 * 职称查询
	 * @param xm 姓名
	 * @param sfzh 身份证号
	 * @param zsbh 证书编码
	 * @return
	 */
	List<Map<String, Object>> getProfessionalTitle(String xm, String sfzh, String zsbh);

}
