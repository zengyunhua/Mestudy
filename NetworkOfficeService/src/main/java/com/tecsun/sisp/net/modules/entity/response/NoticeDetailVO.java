package com.tecsun.sisp.net.modules.entity.response;

import java.util.List;

/**
 * 详情出参
 *
 */
public class NoticeDetailVO {
	private Long id;
	private String title;//标题
	private String secondTitle;//二级标题
	private String content;//内容
	private String auth;//作者
	private String publisher;//发布者
	private String publishTime;//发布时间
	private List<NoticePathVO> noticePathVOs;//附件

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

	public String getSecondTitle() {
		return secondTitle;
	}

	public void setSecondTitle(String secondTitle) {
		this.secondTitle = secondTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public List<NoticePathVO> getNoticePathVOs() {
		return noticePathVOs;
	}

	public void setNoticePathVOs(List<NoticePathVO> noticePathVOs) {
		this.noticePathVOs = noticePathVOs;
	}
}
