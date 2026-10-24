package com.tecsun.sisp.iface.common.vo;

/**
 * 
 * @Title: AreaManage
 * @company :TECSUN
 * @developer: ZENGYUNHUA
 *	@version 1.0
 */
public class AreaManage {

	private int id;	//	id号
	private String regionalcode;//父级区域编码
	private String areaId;	//区域ID
	private String areaName;	//区域名称
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRegionalcode() {
		return regionalcode;
	}
	public void setRegionalcode(String regionalcode) {
		this.regionalcode = regionalcode;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
}
