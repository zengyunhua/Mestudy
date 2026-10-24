package com.tecsun.sisp.iface.common.vo;

/**
 * 地税缴费信息
 *
 */
public class PayApplyBean {
	  
	private String statuCode;  //响应码
	private String desc; //响应码描述
	private String dealCode; //交易码
	private String registNo; //社会保险登记号
	private String cernum; //证件号码
	private String name; //姓名
	private String dealDate; //交易日期
	private String bankSeq;//银行流水号
	private String bankNetNo; //银行网点号
	private String bankAcceptNo;//银行受理号
	private String bankTailNo;//银行尾箱号
	private String earaCode1;//区域代码
	private String fee1;//费目
	private String totalMoney; //核定总费额
	private String applyData;//劳动的申报数据
//	private List<PayApplyDetailBean> detail;
	private PayApplyDetailBean[] beans;
	public String getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public PayApplyDetailBean[] getBeans() {
		return beans;
	}
	public void setBeans(PayApplyDetailBean[] beans) {
		this.beans = beans;
	}

	public String getStatuCode() {
		return statuCode;
	}
	public void setStatuCode(String statuCode) {
		this.statuCode = statuCode;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getDealCode() {
		return dealCode;
	}
	public void setDealCode(String dealCode) {
		this.dealCode = dealCode;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getCernum() {
		return cernum;
	}
	public void setCernum(String cernum) {
		this.cernum = cernum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public String getBankSeq() {
		return bankSeq;
	}
	public void setBankSeq(String bankSeq) {
		this.bankSeq = bankSeq;
	}
	public String getBankNetNo() {
		return bankNetNo;
	}
	public void setBankNetNo(String bankNetNo) {
		this.bankNetNo = bankNetNo;
	}
	public String getBankAcceptNo() {
		return bankAcceptNo;
	}
	public void setBankAcceptNo(String bankAcceptNo) {
		this.bankAcceptNo = bankAcceptNo;
	}
	public String getBankTailNo() {
		return bankTailNo;
	}
	public void setBankTailNo(String bankTailNo) {
		this.bankTailNo = bankTailNo;
	}
	public String getEaraCode1() {
		return earaCode1;
	}
	public void setEaraCode1(String earaCode1) {
		this.earaCode1 = earaCode1;
	}
	public String getFee1() {
		return fee1;
	}
	public void setFee1(String fee1) {
		this.fee1 = fee1;
	}
	public String getApplyData() {
		return applyData;
	}
	public void setApplyData(String applyData) {
		this.applyData = applyData;
	}
	
}
