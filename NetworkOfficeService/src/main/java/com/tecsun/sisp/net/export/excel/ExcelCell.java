package com.tecsun.sisp.net.export.excel;

import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * 复杂表头对象  最小单元是excel的每个cell
 * 用此对象接收前端传过来的复杂表头属性
 *
 */
public class ExcelCell implements WirteHeader{
	
	//name(name要和fields数组对应)
	private String name;
	
	//cell跨列
	private int colspan=1;
	//cell跨行
	private int rowspan=1;
	//在第几列填装name
	private int wirteColIndex;
	
	//当前行下标
	private int cruuentRowIndex;
	//表头总共占用的行数
	private int totalRowNumber;
	
	//复杂表头的下一行表头结构
	private List<ExcelCell> nextRow;

	/**
	 * 实现wirteHeader接口的wirteHeader方法
	 * 递归创建表头(TODO 将递归改成普通的for循环,因为已经知道需要创建多少行的表头,,,用递归会更消耗时间)
	 * @param cellStyleForHeader 
	 */
	@Override
	public void wirteHeader(XSSFSheet sheet,ExcelCell excelCell, XSSFCellStyle cellStyleForHeader) {
		XSSFRow headerRow = null;
		headerRow = sheet.getRow(excelCell.getCruuentRowIndex());
		
		if(headerRow==null){
			headerRow = sheet.createRow(excelCell.getCruuentRowIndex());
			headerRow.setHeight((short) 400);
		}
		
		XSSFCell headerCell = headerRow.createCell((short)excelCell.getWirteColIndex());
		headerCell.setCellStyle(cellStyleForHeader);  
        headerCell.setCellValue(new XSSFRichTextString(excelCell.getName()));
        CellRangeAddress region = null;
    	region = new CellRangeAddress(excelCell.getCruuentRowIndex(), excelCell.getCruuentRowIndex()+excelCell.getRowspan()-1, excelCell.getWirteColIndex(), excelCell.getWirteColIndex()+excelCell.getColspan()-1);
        RegionUtil.setBorderTop(2, region, sheet, sheet.getWorkbook());
        RegionUtil.setBorderRight(2, region, sheet, sheet.getWorkbook());
        RegionUtil.setBorderBottom(2, region, sheet, sheet.getWorkbook());
        RegionUtil.setBorderLeft(2, region, sheet, sheet.getWorkbook());
        
        sheet.addMergedRegion(region);
        
		if(excelCell.getNextRow()!=null){
			for(ExcelCell excelCell_:excelCell.getNextRow()){
				wirteHeader(sheet,excelCell_,cellStyleForHeader);
			}
		}
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}

	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	public List<ExcelCell> getNextRow() {
		return nextRow;
	}

	public void setNextRow(List<ExcelCell> nextRow) {
		this.nextRow = nextRow;
	}

	public int getWirteColIndex() {
		return wirteColIndex;
	}

	public void setWirteColIndex(int wirteColIndex) {
		this.wirteColIndex = wirteColIndex;
	}

	public int getCruuentRowIndex() {
		return cruuentRowIndex;
	}

	public void setCruuentRowIndex(int cruuentRowIndex) {
		this.cruuentRowIndex = cruuentRowIndex;
	}

	public int getTotalRowNumber() {
		return totalRowNumber;
	}

	public void setTotalRowNumber(int totalRowNumber) {
		this.totalRowNumber = totalRowNumber;
	}

	
	
}
