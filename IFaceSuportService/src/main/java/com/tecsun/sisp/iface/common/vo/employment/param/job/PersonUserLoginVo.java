package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class PersonUserLoginVo extends JobBasicVo {

	private String dlid;// 登录id、需编码(不区分大小写)第一个字符必须为字母
	private String dlmm;// 登录密码、需md5加密(加密后的md5码统一转为大写)
	private String zjhm;// 证件号码、需编码(不区分大小写)
	private String sjhm;// 手机号码、需编码
	private String sjyzm;// 手机验证码、需编码(不区分大小写)
	private String dllx;// 登录类型(1:账号 2：证件号 3：手机号码)

	public String getDlid() {
		return dlid;
	}

	public void setDlid(String dlid) {
		this.dlid = dlid;
	}

	public String getDlmm() {
		return dlmm;
	}

	public void setDlmm(String dlmm) {
		this.dlmm = dlmm;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	public String getSjyzm() {
		return sjyzm;
	}

	public void setSjyzm(String sjyzm) {
		this.sjyzm = sjyzm;
	}

	public String getDllx() {
		return dllx;
	}

	public void setDllx(String dllx) {
		this.dllx = dllx;
	}
}
