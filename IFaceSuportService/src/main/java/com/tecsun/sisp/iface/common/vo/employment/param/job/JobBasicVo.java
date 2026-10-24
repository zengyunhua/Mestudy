package com.tecsun.sisp.iface.common.vo.employment.param.job;

import com.tecsun.sisp.iface.common.vo.BaseVO;

public class JobBasicVo extends BaseVO {

	private String deviceid;// 设备编码
	private String channelcode;// 接口类型型(如：大众端、网办、德生宝)
	private String channelType;// 接口类型(如：大众端-02、德生宝-01)

	private String pxzd;// 排序字段 多选、以逗号隔开,如(“QYMC,GWMC”)
	private String pxfs;// 排序方式 多选、以逗号隔开，如(“ASC,DESC”)
	private int ym;// 页码（从1开始）
	private int myhs;// 每页行数（大于0）

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getPxzd() {
		return pxzd;
	}

	public void setPxzd(String pxzd) {
		this.pxzd = pxzd;
	}

	public String getPxfs() {
		return pxfs;
	}

	public void setPxfs(String pxfs) {
		this.pxfs = pxfs;
	}

	public int getYm() {
		return ym;
	}

	public void setYm(int ym) {
		this.ym = ym;
	}

	public int getMyhs() {
		return myhs;
	}

	public void setMyhs(int myhs) {
		this.myhs = myhs;
	}

}
