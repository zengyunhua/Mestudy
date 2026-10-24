package com.tecsun.sisp.net.export.excel;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 拼装表头接口
 *
 */
public interface WirteHeader {

	public void wirteHeader(XSSFSheet sheet,ExcelCell excelCell,XSSFCellStyle cellStyleForHeader);
		
}
