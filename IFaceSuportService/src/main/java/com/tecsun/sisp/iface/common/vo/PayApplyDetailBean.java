package com.tecsun.sisp.iface.common.vo;

/**
 * 地税缴费明细
 *
 */
public class PayApplyDetailBean {
	  
	
	private String startDate ;//开始年月
	private String endDate ;//终止年月
	private String applyClass ;//申报档次
	private String applyDate ;//申报时间
	private String money;//绝对金额
	private String rate ;//费率
	private String feeType;//费种
	private String fee2;//费目
	private String budgetSubject;//预算科目
	private String org;//社保经办机构
	private String earaCode2;//区域代码

	private String checkMoney; //核定费额
	private String phone; //联系电话
	private String registDate; //登记日期
	private String sendStatus;  //批缴结果银行发送状况
	private String checkCode;//核定编号
	private String totalPayMoney;  //总缴费金额
	private String checkPayMoney;//核定缴费金额
	private String lateFee;//核定滞纳金
	private String payStartDate;//应缴缴费开始年月
	private String payEndDate;//应缴缴费结束年月
    private String payBase; //缴费基数
    private String checkDete; //核定日期
    private String electronicInvoiceNo;  //  电子税票号码
	public String getCheckMoney() {
		return checkMoney;
	}
	public void setCheckMoney(String checkMoney) {
		this.checkMoney = checkMoney;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRegistDate() {
		return registDate;
	}
	public void setRegistDate(String registDate) {
		this.registDate = registDate;
	}
	public String getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getCheckCode() {
		return checkCode;
	}
	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	public String getTotalPayMoney() {
		return totalPayMoney;
	}
	public void setTotalPayMoney(String totalPayMoney) {
		this.totalPayMoney = totalPayMoney;
	}
	public String getCheckPayMoney() {
		return checkPayMoney;
	}
	public void setCheckPayMoney(String checkPayMoney) {
		this.checkPayMoney = checkPayMoney;
	}
	public String getLateFee() {
		return lateFee;
	}
	public void setLateFee(String lateFee) {
		this.lateFee = lateFee;
	}
	public String getPayStartDate() {
		return payStartDate;
	}
	public void setPayStartDate(String payStartDate) {
		this.payStartDate = payStartDate;
	}
	public String getPayEndDate() {
		return payEndDate;
	}
	public void setPayEndDate(String payEndDate) {
		this.payEndDate = payEndDate;
	}
	public String getPayBase() {
		return payBase;
	}
	public void setPayBase(String payBase) {
		this.payBase = payBase;
	}
	public String getCheckDete() {
		return checkDete;
	}
	public void setCheckDete(String checkDete) {
		this.checkDete = checkDete;
	}
	public String getElectronicInvoiceNo() {
		return electronicInvoiceNo;
	}
	public void setElectronicInvoiceNo(String electronicInvoiceNo) {
		this.electronicInvoiceNo = electronicInvoiceNo;
	}
    
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getApplyClass() {
		return applyClass;
	}
	public void setApplyClass(String applyClass) {
		this.applyClass = applyClass;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getFee2() {
		return fee2;
	}
	public void setFee2(String fee2) {
		this.fee2 = fee2;
	}
	public String getBudgetSubject() {
		return budgetSubject;
	}
	public void setBudgetSubject(String budgetSubject) {
		this.budgetSubject = budgetSubject;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getEaraCode2() {
		return earaCode2;
	}
	public void setEaraCode2(String earaCode2) {
		this.earaCode2 = earaCode2;
	}
	

}
