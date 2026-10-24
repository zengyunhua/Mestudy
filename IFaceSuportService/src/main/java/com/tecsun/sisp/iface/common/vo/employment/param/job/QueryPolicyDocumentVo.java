package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class QueryPolicyDocumentVo extends JobBasicVo {

	private String zcfgbt;// 政策法规标题 需编码
	private String wjbh;// 文件编号
	private String zcfl;// 政策分类 1.综合 2.高校就业政策 3.促进创业就业 4.加强职业培训 5.统筹城乡就业 6.就业援助
						// 7.失业保险 8.专项资金 9.其他
	private String lx;// 文件类型(1.国家政策2.湖北省政策3.孝感市政策)
	private String fbsjks;// 发布时间开始(yyyy-mm-dd)
	private String fbsjjs;// 发布时间结束(yyyy-mm-dd)

	public String getZcfgbt() {
		return zcfgbt;
	}

	public void setZcfgbt(String zcfgbt) {
		this.zcfgbt = zcfgbt;
	}

	public String getWjbh() {
		return wjbh;
	}

	public void setWjbh(String wjbh) {
		this.wjbh = wjbh;
	}

	public String getZcfl() {
		return zcfl;
	}

	public void setZcfl(String zcfl) {
		this.zcfl = zcfl;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getFbsjks() {
		return fbsjks;
	}

	public void setFbsjks(String fbsjks) {
		this.fbsjks = fbsjks;
	}

	public String getFbsjjs() {
		return fbsjjs;
	}

	public void setFbsjjs(String fbsjjs) {
		this.fbsjjs = fbsjjs;
	}

}
