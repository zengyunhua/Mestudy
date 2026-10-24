package com.tecsun.sisp.iface.common.vo.employment.param.job;

public class GetPdfVo extends JobBasicVo {

	private String wjlj;// 文件路径

	private Integer currentPage;// PDF照片，第几页

	public String getWjlj() {
		return wjlj;
	}

	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
}
