package com.tecsun.sisp.fun.common;

import com.tecsun.sisp.fun.modules.controller.common.param.TransLogParam;

import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ClassName: JXLExcelView
 * Description: 业务分析子系统导出
 * CreateTime： 2015年08月03日 17时:44分
 */
public class JXLExcelView {

    private static Logger logger = LoggerFactory.getLogger(JXLExcelView.class);

    public String getFileName() {
        SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String name = datetime.format(time);
        return name;
    }

    public void export(Map map,HttpServletResponse response) {
        try {
            String[] columnNames = new String[]{};
            List<TransLogParam> list = (List) map.get("list");
            String[] titles = (String[]) map.get("titles");
            if (null != titles && titles.length > 0) {
                columnNames = titles;
            }
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("服务使用汇总");
            HSSFRow header = sheet.createRow(0);
            HSSFFont font = wb.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
            font.setFontName("黑体"); //字体
            font.setFontHeightInPoints((short) 14);// 字体大小
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); //宽度
            HSSFCellStyle headcellStyle = wb.createCellStyle();
            headcellStyle.setFont(font);
            headcellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); //水平布局：居中
            headcellStyle.setWrapText(true);

            HSSFCellStyle bodycellStyle = wb.createCellStyle();
            bodycellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
            bodycellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中
            bodycellStyle.setWrapText(true);
            for (int i = 0; i < columnNames.length; i++) {
                HSSFCell cell = header.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(headcellStyle);
                sheet.setColumnWidth(i,i==0?6000:3500);
            }
            NumberFormat nt = NumberFormat.getPercentInstance();
            nt.setMinimumFractionDigits(2);
            for (int i = 0; i < list.size(); i++) {
                HSSFRow row = sheet.createRow(i + 1);
                TransLogParam element = list.get(i);
                HSSFCell c1=row.createCell(0);
                c1.setCellStyle(bodycellStyle);
                c1.setCellValue(DefualtUseData.getDictName("bus_code", element.getBusinesscode()));
                HSSFCell c2=row.createCell(1);
                c2.setCellStyle(bodycellStyle);
                c2.setCellValue(element.getCount());
                HSSFCell c3=row.createCell(2);
                c3.setCellStyle(bodycellStyle);
                double b=element.getCount().doubleValue() / element.getAllbusicount().doubleValue();
                c3.setCellValue(nt.format(b));
            }
            String fileName="服务使用汇总_"+getFileName()+".xls";
            //tomcate 6:response.setContentType("application/vnd.ms-excel,charset=UTF-8");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");//tomcate 7
            response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出Excel异常："+e.getMessage());
        }
    }

}
