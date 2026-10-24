package com.tecsun.sisp.iface.common.vo.his;

/**
 * 医院科室实体类
 * 
 * @ClassName: DepartmentPO
 * @date 2015年6月18日 下午12:01:23
 *
 */
public class DepartmentPO {

	private String KSDM; // 科室编号
	private String KSMC; // 科室名称
	private String KSMS; // 科室描述

	public DepartmentPO() {
	}

	public String getKSDM() {
		return KSDM;
	}

	public void setKSDM(String kSDM) {
		KSDM = kSDM;
	}

	public String getKSMC() {
		return KSMC;
	}

	public void setKSMC(String kSMC) {
		KSMC = kSMC;
	}

	public String getKSMS() {
		return KSMS;
	}

	public void setKSMS(String kSMS) {
		KSMS = kSMS;
	}

}
