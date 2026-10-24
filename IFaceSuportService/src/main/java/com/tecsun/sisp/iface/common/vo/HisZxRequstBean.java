package com.tecsun.sisp.iface.common.vo;

/**
 * 医院门诊请求bean
 * 
 * @ClassName: DepartmentBean
 */
public class HisZxRequstBean {
	private int pageno;
	  private int pagesize;
	 public int getPageno() {
		return pageno;
	}
	public void setPageno(int pageno) {
		this.pageno = pageno;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	private String deviceid;//设备编码
	 public String getDeviceid() {
		return deviceid;
	}
	 public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	 private String channelcode;// 接口类型型(如：大众端、网办、德生宝)
	 private String channelType;//接口类型(如：大众端-02、德生宝-01)

	private String requestid;//访问者
	//private String pageactionin;
	private String currentpagenum;//当前页码
	private String rowsperpage;//每页行数
    private String pageaction;//翻页动作
	private String topagenum;//翻到哪一页
	//private String retrieveargs;
	private String begindate;//最后修改时间
	private String hospitalid;//医院代号
	//private String request;//
	private String deptcode;//科室代号
	private String doctorno;//医生编号
	private String scheduleid;//日排班 ID

	private String reservemobile;//手机号码;
	public String getExt18() {
		return ext18;
	}
	public void setExt18(String ext18) {
		this.ext18 = ext18;
	}
	public String getExt19() {
		return ext19;
	}
	public void setExt19(String ext19) {
		this.ext19 = ext19;
	}
	private String creator;//创建人
	private String orderid;//订单号

	private String certtypeno;//证件类型
	private String patientidcard;//证件号
	private String patientname;//病人姓名
	private String patientsex;//病人性别
	private String patientbirthday;//出生日期
	private String medicalrecord;//病历号
	private String contactphone;//联系电话
	private String familyaddress;//家庭地址
	private String paymentcode;//支付方式代码
	private String pinkey;//数字订单号
	private String orderstate;//订单状态

	private String cancelreason;//取消原因
	private String operator;//操作人

	private String audopertor;//审核人
	private String backfeemode;//退费方式
	private String outpdate_start;//门诊日期开始
	private String outpdate_end;//门诊日期结束
	private String timeinterval;//时间段
	private String specialty;//专长
	
	
	private String userno;//个人编号
	private String payDate;//医疗类别
	private String payamount;//人员状态
	private String ext1;//参保状态
	private String ext2;//姓名
	private String ext3;//性别
	private String ext4;//身份证号
	private String ext5;//生日
	private String ext6;//医疗人员类别
	private String ext7;//单位名称
	private String ext8;//费用金额
	private String ext9;  //帐户支付
	private String ext10;//统筹支付
	private String ext11;//本次大病支出
	private String ext12; //本次公务员补助支出
	private String ext13; //现金支付
	private String ext14;//自费总金额
	private String ext18;//医保卡号
	private String ext19;//扩展信息
	private String patientId;//患者ID
	private String cardNO;//卡号
	private String idcard;  //身份证号码
	
	private String zdm;//终端号
		public String getZdm() {
		return zdm;
	}
	public void setZdm(String zdm) {
		this.zdm = zdm;
	}
		public int getCardtype() {
		return cardtype;
	}
	public void setCardtype(int cardtype) {
		this.cardtype = cardtype;
	}
	private int cardtype;//卡类型 1 就诊卡 2银行卡 3医保卡 4居民健康卡  
	
	private String channeltpye;//渠道类型 01-德生宝 02-大终端
	
	public String getChanneltpye() {
		return channeltpye;
	}
	public void setChanneltpye(String channeltpye) {
		this.channeltpye = channeltpye;
	}
	//private String cardno; // 社保卡号
	private String zflb;//支付使用的账户类别：01 医院账户 02 社保个人账户 03 银行账户 04集团个人账户 05 社保统筹账户 06 集团统筹账户 07 集团重症账户 08现金 09其他账户
	private String outpdate;//日期
	private String deptname;//科室名称
	private String hospitalname;//医院名称
	private String doctorname;//医生姓名
	private String bankno;//银行卡号；
	private String doctortype;//挂号类型(专家号，普通号)
	
	public String getDoctortype() {
		return doctortype;
	}
	public void setDoctortype(String doctortype) {
		this.doctortype = doctortype;
	}
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getDeptname() {
		return deptname;
	}
	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}
	public String getHospitalname() {
		return hospitalname;
	}
	public void setHospitalname(String hospitalname) {
		this.hospitalname = hospitalname;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getOutpdate() {
		return outpdate;
	}
	public void setOutpdate(String outpdate) {
		this.outpdate = outpdate;
	}
	public String getClinictypename() {
		return clinictypename;
	}
	public void setClinictypename(String clinictypename) {
		this.clinictypename = clinictypename;
	}
	private String clinictypename;//专家号;
	
	public String getCardNO() {
		return cardNO;
	}
	public void setCardNO(String cardNO) {
		this.cardNO = cardNO;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	private String cardType;//卡类型 1 就诊卡 2银行卡 3医保卡 4居民健康卡  
	

	public String getZflb() {
		return zflb;
	}
	public void setZflb(String zflb) {
		this.zflb = zflb;
	}
/*	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}*/
	public String getExt17() {
		return ext17;
	}
	public void setExt17(String ext17) {
		this.ext17 = ext17;
	}
	private String ext15;//个人帐户余额
	private String ext16;//所属区号
	private String ext17;//医疗类别
	
	private String payType;//支付类型 （支付宝、 微信、 银行卡、 医保卡、 其他） 住院流水号(门诊流水号)
	private String outTradeNo;//商户订单号
	private String termId;//设备终端号
	private String payid;//支付 ID
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
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
	public String getExt5() {
		return ext5;
	}
	public void setExt5(String ext5) {
		this.ext5 = ext5;
	}
	public String getExt6() {
		return ext6;
	}
	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}
	public String getExt7() {
		return ext7;
	}
	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}
	public String getExt8() {
		return ext8;
	}
	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}
	public String getExt9() {
		return ext9;
	}
	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}
	public String getExt10() {
		return ext10;
	}
	public void setExt10(String ext10) {
		this.ext10 = ext10;
	}
	public String getExt11() {
		return ext11;
	}
	public void setExt11(String ext11) {
		this.ext11 = ext11;
	}
	public String getExt12() {
		return ext12;
	}
	public void setExt12(String ext12) {
		this.ext12 = ext12;
	}
	public String getExt13() {
		return ext13;
	}
	public void setExt13(String ext13) {
		this.ext13 = ext13;
	}
	public String getExt14() {
		return ext14;
	}
	public void setExt14(String ext14) {
		this.ext14 = ext14;
	}
	public String getExt15() {
		return ext15;
	}
	public void setExt15(String ext15) {
		this.ext15 = ext15;
	}
	public String getExt16() {
		return ext16;
	}
	public void setExt16(String ext16) {
		this.ext16 = ext16;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getTermId() {
		return termId;
	}
	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getPayid() {
		return payid;
	}
	public void setPayid(String payid) {
		this.payid = payid;
	}
	public String getOutpdate_start() {
		return outpdate_start;
	}
	public String getUserno() {
		return userno;
	}
	public void setUserno(String userno) {
		this.userno = userno;
	}
	public void setOutpdate_start(String outpdate_start) {
		this.outpdate_start = outpdate_start;
	}
	public String getOutpdate_end() {
		return outpdate_end;
	}
	public void setOutpdate_end(String outpdate_end) {
		this.outpdate_end = outpdate_end;
	}
	public String getTimeinterval() {
		return timeinterval;
	}
	public void setTimeinterval(String timeinterval) {
		this.timeinterval = timeinterval;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}


	public String getAudopertor() {
		return audopertor;
	}
	public void setAudopertor(String audopertor) {
		this.audopertor = audopertor;
	}
	public String getBackfeemode() {
		return backfeemode;
	}
	public void setBackfeemode(String backfeemode) {
		this.backfeemode = backfeemode;
	}

	public String getCancelreason() {
		return cancelreason;
	}
	public void setCancelreason(String cancelreason) {
		this.cancelreason = cancelreason;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPinkey() {
		return pinkey;
	}
	public void setPinkey(String pinkey) {
		this.pinkey = pinkey;
	}

	public String getOrderstate() {
		return orderstate;
	}
	public void setOrderstate(String orderstate) {
		this.orderstate = orderstate;
	}

	public String getPaymentcode() {
		return paymentcode;
	}

	public void setPaymentcode(String paymentcode) {
		this.paymentcode = paymentcode;
	}
	public String getCerttypeno() {
		return certtypeno;
	}
	public void setCerttypeno(String certtypeno) {
		this.certtypeno = certtypeno;
	}
	public String getPatientidcard() {
		return patientidcard;
	}
	public void setPatientidcard(String patientidcard) {
		this.patientidcard = patientidcard;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public String getPatientsex() {
		return patientsex;
	}
	public void setPatientsex(String patientsex) {
		this.patientsex = patientsex;
	}
	public String getPatientbirthday() {
		return patientbirthday;
	}
	public void setPatientbirthday(String patientbirthday) {
		this.patientbirthday = patientbirthday;
	}
	public String getMedicalrecord() {
		return medicalrecord;
	}
	public void setMedicalrecord(String medicalrecord) {
		this.medicalrecord = medicalrecord;
	}
	public String getContactphone() {
		return contactphone;
	}
	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}
	public String getFamilyaddress() {
		return familyaddress;
	}
	public void setFamilyaddress(String familyaddress) {
		this.familyaddress = familyaddress;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getReservemobile() {
		return reservemobile;
	}
	public void setReservemobile(String reservemobile) {
		this.reservemobile = reservemobile;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getParttimeid() {
		return parttimeid;
	}
	public void setParttimeid(String parttimeid) {
		this.parttimeid = parttimeid;
	}
	private String parttimeid;//分时 ID

	public String getScheduleid() {
		return scheduleid;
	}

	public void setScheduleid(String scheduleid) {
		this.scheduleid = scheduleid;
	}
	public String getDoctorno() {
		return doctorno;
	}

	public void setDoctorno(String doctorno) {
		this.doctorno = doctorno;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}
	public String getChannelcode() {
		return channelcode;
	}
	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}
	public String getChannelType() {
		return channelType;
	}
	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}
	public String getRequestid() {
		return requestid;
	}
	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}
	public String getCurrentpagenum() {
		return currentpagenum;
	}
	public void setCurrentpagenum(String currentpagenum) {
		this.currentpagenum = currentpagenum;
	}
	public String getRowsperpage() {
		return rowsperpage;
	}
	public void setRowsperpage(String rowsperpage) {
		this.rowsperpage = rowsperpage;
	}
	public String getPageaction() {
		return pageaction;
	}
	public void setPageaction(String pageaction) {
		this.pageaction = pageaction;
	}
	public String getTopagenum() {
		return topagenum;
	}
	public void setTopagenum(String topagenum) {
		this.topagenum = topagenum;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getHospitalid() {
		return hospitalid;
	}
	public void setHospitalid(String hospitalid) {
		this.hospitalid = hospitalid;
	}

	private String payId; //支付 ID
	private String payAmount;//支付金额（元）
	private String userNo;//用户标识
	private String createIp;//生成订单客户端 IP
	private String payTime;//支付时间
	private String extInfo;//扩展信息（JSON 格式） 
	private String reqReserved;//透传信息
	private String billGuid;//单据唯一 ID
	private String billId;//单据 ID
	private String billMoney;//单据金额（元）
	public String getPayId() {
		return payId;
	}
	public void setPayId(String payId) {
		this.payId = payId;
	}
	public String getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getCreateIp() {
		return createIp;
	}
	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getExtInfo() {
		return extInfo;
	}
	public void setExtInfo(String extInfo) {
		this.extInfo = extInfo;
	}
	public String getReqReserved() {
		return reqReserved;
	}
	public void setReqReserved(String reqReserved) {
		this.reqReserved = reqReserved;
	}
	public String getBillGuid() {
		return billGuid;
	}
	public void setBillGuid(String billGuid) {
		this.billGuid = billGuid;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(String billMoney) {
		this.billMoney = billMoney;
	}
	
	private String pinKey;//数字订单号
	private String orderId;//订单 ID
	public String getPinKey() {
		return pinKey;
	}
	public void setPinKey(String pinKey) {
		this.pinKey = pinKey;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	private String doctorTitle;//医生职称
	private String registerFee;//挂号费
	private String clinicFee;//诊疗费
	private String registerDate;//出诊日期
	private String sectionType;//区域代码
	private String doctorCode;//医生代码
	
	public String getDoctorTitle() {
		return doctorTitle;
	}
	public void setDoctorTitle(String doctorTitle) {
		this.doctorTitle = doctorTitle;
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
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	public String getSectionType() {
		return sectionType;
	}
	public void setSectionType(String sectionType) {
		this.sectionType = sectionType;
	}
	public String getDoctorCode() {
		return doctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}
	
	
	private String sectionCode;//区域代码
	private String  level ;//科室等级
	public String getSectionCode() {
		return sectionCode;
	}
	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	private String doctorlike; //医生姓名或拼音输入码
	private String illnessesname; //疾病名称
	private String patientid; //患者 ID
	public String getDoctorlike() {
		return doctorlike;
	}
	public void setDoctorlike(String doctorlike) {
		this.doctorlike = doctorlike;
	}
	public String getIllnessesname() {
		return illnessesname;
	}
	public void setIllnessesname(String illnessesname) {
		this.illnessesname = illnessesname;
	}
	public String getPatientid() {
		return patientid;
	}
	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}
}
