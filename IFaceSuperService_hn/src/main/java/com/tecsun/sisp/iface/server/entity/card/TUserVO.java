package com.tecsun.sisp.iface.server.entity.card;

import java.util.Date;

/**
 * 用户VO类
 *2017年10月13日
 *
 */
public class TUserVO {
	private int userid;
	private String username;  //名称
	private String createuser;  //创建人
	private Date createtime; //创建时间
	private String remark; //备注
	private int flag;   //状态
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	
	

}
