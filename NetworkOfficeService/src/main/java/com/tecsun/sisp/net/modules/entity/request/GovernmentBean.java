package com.tecsun.sisp.net.modules.entity.request;

import com.tecsun.sisp.net.common.vo.faceverify.SecQueryBean;
import com.tecsun.sisp.net.modules.entity.BaseVO;

/**
 * 查询列表入参
 *
 */
public class GovernmentBean  extends BaseVO {
	private String title;
	private String type;
	private String secondType; 
	private String publishTime;
	private String auth;
	//数字字典表的一级、二级编码
	private String groupId;
	private String code;

	private int    		pageno=0;		//页码
	private int    		pagesize=30;		//页数
	private String 		channelcode;

	public String getChannelcode() {return channelcode;}
	public void setChannelcode(String channelcode) {this.channelcode = channelcode;}

	public int getPageno() {
		if(pageno==0)
			pageno = 1;
		return pageno;
	}
	public void setPageno(int pageno) {
		this.pageno = pageno;
	}
	public int getPagesize() {
		if(pagesize==0)
			pagesize = 30;
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

    
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSecondType() {
		return secondType;
	}
	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}

}
