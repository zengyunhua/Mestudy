package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月22日 上午10:35:07
 */
public class SmsSendCodeVo extends JobBasicVo {

	private String sjhm;// 手机号码
	private String yhlx;// 用户类型（1.求职者2.企业用户）
	private String yzlx;// 验证类型（1.登录验证，2.注册验证）

	public String getSjhm() {
		return sjhm;
	}

	public void setSjhm(String sjhm) {
		this.sjhm = sjhm;
	}

	public String getYhlx() {
		return yhlx;
	}

	public void setYhlx(String yhlx) {
		this.yhlx = yhlx;
	}

	public String getYzlx() {
		return yzlx;
	}

	public void setYzlx(String yzlx) {
		this.yzlx = yzlx;
	}

}
