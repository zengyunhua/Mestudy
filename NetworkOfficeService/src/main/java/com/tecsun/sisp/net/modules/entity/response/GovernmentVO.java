package com.tecsun.sisp.net.modules.entity.response;

import com.tecsun.sisp.net.modules.entity.BaseVO;

/**
 * 查询列表出参
 * 
 *
 */
public class GovernmentVO extends BaseVO {
	private Long id;
	private String title;//文章标题
	private String publishTime;//发布时间
	private String publisher;//发布方
	private String picBase64String; //图片base64编码
	private String detailUrl;//详情url

	private int    		pageno;		//页码
	private int    		pagesize;		//页数
	public int getPageno() {
		return pageno;
	}
	public void setPageno(int pageno) {
		this.pageno = pageno;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPicBase64String() {
		return picBase64String;
	}

	public void setPicBase64String(String picBase64String) {
		this.picBase64String = picBase64String;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
}
