package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月16日 下午4:21:01
 */
public class PersonJobVo extends JobBasicVo {

	private String grxxid;// 个人信息ID
	private String gwid;// 岗位ID

	public String getGrxxid() {
		return grxxid;
	}

	public void setGrxxid(String grxxid) {
		this.grxxid = grxxid;
	}

	public String getGwid() {
		return gwid;
	}

	public void setGwid(String gwid) {
		this.gwid = gwid;
	}

}
