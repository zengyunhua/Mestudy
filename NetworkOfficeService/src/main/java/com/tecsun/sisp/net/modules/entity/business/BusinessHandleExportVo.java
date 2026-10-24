package com.tecsun.sisp.net.modules.entity.business;

import com.tecsun.sisp.net.export.excel.ExcelCell;

/**
 * BusinessHandle 导出业务的导出条件包装对象
 *
 */
public class BusinessHandleExportVo {

	/**
	 * 导出excel的名称
	 */
	private String title;
	/**
	 * ExcelcCell复杂表头对象,最小单位是每个Cell,
	 */
	private ExcelCell excelCell;
	/**
	 * 要导出的数据的查询条件
	 */
	private BusinessHandle businessHandle;
	/**
	 * 要导出的字段名
	 */
	private String[] fields;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public ExcelCell getExcelCell() {
		return excelCell;
	}
	public void setExcelCell(ExcelCell excelCell) {
		this.excelCell = excelCell;
	}
	public BusinessHandle getBusinessHandle() {
		return businessHandle;
	}
	public void setBusinessHandle(BusinessHandle businessHandle) {
		this.businessHandle = businessHandle;
	}
	public String[] getFields() {
		return fields;
	}
	public void setFields(String[] fields) {
		this.fields = fields;
	}
	
}
