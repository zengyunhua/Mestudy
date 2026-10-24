package com.tecsun.sisp.net.modules.entity.business;

import com.tecsun.sisp.net.common.Page;

/**
 * query业务查询条件包装对象
 *
 */
public class QueryVo extends Page<QueryVo>{
	
	public QueryVo(){};
	
	public QueryVo(String idcard,Integer pageNo,Integer pageSize){
		this.idcard = idcard;
		super.setPageNo(pageNo);
		super.setPageSize(pageSize);
	}
	
	/**
	 * 构造器
	 */
	public QueryVo(String grbh,String xz,String nd,Integer pageNo,Integer pageSize){
		this.grbh = grbh;
		this.xz = xz;
		this.nd = nd;
		super.setPageNo(pageNo);
		super.setPageSize(pageSize);
	}

	/**
	 * 险种KEy'
	 */
	private String xzKey;
	/**
	 * 身份证号码
	 */
	private String idcard;
	/**
	 * 个人编号
	 */
	private String grbh;
	/**
	 * 险种
	 */
	private String xz;
	/**
	 * 年度
	 */
	private String nd;
	public String getGrbh() {
		return grbh;
	}
	public void setGrbh(String grbh) {
		this.grbh = grbh;
	}
	public String getXz() {
		return xz;
	}
	public void setXz(String xz) {
		this.xz = xz;
	}
	public String getNd() {
		return nd;
	}
	public void setNd(String nd) {
		this.nd = nd;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getXzKey() {
		return xzKey;
	}
	public void setXzKey(String xzKey) {
		this.xzKey = xzKey;
	}
	
	
}
