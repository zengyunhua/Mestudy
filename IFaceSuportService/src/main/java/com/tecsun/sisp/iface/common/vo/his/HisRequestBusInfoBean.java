package com.tecsun.sisp.iface.common.vo.his;

/**
 *  航天医院业务实体类
* @ClassName: HisRequestBusInfoBean 
* @Description: TODO(这里用一句话描述这个类的作用) 
*
 */
public class HisRequestBusInfoBean {
	
	 /**
     * 1 请求头 start
     */
	 private String channelcode;//渠道编码
	 private String deviceid;//设备编码
	 private String id;//身份证

	private String OtherParams;
	private String CardType; // 卡类型编码：01医院就诊卡、02社保卡、03银行卡 、04 身份证号(小终端 01；大终端02)
	private String YHC;
	private String UNM; //姓名
	private String SEX;//性别
	private String AGE;//年龄
	private String ZXL;//中心流水号 -- 大终端
	private String ZXD;//中心日期 -- 大终端
	private String YYL;//医院流水号  医院流水号(本地时间（20150702223124）+医院编号（140034）+四位随机数（0250）)
	private String YYD;//医院日期 (本地时间20150702)
	private String JBS;//0缴费 1 挂号 2预约挂号 (默认为1)
	private String TYPE;//0缴费 1退费
	private String TIM;//AM 上午 PM 下午
	private String YYM;//医院代码
	private String ZDM;//终端号
	private String QDM;//渠道代码
	private String FLOWID;//号表ID
	private double YYZF;//医院账户支付金额
	private String ZFLB;//支付使用的账户类别：01 医院账户 02 社保个人账户 03 银行账户 04集团个人账户 05 社保统筹账户 06 集团统筹账户 07 集团重症账户 08现金 09其他账户
	private String CHANNELTPYE; //渠道类型 01-德生宝 02-大终端
	private String REGDATE;// 挂号日期
	private String DEPARTMENT;// 科室
	private String MZTRANSNO;//门诊住院流水号  医院流水号(本地时间（20150702223124))

	public String getMZTRANSNO() {
		return MZTRANSNO;
	}

	public void setMZTRANSNO(String mZTRANSNO) {
		MZTRANSNO = mZTRANSNO;
	}

	private String JZH	 ;
	private String SJF	 ;
	private String XJZF	;	
	private String ZHZF	;	
	private String TCZF	;	
	private String ZPZF	;	
	private String SFLX	;	
	private String YHL	;
	private String YHD	;
	private String YHT	;
	private String YHM	;
	
	private String DEPARTMENTMC;//科室名称
	private String DOCTORMC;//专家名称；
	private String HOSPITALMC;//医院名称；
	private String DOCTORTYPE;//挂号类型（专家号、普通号）
	
	private String  phoneNo;//手机号码;
	//20151201
	
/*	private String OtherParamsBase;

	public String getOtherParamsBase() {
		return OtherParamsBase;
	}

	public void setOtherParamsBase(String otherParamsBase) {
		OtherParamsBase = otherParamsBase;
	}*/

	public String getPhoneNo() {
		return phoneNo;
	}

	public String getDOCTORTYPE() {
		return DOCTORTYPE;
	}

	public void setDOCTORTYPE(String dOCTORTYPE) {
		DOCTORTYPE = dOCTORTYPE;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getDEPARTMENTMC() {
		return DEPARTMENTMC;
	}

	public void setDEPARTMENTMC(String dEPARTMENTMC) {
		DEPARTMENTMC = dEPARTMENTMC;
	}
	

	public String getDOCTORMC() {
		return DOCTORMC;
	}

	public void setDOCTORMC(String dOCTORMC) {
		DOCTORMC = dOCTORMC;
	}

	public String getHOSPITALMC() {
		return HOSPITALMC;
	}

	public void setHOSPITALMC(String hOSPITALMC) {
		HOSPITALMC = hOSPITALMC;
	}

	public String getOtherParams() {
		return OtherParams;
	}

	public void setOtherParams(String otherParams) {
		OtherParams = otherParams;
	}

	public String getCardType() {
		return CardType;
	}

	public void setCardType(String cardType) {
		CardType = cardType;
	}

	public String getYHC() {
		return YHC;
	}

	public void setYHC(String yHC) {
		YHC = yHC;
	}

	public String getUNM() {
		return UNM;
	}

	public void setUNM(String uNM) {
		UNM = uNM;
	}

	public String getSEX() {
		return SEX;
	}

	public void setSEX(String sEX) {
		SEX = sEX;
	}

	public String getAGE() {
		return AGE;
	}

	public void setAGE(String aGE) {
		AGE = aGE;
	}

	public String getZXL() {
		return ZXL;
	}

	public void setZXL(String zXL) {
		ZXL = zXL;
	}

	public String getZXD() {
		return ZXD;
	}

	public void setZXD(String zXD) {
		ZXD = zXD;
	}

	public String getYYL() {
		return YYL;
	}

	public void setYYL(String yYL) {
		YYL = yYL;
	}

	public String getYYD() {
		return YYD;
	}

	public void setYYD(String yYD) {
		YYD = yYD;
	}

	public String getJBS() {
		return JBS;
	}

	public void setJBS(String jBS) {
		JBS = jBS;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public String getTIM() {
		return TIM;
	}

	public void setTIM(String tIM) {
		TIM = tIM;
	}

	public String getYYM() {
		return YYM;
	}

	public void setYYM(String yYM) {
		YYM = yYM;
	}

	public String getZDM() {
		return ZDM;
	}

	public void setZDM(String zDM) {
		ZDM = zDM;
	}

	public String getQDM() {
		return QDM;
	}

	public void setQDM(String qDM) {
		QDM = qDM;
	}

	public String getFLOWID() {
		return FLOWID;
	}

	public void setFLOWID(String fLOWID) {
		FLOWID = fLOWID;
	}

	public double getYYZF() {
		return YYZF;
	}

	public void setYYZF(double yYZF) {
		YYZF = yYZF;
	}

	public String getZFLB() {
		return ZFLB;
	}

	public void setZFLB(String zFLB) {
		ZFLB = zFLB;
	}

	public String getCHANNELTPYE() {
		return CHANNELTPYE;
	}

	public void setCHANNELTPYE(String cHANNELTPYE) {
		CHANNELTPYE = cHANNELTPYE;
	}

	public String getREGDATE() {
		return REGDATE;
	}

	public void setREGDATE(String rEGDATE) {
		REGDATE = rEGDATE;
	}

	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}

	public String getJZH() {
		return JZH;
	}

	public void setJZH(String jZH) {
		JZH = jZH;
	}

	public String getSJF() {
		return SJF;
	}

	public void setSJF(String sJF) {
		SJF = sJF;
	}

	public String getXJZF() {
		return XJZF;
	}

	public void setXJZF(String xJZF) {
		XJZF = xJZF;
	}

	public String getZHZF() {
		return ZHZF;
	}

	public void setZHZF(String zHZF) {
		ZHZF = zHZF;
	}

	public String getTCZF() {
		return TCZF;
	}

	public void setTCZF(String tCZF) {
		TCZF = tCZF;
	}

	public String getZPZF() {
		return ZPZF;
	}

	public void setZPZF(String zPZF) {
		ZPZF = zPZF;
	}

	public String getSFLX() {
		return SFLX;
	}

	public void setSFLX(String sFLX) {
		SFLX = sFLX;
	}

	public String getYHL() {
		return YHL;
	}

	public void setYHL(String yHL) {
		YHL = yHL;
	}

	public String getYHD() {
		return YHD;
	}

	public void setYHD(String yHD) {
		YHD = yHD;
	}

	public String getYHT() {
		return YHT;
	}

	public void setYHT(String yHT) {
		YHT = yHT;
	}

	public String getYHM() {
		return YHM;
	}

	public void setYHM(String yHM) {
		YHM = yHM;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
