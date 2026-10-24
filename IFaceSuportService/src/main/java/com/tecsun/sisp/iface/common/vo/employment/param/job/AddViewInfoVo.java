package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月22日 下午2:29:23
 */
public class AddViewInfoVo extends JobBasicVo {

	private String sblx;// 设备类型 3:自助一体机
	private String ip;// ip地址
	private String mknr;// 模块内容
	private String yhlx;// 用户类型 1.求职者 2.企业 3.游客
	private String czlx;// 操作类型 1.查看 2.添加 3.删除 4.修改
	private String fzid;// 分组id
	private String sbbs;// 设备标识

	public String getSblx() {
		return sblx;
	}

	public void setSblx(String sblx) {
		this.sblx = sblx;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMknr() {
		return mknr;
	}

	public void setMknr(String mknr) {
		this.mknr = mknr;
	}

	public String getYhlx() {
		return yhlx;
	}

	public void setYhlx(String yhlx) {
		this.yhlx = yhlx;
	}

	public String getCzlx() {
		return czlx;
	}

	public void setCzlx(String czlx) {
		this.czlx = czlx;
	}

	public String getFzid() {
		return fzid;
	}

	public void setFzid(String fzid) {
		this.fzid = fzid;
	}

	public String getSbbs() {
		return sbbs;
	}

	public void setSbbs(String sbbs) {
		this.sbbs = sbbs;
	}

}
