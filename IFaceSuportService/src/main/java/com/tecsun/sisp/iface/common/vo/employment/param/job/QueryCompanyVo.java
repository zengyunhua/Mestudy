package com.tecsun.sisp.iface.common.vo.employment.param.job;

import java.util.Date;

public class QueryCompanyVo extends JobBasicVo {

	private String jjlx;// 经济类型（字典表的id、多选）
	private String sshy;// 所属行业（第二字典表的代码、多选）
	private String dqbh;// 地区编号（第二字典表的代码、多选）
	private String shzt;// 审核状态(传数字、多选）
	private String gjz;// 关键字（对应的是企业名称、简称、简拼、全拼、模糊查询、需编码）
	private String djkssj;// 登记时间开始时间（yyyy-mm-dd）
	private String djjssj;// 登记结束时间（yyyy-mm-dd）
	private String cxsj;// 查询数据
	private String sjly;// 数据来源，（参数值可为分组id，也可为设备类型，设备类型为3:自助一体机)）支持多选

	public String getJjlx() {
		return jjlx;
	}

	public void setJjlx(String jjlx) {
		this.jjlx = jjlx;
	}

	public String getSshy() {
		return sshy;
	}

	public void setSshy(String sshy) {
		this.sshy = sshy;
	}

	public String getDqbh() {
		return dqbh;
	}

	public void setDqbh(String dqbh) {
		this.dqbh = dqbh;
	}

	public String getShzt() {
		return shzt;
	}

	public void setShzt(String shzt) {
		this.shzt = shzt;
	}

	public String getGjz() {
		return gjz;
	}

	public void setGjz(String gjz) {
		this.gjz = gjz;
	}

	public String getDjkssj() {
		return djkssj;
	}

	public void setDjkssj(String djkssj) {
		this.djkssj = djkssj;
	}

	public String getDjjssj() {
		return djjssj;
	}

	public void setDjjssj(String djjssj) {
		this.djjssj = djjssj;
	}

	public String getCxsj() {
		return cxsj;
	}

	public void setCxsj(String cxsj) {
		this.cxsj = cxsj;
	}

	public String getSjly() {
		return sjly;
	}

	public void setSjly(String sjly) {
		this.sjly = sjly;
	}

}
