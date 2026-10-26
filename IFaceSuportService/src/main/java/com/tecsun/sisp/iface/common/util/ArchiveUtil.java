package com.tecsun.sisp.iface.common.util;

import com.tecsun.sisp.iface.common.vo.ArchiveRequstBean;
import com.tecsun.sisp.iface.common.vo.HisZxRequstBean;
import com.tecsun.sisp.iface.common.vo.card.CardBaseInfo;
import com.tecsun.sisp.iface.server.outerface.card.CardInfoBus;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015/8/12.
 */

public class ArchiveUtil {
    public static final Logger logger = Logger.getLogger(ArchiveUtil.class);

    /*
 @ 组装报文的开始部分
 */
    public static String setStartXml(ArchiveRequstBean bean){
        StringBuffer startXml = new StringBuffer();
        startXml.append("<?xml version='1.0' encoding='utf-8'?>")
                .append("<StorageQuery>");
        return startXml.toString();
    }

    public static String setBase64EncoderXml(final byte[] bytes) {
        if ((bytes != null) && (bytes.length != 0)) {
            return new String(Base64.encodeBase64(bytes));
        }
        return "";
    }
    public  static byte[] getBase64DecoderXml(String xml){
        if((xml!=null) && !"".equals(xml)){
            return Base64.decodeBase64(xml.getBytes());
        }
        return  "".getBytes();
    }

    /*
   @ 组装报文的结束部分
   */
    public static String setEndXml(ArchiveRequstBean bean){
        StringBuffer endXml = new StringBuffer();
        endXml.append("</StorageQuery>");
        return endXml.toString();
    }

    /*
     <?xml version=’1.0’ encoding="UTF-8"?>
        <!--type业务类型 -->
        <StorageQuery>
        <row>
            <!--name：字段名
                value：字段值
        -->
        <column name='aac002'>1</column>
        <column name='aae017'>42</column>
        <column name='acc709'>2014-07-22</column>
        <column name='acc709_1'>2014-07-22</column>
        </row>
        </StorageQuery>
     @ 1.1 获取档案信息
     @ 1、组装报文的开始部分 setStartXml
     @ 2、组装文的报开始部分setEndXml
     @ 3、组装报文的结束部分 setEndXml
     */
    public String getArchiveListXml(ArchiveRequstBean bean,String AAE017){
        StringBuffer contextXml = new StringBuffer();
        String aae017=StringUtils.isEmpty(AAE017)?"":AAE017;
        String aac002=StringUtils.isEmpty(bean.getAAC002())?"":bean.getAAC002();
        String acc709=StringUtils.isEmpty(bean.getACC709())?"":bean.getACC709();
        String acc709_1=StringUtils.isEmpty(bean.getACC709_1())?"":bean.getACC709_1();
        String acc70b=StringUtils.isEmpty(bean.getACC70B())?"":bean.getACC70B();
        String aaf036=StringUtils.isEmpty(bean.getAAF036())?"":bean.getAAF036();
        String aaf036_1=StringUtils.isEmpty(bean.getAAF036_1())?"":bean.getAAF036_1();
        String ace750=StringUtils.isEmpty(bean.getACE750())?"":bean.getACE750();
        contextXml.append(setStartXml(bean))
                .append("<row>")
                .append("<column name='aae017'>" +aae017+ "</column>")//机构编号
                .append("<column name='aac002'>" + aac002+ "</column>")//身份证号
                .append("<column name='acc709'>" + acc709+ "</column>")//接收日期开始
                .append("<column name='acc709_1'>" + acc709_1+ "</column>")//接收日期截止
                .append("<column name='acc70b'>" +acc70b + "</column>")//入库状态
                .append("<column name='aaf036'>" +aaf036 + "</column>")//最后一次修改时间开始
                .append("<column name='aaf036_1'>" + aaf036_1+ "</column>")//最后一次修改时间止
                .append("<column name='ace750'>" + ace750+ "</column>")//数据更新标识
                .append("</row>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }
}
