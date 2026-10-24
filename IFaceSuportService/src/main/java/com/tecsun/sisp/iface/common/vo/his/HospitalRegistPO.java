package com.tecsun.sisp.iface.common.vo.his;

/**
 * 医院挂号号表
 * 
 * @ClassName: HospitalRegistPO
 *
 */
public class HospitalRegistPO {

	private String KSBH;
	private String KSMC;// 科室名称
	private String YSXM; // 医生姓名
	private String YSJS;// 医生介绍
	private String HBBM; // 号别编码
	private String HBMC; // 号别名称
	private String JE; // 金额
	private String SXH;// 顺序号
	private String SYHS;// 剩余号数
	private String TIME_FLAG; // 上午下午标识 AM ^ PM
	private String FLOWID;// 号表唯一ID

	public String getKSMC() {
		return KSMC;
	}

	public void setKSMC(String kSMC) {
		KSMC = kSMC;
	}

	public String getYSXM() {
		return YSXM;
	}

	public void setYSXM(String ySXM) {
		YSXM = ySXM;
	}

	public String getYSJS() {
		return YSJS;
	}

	public void setYSJS(String ySJS) {
		YSJS = ySJS;
	}

	public String getHBBM() {
		return HBBM;
	}

	public void setHBBM(String hBBM) {
		HBBM = hBBM;
	}

	public String getHBMC() {
		return HBMC;
	}

	public void setHBMC(String hBMC) {
		HBMC = hBMC;
	}

	public String getSXH() {
		return SXH;
	}

	public void setSXH(String sXH) {
		SXH = sXH;
	}

	public String getSYHS() {
		return SYHS;
	}

	public void setSYHS(String sYHS) {
		SYHS = sYHS;
	}

	public String getTIME_FLAG() {
		return TIME_FLAG;
	}

	public void setTIME_FLAG(String tIME_FLAG) {
		TIME_FLAG = tIME_FLAG;
	}

	public String getJE() {
		return JE;
	}

	public void setJE(String jE) {
		JE = jE;
	}

	public String getFLOWID() {
		return FLOWID;
	}

	public void setFLOWID(String fLOWID) {
		FLOWID = fLOWID;
	}

	public String getKSBH() {
		return KSBH;
	}

	public void setKSBH(String kSBH) {
		KSBH = kSBH;
	}

}
