package com.tecsun.sisp.iface.server.entity.card;

import java.util.Date;

/**
 * 数据提取记录表VO类
 *2017年10月13日
 *
 */
public class TTakeDatasVO {
	
	private int dataid;
	private String username;  // 提取人
	private Date taketime;  //提取时间
	private String idcardnum; //身份证号
	private String sscardnum;  //社保卡号
	private String pboccardnum;  //银行卡号
	private String atr;     //复位信息
	private int flag;   // 状态
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
	public Date getTaketime() {
		return taketime;
	}
	public void setTaketime(Date taketime) {
		this.taketime = taketime;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
