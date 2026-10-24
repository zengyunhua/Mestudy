package com.tecsun.sisp.iface.common.vo.employment.param.job;

/**
 * @date 2016年12月22日 下午2:46:37
 */
public class GetPdfVo2 {

	private String picBase64Str;// 照片base64字符串
	private Integer currentPage;// 当前页
	private Integer totalPage;// 一共多少页

	public String getPicBase64Str() {
		return picBase64Str;
	}

	public void setPicBase64Str(String picBase64Str) {
		this.picBase64Str = picBase64Str;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
}
