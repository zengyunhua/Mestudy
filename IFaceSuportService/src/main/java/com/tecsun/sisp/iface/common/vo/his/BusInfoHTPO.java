package com.tecsun.sisp.iface.common.vo.his;

import com.tecsun.sisp.iface.common.vo.Page;

/**
 * 航天医院挂号实体类
* @ClassName: BusInfoHTPO 
* @date 2015年7月14日 下午10:26:09 
*
 */
public class BusInfoHTPO {
	private long ID;
	private String HOSID; // 医院编码
	private String TERMINALID; // 终端编码
	private String NAME;// 姓名
	private String IDENTITY; // 身份证号
	private String CARDTYPE; // 卡类型
	private String CARDNO; // 社保卡号
	private String ACCOUNTTYPE; // 帐户支付类型
	private String SEX;// 性别
	private int AGR;// 年龄
	private String VISID;// 医院就诊号
	private String TRANSNO;// 医院交易流水号
	private String DEPARTMENT;// 科室
	private String FLOWID;// 号表ID
	private String REGDATE;// 挂号日期
	private String REGTIME;// AM/PM
	private String REGFEE;// 挂号费
	private String SN;// ARCH顺序号
	private String ADDRESS;// 就诊地址
	private String STATUS;// 状态(预约-01，缴费成功-02，挂号成功-03，已取号-04，挂号失败-05，未取号-06，已退号-07)
	private String SUBTIME;// 预约时间
	private String PAYTIME;// 缴费时间
	private String REGSUCCTIME;// 挂号成功时间
	private String REGSERRORTIME;// 挂号失败时间
	private String CHANGETIME;// 已退号时间
	private String GETNO;// 已取号时间
	private String NOTGETNO;// 未取号时间
	private String CHANNELTPYE;//接口类型型(如：大众端-02、德生宝-01)
	private String CHANGERRORETIME;//退号失败时间
	private String MZTRANSNO;// m门诊住院流水号
	
	public String getMZTRANSNO() {
		return MZTRANSNO;
	}

	public void setMZTRANSNO(String mZTRANSNO) {
		MZTRANSNO = mZTRANSNO;
	}

	private String DEPARTMENTMC;//科室名称
	private String DOCTORMC;//专家名称；
	private String HOSPITALMC;//医院名称；
	private String DOCTORTYPE;//挂号类型（专家号、普通号）

	public String getDOCTORTYPE() {
		return DOCTORTYPE;
	}

	public void setDOCTORTYPE(String dOCTORTYPE) {
		DOCTORTYPE = dOCTORTYPE;
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

	

	public long getID() {
		return ID;
	}

	public void setID(long iD) {
		ID = iD;
	}

	public String getHOSID() {
		return HOSID;
	}

	public void setHOSID(String hOSID) {
		HOSID = hOSID;
	}

	public String getTERMINALID() {
		return TERMINALID;
	}

	public void setTERMINALID(String tERMINALID) {
		TERMINALID = tERMINALID;
	}

	public String getIDENTITY() {
		return IDENTITY;
	}

	public void setIDENTITY(String iDENTITY) {
		IDENTITY = iDENTITY;
	}

	public String getCARDTYPE() {
		return CARDTYPE;
	}

	public void setCARDTYPE(String cARDTYPE) {
		CARDTYPE = cARDTYPE;
	}

	public String getCARDNO() {
		return CARDNO;
	}

	public void setCARDNO(String cARDNO) {
		CARDNO = cARDNO;
	}

	public String getACCOUNTTYPE() {
		return ACCOUNTTYPE;
	}

	public void setACCOUNTTYPE(String aCCOUNTTYPE) {
		ACCOUNTTYPE = aCCOUNTTYPE;
	}

	public String getSEX() {
		return SEX;
	}

	public void setSEX(String sEX) {
		SEX = sEX;
	}

	public int getAGR() {
		return AGR;
	}

	public void setAGR(int aGR) {
		AGR = aGR;
	}

	public String getVISID() {
		return VISID;
	}

	public void setVISID(String vISID) {
		VISID = vISID;
	}

	public String getTRANSNO() {
		return TRANSNO;
	}

	public void setTRANSNO(String tRANSNO) {
		TRANSNO = tRANSNO;
	}

	public String getDEPARTMENT() {
		return DEPARTMENT;
	}

	public void setDEPARTMENT(String dEPARTMENT) {
		DEPARTMENT = dEPARTMENT;
	}

	public String getFLOWID() {
		return FLOWID;
	}

	public void setFLOWID(String fLOWID) {
		FLOWID = fLOWID;
	}

	public String getREGDATE() {
		return REGDATE;
	}

	public void setREGDATE(String rEGDATE) {
		REGDATE = rEGDATE;
	}

	public String getREGTIME() {
		return REGTIME;
	}

	public void setREGTIME(String rEGTIME) {
		REGTIME = rEGTIME;
	}

	public String getREGFEE() {
		return REGFEE;
	}

	public void setREGFEE(String rEGFEE) {
		REGFEE = rEGFEE;
	}

	public String getSN() {
		return SN;
	}

	public void setSN(String sN) {
		SN = sN;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getSUBTIME() {
		return SUBTIME;
	}

	public void setSUBTIME(String sUBTIME) {
		SUBTIME = sUBTIME;
	}

	public String getPAYTIME() {
		return PAYTIME;
	}

	public void setPAYTIME(String pAYTIME) {
		PAYTIME = pAYTIME;
	}

	public String getREGSUCCTIME() {
		return REGSUCCTIME;
	}

	public void setREGSUCCTIME(String rEGSUCCTIME) {
		REGSUCCTIME = rEGSUCCTIME;
	}

	public String getREGSERRORTIME() {
		return REGSERRORTIME;
	}

	public void setREGSERRORTIME(String rEGSERRORTIME) {
		REGSERRORTIME = rEGSERRORTIME;
	}

	public String getCHANGETIME() {
		return CHANGETIME;
	}

	public void setCHANGETIME(String cHANGETIME) {
		CHANGETIME = cHANGETIME;
	}

	public String getGETNO() {
		return GETNO;
	}

	public void setGETNO(String gETNO) {
		GETNO = gETNO;
	}

	public String getNOTGETNO() {
		return NOTGETNO;
	}

	public void setNOTGETNO(String nOTGETNO) {
		NOTGETNO = nOTGETNO;
	}

	public String getNAME() {
		return NAME;
	}

	public void setNAME(String nAME) {
		NAME = nAME;
	}

	public String getCHANNELTPYE() {
		return CHANNELTPYE;
	}

	public void setCHANNELTPYE(String cHANNELTPYE) {
		CHANNELTPYE = cHANNELTPYE;
	}

	public String getCHANGERRORETIME() {
		return CHANGERRORETIME;
	}

	public void setCHANGERRORETIME(String cHANGERRORETIME) {
		CHANGERRORETIME = cHANGERRORETIME;
	}
	
	
}
