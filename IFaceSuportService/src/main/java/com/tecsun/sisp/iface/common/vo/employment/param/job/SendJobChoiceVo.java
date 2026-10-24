package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月21日 上午11:40:30
 */
public class SendJobChoiceVo extends JobBasicVo {

	private String grxxid;// 个人信息id
	private String qyids;// 企业id列表
	private String gwids;// 岗位id列表
	private String xxly;// 信息来源 3:自助一体机
	private String xzjd;// 街道/乡镇
	private String zphid;// 招聘会id
	private String sblx;// 设备类型(3:自助一体机)

	public String getGrxxid() {
		return grxxid;
	}

	public void setGrxxid(String grxxid) {
		this.grxxid = grxxid;
	}

	public String getQyids() {
		return qyids;
	}

	public void setQyids(String qyids) {
		this.qyids = qyids;
	}

	public String getGwids() {
		return gwids;
	}

	public void setGwids(String gwids) {
		this.gwids = gwids;
	}

	public String getXxly() {
		return xxly;
	}

	public void setXxly(String xxly) {
		this.xxly = xxly;
	}

	public String getXzjd() {
		return xzjd;
	}

	public void setXzjd(String xzjd) {
		this.xzjd = xzjd;
	}

	public String getZphid() {
		return zphid;
	}

	public void setZphid(String zphid) {
		this.zphid = zphid;
	}

	public String getSblx() {
		return sblx;
	}

	public void setSblx(String sblx) {
		this.sblx = sblx;
	}
}
