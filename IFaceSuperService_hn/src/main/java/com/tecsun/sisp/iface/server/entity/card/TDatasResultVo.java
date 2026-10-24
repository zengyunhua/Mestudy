package com.tecsun.sisp.iface.server.entity.card;

import java.util.Date;

/**
 * 数据回盘Vo类
 *2017年10月14日
 *
 */
public class TDatasResultVo {
	
	
	private int dataid;
	private String username; //回盘人
	private Date resulttime;  //回盘时间
	private String idcardnum;  //身份证号
	private String sscardnum;  //社保卡号
	private String pboccardnum;  //银行卡号
	private String atr;    //复位信息
	private int flag;   //状态，0表示为制卡，1表示回盘成功
	private String reason; //制卡失败原因
	private String remark;  //备注
	public int getDataid() {
		return dataid;
	}
	public void setDataid(int dataid) {
		this.dataid = dataid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getResulttime() {
		return resulttime;
	}
	public void setResulttime(Date resulttime) {
		this.resulttime = resulttime;
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
	public String getPboccardnum() {
		return pboccardnum;
	}
	public void setPboccardnum(String pboccardnum) {
		this.pboccardnum = pboccardnum;
	}
	public String getAtr() {
		return atr;
	}
	public void setAtr(String atr) {
		this.atr = atr;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
