package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月22日 下午2:14:48
 */
public class QuerySalaryguidanceVo extends JobBasicVo {

	private String wjlx;// 工资指导价文件类型 1.职业供求分析 2.最新工资价位

	public String getWjlx() {
		return wjlx;
	}

	public void setWjlx(String wjlx) {
		this.wjlx = wjlx;
	}

}
