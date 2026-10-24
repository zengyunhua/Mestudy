package com.tecsun.sisp.iface.common.vo.his.zx;

/**
 * 医院挂号号表
 * 
 * @ClassName: HospitalRegistPO
 *
 */
public class ZxHosInfoRegistPO {
	private String hospitalname;//医院名称
	private String hospitalid;//医院代码
	private String inputcode;//拼音输入码
	private String hospitallevel;//医院级别代码
	private String hospitallevelname;//医院级别名称
	private String hospitalsmallpicurl;//医院图片(小)
	private String hospitalmiddlepicurl;//医院图片(中)
	private String hospitalbigpicurl;//医院图片(大)
	private String hospitaladdr;//医院地址
	private String hospitaldes;//医院简介
	
	private String  zipcode;//邮政编码
	private String provcode;//省份代码
	private String provname;//省份名称
	private String citycode;//城市代码
	private String cityname;//城市名称
	private String phone;//公司电话
	private String website;//公司网站

	public String getHospitallevel() {
		return hospitallevel;
	}
	public void setHospitallevel(String hospitallevel) {
		this.hospitallevel = hospitallevel;
	}
	public String getHospitallevelname() {
		return hospitallevelname;
	}
	public void setHospitallevelname(String hospitallevelname) {
		this.hospitallevelname = hospitallevelname;
	}
	public String getHospitalsmallpicurl() {
		return hospitalsmallpicurl;
	}
	public void setHospitalsmallpicurl(String hospitalsmallpicurl) {
		this.hospitalsmallpicurl = hospitalsmallpicurl;
	}
	public String getHospitalmiddlepicurl() {
		return hospitalmiddlepicurl;
	}
	public void setHospitalmiddlepicurl(String hospitalmiddlepicurl) {
		this.hospitalmiddlepicurl = hospitalmiddlepicurl;
	}
	public String getHospitalbigpicurl() {
		return hospitalbigpicurl;
	}
	public void setHospitalbigpicurl(String hospitalbigpicurl) {
		this.hospitalbigpicurl = hospitalbigpicurl;
	}
	public String getHospitaladdr() {
		return hospitaladdr;
	}
	public void setHospitaladdr(String hospitaladdr) {
		this.hospitaladdr = hospitaladdr;
	}
	public String getHospitaldes() {
		return hospitaldes;
	}
	public void setHospitaldes(String hospitaldes) {
		this.hospitaldes = hospitaldes;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getProvcode() {
		return provcode;
	}
	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}
	public String getProvname() {
		return provname;
	}
	public void setProvname(String provname) {
		this.provname = provname;
	}
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
	public String getHospitalid() {
		return hospitalid;
	}
	public void setHospitalid(String hospitalid) {
		this.hospitalid = hospitalid;
	}

	private String deptcode;//科室代码
	private String deptname;//科室名称
	private String count;
	private String deptsmallpicurl;//科室图片(小)
	private String deptmiddlepicurl;//科室图片(中)
	private String deptbigpicurl;//科室图片(大
	private String deptdes;//科室简介
	private String parentid;//上级科室
	private String deptlevel;//科室层次
	private String location;//楼层位置
	private String ordernum;//排序数字
	private String createtime;//创建时间；
	private String lastmodify;//修改时间；
	

	public String getCount() {
		return count;
	}
	public String getDeptsmallpicurl() {
		return deptsmallpicurl;
	}
	public void setDeptsmallpicurl(String deptsmallpicurl) {
		this.deptsmallpicurl = deptsmallpicurl;
	}
	public String getDeptmiddlepicurl() {
		return deptmiddlepicurl;
	}
	public void setDeptmiddlepicurl(String deptmiddlepicurl) {
		this.deptmiddlepicurl = deptmiddlepicurl;
	}
	public String getDeptbigpicurl() {
		return deptbigpicurl;
	}
	public void setDeptbigpicurl(String deptbigpicurl) {
		this.deptbigpicurl = deptbigpicurl;
	}
	public String getDeptdes() {
		return deptdes;
	}
	public void setDeptdes(String deptdes) {
		this.deptdes = deptdes;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getDeptlevel() {
		return deptlevel;
	}
	public void setDeptlevel(String deptlevel) {
		this.deptlevel = deptlevel;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getLastmodify() {
		return lastmodify;
	}
	public void setLastmodify(String lastmodify) {
		this.lastmodify = lastmodify;
	}
	public void setCount(String count) {
		this.count = count;
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
	private String doctorno;//医生编号
	private String doctorname;//医生姓名
	private String doctordes;//医生简介
	private String scheduleflag;//可挂状态 string 1：可挂 -1 不可挂
	private String realnamecheckin;//实名取票
	private String doctortitle;//职务职称
	private String specialty;//疾病专长描述
	public String getDoctorno() {
		return doctorno;
	}
	public void setDoctorno(String doctorno) {
		this.doctorno = doctorno;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}

	public String getDoctordes() {
		return doctordes;
	}
	public void setDoctordes(String doctordes) {
		this.doctordes = doctordes;
	}

	public String getScheduleflag() {
		return scheduleflag;
	}
	public void setScheduleflag(String scheduleflag) {
		this.scheduleflag = scheduleflag;
	}

	public String getRealnamecheckin() {
		return realnamecheckin;
	}
	public void setRealnamecheckin(String realnamecheckin) {
		this.realnamecheckin = realnamecheckin;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getDoctortitle() {
		return doctortitle;
	}
	public void setDoctortitle(String doctortitle) {
		this.doctortitle = doctortitle;
	}

	private String ScheduleId;//日排班 ID
	public String getScheduleId() {
		return ScheduleId;
	}
	public void setScheduleId(String scheduleId) {
		ScheduleId = scheduleId;
	}

/*
	private String hospitallevelname;//医院级别名称
	private String hospitallevel;//医院级别代码
	private String hospitaladdr;//医院地址
	private String hospitaldes;//医院简介
	private String phone;//公司电话
	private String website;//公司网站
	private String hospitalid;//医院代码
	private String hospitalsmallpicurl;//医院图片(小)
	private String hospitalmiddlepicurl;//>医院图片(中)
	private String hospitalbigpicurl;//医院图片(大)

	private String citycode;//'
	private String zipcode;//
	private String provcode;//
	public String getCitycode() {
		return citycode;
	}
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getProvcode() {
		return provcode;
	}
	public void setProvcode(String provcode) {
		this.provcode = provcode;
	}

	private String spinputcode;//疾病专长输入码
	private String doctorsmallpicurl;//医生图片(小)
	private String doctormiddlepicurl;//医生图片(中
	private String doctorbigpicurl;//医生图片(大

	//private String scheduleflag;//预约状态

	private String scheduleid;//日排班 ID
	private String outpdate;//日期
	private String timeinterval;//时间段
	private String outptypename;//门诊类别名称
	private Double clinicfee;//诊疗费
	private Double registrationfee;//挂号费
	private int reserveflag;//预约状态1:可挂 ( 院方门办正式确定了的排班表）2:已满（已经全部挂满或者放给预约的约满了）	3:停号 （院方通知医生停诊了，停止继续预约）	4:可约( 院方门办没有正式确定的排班，只是根据惯例生成的排班表）	5:过期 （已经过了预约时间的排班）	6:未开 （还没到允许预约时间的排班）	7:分时（可挂/可约并且有分时）	备注 选得一个医生后，列出该医生的全部排班时需要这个

	private String schedulelist;//坐诊安排  如：周一上午|周二下午|周三下午

	public String getSchedulelist() {
		return schedulelist;
	}

	public void setSchedulelist(String schedulelist) {
		this.schedulelist = schedulelist;
	}

	public String getScheduleid() {
		return scheduleid;
	}
	public void setScheduleid(String scheduleid) {
		this.scheduleid = scheduleid;
	}
	public String getOutpdate() {
		return outpdate;
	}
	public void setOutpdate(String outpdate) {
		this.outpdate = outpdate;
	}
	public String getTimeinterval() {
		return timeinterval;
	}
	public void setTimeinterval(String timeinterval) {
		this.timeinterval = timeinterval;
	}
	public String getOutptypename() {
		return outptypename;
	}
	public void setOutptypename(String outptypename) {
		this.outptypename = outptypename;
	}
	public Double getClinicfee() {
		return clinicfee;
	}
	public void setClinicfee(Double clinicfee) {
		this.clinicfee = clinicfee;
	}
	public Double getRegistrationfee() {
		return registrationfee;
	}
	public void setRegistrationfee(Double registrationfee) {
		this.registrationfee = registrationfee;
	}
	public int getReserveflag() {
		return reserveflag;
	}
	public void setReserveflag(int reserveflag) {
		this.reserveflag = reserveflag;
	}

	public String getSpinputcode() {
		return spinputcode;
	}
	public void setSpinputcode(String spinputcode) {
		this.spinputcode = spinputcode;
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

	public String getDeptcode() {
		return deptcode;
	}
	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getInputcode() {
		return inputcode;
	}
	public void setInputcode(String inputcode) {
		this.inputcode = inputcode;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getDeptsmallpicurl() {
		return deptsmallpicurl;
	}
	public void setDeptsmallpicurl(String deptsmallpicurl) {
		this.deptsmallpicurl = deptsmallpicurl;
	}
	public String getDeptmiddlepicurl() {
		return deptmiddlepicurl;
	}
	public void setDeptmiddlepicurl(String deptmiddlepicurl) {
		this.deptmiddlepicurl = deptmiddlepicurl;
	}
	public String getDeptbigpicurl() {
		return deptbigpicurl;
	}
	public void setDeptbigpicurl(String deptbigpicurl) {
		this.deptbigpicurl = deptbigpicurl;
	}
	public String getDeptdes() {
		return deptdes;
	}
	public void setDeptdes(String deptdes) {
		this.deptdes = deptdes;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getDeptlevel() {
		return deptlevel;
	}
	public void setDeptlevel(String deptlevel) {
		this.deptlevel = deptlevel;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getOrdernum() {
		return ordernum;
	}
	public void setOrdernum(String ordernum) {
		this.ordernum = ordernum;
	}

	public String getHospitallevelname() {
		return hospitallevelname;
	}
	public void setHospitallevelname(String hospitallevelname) {
		this.hospitallevelname = hospitallevelname;
	}
	public String getHospitallevel() {
		return hospitallevel;
	}
	public void setHospitallevel(String hospitallevel) {
		this.hospitallevel = hospitallevel;
	}
	public String getHospitaladdr() {
		return hospitaladdr;
	}
	public void setHospitaladdr(String hospitaladdr) {
		this.hospitaladdr = hospitaladdr;
	}
	public String getHospitaldes() {
		return hospitaldes;
	}
	public void setHospitaldes(String hospitaldes) {
		this.hospitaldes = hospitaldes;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getHospitalid() {
		return hospitalid;
	}
	public void setHospitalid(String hospitalid) {
		this.hospitalid = hospitalid;
	}
	public String getHospitalsmallpicurl() {
		return hospitalsmallpicurl;
	}
	public void setHospitalsmallpicurl(String hospitalsmallpicurl) {
		this.hospitalsmallpicurl = hospitalsmallpicurl;
	}
	public String getHospitalmiddlepicurl() {
		return hospitalmiddlepicurl;
	}
	public void setHospitalmiddlepicurl(String hospitalmiddlepicurl) {
		this.hospitalmiddlepicurl = hospitalmiddlepicurl;
	}
	public String getHospitalbigpicurl() {
		return hospitalbigpicurl;
	}
	public void setHospitalbigpicurl(String hospitalbigpicurl) {
		this.hospitalbigpicurl = hospitalbigpicurl;
	}

	*/

	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}

    private String patientId;//患者ID
    private String cardNo;// 卡号
    private String cardType;//卡类型
    private String chargeType;//费用类型
    private String idType;//费用类型
    private String idCard;//身份证号码
    private String patientName;//病人姓名
    private String patientSex;//性别
    private String dob;//出生日期
    private String address;//家庭住址
    private String balance;//病人可用余额
    private String ext1;//扩展信息
    private String ext2;//扩展信息
    private String ext3;//扩展信息
    private String ext4;//扩展信息

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getChargeType() {
        return chargeType;
    }

    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
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

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }
}
