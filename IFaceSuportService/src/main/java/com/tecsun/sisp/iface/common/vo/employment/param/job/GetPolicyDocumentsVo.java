package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class GetPolicyDocumentsVo extends JobBasicVo {

	private String id;// 政策文件id
	private String ptlx;// 平台类型(1：查询平台)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPtlx() {
		return ptlx;
	}

	public void setPtlx(String ptlx) {
		this.ptlx = ptlx;
	}

}
