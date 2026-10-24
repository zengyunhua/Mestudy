package com.tecsun.sisp.iface.common.vo;

/**
 * 医院门诊缴费请求bean
 * 
 * : DepartmentBean
 *
 */
public class HisRequstPayBean {
	private String channelcode;
	private String OtherParams;//挂号
	private String CardType;//卡类型编码：01医院就诊卡、02社保卡、03银行卡 、04 身份证号(小终端 01；大终端02)
	private String JZH;//医院就诊流水号
	private String YHC;//”社保卡卡号+”|”+”身份证号”，例如：”
	private String UNM;//	用户名（病人姓名）	VARCHAR(20)
	private String SEX;//	性别 1 男 2 女 9 未知	CHAR(1)
	private String AGE;//	年龄	VARCHAR(6)
	private  double SJF;//	银联支付金额 (无值传0)	NUMERIC (18,2) 
	private  double  XJZF;//	现金支付金额(无值传0)	NUMERIC (18,2) 
	private  double ZHZF;//	医保个人账户支付金额(无值传0)	NUMERIC (18,2)
	private  double TCZF;//	医保统筹支付金额(无值传0)	NUMERIC (18,2)
	private  double  ZPZF;//	支票支付金额(无值传0)	NUMERIC (18,2)
	private String SFLX;//	身份类型：孝感市医保A001 职工医保A002	VARCHAR(4)
	private String ZXL;//	中心流水号(小终端传空，大终端金保系统提供)	VARCHAR(12)
	private String ZXD;//	中心日期(小终端传空，大终端金保系统提供)	CHAR(8)
	private String YHL;//	银行流水号	VARCHAR(12)
	private String YHD;//	银行日期	CHAR(8)
	private String YHT;//	银行时间	CHAR(6)
	private String YYL;	//医院流水号(本地时间（20150702223124）+医院编号（140034）+四位随机数（0250）)	VARCHAR(24)
	private String YYD;//	医院日期(本地时间20150702)	CHAR(8)
	private String JBS;//	0缴费 1 挂号 2预约挂号 (默认为1)	CHAR(1)
	private String TYPE;//	0缴费 1退费	CHAR(1)
	private String TIM;//	AM 上午 PM 下午	CHAR(2)
	private String YYM;//	医院代码	VARCHAR(12)
	private String YHM;//	银行代码	VARCHAR(12)
	private String ZDM;//	"终端代码（柜员号）
	private String QDM;//	渠道代码 0-医院窗口；1-自助,2-网银，3-软POS(默认为1)	CHAR(1)
	private String FLOWID;//业务id号 挂号号表ID, 缴费为处方号(号表ID)	VARCHAR(800)
	private String YYZF;	//医院账户支付金额	NUMERIC (18,2)
	private String ZFLB;//	支付使用的账户类别：01 医院账户 02 社保个人账户 03 银行账户 04集团个人账户 05 社保统筹账户 06 集团统筹账户 07 集团重症账户 08现金 09其他账户	ZFLB VARCHAR(2)
	public String getChannelcode() {
		return channelcode;
	}
	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}
	public String getOtherParams() {
		return OtherParams;
	}
	public void setOtherParams(String OtherParams) {
		this.OtherParams = OtherParams;
	}
	public String getCardType() {
		return CardType;
	}
	public void setCardType(String CardType) {
		this.CardType = CardType;
	}
	public String getJZH() {
		return JZH;
	}
	public void setJZH(String JZH) {
		this.JZH = JZH;
	}
	public String getYHC() {
		return YHC;
	}
	public void setYHC(String YHC) {
		this.YHC = YHC;
	}
	public String getUNM() {
		return UNM;
	}
	public void setUNM(String UNM) {
		this.UNM = UNM;
	}
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String SEX) {
		this.SEX = SEX;
	}
	public String getAGE() {
		return AGE;
	}
	public void setAGE(String AGE) {
		this.AGE = AGE;
	}
	public double getSJF() {
		return SJF;
	}
	public void setSJF(double SJF) {
		this.SJF = SJF;
	}
	public double getXJZF() {
		return XJZF;
	}
	public void setXJZF(double XJZF) {
		this.XJZF = XJZF;
	}
	public double getZHZF() {
		return ZHZF;
	}
	public void setZHZF(double ZHZF) {
		this.ZHZF = ZHZF;
	}
	public double getTCZF() {
		return TCZF;
	}
	public void setTCZF(double TCZF) {
		this.TCZF = TCZF;
	}
	public double getZPZF() {
		return ZPZF;
	}
	public void setZPZF(double ZPZF) {
		this.ZPZF = ZPZF;
	}
	public String getSFLX() {
		return SFLX;
	}
	public void setSFLX(String SFLX) {
		this.SFLX = SFLX;
	}
	public String getZXL() {
		return ZXL;
	}
	public void setZXL(String ZXL) {
		this.ZXL = ZXL;
	}
	public String getZXD() {
		return ZXD;
	}
	public void setZXD(String ZXD) {
		this.ZXD = ZXD;
	}
	public String getYHL() {
		return YHL;
	}
	public void setYHL(String YHL) {
		this.YHL = YHL;
	}
	public String getYHD() {
		return YHD;
	}
	public void setYHD(String YHD) {
		this.YHD = YHD;
	}
	public String getYHT() {
		return YHT;
	}
	public void setYHT(String YHT) {
		this.YHT = YHT;
	}
	public String getYYL() {
		return YYL;
	}
	public void setYYL(String YYL) {
		this.YYL = YYL;
	}
	public String getYYD() {
		return YYD;
	}
	public void setYYD(String YYD) {
		this.YYD = YYD;
	}
	public String getJBS() {
		return JBS;
	}
	public void setJBS(String JBS) {
		this.JBS = JBS;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String TYPE) {
		this.TYPE = TYPE;
	}
	public String getTIM() {
		return TIM;
	}
	public void setTIM(String TIM) {
		this.TIM = TIM;
	}
	public String getYYM() {
		return YYM;
	}
	public void setYYM(String YYM) {
		this.YYM = YYM;
	}
	public String getYHM() {
		return YHM;
	}
	public void setYHM(String YHM) {
		this.YHM = YHM;
	}
	public String getZDM() {
		return ZDM;
	}
	public void setZDM(String ZDM) {
		this.ZDM = ZDM;
	}
	public String getQDM() {
		return QDM;
	}
	public void setQDM(String QDM) {
		this.QDM = QDM;
	}
	public String getFLOWID() {
		return FLOWID;
	}
	public void setFLOWID(String FLOWID) {
		this.FLOWID = FLOWID;
	}
	public String getYYZF() {
		return YYZF;
	}
	public void setYYZF(String YYZF) {
		this.YYZF = YYZF;
	}
	public String getZFLB() {
		return ZFLB;
	}
	public void setZFLB(String ZFLB) {
		this.ZFLB = ZFLB;
	}

}
