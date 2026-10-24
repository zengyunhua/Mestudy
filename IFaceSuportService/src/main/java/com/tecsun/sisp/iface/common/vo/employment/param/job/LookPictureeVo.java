package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class LookPictureeVo extends JobBasicVo {

	private String tpdz;// 图片地址
	private String tplx;// 类型（1：一般图片，2：企业图片）

	public String getTpdz() {
		return tpdz;
	}

	public void setTpdz(String tpdz) {
		this.tpdz = tpdz;
	}

	public String getTplx() {
		return tplx;
	}

	public void setTplx(String tplx) {
		this.tplx = tplx;
	}

}
