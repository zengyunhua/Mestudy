package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class QueryJobOfferVo extends JobBasicVo {

	private String qyid;// 企业ID
	private String zy;// 专业（第二字典表的代码、多选）
	private String xlyq;// 学历要求（字典表的ID、多选）
	private String dqbh;// 地区编号（第二字典表的代码、多选）
	private String gz;// 工种（第二字典表的代码、多选）
	private String gwfl;// 岗位分类（第二字典表的代码、多选）
	private String gzxz;// 工作性质（字典表ID，多选）
	private String xzdy;// 薪资待遇（字典表remark、多选）
	private String shzt;// 审核状态(传数字、多选）
	private String fbzt;// 发布状态(传数字、多选）
	private String gjz;// 关键字（对应的是岗位名称、公司名称、模糊查询、需编码）
	private String dqsj;// 当前时间(如2014-12-12 12:00:00)
	private String jzsj;// 当前时间大于截止时间(如2014-12-12 12:00:00)
	private String jzsjtwo;// 当前时间小于截止时间(如2014-12-12 12:00:00)
	private String qycxsj;// 企业模糊搜索数据(全拼简拼)

	public String getQyid() {
		return qyid;
	}

	public void setQyid(String qyid) {
		this.qyid = qyid;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getXlyq() {
		return xlyq;
	}

	public void setXlyq(String xlyq) {
		this.xlyq = xlyq;
	}

	public String getDqbh() {
		return dqbh;
	}

	public void setDqbh(String dqbh) {
		this.dqbh = dqbh;
	}

	public String getGz() {
		return gz;
	}

	public void setGz(String gz) {
		this.gz = gz;
	}

	public String getGwfl() {
		return gwfl;
	}

	public void setGwfl(String gwfl) {
		this.gwfl = gwfl;
	}

	public String getGzxz() {
		return gzxz;
	}

	public void setGzxz(String gzxz) {
		this.gzxz = gzxz;
	}

	public String getXzdy() {
		return xzdy;
	}

	public void setXzdy(String xzdy) {
		this.xzdy = xzdy;
	}

	public String getShzt() {
		return shzt;
	}

	public void setShzt(String shzt) {
		this.shzt = shzt;
	}

	public String getFbzt() {
		return fbzt;
	}

	public void setFbzt(String fbzt) {
		this.fbzt = fbzt;
	}

	public String getGjz() {
		return gjz;
	}

	public void setGjz(String gjz) {
		this.gjz = gjz;
	}

	public String getDqsj() {
		return dqsj;
	}

	public void setDqsj(String dqsj) {
		this.dqsj = dqsj;
	}

	public String getJzsj() {
		return jzsj;
	}

	public void setJzsj(String jzsj) {
		this.jzsj = jzsj;
	}

	public String getJzsjtwo() {
		return jzsjtwo;
	}

	public void setJzsjtwo(String jzsjtwo) {
		this.jzsjtwo = jzsjtwo;
	}

	public String getQycxsj() {
		return qycxsj;
	}

	public void setQycxsj(String qycxsj) {
		this.qycxsj = qycxsj;
	}
}
