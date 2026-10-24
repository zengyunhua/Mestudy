package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * 就业-注册信息，不能为空的字段
 * 
 */
public class NoNullRegisteVo {

	private String xm;// 姓名
	private String xbid;// 性别1：男2：女3：未说明性别
	private String sfzh;// 身份证号
	private String mz;// 民族
	private String jtzz;// 家庭住址
//	private String sfzzplj;// 身份证照片路径
	private String csny;// 出生年月yyyy-mm-dd
	private String sjhm;// 手机号码
	private String xldm;// 学历代码
	private String zzmmid;// 政治面貌
	private String hyzkid;// 婚姻状况
	private String jkzkid;// 健康状况
	private String zydm;// 专业
	private String zxzz;// 最新住址

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

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getJtzz() {
		return jtzz;
	}

	public void setJtzz(String jtzz) {
		this.jtzz = jtzz;
	}

	public String getCsny() {
		return csny;
	}

	public void setCsny(String csny) {
		this.csny = csny;
	}

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	public String getXldm() {
		return xldm;
	}

	public void setXldm(String xldm) {
		this.xldm = xldm;
	}

	public String getZzmmid() {
		return zzmmid;
	}

	public void setZzmmid(String zzmmid) {
		this.zzmmid = zzmmid;
	}

	public String getHyzkid() {
		return hyzkid;
	}

	public void setHyzkid(String hyzkid) {
		this.hyzkid = hyzkid;
	}

	public String getJkzkid() {
		return jkzkid;
	}

	public void setJkzkid(String jkzkid) {
		this.jkzkid = jkzkid;
	}

	public String getZydm() {
		return zydm;
	}

	public void setZydm(String zydm) {
		this.zydm = zydm;
	}

	public String getZxzz() {
		return zxzz;
	}

	public void setZxzz(String zxzz) {
		this.zxzz = zxzz;
	}

}
