package com.tecsun.sisp.iface.common.vo.his.zx;

/**
 * //中心医院--当日挂号、门诊缴费、挂号订单返回对象
 *
 */
public class ZhongxinHisSceRegPO {
	private String is_check_bill; //单据是否可选
	private String flowNo; //单据号码
	private String doDeptCode; //科室代码
	private String doDeptName; //科室名称
	private String billType; //单据类型
	private String billName; //单据名称
	private String billDes; //单据描述
	private String billTime; //开单时间
	private String billMoney; //项目金额
	private String canDelayPay; //可否后付费
	private String setDeptCode; //开单科室代码
	private String setDeptName; //开单科室名称
	private String deptLocation; //执行科室地址
	private String checkBill; //是否选中
	private String arcptNo; //收据号
	private String ext1; //扩展信息 1
	private String ext2; //扩展信息 2
	private String ext3; //扩展信息 3
	private String costName; //费用名称
	private String costSum; //费用数量
	private String unit; //单位
	private String amount; //金额
	private String payBatchNo; //批次号
	private String payFlowNo; //HIS 交易流水
	private String displayWin; //窗口号
	private String winCount; //排队人数
	private String alertMsg; //温馨提示
	private String reqReserved; //透传信息
	
	private int returncode;//返回代码 int 1：成功， -1 失败
	private String errormsg;//出错消息
	private String orderId;// 订单 ID
	private String pinKey;// 数字订单号 
	private String orderTime;// 订 单 生 成 时 间 yyyy-mm-dd hh24:mm:ss
	
	private String hospitalid;//医院代码
	private String hospitalname;//医院名称
	private String deptcode;//科室代码
	private String deptname;//科室名称
	private String doctorno;//医生编号
	private String inputcode;//输入码
	private String doctorname;//医生姓名
	private String doctortitle;//职务职称
	private String specialty;//疾病专长描述
	private String doctorsmallpicurl;//医生图片(小)
	private String doctormiddlepicurl;//医生图片(中)
	private String doctorbigpicurl;//医生图片(大)
	private String doctordes;//医生简介
	private String registerdate;//出诊日期
	private String timeInterval;//时间段
	private String registerFee;//挂号费
	private String clinicFee;//治疗费
	private String regFlag;//可挂状态
	private String extInfo;//扩展信息（带格式） 
	
	public String getHospitalid() {
		return hospitalid;
	}
	public void setHospitalid(String hospitalid) {
		this.hospitalid = hospitalid;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getDoctorno() {
		return doctorno;
	}
	public void setDoctorno(String doctorno) {
		this.doctorno = doctorno;
	}
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getDoctortitle() {
		return doctortitle;
	}
	public void setDoctortitle(String doctortitle) {
		this.doctortitle = doctortitle;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getDoctorsmallpicurl() {
		return doctorsmallpicurl;
	}
	public void setDoctorsmallpicurl(String doctorsmallpicurl) {
		this.doctorsmallpicurl = doctorsmallpicurl;
	}
	public String getDoctormiddlepicurl() {
		return doctormiddlepicurl;
	}
	public void setDoctormiddlepicurl(String doctormiddlepicurl) {
		this.doctormiddlepicurl = doctormiddlepicurl;
	}
	public String getDoctorbigpicurl() {
		return doctorbigpicurl;
	}
	public void setDoctorbigpicurl(String doctorbigpicurl) {
		this.doctorbigpicurl = doctorbigpicurl;
	}
	public String getDoctordes() {
		return doctordes;
	}
	public void setDoctordes(String doctordes) {
		this.doctordes = doctordes;
	}
	public String getRegisterdate() {
		return registerdate;
	}
	public void setRegisterdate(String registerdate) {
		this.registerdate = registerdate;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	public String getRegisterFee() {
		return registerFee;
	}
	public void setRegisterFee(String registerFee) {
		this.registerFee = registerFee;
	}
	public String getClinicFee() {
		return clinicFee;
	}
	public void setClinicFee(String clinicFee) {
		this.clinicFee = clinicFee;
	}
	public String getRegFlag() {
		return regFlag;
	}
	public void setRegFlag(String regFlag) {
		this.regFlag = regFlag;
	}
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	public int getReturncode() {
		return returncode;
	}
	public void setReturncode(int returncode) {
		this.returncode = returncode;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPinKey() {
		return pinKey;
	}
	public void setPinKey(String pinKey) {
		this.pinKey = pinKey;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	
	
	public String getIs_check_bill() {
		return is_check_bill;
	}
	public void setIs_check_bill(String is_check_bill) {
		this.is_check_bill = is_check_bill;
	}
	public String getFlowNo() {
		return flowNo;
	}
	public void setFlowNo(String flowNo) {
		this.flowNo = flowNo;
	}
	public String getDoDeptCode() {
		return doDeptCode;
	}
	public void setDoDeptCode(String doDeptCode) {
		this.doDeptCode = doDeptCode;
	}
	public String getDoDeptName() {
		return doDeptName;
	}
	public void setDoDeptName(String doDeptName) {
		this.doDeptName = doDeptName;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	public String getBillDes() {
		return billDes;
	}
	public void setBillDes(String billDes) {
		this.billDes = billDes;
	}
	public String getBillTime() {
		return billTime;
	}
	public void setBillTime(String billTime) {
		this.billTime = billTime;
	}
	public String getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(String billMoney) {
		this.billMoney = billMoney;
	}
	public String getCanDelayPay() {
		return canDelayPay;
	}
	public void setCanDelayPay(String canDelayPay) {
		this.canDelayPay = canDelayPay;
	}
	public String getSetDeptCode() {
		return setDeptCode;
	}
	public void setSetDeptCode(String setDeptCode) {
		this.setDeptCode = setDeptCode;
	}
	public String getSetDeptName() {
		return setDeptName;
	}
	public void setSetDeptName(String setDeptName) {
		this.setDeptName = setDeptName;
	}
	public String getDeptLocation() {
		return deptLocation;
	}
	public void setDeptLocation(String deptLocation) {
		this.deptLocation = deptLocation;
	}
	public String getCheckBill() {
		return checkBill;
	}
	public void setCheckBill(String checkBill) {
		this.checkBill = checkBill;
	}
	public String getArcptNo() {
		return arcptNo;
	}
	public void setArcptNo(String arcptNo) {
		this.arcptNo = arcptNo;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getExt3() {
		return ext3;
	}
	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public String getCostSum() {
		return costSum;
	}
	public void setCostSum(String costSum) {
		this.costSum = costSum;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPayBatchNo() {
		return payBatchNo;
	}
	public void setPayBatchNo(String payBatchNo) {
		this.payBatchNo = payBatchNo;
	}
	public String getPayFlowNo() {
		return payFlowNo;
	}
	public void setPayFlowNo(String payFlowNo) {
		this.payFlowNo = payFlowNo;
	}
	public String getDisplayWin() {
		return displayWin;
	}
	public void setDisplayWin(String displayWin) {
		this.displayWin = displayWin;
	}
	public String getWinCount() {
		return winCount;
	}
	public void setWinCount(String winCount) {
		this.winCount = winCount;
	}
	public String getAlertMsg() {
		return alertMsg;
	}
	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}
	public String getReqReserved() {
		return reqReserved;
	}
	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}
	
}
