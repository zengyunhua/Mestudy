package com.tecsun.sisp.iface.common.vo.his.zx;

/**
 * 中心医院历史订单列表
 *
 * @ClassName: HospitalZxOrderPO
 */
public class HospitalZxOrderPO {
    private String pinkey;//数字订单号
    private String patientname;//患者姓名
    private String hospitalname;//医院名称
    private String deptname;//科室名称
    private String doctorname;//医生姓名
    private String registrationfee;//挂号费
    private String clinicfee;//诊疗费
    private String backfeemoney;//退费金额
    private String outpdate ;//出诊日期
    private String timeinterval ;//时间段
    private String clinictypename;//挂号类型（专家号、普通号）
    private String orderid;//订单号
    private String scheduleid;//排班id
    private String contactphone;//身份证
    private String patientidcard;//手机号
    private String hospitalid;//医院代码
    private String deptcode;//科室代码

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
    }

    public String getHospitalid() {
        return hospitalid;
    }

    public void setHospitalid(String hospitalid) {
        this.hospitalid = hospitalid;
    }

    public String getPatientidcard() {
        return patientidcard;
    }

    public void setPatientidcard(String patientidcard) {
        this.patientidcard = patientidcard;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(String scheduleid) {
        this.scheduleid = scheduleid;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getClinictypename() {
		return clinictypename;
	}

	public void setClinictypename(String clinictypename) {
		this.clinictypename = clinictypename;
	}

	public String getTimeinterval() {
        return timeinterval;
    }

    public void setTimeinterval(String timeinterval) {
        this.timeinterval = timeinterval;
    }

    public String getPinkey() {
        return pinkey;
    }

    public void setPinkey(String pinkey) {
        this.pinkey = pinkey;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getRegistrationfee() {
        return registrationfee;
    }

    public void setRegistrationfee(String registrationfee) {
        this.registrationfee = registrationfee;
    }

    public String getClinicfee() {
        return clinicfee;
    }

    public void setClinicfee(String clinicfee) {
        this.clinicfee = clinicfee;
    }

    public String getBackfeemoney() {
        return backfeemoney;
    }

    public void setBackfeemoney(String backfeemoney) {
        this.backfeemoney = backfeemoney;
    }

    public String getOutpdate() {
        return outpdate;
    }

    public void setOutpdate(String outpdate) {
        this.outpdate = outpdate;
    }
}
