package com.tecsun.sisp.iface.common.vo.his;

/**
 * 终端缴费
 * 
 * @ClassName: TerminaConsumptionPO
 * @date 2015年6月18日 下午6:13:18
 *
 */
public class TerminaConsumptionPO {

	private String CardType;// 卡类型编码
	private String YHC;// 银联卡号
	private String JZH;// 医院就诊号
	private String UNM;// 用户名
	private String SEX;// 性别 1 男 2 女 9 未知
	private String AGE;// 年龄
	private String SJF;// 银联支付金额
	private String XJZF;// 现金支付金额
	private String ZHZF;// 医保个人账户支付金额
	private String TCZF;// 医保统筹支付金额
	private String ZPZF;// 支票支付金额
	private String SFLX;// 身份类型：孝感市医保A001 职工医保A002
	private String ZXL;// 中心流水号
	private String ZXD;// 中心日期
	private String YHL;// 银行流水号
	private String YHD;// 银行日期
	private String YHT;// 银行时间
	private String YYL;// 医院流水号
	private String YYD;// 医院日期
	private String JBS;// 0缴费 1 挂号 2预约挂号
	private String TYPE;// 0缴费 1退费
	private String TIM;// AM 上午 PM 下午
	private String YYM;// 医院代码
	private String YHM;// 银行代码
	private String ZDM;// 终端代码（柜员号）说明:由HIS端分配：A001-A999
	private String QDM;// 渠道代码 0-医院窗口；1-自助,2-网银，3-软POS
	private String FLOWID;// 业务id号 挂号号表ID, 缴费为处方号

	private String YLFY;// 本次医疗费用
	private String ZHZC;// 本次帐户支出
	private String TCZC;// 本次统筹支出
	private String DDYLJGFDJE;// 本次定点医疗机构分担金额
	private String DBZC;// 本次大病支出
	private String GWYBZZC;// 本次公务员补助支出
	private String XJZC;// 本次现金支出（工伤、生育除工伤生育统筹支出外只有现金支出）
	private String LXTCZC;// 本次离休统筹支出
	private String SCJRBZZC;// 本次伤残军人补助支出
	private String QFBZ;// 起付标准
	private String TCFDZF;// 统筹分段自付
	private String JRTC;// 本次进入统筹
	private String ZFZJE;// 自费总金额
	private String ZHYE;// 帐户余额
	private String GRZFZE;// 本次个人自付总额
	private String DEZC;// 本次大额2支出
	private String TJGBZC;// 本次厅级干部支出
	private String GSTCZFE;// 工伤统筹支付额
	private String SYTCZFE;// 生育统筹支付额

	private String SXH; // 顺序号
	private String ADR;// 就诊地址

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

	public String getJZH() {
		return JZH;
	}

	public void setJZH(String jZH) {
		JZH = jZH;
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

	public String getYHM() {
		return YHM;
	}

	public void setYHM(String yHM) {
		YHM = yHM;
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

	public String getYLFY() {
		return YLFY;
	}

	public void setYLFY(String yLFY) {
		YLFY = yLFY;
	}

	public String getZHZC() {
		return ZHZC;
	}

	public void setZHZC(String zHZC) {
		ZHZC = zHZC;
	}

	public String getTCZC() {
		return TCZC;
	}

	public void setTCZC(String tCZC) {
		TCZC = tCZC;
	}

	public String getDDYLJGFDJE() {
		return DDYLJGFDJE;
	}

	public void setDDYLJGFDJE(String dDYLJGFDJE) {
		DDYLJGFDJE = dDYLJGFDJE;
	}

	public String getDBZC() {
		return DBZC;
	}

	public void setDBZC(String dBZC) {
		DBZC = dBZC;
	}

	public String getGWYBZZC() {
		return GWYBZZC;
	}

	public void setGWYBZZC(String gWYBZZC) {
		GWYBZZC = gWYBZZC;
	}

	public String getXJZC() {
		return XJZC;
	}

	public void setXJZC(String xJZC) {
		XJZC = xJZC;
	}

	public String getLXTCZC() {
		return LXTCZC;
	}

	public void setLXTCZC(String lXTCZC) {
		LXTCZC = lXTCZC;
	}

	public String getSCJRBZZC() {
		return SCJRBZZC;
	}

	public void setSCJRBZZC(String sCJRBZZC) {
		SCJRBZZC = sCJRBZZC;
	}

	public String getQFBZ() {
		return QFBZ;
	}

	public void setQFBZ(String qFBZ) {
		QFBZ = qFBZ;
	}

	public String getTCFDZF() {
		return TCFDZF;
	}

	public void setTCFDZF(String tCFDZF) {
		TCFDZF = tCFDZF;
	}

	public String getJRTC() {
		return JRTC;
	}

	public void setJRTC(String jRTC) {
		JRTC = jRTC;
	}

	public String getZFZJE() {
		return ZFZJE;
	}

	public void setZFZJE(String zFZJE) {
		ZFZJE = zFZJE;
	}

	public String getZHYE() {
		return ZHYE;
	}

	public void setZHYE(String zHYE) {
		ZHYE = zHYE;
	}

	public String getGRZFZE() {
		return GRZFZE;
	}

	public void setGRZFZE(String gRZFZE) {
		GRZFZE = gRZFZE;
	}

	public String getDEZC() {
		return DEZC;
	}

	public void setDEZC(String dEZC) {
		DEZC = dEZC;
	}

	public String getTJGBZC() {
		return TJGBZC;
	}

	public void setTJGBZC(String tJGBZC) {
		TJGBZC = tJGBZC;
	}

	public String getGSTCZFE() {
		return GSTCZFE;
	}

	public void setGSTCZFE(String gSTCZFE) {
		GSTCZFE = gSTCZFE;
	}

	public String getSYTCZFE() {
		return SYTCZFE;
	}

	public void setSYTCZFE(String sYTCZFE) {
		SYTCZFE = sYTCZFE;
	}

	public String getSXH() {
		return SXH;
	}

	public void setSXH(String sXH) {
		SXH = sXH;
	}

	public String getADR() {
		return ADR;
	}

	public void setADR(String aDR) {
		ADR = aDR;
	}

}
