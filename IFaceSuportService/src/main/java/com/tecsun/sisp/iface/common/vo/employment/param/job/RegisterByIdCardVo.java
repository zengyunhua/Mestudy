package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class RegisterByIdCardVo extends JobBasicVo {
	private String sfzh;// 身份证号、需编码
	private String xm;// 姓名、需编码
	private String xbid;// 性别1：男2：女3：未说明性别
	private String mz;// 民族、需编码
	private String mzdm;// 民族代码
	private String csny;// 出生年月yyyy-mm-dd
	private String jtzz;// 家庭住址、需编码
	private String zxzz;// 最新住址、需编码
	private String qfjg;// 签发机关、需编码
	private String qssj;// 起始时间yyyy-mm-dd
	private String jssj;// 结束时间yyyy-mm-dd
	private String sfzzplj;// 身份证照片路径

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getXbid() {
		return xbid;
	}

	public void setXbid(String xbid) {
		this.xbid = xbid;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getMzdm() {
		return mzdm;
	}

	public void setMzdm(String mzdm) {
		this.mzdm = mzdm;
	}

	public String getCsny() {
		return csny;
	}

	public void setCsny(String csny) {
		this.csny = csny;
	}

	public String getJtzz() {
		return jtzz;
	}

	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}

	public String getZxzz() {
		return zxzz;
	}

	public void setZxzz(String zxzz) {
		this.zxzz = zxzz;
	}

	public String getQfjg() {
		return qfjg;
	}

	public void setQfjg(String qfjg) {
		this.qfjg = qfjg;
	}

	public String getQssj() {
		return qssj;
	}

	public void setQssj(String qssj) {
		this.qssj = qssj;
	}

	public String getJssj() {
		return jssj;
	}

	public void setJssj(String jssj) {
		this.jssj = jssj;
	}

	public String getSfzzplj() {
		return sfzzplj;
	}

	public void setSfzzplj(String sfzzplj) {
		this.sfzzplj = sfzzplj;
	}

}
