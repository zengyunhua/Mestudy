package com.tecsun.sisp.net.modules.entity.request;

import com.tecsun.sisp.net.common.vo.faceverify.SecQueryBean;

/**
 * 根据一级编码查询二级编码、名称的入参
 *
 */
public class DictionaryCodeBean extends SecQueryBean {
	private String groupId;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}
