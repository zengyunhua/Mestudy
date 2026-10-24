package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月20日 下午3:14:47
 */
public class QueryJobFairVo extends JobBasicVo {

	private String shzt;// 审核状态(传数字、多选）
	private String fbzt;// 发布状态(传数字、多选）
	private String kssjone;// 开始时间1（如2014-10-09）
	private String kssjtwo;// 开始时间2（如2014-10-09）
	private String dhzt;// 大会主题、需编码
	private String dhmc;// 大会名称、需编码
	private String zbf;// 主办方、需编码
	private String xbf;// 协办方、需编码
	private String zphlx;// 招聘会类型（第一字典表id，多选）
	private String zphztlx;// 招聘会主题类型（第一字典表id，多选）
	private String lblx;// 列表类型（1.正在进行、2.即将召开、3.历史、4.可入驻）支持多选，以逗号隔开

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

	public String getKssjone() {
		return kssjone;
	}

	public void setKssjone(String kssjone) {
		this.kssjone = kssjone;
	}

	public String getKssjtwo() {
		return kssjtwo;
	}

	public void setKssjtwo(String kssjtwo) {
		this.kssjtwo = kssjtwo;
	}

	public String getDhzt() {
		return dhzt;
	}

	public void setDhzt(String dhzt) {
		this.dhzt = dhzt;
	}

	public String getDhmc() {
		return dhmc;
	}

	public void setDhmc(String dhmc) {
		this.dhmc = dhmc;
	}

	public String getZbf() {
		return zbf;
	}

	public void setZbf(String zbf) {
		this.zbf = zbf;
	}

	public String getXbf() {
		return xbf;
	}

	public void setXbf(String xbf) {
		this.xbf = xbf;
	}

	public String getZphlx() {
		return zphlx;
	}

	public void setZphlx(String zphlx) {
		this.zphlx = zphlx;
	}

	public String getZphztlx() {
		return zphztlx;
	}

	public void setZphztlx(String zphztlx) {
		this.zphztlx = zphztlx;
	}

	public String getLblx() {
		return lblx;
	}

	public void setLblx(String lblx) {
		this.lblx = lblx;
	}

}
