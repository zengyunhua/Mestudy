package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class SavePictureVo extends JobBasicVo {

	private String picName;
	private String picBase64Str;// 照片的base64字符串
	private String zjhm;// 证件号码、需编码
	private String sfzzplj;// 身份证照片路径

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicBase64Str() {
		return picBase64Str;
	}

	public void setPicBase64Str(String picBase64Str) {
		this.picBase64Str = picBase64Str;
	}

	public String getZjhm() {
		return zjhm;
	}

	public void setZjhm(String zjhm) {
		this.zjhm = zjhm;
	}

	public String getSfzzplj() {
		return sfzzplj;
	}

	public void setSfzzplj(String sfzzplj) {
		this.sfzzplj = sfzzplj;
	}

}
