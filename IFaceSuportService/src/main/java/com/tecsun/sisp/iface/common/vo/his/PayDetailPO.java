package com.tecsun.sisp.iface.common.vo.his;

/**
 * 医院病人消费明细
 * 
 * @date 2015年6月18日 下午5:52:09
 *
 */
public class PayDetailPO {

	private String JZH;// 门诊流水号(住院流水号)
	private String XMLB;// 项目类别：1药品、2诊疗项目、3服务设施
	private String FYLB;// 费用类别
	private String CFH;// 处方号：如果是退方则传入要退的原处方号
	private String CFRQ;// 处方日期：YYYYMMDDHH24MISS
	private String XMNM;// 医院收费项目内码
	private String ZXBM;// 收费项目中心编码
	private String XMMC;// 医院收费项目名称
	private String DJ;// 单价：4位小数
	private String SL;// 数量：2位小数
	private String JX;// 剂型
	private String GG;// 规格
	private String MCYL;// 每次用量：2位小数
	private String SYPC;// 使用频次
	private String YSXM;// 医生姓名：传处方医师姓名
	private String CFYS;// 处方医师：传处方医师编码
	private String YF;// 用法
	private String DW;// 单位
	private String KBMC;// 科别名称
	private String ZXTS;// 执行天数
	private String CYDFFBZ;// 草药单复方标志：0单方，1复方
	private String JBR;// 经办人：终端编号
	private String YHDJ;// 优惠单价：4位小数（非惠民医院不传）
	private String BRXM;// 病人姓名
	private String JE;// 金额
	private String ZXKS;// 执行科室编号
	private String KSMC;// 执行科室名称
	private String FLOWID;// NUMBER(18,0) 消费处方唯一ID
	private String WINDOWNO;// 发药窗口号

	public String getJZH() {
		return JZH;
	}

	public void setJZH(String jZH) {
		JZH = jZH;
	}

	public String getXMLB() {
		return XMLB;
	}

	public void setXMLB(String xMLB) {
		XMLB = xMLB;
	}

	public String getFYLB() {
		return FYLB;
	}

	public void setFYLB(String fYLB) {
		FYLB = fYLB;
	}

	public String getCFH() {
		return CFH;
	}

	public void setCFH(String cFH) {
		CFH = cFH;
	}

	public String getCFRQ() {
		return CFRQ;
	}

	public void setCFRQ(String cFRQ) {
		CFRQ = cFRQ;
	}

	public String getXMNM() {
		return XMNM;
	}

	public void setXMNM(String xMNM) {
		XMNM = xMNM;
	}

	public String getZXBM() {
		return ZXBM;
	}

	public void setZXBM(String zXBM) {
		ZXBM = zXBM;
	}

	public String getXMMC() {
		return XMMC;
	}

	public void setXMMC(String xMMC) {
		XMMC = xMMC;
	}

	public String getDJ() {
		return DJ;
	}

	public void setDJ(String dJ) {
		DJ = dJ;
	}

	public String getSL() {
		return SL;
	}

	public void setSL(String sL) {
		SL = sL;
	}

	public String getJX() {
		return JX;
	}

	public void setJX(String jX) {
		JX = jX;
	}

	public String getGG() {
		return GG;
	}

	public void setGG(String gG) {
		GG = gG;
	}

	public String getMCYL() {
		return MCYL;
	}

	public void setMCYL(String mCYL) {
		MCYL = mCYL;
	}

	public String getSYPC() {
		return SYPC;
	}

	public void setSYPC(String sYPC) {
		SYPC = sYPC;
	}

	public String getYSXM() {
		return YSXM;
	}

	public void setYSXM(String ySXM) {
		YSXM = ySXM;
	}

	public String getCFYS() {
		return CFYS;
	}

	public void setCFYS(String cFYS) {
		CFYS = cFYS;
	}

	public String getYF() {
		return YF;
	}

	public void setYF(String yF) {
		YF = yF;
	}

	public String getDW() {
		return DW;
	}

	public void setDW(String dW) {
		DW = dW;
	}

	public String getKBMC() {
		return KBMC;
	}

	public void setKBMC(String kBMC) {
		KBMC = kBMC;
	}

	public String getZXTS() {
		return ZXTS;
	}

	public void setZXTS(String zXTS) {
		ZXTS = zXTS;
	}

	public String getCYDFFBZ() {
		return CYDFFBZ;
	}

	public void setCYDFFBZ(String cYDFFBZ) {
		CYDFFBZ = cYDFFBZ;
	}

	public String getJBR() {
		return JBR;
	}

	public void setJBR(String jBR) {
		JBR = jBR;
	}

	public String getYHDJ() {
		return YHDJ;
	}

	public void setYHDJ(String yHDJ) {
		YHDJ = yHDJ;
	}

	public String getBRXM() {
		return BRXM;
	}

	public void setBRXM(String bRXM) {
		BRXM = bRXM;
	}

	public String getJE() {
		return JE;
	}

	public void setJE(String jE) {
		JE = jE;
	}

	public String getZXKS() {
		return ZXKS;
	}

	public void setZXKS(String zXKS) {
		ZXKS = zXKS;
	}

	public String getKSMC() {
		return KSMC;
	}

	public void setKSMC(String kSMC) {
		KSMC = kSMC;
	}

	public String getFLOWID() {
		return FLOWID;
	}

	public void setFLOWID(String fLOWID) {
		FLOWID = fLOWID;
	}

	public String getWINDOWNO() {
		return WINDOWNO;
	}

	public void setWINDOWNO(String wINDOWNO) {
		WINDOWNO = wINDOWNO;
	}

}
