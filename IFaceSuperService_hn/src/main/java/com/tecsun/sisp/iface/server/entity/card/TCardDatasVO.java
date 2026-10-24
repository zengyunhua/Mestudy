package com.tecsun.sisp.iface.server.entity.card;

import java.util.Date;

/**
 * 制卡数据VO类
 *2017年10月13日
 *
 */
public class TCardDatasVO {

   private int dataid;
   private String idcardnum; //身份证号
   private String sscardnum; //社保卡号
   private String pboccardnum; //银行卡号
   private String ordernum; //订单号
   private String atr; //复位信息
   private String pbocdatas; //金融数据
   private String ssdatas; //社保数据
   private int flag;           //状态，0表示为制卡，1表示回盘成功
   private String remark; // 备注
   private String author;  //作者
   private Date operatime;  //操作时间
   private String extend1; //扩展字段1
   private String extend2;  //扩展字段2
   
   private String channelcode;//渠道编码
   private String deviceid;//设备编码
   private String reason;//制卡失败原因；
   
   
   
   

public String getReason() {
	return reason;
}

public void setReason(String reason) {
	this.reason = reason;
}

public String getChannelcode() {
	return channelcode;
}

public void setChannelcode(String channelcode) {
	this.channelcode = channelcode;
}

public String getDeviceid() {
	return deviceid;
}

public void setDeviceid(String deviceid) {
	this.deviceid = deviceid;
}

public int getDataid() {
	return dataid;
}

public void setDataid(int dataid) {
	this.dataid = dataid;
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
public String getOrdernum() {
	return ordernum;
}
public void setOrdernum(String ordernum) {
	this.ordernum = ordernum;
}
public String getAtr() {
	return atr;
}
public void setAtr(String atr) {
	this.atr = atr;
}
public String getPbocdatas() {
	return pbocdatas;
}
public void setPbocdatas(String pbocdatas) {
	this.pbocdatas = pbocdatas;
}
public String getSsdatas() {
	return ssdatas;
}
public void setSsdatas(String ssdatas) {
	this.ssdatas = ssdatas;
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
public String getAuthor() {
	return author;
}
public void setAuthor(String author) {
	this.author = author;
}
public Date getOperatime() {
	return operatime;
}
public void setOperatime(Date operatime) {
	this.operatime = operatime;
}
public String getExtend1() {
	return extend1;
}
public void setExtend1(String extend1) {
	this.extend1 = extend1;
}
public String getExtend2() {
	return extend2;
}
public void setExtend2(String extend2) {
	this.extend2 = extend2;
}
   
   

}
