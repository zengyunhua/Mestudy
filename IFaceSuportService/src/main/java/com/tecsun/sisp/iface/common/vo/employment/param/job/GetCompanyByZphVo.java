package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class GetCompanyByZphVo extends JobBasicVo {

	private String zphid;// 招聘会id
	private String qyshzt;// 企业审核状态，多选、以逗号隔开,
	private String zphqyshzt;// 招聘会企业审核状态

	public String getZphid() {
		return zphid;
	}

	public void setZphid(String zphid) {
		this.zphid = zphid;
	}

	public String getQyshzt() {
		return qyshzt;
	}

	public void setQyshzt(String qyshzt) {
		this.qyshzt = qyshzt;
	}

	public String getZphqyshzt() {
		return zphqyshzt;
	}

	public void setZphqyshzt(String zphqyshzt) {
		this.zphqyshzt = zphqyshzt;
	}
}
