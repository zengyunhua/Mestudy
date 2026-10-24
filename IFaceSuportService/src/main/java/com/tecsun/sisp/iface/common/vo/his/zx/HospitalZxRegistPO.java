package com.tecsun.sisp.iface.common.vo.his.zx;

/**
 * 医院挂号号表
 * 
 * @ClassName: HospitalRegistPO
 * @date 2015年6月18日 下午12:09:43
 *
 */
public class HospitalZxRegistPO {

	private String ScheduleId;//日排班 ID
	private String OutpDate;//日期

	private String ClinicTypeName;//专家号;
	public String getScheduleId() {
		return ScheduleId;
	}
	public void setScheduleId(String scheduleId) {
		ScheduleId = scheduleId;
	}
	public String getOutpDate() {
		return OutpDate;
	}
	public void setOutpDate(String outpDate) {
		OutpDate = outpDate;
	}

	private String TimeInterval;//时间段

	public String getTimeInterval() {
		return TimeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		TimeInterval = timeInterval;
	}

	public String getClinicTypeName() {
		return ClinicTypeName;
	}
	public void setClinicTypeName(String clinicTypeName) {
		ClinicTypeName = clinicTypeName;
	}
	private String ClinicFee;//诊疗费

	public String getClinicFee() {
		return ClinicFee;
	}
	public void setClinicFee(String clinicFee) {
		ClinicFee = clinicFee;
	}
	private String RegistrationFee;//挂号费

	public String getRegistrationFee() {
		return RegistrationFee;
	}
	public void setRegistrationFee(String registrationFee) {
		RegistrationFee = registrationFee;
	}

	public String getReserveFlag() {
		return ReserveFlag;
	}
	public void setReserveFlag(String reserveFlag) {
		ReserveFlag = reserveFlag;
	}

	private  String ReserveFlag;//记录状态
	private String orderid;//订单号

	private String lockflag;//锁定状态
	private String errormsg;//出错信息
	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getLockflag() {
		return lockflag;
	}

	public void setLockflag(String lockflag) {
		this.lockflag = lockflag;
	}
	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	private String pinkey;//
	public String getPinkey() {
		return pinkey;
	}
	public void setPinkey(String pinkey) {
		this.pinkey = pinkey;
	}

	private String returncode;//
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
}
