package com.tecsun.sisp.net.modules.entity.request;

import com.tecsun.sisp.net.common.vo.faceverify.SecQueryBean;

/**
 * 详情入参
 *
 */
public class NoticeBean extends SecQueryBean {
	private long id;//根据id查看详情

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
}
