package com.tecsun.sisp.iface.common.util;

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

public class ZxHisUtil {
    public static final Logger logger = Logger.getLogger(ZxHisUtil.class);

    /*
@ 生成BASE64加密
*/
 /*   public static String setBase64EncoderXml(String xml){
        String strXml ="";
        if(StringUtils.isNotEmpty(xml)){
            try {
                strXml = new BASE64Encoder().encode(xml.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("生成BASE64加密失败");
            }
        }else{
            logger.error("生成BASE64加密失败");
        }
        return strXml;
    }*/

    public static String setBase64EncoderXml(final byte[] bytes) {
        if ((bytes != null) && (bytes.length != 0)) {
            return new String(Base64.encodeBase64(bytes));
        }
        return "";
    }

    public static String setBase64EncoderXml_Zx(final byte[] bytes) {
        if ((bytes != null) && (bytes.length != 0)) {
            return new String(Base64.encodeBase64(bytes));
        }
        return "";
    }

      /*  public static String  setBase64EncoderXml1(byte[] bytes) {
            if ((bytes != null) && (bytes.length != 0)) {
                return new String(Base64.encodeBase64(bytes));
            }
            return "";
        }*/

    public  static byte[] getBase64DecoderXml_Zx(String xml){
        if((xml!=null) && !"".equals(xml)){
            return Base64.decodeBase64(xml.getBytes());
        }
        return  "".getBytes();
    }
    /*
@ 解密BASE64
*/
    public static String getBase64DecoderXml(String xml){
        String strXml ="";
        if(StringUtils.isNotEmpty(xml)){
            try {
                strXml = new String(Base64.decodeBase64(xml));
                strXml = new String(strXml.getBytes(),"UTF8");
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("生成BASE64解密失败");
            }
        }else{
            logger.error("生成BASE64解密失败");
        }
        return strXml;

    }

    /*
 @ 组装报文的开始部分
 */
    public static String setStartXml(HisZxRequstBean bean){
        StringBuffer startXml = new StringBuffer();
        startXml.append("<?xml version='1.0' encoding='utf-8'?>")
                .append("<request>")
                .append("<requestid>" + bean.getRequestid() + "</requestid>");
        return startXml.toString();
    }

    /*
       @ 组装分页报文
       */
    public static String getHeadXml(HisZxRequstBean bean){
        StringBuffer headXml = new StringBuffer();
        headXml.append("<pageactionin>")
               .append("<currentpagenum>" + bean.getCurrentpagenum() + "</currentpagenum>")//当前页码
               .append("<rowsperpage>" + bean.getRowsperpage() + "</rowsperpage>")//每页行数
               .append("<pageaction>" + bean.getPageaction() + "</pageaction>")//翻页动作
               .append("<topagenum>" + bean.getTopagenum() + "</topagenum>")//翻到哪一页
               .append("</pageactionin>");
        return headXml.toString();
    }
    /*
   @ 组装报文的结束部分
   */
    public static String setEndXml(HisZxRequstBean bean){
        StringBuffer endXml = new StringBuffer();
        endXml.append("</request>");
        return endXml.toString();
    }

    /*
     @ 1.1 获取医院信息
     @ 1、组装报文的开始部分 setStartXml
     @ 2、组装分页报文  getHeadXml();
     @ 3、组装文的报开始部分setEndXml
     @ 4、组装报文的结束部分 setEndXml
     */
    public String getHospitalListXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append(getHeadXml(bean))
                .append("<retrieveargs>")
                .append("<begindate>"+bean.getBegindate()+"</begindate>")//最后修改时间
                .append("<hospitalid>"+bean.getHospitalid()+"</hospitalid>")//医院代号
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
    @ 1.2  获取科室信息
    @ 1、组装报文的开始部分 setStartXml
    @ 2、组装分页报文  getHeadXml();
    @ 3、组装文的报开始部分setEndXml
    @ 4、组装报文的结束部分 setEndXml
    @ param
    <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <pageactionin>
            <currentpagenum>当前页码</currentpagenum>
            <rowsperpage>每页行数</rowsperpage>
            <pageaction>翻页动作</pageaction>
            <topagenum>翻到哪一页</topagenum>
            </pageactionin>
            <retrieveargs>
            <begindate>最后修改时间</begindate>
            <hospitalid>医院代号</hospitalid>
            <deptcode>科室代号</deptcode>
            </retrieveargs>
        </request>
    */
    public String getDeptmentListXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append(getHeadXml(bean))
                .append("<retrieveargs>")
                .append("<begindate>"+bean.getBegindate()+"</begindate>")//最后修改时间
                .append("<hospitalid>"+bean.getHospitalid()+"</hospitalid>")//医院代号
                .append("<deptcode>"+bean.getDeptcode()+ "</deptcode>")//科室代号
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

 /*
    @   1.3  获取医生信息
    @ 1、组装报文的开始部分 setStartXml
    @ 2、组装分页报文  getHeadXml();
    @ 3、组装文的报开始部分setEndXml
    @ 4、组装报文的结束部分 setEndXml
    @  param
    <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <pageactionin>
                <currentpagenum>当前页码</currentpagenum>
                <rowsperpage>每页行数</rowsperpage>
                <pageaction>翻页动作</pageaction>
                <topagenum>翻到哪一页</topagenum>
            </pageactionin>
            <retrieveargs>
                <begindate>最后修改时间</begindate>
                <hospitalid>医院代号</hospitalid>
                <doctorno>医生编号</doctorno>
            </retrieveargs>
        </request>
    */
 public String getDoctorListXml(HisZxRequstBean bean){
     StringBuffer contextXml = new StringBuffer();
     contextXml.append(setStartXml(bean))
             .append(getHeadXml(bean))
             .append("<retrieveargs>")
             .append("<begindate>"+bean.getBegindate()+"</begindate>")//最后修改时间
             .append("<hospitalid>"+bean.getHospitalid()+"</hospitalid>")//医院代号
             .append("<doctorno>"+bean.getDoctorno()+"</doctorno>")//医生编号
            .append("<deptcode>"+bean.getDeptcode()+"</deptcode>")//科室代码
             .append("</retrieveargs>")
             .append(setEndXml(bean));
     return contextXml.toString();
 }

    /*
    @   1.4获取指定医生的当前可约状态
    @ 1、组装报文的开始部分 setStartXml
    @ 2、组装分页报文  getHeadXml();
    @ 3、组装文的报开始部分setEndXml
    @ 4、组装报文的结束部分 setEndXml
    @  param
    @ 1.request->retrieveargs->hospitalid 医院代号 string 必填项
    @ 2.request->retrieveargs->deptcode 科室代码 string 可选项
    @ 3.request->retrieveargs->doctorno 医生编号 string 可选项，多个医生变化用,分开

    <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <pageactionin>
                <currentpagenum>当前页码</currentpagenum>
                <rowsperpage>每页行数</rowsperpage>
                <pageaction>翻页动作</pageaction>
                <topagenum>翻到哪一页</topagenum>
            </pageactionin>
            <retrieveargs>
                <hospitalid>医院代号</hospitalid>
                <deptcode>科室代码</deptcode>
                <doctorno>医生编号</doctorno>
            </retrieveargs>
        </request>
    */
    public String getDoctorScheduleFlagXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append(getHeadXml(bean))
                .append("<retrieveargs>")
                .append("<hospitalid>"+ bean.getHospitalid() + "</hospitalid>")//医院代号
                .append("<deptcode>"+bean.getDeptcode()+"</deptcode>")//科室代号
                .append("<doctorno>"+bean.getDoctorno()+"</doctorno>")//医生编号
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
   @   1.5 取一个医生的排班列表
   @ 1、组装报文的开始部分 setStartXml
   @ 2、组装分页报文  getHeadXml();
   @ 3、组装文的报开始部分setEndXml
   @ 4、组装报文的结束部分 setEndXml
   @   param
   @   1.request-> requestid 访问者 id string
   @   2.request-> retrieveargs-> hospitalid 医院代号 string 不可为空
   @   3.request-> retrieveargs-> doctorno 医生编码 string 不可为空
   <?xml version=”1.0” encoding=”utf-8”?>
        <request>
        <requestid>访问者 id</requestid>
        <retrieveargs>
            <hospitalid>医院代号</hospitalid>
            <doctorno>医生编码</doctorno>
        </retrieveargs>
        </request>
    */
    public String getDoctorScheduleXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
               // .append(getHeadXml(bean))
                //.append("<requestid>" + bean.getRequestid() + "</requestid>")
                .append("<retrieveargs>")
                .append("<hospitalid>"+ bean.getHospitalid() + "</hospitalid>")//医院代号
                .append("<doctorno>"+ bean.getDoctorno()+ "</doctorno>")//医生编码
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
      @ 1.6 获取具体日期门诊安排的号源情况( 列表)
      @ 1、组装报文的开始部分 setStartXml
      @ 2、组装分页报文  getHeadXml();
      @ 3、组装文的报开始部分setEndXml
      @ 4、组装报文的结束部分 setEndXml
      @   param
      <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <pageactionin>
                <currentpagenum>当前页码</currentpagenum>
                <rowsperpage>每页行数</rowsperpage>
                <pageaction>翻页动作</pageaction>
                <topagenum>翻到哪一页</topagenum>
            </pageactionin>
            <retrieveargs>
                < outpdate_start>门诊日期开始</outpdate_start>
                < outpdate_end>门诊日期结束</outpdate_end>
                <timeinterval>时间段</timeinterval >
                <hospitalid>医院代号</hospitalid>
                <deptcode>科室代码</deptcode>
                <doctorno>医生编码</doctorno>
                <specialty>专长</specialty>
            </retrieveargs>
        </request>

      @ 1.request-> retrieveargs-> outpdate_start 门 诊 日 期 开 始 datetime yyyy-mm-dd 不可为空 格式：yyyy-mm-dd 必须不小于今天
      @ 2.request-> retrieveargs-> outpdate_end 门 诊 日 期 结 束 datetime yyyy-mm-dd 可为空 格式：yyyy-mm-dd
      @ 3.request-> retrieveargs-> timeinterval >时间段 string 取值：am 上午 pm 下午 al 全天 可为空
      @ 4.request-> retrieveargs-> hospitalid 医院代号 string 不可为空
      @ 5.request-> retrieveargs-> deptcode 科室代码 string 可为空
      @ 6.request-> retrieveargs-> doctorno 医生编码 string 可为空
      @ 7.request-> retrieveargs->specialty 医生专长 string 可为空 增加以 上专长可以实现快速根据专长进行关联查询( 逻辑为 Like，包含关系)
       */
    public String getScheduleListXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append(getHeadXml(bean))
                .append("<retrieveargs>")
                .append("<outpdate_start>"+ bean.getOutpdate_start() + "</outpdate_start>")//门诊日期开始
                .append("<outpdate_end>" + bean.getOutpdate_end() + "</outpdate_end>")//门诊日期结束
                .append("<timeinterval>" + bean.getTimeinterval() + "</timeinterval>")//时间段
                .append("<hospitalid>" + bean.getHospitalid() + "</hospitalid>")//医院代号
                .append("<deptcode>" + bean.getDeptcode() + "</deptcode>")//科室代码
                .append("<doctorno>" + bean.getDoctorno() + "</doctorno>")//doctorno
                .append("<specialty>" + bean.getSpecialty() + "</specialty>")//医生专长
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
     @ 1.7 取一个日排班的分时信息
     @ 1、组装报文的开始部分 setStartXml
     @ 2、组装分页报文  getHeadXml();
     @ 3、组装文的报开始部分setEndXml
     @ 4、组装报文的结束部分 setEndXml
     @   param

     <?xml version=”1.0” encoding=”utf-8”?>
        <request>
        <requestid>访问者 id</requestid>
        <retrieveargs>
            <hospitalid>医院代号</hospitalid>
            <scheduleid >日排班 ID</scheduleid >
        </retrieveargs>
        </request>
     @ 1.request-> requestid 访问者 id string
     @ 2.request-> retrieveargs-> hospitalid 医院代号 string 不可为空
     @ 3.request-> retrieveargs-> scheduleid 日排班 ID string 不可为空
      */
    public String getSchedulePartTimeXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
               // .append(getHeadXml(bean))
                .append("<retrieveargs>")
                .append("<hospitalid>"+ bean.getHospitalid() + "</hospitalid>")//医院代号
                .append("<scheduleid>"+ bean.getScheduleid()+ "</scheduleid>")//日排班 ID
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*    电子文档和纸质文档参数不一样 XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
         @ 1.8 生成挂号订单并申请锁号
         @ 1、组装报文的开始部分 setStartXml
         @ 2、组装分页报文  getHeadXml();
         @ 3、组装文的报开始部分setEndXml
         @ 4、组装报文的结束部分 setEndXml
         @   param

         <?xml version=”1.0” encoding=”utf-8”?>
            <request>
            <requestid>访问者 id</requestid>
            <inputvalues>
                <reservemobile>手机号码</reservemobile>
                <hospitalid>医院 ID </hospitalid>
                <scheduleid>日排班 id</scheduleid>
                <parttimeid>分时 ID</parttimeid>
                <creator>创建人（工号）</creator>
                <jsonext>JSON 格式字符串</jsonext>
            </inputvalues>
            </request>
         @ 1.request-> requestid 访问者 id string
         @ 2. request-> inputvalues-> reservemobile 手机号码 string
         @ 3.request-> inputvalues-> scheduleid 日排班 id string 不能为空
         @ 4.request-> inputvalues->parttimeid 分时 ID String 可以为空
         @ 5.request->inputvalues->creator 创建人 string 可以为空
         @
          */
    public String getNewOrderXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                //.append(getHeadXml(bean))
               // .append("<requestid>" + bean.getRequestid() + "</requestid>")
                .append("<inputvalues>")
                .append("<reservemobile>"+ bean.getReservemobile() + "</reservemobile>")//手机号码
                .append("<scheduleid>" + bean.getScheduleid() + "</scheduleid>")//日排班 id
                .append("<hospitalid>"+bean.getHospitalid()+"</hospitalid>")//医院 ID
                .append("<parttimeid>" + bean.getParttimeid() + "</parttimeid>")//分时 ID
                .append("<creator>" + bean.getCreator() + "</creator>")//创建人
                .append("</inputvalues>")

                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
         @ 1.9 查询锁号是否成功
         @ 1、组装报文的开始部分 setStartXml
         @ 2、组装报文的结束部分 setEndXml
         @   param
          <?xml version=”1.0” encoding=”utf-8”?>
            <request>
            <requestid>访问者 id</requestid>
            <inputvalues>
            <orderid>订单号</orderid>
            </inputvalues>
            </request>
         @ 1.request->requestid->访问者 id string 由中间平台提供
         @ 4.inputvalues-> orderid pinkey 码 string
          */
    public String checkLockXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                //.append("<requestid>"+bean.getRequestid()+"</requestid>")
                .append("<inputvalues>")
                .append("<orderid>" + bean.getOrderid() + "</orderid>")//订单号
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
        @ 1.10更新订单信息 
        @ 1、组装报文的开始部分 setStartXml
        @ 2、组装报文的结束部分 setEndXml
        @   param
         <?xml version=”1.0” encoding=”utf-8”?>
            <request>
                <requestid>访问者 id</requestid>
                <inputvalues>
                    <orderid>订单号</orderid>
                    <certtypeno>证件类型</certtypeno>
                    <patientidcard>证件号</patientidcard>
                    <patientname>病人姓名</patientname>
                    <patientsex>病人性别</patientsex>
                    <patientbirthday>出生日期</patientbirthday>
                    <medicalrecord>病历号</medicalrecord>
                    <contactphone>联系电话</contactphone>
                    <familyaddress>家庭地址</familyaddress>
                        <data>
                            <data_row>
                            <field_code>字段代码</field_code>
                            <field_value>字段取值 </field_value>
                            </data_row>
                        </data>
                </inputvalues >
            </request>
         */
    public String updateOrderXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
               // .append("<requestid>"+bean.getRequestid()+"</requestid>")
                .append("<inputvalues>")
                .append("<orderid>" + bean.getOrderid() + "</orderid>")//订单号
                .append("<certtypeno>" + bean.getCerttypeno() + "</certtypeno>")//证件类型
                .append("<patientidcard>" + bean.getContactphone() + "</patientidcard>")//证件号改：就诊人手机号码
                .append("<patientname>" + bean.getPatientname() + "</patientname>")//病人姓名
                .append("<patientsex>" + bean.getPatientsex() + "</patientsex>")//病人性别
                .append("<patientbirthday>" + bean.getPatientbirthday() + "</patientbirthday>")//出生日期
                .append("<medicalrecord>" + bean.getMedicalrecord() + "</medicalrecord>")//病历号
                .append("<contactphone>" + bean.getContactphone() + "</contactphone>")//联系电话
                .append("<familyaddress>" + bean.getFamilyaddress() + "</familyaddress>")//家庭地址
                .append("<data>")
                .append("<data_row>")
                .append("<field_code></field_code>")
                .append("<field_value></field_value>")
                .append("</data_row>")
                .append("</data>")
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
        @ 1.11   提交订单生效
        @ 1、组装报文的开始部分 setStartXml
        @ 2、组装报文的结束部分 setEndXml
        @   param
         <?xml version=”1.0” encoding=”utf-8”?>
            <request>
                <requestid>访问者 id.</requestid>
                <inputvalues>
                <orderid>订单号</orderid>
                <paymentcode>支付方式代码</paymentcode>
                </inputvalues>
            </request>
         */
    public String confirmOrderXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append("<inputvalues>")
                .append("<orderid>" + bean.getOrderid() + "</orderid>")//订单号
                .append("<paymentcode>" + bean.getPaymentcode() + "</paymentcode>")//支付方式代码
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
        @ 1.12   取历史订单列表
        @ 1、组装报文的开始部分 setStartXml
        @ 2、组装报文的结束部分 setEndXml
        @   param
         <?xml version=”1.0” encoding=”utf-8”?>
            <request>
                <requestid>访问者 id</requestid>
                <pageactionin>
                    <currentpagenum>当前页码</currentpagenum>
                    <rowsperpage>每页行数</rowsperpage>
                    <pageaction>翻页动作</pageaction>
                    <topagenum>翻到哪一页</topagenum>
                </pageactionin>
                <inputvalues>
                    <pinkey>数字订单号</pinkey>
                    <reservemobile>预约人手机号码</reservemobile>
                    <patientname>就诊人姓名</patientname>
                    <patientidcard>就诊人证件号码</patientidcard>
                    <creator>创建人（工号）</creator>
                    <orderstate>订单状态</orderstate>
                </inputvalues>
            </request>
         */
    public String getOrderInfoXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append(getHeadXml(bean))
                .append("<inputvalues>")
                .append("<pinkey>" + bean.getPinkey() + "</pinkey>")//数字订单号
                .append("<reservemobile>" + bean.getReservemobile() + "</reservemobile>")//预约人手机号码
                .append("<patientname>" + bean.getPatientname() + "</patientname>")//就诊人姓名
                .append("<patientidcard>" + bean.getContactphone() + "</patientidcard>")//改：就诊人手机号码
                .append("<creator>" + bean.getCreator() + "</creator>")//创建人（工号）
                .append("<orderstate>" + bean.getOrderstate() + "</orderstate>")//订单状态
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
        @ 1.13   取消订单
        @ 1、组装报文的开始部分 setStartXml
        @ 2、组装报文的结束部分 setEndXml
        @   param
         <?xml version=”1.0” encoding=”utf-8”?>
            <request>
                <requestid>访问者 id</requestid>
                <inputvalues>
                    <orderid>订单号</orderid>
                    <pinkey>数字订单号</pinkey>
                    <cancelreason>取消原因 </cancelreason>
                    <operator>操作人</oprator>
                </inputvalues >
            </request>
         */
    public String cancelOrderXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
               // .append("<requestid>"+bean.getRequestid()+"</requestid>")
                .append("<inputvalues>")
                .append("<orderid>" + bean.getOrderid() + "</orderid>")//订单号
                .append("<pinkey>" + bean.getPinkey() + "</pinkey>")//数字订单号
                .append("<cancelreason>" + bean.getCancelreason() + "</cancelreason>")//取消原因
                .append("<operator>" + bean.getOperator() + "</operator>")//操作人
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
        @ 1.14 确定退费
        @ 1、组装报文的开始部分 setStartXml
        @ 2、组装报文的结束部分 setEndXml
        @   param
        <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <inputvalues>
                <orderid>订单 ID</orderid>
                <audopertor>审核人</audopertor>
                <backfeemode>退费方式</backfeemode>
            </inputvalues >
        </request>
         */
    public String refundXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append("<inputvalues>")
                .append("<orderid>" + bean.getPinkey() + "</orderid>")//订单号
                .append("<audopertor>" + bean.getAudopertor() + "</audopertor>")//审核人
                .append("<backfeemode>" + bean.getBackfeemode() + "</backfeemode>")//退费方式
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
       @ 1.15 取短信文本
       @ 1、组装报文的开始部分 setStartXml
       @ 2、组装报文的结束部分 setEndXml
       @   param
      <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <inputvalues>
                  <pinkey>数字订单号</pinkey>
            </inputvalues >
        </request>
        */
    public String getSmsXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append("<inputvalues>")
                .append("<orderid>" + bean.getPinkey() + "</orderid>")//订单号
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
       @ 1.16 获取指定排班的当前可约状态
       @ 1、组装报文的开始部分 setStartXml
       @ 2、组装报文的结束部分 setEndXml
       @   param
     <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <retrieveargs>
                <hospitalid>医院代号</hospitalid>
                <scheduleid >排班 ID</scheduleid>
            </retrieveargs>
        </request>
        */
    public String getScheduleFlagXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append("<inputvalues>")
                .append("<hospitalid>" + bean.getHospitalid() + "</hospitalid>")//医院代号
                .append("<scheduleid>" + bean.getScheduleid() + "</scheduleid>")//排班
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }

    /*
      @ 1.17   获取退费记录接口
      @ 1、组装报文的开始部分 setStartXml
      @ 2、组装报文的结束部分 setEndXml
      @   param
    <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id</requestid>
            <pageactionin>
                <currentpagenum>当前页码</currentpagenum>
                <rowsperpage>每页行数</rowsperpage>
                <pageaction>翻页动作</pageaction>
                <topagenum>翻到哪一页</topagenum>
            </pageactionin>
        </request>
       */
    public String getRefundListXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                  .append(getHeadXml(bean))
                  .append(setEndXml(bean));
        return contextXml.toString();
    }
    /*
        @ 1.18 获取医院信息
        @ 1、组装报文的开始部分 setStartXml
        @ 2、组装分页报文  getHeadXml();
        @ 3、组装文的报开始部分setEndXml
        @ 4、组装报文的结束部分 setEndXml

        <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id.</requestid>
            <inputvalues>
                <orderid>订单号</orderid>
                <payType>支付类型 （支付宝、 微信、 银行卡、 医保卡、 其他） </payType>
                <outTradeNo>商户订单号</outTradeNo>
                <payId>支付 ID</payId>
                <payAmount>支付金额（元）</payAmount>
                <userNo>用户标识</userNo>
                <createIp>生成订单客户端 IP</createIp>
                <payTime>支付时间</payTime>
                <termId>设备终端号</termId>
                <reqReserved>透传信息</reqReserved>
                <extInfo>
                <ext1></ext1>
                <ext2></ext2>
                <ext3></ext3>
                <ext4></ext4>
                <ext5></ext5>
                <ext6></ext6>
                <extInfo/>
            </inputvalues>
        </request>
        
        
        
        
        
         <?xml version=”1.0” encoding=”utf-8”?>
        <request>
            <requestid>访问者 id.</requestid>
            <inputvalues>
                <orderid>订单号</orderid>
                <outTradeNo>排班id</outTradeNo>
                <userno>个人编号</userno>
                <payDate>医疗类别</payDate>
                <payamount>人员状态</payamount>
                <termId>医保卡号</termId>
                
                <payType>住院流水号(门诊流水号)</payType>
                <ext1>参保状态</ext1>
                <ext2>姓名</ext2>
                <ext3>性别</ext3>
                <ext4>身份证号</ext4>
                <ext5>生日</ext5>
                <ext6>医疗人员类别</ext6>
                <ext7>单位名称</ext7>
                <ext8>费用金额</ext8>  //这个挂号费么
                <ext9>帐户支付</ext9>  // 什么意思
                <ext10>统筹支付</ext10> //什么意思
                <ext11>本次大病支出</ext11>//什么意思
                <ext12>本次公务员补助支出</ext12>//什么意思
                
                <ext13>现金支付</ext13> //什么意思
                <ext14>自费总金额</ext14>//什么意思
                <ext15>个人帐户余额</ext15>
                <ext16>所属区号</ext16>
                <payid>单据号</payid> //什么意思
               
            </inputvalues>
        </request>
        
        */
    public String confirmOrderPayXml(HisZxRequstBean bean){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = df.format(new Date());// new Date()为获取当前系统时间
        
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
	     SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd");
	     String sfstr = "";
	       try {
		    	 if(bean!=null && !bean.getExt5().isEmpty()){
		    		 sfstr = sf2.format(sf1.parse(bean.getExt5()));
		    	 }
		   } catch (Exception e) {
		       e.printStackTrace();
		   }
        
        
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
            /*    .append("<requestid>" + bean.getRequestid() + "</requestid>")//访问者 id
                .append("<inputvalues>")
                .append("<orderid>" + bean.getOrderid() + "</orderid>")//订单号
                .append("<payType>" +bean.getPayType()+"</payType>")//支付类型 （支付宝、 微信、 银行卡、 医保卡、 其他）
                .append("<outTradeNo>" + bean.getOutTradeNo() + "</outTradeNo>")//商户订单号
                .append("<payId>" + bean.getPayId() + "</payId>")//支付 ID
                .append("<payAmount>" +bean.getPayAmount() +"</payAmount>")//支付金额（元）
                .append("<userNo>" +bean.getUserNo() +"</userNo>")//用户标识
                .append("<createIp>" +bean.getCreateIp() +"</createIp>")//生成订单客户端 IP
                .append("<payTime>" + current+"</payTime>")//支付时间
                .append("<termId>" +bean.getTermId() +"</termId>")//设备终端号
                .append("<reqReserved>" +bean.getRequestid() +"</reqReserved>")//透传信息
                .append("</inputvalues>")*/
        
        
        //.append("<requestid>" + bean.getRequestid() + "</requestid>")//访问者 id
        .append("<inputvalues>")
        .append("<orderid>" + bean.getOrderid() + "</orderid>")//订单号
        .append("<outTradeNo>"+bean.getOutTradeNo()+"</outTradeNo>")//排班id
        .append("<userno>"+bean.getUserno()+"</userno>")//个人编号
        .append("<payTime>"+nowTime+"</payTime>")//支付时间(系统本地时间)
        .append("<payamount>"+bean.getPayamount()+"</payamount>")//人员状态
        .append("<termId>"+bean.getTermId()+"</termId>")//医保卡号
        .append("<payType>"+bean.getPayType()+"</payType>")//住院流水号(门诊流水号)
        .append("<payid>"+bean.getPayid()+"</payid>")//单据号 
        .append("<extInfo>")
        .append("<ext1>"+bean.getExt1()+"</ext1>")//参保状态
        .append("<ext2>"+bean.getExt2()+"</ext2>")//姓名
        .append("<ext3>"+bean.getExt3()+"</ext3>")//性别
        .append("<ext4>"+bean.getExt4()+"</ext4>")//身份证号
        .append("<ext5>"+sfstr+"</ext5>")//生日
        .append("<ext6>"+bean.getExt6()+"</ext6>")//医疗人员类别
	    .append("<ext7>"+bean.getExt7()+"</ext7>")//单位名称
	    .append("<ext8>"+bean.getExt8()+"</ext8> ")//费用金额
	    .append("<ext9>"+bean.getExt9()+"</ext9>")  //帐户支付
	    .append("<ext10>"+bean.getExt10()+"</ext10>")//统筹支付
	    .append("<ext11>"+bean.getExt11()+"</ext11>")//本次大病支出
	    .append("<ext12>"+bean.getExt12()+"</ext12>") //本次公务员补助支出
	    .append("<ext13>"+bean.getExt13()+"</ext13>") //现金支付
	    .append("<ext14>"+bean.getExt14()+"</ext14>")//自费总金额
	    .append("<ext15>"+bean.getExt15()+"</ext15>")//个人帐户余额
	    .append("<ext16>"+bean.getExt16()+"</ext16>")//所属区号
	    .append("<ext17>"+bean.getExt17()+"</ext17>")//医疗类别
	    .append("</extInfo>")
        .append("</inputvalues>")
        .append(setEndXml(bean));
        return contextXml.toString();
    }
    
    
    
    public static String getStringDate() { 
        Date currentTime = new Date(); 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); 
        String dateString = formatter.format(currentTime); 
        return dateString; 
    } 

    

    /** 
     * 两个时间之间的天数 
     */ 
    public static long getDays(String date1, String date2) { 
        if (date1 == null || date1.equals("")) 
            return 0; 
        if (date2 == null || date2.equals("")) 
            return 0; 
        // 转换为标准时间 
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd"); 
        java.util.Date date = null; 
        java.util.Date mydate = null; 
        try { 
            date = myFormatter.parse(date1); 
            mydate = myFormatter.parse(date2); 
        } catch (Exception e) { 
        } 
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000); 
        return Math.abs(day); 
    }
    
    /**
     * 检查诊疗卡是否绑定入参报文
     * @param bean
     * @return
     * <?xml version=”1.0” encoding=”utf-8”?>
		<request>
		  <requestid>访问者id</requestid>
		 <retrieveargs>
		    <hospitalId>医院ID</hospitalId>
		    <cardNO>卡号</cardNO>
		    <cardType>卡类型</cardType>
		    <ext1>扩展信息</ext1>
		    <ext2>扩展信息</ext2>
		    <ext3>扩展信息</ext3>
		  </retrieveargs>
		</request>
     */
    public String getCheckCardSignXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                //.append("<requestid>" + bean.getRequestid() + "</requestid>")
                .append("<Retrieveargs>")
                .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院代号
                .append("<cardNO>"+bean.getCardNO()+"</cardNO>")//卡号
                .append("<cardType>"+bean.getCardtype()+"</cardType>")//卡类型
                .append("<ext1>"+bean.getExt1()+"</ext1>")//扩展信息
                .append("<ext2>"+bean.getExt2()+"</ext2>")//扩展信息
                .append("<ext3>"+bean.getExt3()+"</ext3>")//扩展信息
                .append("</Retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }
    
    /**
     * 新患者HIS绑定接口入参报文
     * @param bean
     * @return
     * <hospitalId>医院ID</hospitalId>
<patientId>患者ID</patientId>
    <cardType>卡类型</cardType>
    <cardNO>卡号</cardNO>
    <name>姓名</name>
    <sex>性别</sex>
    <dob>生日</dob>
    <idCode>证件号</idCode>
    <idType>证件类型</idType>
    <address>家庭住址</address>
    <ext1>医保卡号</ext1>
    <ext2>扩展信息</ext2>
    <ext3>手机号码</ext3>
     */
    public String getCardSignNewPatientXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
               // .append("<requestid>" + bean.getRequestid() + "</requestid>")
                .append("<inputvalues>")
                .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院代号
                .append("<patientId>"+bean.getPatientId()+"</patientId>")//患者ID
                .append("<cardType>"+bean.getCardType()+"</cardType>")//卡类型
                .append("<cardNO>"+bean.getBankno()+"</cardNO>")//卡号
                .append("<name>"+bean.getPatientname()+"</name>")//姓名
                .append("<sex>"+bean.getPatientsex()+"</sex>")//性别
                .append("<dob>"+formatDate(bean.getPatientbirthday())+"</dob>")//生日
                .append("<idCode>"+bean.getIdcard()+"</idCode>")//证件号
                .append("<idType>"+bean.getCerttypeno()+"</idType>")//证件类型
                .append("<address>"+bean.getFamilyaddress()+"</address>")//家庭住址
                .append("<ext1>"+bean.getContactphone()+"</ext1>")//扩展信息-手机号码
                .append("<ext2>"+bean.getExt2()+"</ext2>")//扩展信息
                .append("<ext3>"+bean.getCardNO()+"</ext3>")//扩展信息-医保卡号
                .append("</inputvalues>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }
    
    /**
     * 
     * @param bean
     * @return
     * <?xml version=”1.0” encoding=”utf-8”?>
		<request>
		  <requestid>访问者id</requestid>
		 <retrieveargs>
		    <hospitalId>医院ID</hospitalId>
		<codeType>卡类型</codeType>
		<queryCode>查询号码</queryCode>
		    <patientName>患者姓名</patientName>
		    <idCard>身份证号码</idCard>
		    <ext1>扩展信息<ext1>
		    <ext2>扩展信息<ext2>
		    <ext3>扩展信息<ext3>
		    <ext4>扩展信息<ext4>
		  </retrieveargs>
		</request>
     */
    public String getPatientInfoXml(HisZxRequstBean bean){
        StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                //.append("<requestid>" + bean.getRequestid() + "</requestid>")
                .append("<retrieveargs>")
                .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院代号
                .append("<cardType>"+bean.getCardType()+"</cardType>")//卡类型
                .append("<cardNO>"+bean.getBankno()+"</cardNO>")//银行卡号
                .append("<name>"+bean.getPatientname()+"</name>")//姓名
                .append("<idCard>"+bean.getIdcard()+"</idCard>")//身份证号码
                .append("<ext1>"+bean.getExt1()+"</ext1>")//扩展信息
                .append("<ext2>"+bean.getExt2()+"</ext2>")//扩展信息
                .append("<ext3>"+bean.getExt3()+"</ext3>")//扩展信息
                .append("<ext4>"+bean.getExt4()+"</ext4>")//扩展信息
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
    }
    
   // 把yyyymmdd转成yyyy-MM-dd格式
    public static String formatDate(String str){
    	String sfstr = "";
    	if(!str.isEmpty()&&str.length()>0){
    		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sf2 =new SimpleDateFormat("yyyy-MM-dd");
            try {
             sfstr = sf2.format(sf1.parse(str));
         } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
         }
    	}
     return sfstr;
    }

    //获取银行卡号   社保卡号和身份证号码至少有一项不为空
    public String getBankNo(String cardno,String cernum)throws Exception{
        String bankNo = null;
        CardInfoBus bus = new CardInfoBus();
        if ((cardno != null && !cardno.equals("")) || (cernum != null && !cernum.equals(""))) {
            CardBaseInfo info = bus.getCardBaseInfo(cardno, cernum);
            bankNo=info.getBankno();
        }
        return bankNo;
    }
    /**
     * 【中心医院】现场挂号-获取代缴费的单据xml
     * @param bean
     * @return 
     <?xml version=” 1.0” encoding=” utf-8” ?>
		<request>
		<requestid>访问者 id</requestid>
		<retrieveargs>
		<hospitalId>医院代号</hospitalId>
		<patientId>患者 ID</patientId>
		<ext1>扩展信息<ext1>
		<ext2>扩展信息<ext2>
		<ext3>扩展信息<ext3>
		<ext4>扩展信息<ext4>
		</retrieveargs>
		</request>
     */
    public String getOutPatientPayBillsXml(HisZxRequstBean bean){
    	 StringBuffer contextXml = new StringBuffer();
         contextXml.append(setStartXml(bean))
             //    .append("<requestid>" + bean.getRequestid() + "</requestid>")
                 .append("<retrieveargs>")
                 .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院代号
                 .append("<patientId>"+bean.getPatientId()+"</patientId>")//患者 ID
                 .append("<ext1>"+bean.getExt1()+"</ext1>")//扩展信息
                 .append("<ext2>"+bean.getExt2()+"</ext2>")//扩展信息
                 .append("<ext3>"+bean.getExt3()+"</ext3>")//扩展信息
                 .append("<ext4>"+bean.getExt4()+"</ext4>")//扩展信息
                 .append("</retrieveargs>")
                 .append(setEndXml(bean));
         return contextXml.toString();
    }
    
    /**
     * 【中心医院】现场挂号-获取单据详情
     * @param bean
     * @return
     * <?xml version=” 1.0” encoding=” utf-8” ?>
		<request>
		<requestid>访问者 id</requestid>
		<retrieveargs>
		<hospitalid>医院代号</hospitalid>
		<billId>单据唯一 ID</billId>
		</retrieveargs>
		</request>
     */
    public String getOutPatientPayBillsDetialXml(HisZxRequstBean bean){
   	 StringBuffer contextXml = new StringBuffer();
        contextXml.append(setStartXml(bean))
                .append("<retrieveargs>")
                .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院代号
                .append("<billId>"+bean.getBillId()+"</billId>")//单据唯一 ID
                .append("</retrieveargs>")
                .append(setEndXml(bean));
        return contextXml.toString();
   }
    
    /**
     * 【中心医院】现场挂号-单据缴费
     * @param bean
     * @return
     * <?xml version=” 1.0” encoding=” utf-8” ?>
		<request>
		<requestid>访问者 id</requestid>
		<inputvalues>
		<patientId>患者 ID</patientId>
		<hospitalId>医院 ID</hospitalId>
		<payType>支付类型（支付宝、微信、其他） </payType>
		<outTradeNo>商户订单号</outTradeNo>
		<payId>支付 ID</payId>
		<payAmount>支付金额（元） </payAmount>
		<userNo>用户标识</userNo>
		<createIp>生成订单客户端 IP</createIp>
		<payTime>支付时间</payTime>
		<termId>设备终端号</termId>
		<extInfo>扩展信息（JSON 格式） <extInfo/>
		<reqReserved>透传信息</reqReserved>
		<bills>
		<bill>
		<billGuid>单据唯一 ID</billGuid>
		<billId>单据 ID</billId>
		<billMoney>单据金额（元） </billMoney>
		</bill>
		<bill>
		<billGuid>单据唯一 ID</billGuid>
		<billId>单据 ID</billId>
		<billMoney>单据金额（元） </billMoney>
		</bill>
		</bills>
		</inputvalues>
		</request>
     */
   public String outpatientPayBillsXml(HisZxRequstBean bean){
      	 StringBuffer contextXml = new StringBuffer();
           contextXml.append(setStartXml(bean))
           .append("<retrieveargs>")
           .append("<patientId>"+bean.getPatientId()+"</patientId>")//患者 ID
           .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院代号
			.append("<payType>"+bean.getPayType()+"</payType>")//支付类型（支付宝、微信、其他）
			.append("<outTradeNo>"+bean.getOutTradeNo()+"</outTradeNo>")//商户订单号
			.append("<payId>"+bean.getPayId()+"</payId>")//支付 ID
			.append("<payAmount>"+bean.getPayAmount()+"</payAmount>")//支付金额（元）
			.append("<userNo>"+bean.getUserNo()+"</userNo>")//用户标识
			.append("<createIp>"+bean.getCreateIp()+"</createIp>")//生成订单客户端 IP
			.append("<payTime>"+bean.getPayTime()+"</payTime>")//支付时间
			.append("<termId>"+bean.getTermId()+"</termId>")//设备终端号
			.append("<extInfo>"+bean.getExtInfo()+"</extInfo>")//扩展信息（JSON 格式） 
			.append("<reqReserved>"+bean.getReqReserved()+"</reqReserved>")//透传信息
			.append("<billGuid>"+bean.getBillGuid()+"</billGuid>")//单据唯一 ID
			.append("<billId>"+bean.getBillId()+"</billId>")//单据 ID
			.append("<billMoney>"+bean.getBillMoney()+"</billMoney>")//单据金额（元）
            .append("</retrieveargs>")
            .append(setEndXml(bean));
           return contextXml.toString();
      }
    
   
   /*
   @ 现场挂号-提交订单
   @ 1、组装报文的开始部分 setStartXml
   @ 2、组装报文的结束部分 setEndXml
   @   param
		<?xml version=”1.0” encoding=”utf-8”?>
		<request>
		<requestid>访问者id</requestid>
		<inputvalues>
		<patientId>患者ID</patientId>
		<hospitalId>医院ID</hospitalId>
		<orderId>订单ID</orderId>
		<payType>支付类型（支付宝、微信、其他）</payType>
		<outTradeNo>商户订单号</outTradeNo>
		<payId>支付ID</payId>
		<payAmount>支付金额（元）</payAmount>
		<userNo>用户标识</userNo>
		<createIp>生成订单客户端IP</createIp>
		<payTime>支付时间</payTime>
		<termId>设备终端号</termId>
		<extInfo>扩展信息（JSON 格式）<extInfo/>
		<reqReserved>透传信息</reqReserved>
		</inputvalues>
		</request>
    */
	public String getRegisterConfirmOrderXml(HisZxRequstBean bean){
     StringBuffer contextXml = new StringBuffer();
     contextXml.append(setStartXml(bean))
               .append("<inputvalues>")
               .append("<patientId>")
               .append("<patientId>"+bean.getPatientId()+"</patientId>")//患者ID
               .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院ID
               .append("<orderId>"+bean.getHospitalid()+"</orderId>")//订单ID
               .append("<payType>"+bean.getPayType()+"</payType>")//支付类型（支付宝、微信、其他）
               .append("<outTradeNo>"+bean.getOutTradeNo()+"</outTradeNo>")//商户订单号
               .append("<payId>"+bean.getPayId()+"</payId>")//支付ID
               .append("<payAmount>"+bean.getPayAmount()+"</payAmount>")//支付金额（元）
               .append("<userNo>"+bean.getUserNo()+"</userNo>")//用户标识
               .append("<createIp>"+bean.getCreateIp()+"</createIp>")//生成订单客户端IP
               .append("<payTime>"+bean.getPayTime()+"</payTime>")//支付时间
               .append("<termId>"+bean.getTermId()+"</termId>")//设备终端号
               .append("<extInfo>"+bean.getExtInfo()+"<extInfo/>")//扩展信息（JSON 格式）
               .append("<reqReserved>"+bean.getReqReserved()+"</reqReserved>")//透传信息
               .append("</inputvalues>")
               .append(setEndXml(bean));
     return contextXml.toString();
 }
   /**
    * 取消订单
    * @param bean
    * @return
    * <?xml version=” 1.0” encoding=” utf-8” ?>
        <request>
        <requestid> 访问者 id</requestid>
        <inputvalues>
        <patientId> 患者 ID</patientId>
        <hospitalId> 医院 ID</hospitalId>
        <orderId> 订单 ID</orderId>
        <pinKey> 数字订单号 </pinKey>
        </inputvalues>
        </request>
	    */
	   public String getRegisterCancelOrderXml(HisZxRequstBean bean){
	           StringBuffer contextXml = new StringBuffer();
	             contextXml.append(setStartXml(bean))
	                     .append( "<retrieveargs>" )
	                     .append("<patientId>" +bean.getPatientId()+ "</patientId>") //患者  ID
	                     .append("<hospitalId>" +bean.getHospitalid()+ "</hospitalId>") //医院代号
	                     .append("<orderId>" +bean.getOrderId()+ "</orderId>") //订单 ID
	                     .append("<pinKey>" +bean.getPinKey()+ "</pinKey>") //数字订单号
	                     .append( "</retrieveargs>" )
	                     .append(setEndXml(bean));
	             return contextXml.toString();
	        }

	   /**
	     * 
	     * @param bean
	     * @return
	     *<?xml version=” 1.0” encoding=” utf-8” ?>
	         <request>
	          <requestid>访问者 id</requestid>
	           <inputvalues>
	             <patientId>患者 ID</patientId>
	             <hospitalId>医院 ID</hospitalId>
	             <patientName>患者姓名</patientName>
	             <doctorCode>医生代码</doctorCode>
	             <doctorName>医生姓名</doctorName>
	             <doctorTitle>医生职称</doctorTitle>
	             <deptCode>科室代码</deptCode>
	             <deptName>科室名称</deptName>
	             <sectionType>区域代码</sectionType>
	             <timeInterval>时间段</timeInterval>
	             <registerFee>挂号费</registerFee>
	             <clinicFee>诊疗费</clinicFee>
	             <registerDate>出诊日期</registerDate>
	             <scheduleId>排班 ID</scheduleId>
	             <ext1>扩展信息<ext1>
	             <ext2>扩展信息<ext2>
	             <ext3>扩展信息<ext3>
	             <ext4>扩展信息<ext4>
	          </inputvalues>
	      </request>
	     */
	    public String getRegisterNewOrderXml(HisZxRequstBean bean){
	        StringBuffer contextXml = new StringBuffer();
	        contextXml.append(setStartXml(bean))
	                .append("<requestid>" + bean.getRequestid() + "</requestid>")
	                .append("<inputvalues>")
	                .append("<patientId>"+bean.getPatientId()+"</patientId>")//患者Id
	                .append("<hospitalId>"+bean.getHospitalid()+"</hospitalId>")//医院Id
	                .append("<patientName>"+bean.getPatientname()+"</patientName>")//患者姓名
	                .append("<doctorCode>"+bean.getDoctorCode()+"</doctorCode>")//医生代码
	                .append("<doctorName>"+bean.getDoctorname()+"</doctorName>")//医生姓名
	                .append("<doctorTitle>"+bean.getDoctorTitle()+"</doctorTitle>")//医生职称
	                .append("<deptCode>"+bean.getDeptcode()+"</deptCode>")//科室代码
	                .append("<deptName>"+bean.getDeptname()+"</deptName>")//科室名称
	                .append("<sectionType>"+bean.getSectionType()+"</sectionType>")//区域代码
	                .append("<timeInterval>"+bean.getTimeinterval()+"</timeInterval>")//时间段
	                .append("<registerFee>"+bean.getRegisterFee()+"</registerFee>")//挂号费
	                .append("<clinicFee>"+bean.getClinicFee()+"</clinicFee>")//诊疗费
	                .append("<registerDate>"+bean.getRegisterDate()+"</registerDate>")//出诊日期
	                .append("<scheduleId>"+bean.getScheduleid()+"</scheduleId>")//排班
	                .append("<ext1>"+bean.getExt1()+"</ext1>")//扩展信息
	                .append("<ext2>"+bean.getExt2()+"</ext2>")//扩展信息
	                .append("<ext3>"+bean.getExt3()+"</ext3>")//扩展信息
	                .append("<ext4>"+bean.getExt4()+"</ext4>")//扩展信息
	                .append("</inputvalues>")
	                .append(setEndXml(bean));
	        return contextXml.toString();
	    }
	    
	    /*
	    @ 现场挂号-获取科室列表
	    @ 1、组装报文的开始部分 setStartXml
	    @ 2、组装报文的结束部分 setEndXml
	    @   param
	 		<?xml version=”1.0” encoding=”utf-8”?>
			<request>
			<requestid>访问者id</requestid>
			<pageactionin>
			<currentpagenum>当前页码</currentpagenum>
			<rowsperpage>每页行数</rowsperpage>
			<pageaction>翻页动作</pageaction>
			<topagenum>翻到哪一页</topagenum>
			</pageactionin>
			<retrieveargs>
			<hospitalid>医院代号</hospitalid>
			<sectionCode>区域代码</sectionCode>
			<deptcode>科室代号</deptcode>
			<level>科室等级</level>
			<extInfo>扩展信息带格式</extInfo>
			</retrieveargs>
			</request>
	     */
	 	public String getGetDeptmentListXml(HisZxRequstBean bean){
	      StringBuffer contextXml = new StringBuffer();
	      contextXml.append(setStartXml(bean))
	                .append(getHeadXml(bean))
	                .append("<retrieveargs>")
	                .append("<hospitalid>"+bean.getHospitalid()+"</hospitalid>")//医院代号
	                .append("<sectionCode>"+bean.getSectionCode()+"</sectionCode>")//区域代码
	                .append("<deptcode>"+bean.getDeptcode()+"</deptcode>")//科室代号
	                .append("<level>"+bean.getLevel()+"</level>")//科室等级
	                .append("<extInfo>"+bean.getExtInfo()+"</extInfo>")//扩展信息带格式
	                .append("</retrieveargs>")
	                .append(setEndXml(bean));
	      return contextXml.toString();
	  }
	 	
   /**
    * 现场挂号--获取当日挂号科室下医生 
    * @param bean
    * @return
    * <?xml version=” 1.0” encoding=” utf-8” ?>
		<request>
		<requestid>访问者 id</requestid>
		<retrieveargs>
		<doctorlike>医生姓名或拼音输入码</doctorlike>
		<illnessesname>疾病名称</illnessesname>
		<deptcode>科室代码</deptcode>
		<patientid>患者 ID</patientid>
		</retrieveargs>
		</request>
    */
   public String getXCDoctorListXml(HisZxRequstBean bean){
       StringBuffer contextXml = new StringBuffer();
         contextXml.append(setStartXml(bean))
                 .append( "<retrieveargs>" )
                 .append("<doctorlike>" +bean.getDoctorlike()+ "</doctorlike>") //医生姓名或拼音输入码
                 .append("<illnessesname>" +bean.getIllnessesname()+ "</illnessesname>") //疾病名称
                 .append("<deptcode>" +bean.getDeptcode()+ "</deptcode>") //科室代码
                 .append("<patientid>" +bean.getPatientid()+ "</patientid>") //患者 ID
                 .append( "</retrieveargs>" )
                 .append(setEndXml(bean));
         return contextXml.toString();
    }
}
