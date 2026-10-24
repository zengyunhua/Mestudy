package com.tecsun.sisp.iface.server.entity.card;

import java.util.Date;

/**
 * 日志信息表VO
 *2017年10月13日
 *
 */
public class TLogsVO {
	
	private int logid;
	private String devicesn; //终端设备
	private String  idcardnum; //身份证号
	private String sscardnum; //社保卡号
	private Date operatime; //操作时间
	private int logtype; //日志类型
	private String devicetype;//终端类型
	private String frommethod; //接口备注
	private String contenttext;//失败原因
	
	
	public String getContenttext() {
		return contenttext;
	}
	public void setContenttext(String contenttext) {
		this.contenttext = contenttext;
	}
	public int getLogid() {
		return logid;
	}
	public void setLogid(int logid) {
		this.logid = logid;
	}
	public String getDevicesn() {
		return devicesn;
	}
	public void setDevicesn(String devicesn) {
		this.devicesn = devicesn;
	}
	public String getIdcardnum() {
		return idcardnum;
	}
	public void setIdcardnum(String idcardnum) {
		this.idcardnum = idcardnum;
	}
	public String getSscardnum() {
		return sscardnum;
	}
	public void setSscardnum(String sscardnum) {
		this.sscardnum = sscardnum;
	}
	public Date getOperatime() {
		return operatime;
	}
	public void setOperatime(Date operatime) {
		this.operatime = operatime;
	}
	public int getLogtype() {
		return logtype;
	}
	public void setLogtype(int logtype) {
		this.logtype = logtype;
	}
	public String getDevicetype() {
		return devicetype;
	}
	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}
	public String getFrommethod() {
		return frommethod;
	}
	public void setFrommethod(String frommethod) {
		this.frommethod = frommethod;
	}

	

}