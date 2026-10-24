package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class GetQzyxByQzzVo extends JobBasicVo {

	private String qzzid;// 求职者id
	private String sfdwgk;// 是否对外公开（1：是，2：否）

	public String getQzzid() {
		return qzzid;
	}

	public void setQzzid(String qzzid) {
		this.qzzid = qzzid;
	}

	public String getSfdwgk() {
		return sfdwgk;
	}

	public void setSfdwgk(String sfdwgk) {
		this.sfdwgk = sfdwgk;
	}

}
