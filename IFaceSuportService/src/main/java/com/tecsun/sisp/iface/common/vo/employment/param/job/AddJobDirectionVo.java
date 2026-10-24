package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月20日 下午4:35:31
 */
public class AddJobDirectionVo extends JobBasicVo {

	private String grxxid;// 个人信息id
	private String zy;// 专业（第二字典表的代码）
	private String gz;// 工种（第二字典表的代码）
	private String gwfl;// 岗位分类（第二字典表的代码）
	private String dqbh;// 地区编号（第二字典表的代码）
	private String xzdy;// 薪资待遇(字典表的id)
	private String ygxs;// 用工形式(字典表的id)
	private String yxqks;// 有效期开始(如2014-09-08 12:30:00)
	private String yxqjs;// 有效期结束(如2014-09-08 12:30:00)
	private String ms;// 描述(需编码)
	private String fbzt;// 发布状态（1：未发布,2：发布中,3：已过期,4：暂停）
	private String sfdwgk;// 是否对外公开
	private String ssyq;// 食宿要求0.食宿 1.食2.宿3.无
	private String zygz;// 择业工种手填 需编码
	private String jlmc;// 简历名称 需编码
	private String sblx;// 设备类型 3:自助一体机

	public String getGrxxid() {
		return grxxid;
	}

	public void setGrxxid(String grxxid) {
		this.grxxid = grxxid;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
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

	public String getDqbh() {
		return dqbh;
	}

	public void setDqbh(String dqbh) {
		this.dqbh = dqbh;
	}

	public String getXzdy() {
		return xzdy;
	}

	public void setXzdy(String xzdy) {
		this.xzdy = xzdy;
	}

	public String getYgxs() {
		return ygxs;
	}

	public void setYgxs(String ygxs) {
		this.ygxs = ygxs;
	}

	public String getYxqks() {
		return yxqks;
	}

	public void setYxqks(String yxqks) {
		this.yxqks = yxqks;
	}

	public String getYxqjs() {
		return yxqjs;
	}

	public void setYxqjs(String yxqjs) {
		this.yxqjs = yxqjs;
	}

	public String getMs() {
		return ms;
	}

	public void setMs(String ms) {
		this.ms = ms;
	}

	public String getFbzt() {
		return fbzt;
	}

	public void setFbzt(String fbzt) {
		this.fbzt = fbzt;
	}

	public String getSfdwgk() {
		return sfdwgk;
	}

	public void setSfdwgk(String sfdwgk) {
		this.sfdwgk = sfdwgk;
	}

	public String getSsyq() {
		return ssyq;
	}

	public void setSsyq(String ssyq) {
		this.ssyq = ssyq;
	}

	public String getZygz() {
		return zygz;
	}

	public void setZygz(String zygz) {
		this.zygz = zygz;
	}

	public String getJlmc() {
		return jlmc;
	}

	public void setJlmc(String jlmc) {
		this.jlmc = jlmc;
	}

	public String getSblx() {
		return sblx;
	}

	public void setSblx(String sblx) {
		this.sblx = sblx;
	}

}
