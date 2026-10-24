package com.tecsun.sisp.iface.server.controller.his;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.tecsun.sisp.iface.common.vo.*;
import com.tecsun.sisp.iface.server.util.*;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.his.BusInfoHTPO;
import com.tecsun.sisp.iface.common.vo.his.DepartmentPO;
import com.tecsun.sisp.iface.common.vo.his.HisRequestBusInfoBean;
import com.tecsun.sisp.iface.common.vo.his.HospitalRegistPO;
import com.tecsun.sisp.iface.common.vo.his.PayResultPO;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.NetUserServiceImpl;
import com.tecsun.sisp.iface.server.outerface.his.HTHisIface;

/**
 * 航天医院接口
 *
 * @ClassName: HangTianHisController
 */
@Controller
@RequestMapping(value = "/iface/his/hangtianhis")
public class HangTianHisController extends BaseController {
    public static final Logger logger = Logger.getLogger(HangTianHisController.class);
    
    @Autowired
    private NetUserServiceImpl netUserService;

    /**
     * 获取医院门诊科室数据
     * <p/>
     * deptNo 科室编号   1.若传值为空时，WS接口会返回所有一级科室
     * 2. 若有值，WS接口会返回的是这个科室编号下的子科室
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisDepartment
     */
    @RequestMapping(value = "/getHisDepartment_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisDepartment_ht(@RequestBody HisRequstBean bean) {
        String deptNo = bean.getDeptNo() == null ? "" : bean.getDeptNo();  //科室编号
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        //调用航天医院业务科室接口获取数据
        HTHisIface iface = new HTHisIface();
        map = iface.getHisKS(deptNo, result, message);
        wsResult = map.get("wsResult");
        result = map.get("result");
        message = map.get("message");
        List<DepartmentPO> list = null;
      	if(result.equals(Constants.RESULT_MESSAGE_SUCCESS)){
      		 Document document;
             String RESULT = "";
             try {
                 document = DocumentHelper.parseText(wsResult);
                 Element element = document.getRootElement();
                 Element description = element.element("RESULT");
                 RESULT = description.getText();

             } catch (DocumentException e1) {
                 // TODO Auto-generated catch block
                 e1.printStackTrace();
                 logger.error("获取医院门诊科室数据失败");
             }

             if (result.equals("200") && RESULT.equals("0")) {
                 result = Constants.RESULT_MESSAGE_SUCCESS;
             } else {
                 result = Constants.RESULT_MESSAGE_ERROR;
             }

             
             try {
                 if (wsResult != null && !"".equals(wsResult)) {
                     list = Dom4JUtil.readXMLToJavaBean(new DepartmentPO(), wsResult);
                     message = list.size()+"";
                 }
             } catch (Exception e) {
                 result = Constants.RESULT_MESSAGE_ERROR;
                 message = "解析his科室xml失败";
             }

             if (list != null && list.size() > 0) {
             	int pageRecorders = bean.getPagesize()==0?list.size():bean.getPagesize();
             	int pageno = bean.getPageno()==0?list.size():bean.getPageno();
             	PageModel<DepartmentPO> page = new PageModel<DepartmentPO>(list , pageRecorders);
                  list = page.getObjects(pageno);
          		page.setData(list);
             }
      	}else{
      		 logger.error(message);
             result = Constants.RESULT_MESSAGE_ERROR;
      	}
        return this.result(result, message, list);
    }

    /**
     * 获取医院门诊挂号号表
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisRegister
     */
    @RequestMapping(value = "/getHisRegister_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisRegister_ht(@RequestBody HisRequstBean bean) {
        String deptNo = bean.getDeptNo() == null ? "" : bean.getDeptNo();  //科室编号
        String registerTime = bean.getRegisterTime();  //号表日期
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        //调用航天医院业务挂号号表接口获取数据
        HTHisIface iface = new HTHisIface();
        map = iface.getHisRegister(deptNo, registerTime, result, message);
        wsResult = map.get("wsResult");
        result = map.get("result");
        message = map.get("message");
        List<HospitalRegistPO> list = new ArrayList<HospitalRegistPO>();
        if(result.equals(Constants.RESULT_MESSAGE_SUCCESS)){
        	Document document;
            String RESULT="";
    		try {
    			document = DocumentHelper.parseText(wsResult);
    			Element element = document.getRootElement();
    			Element description = element.element("RESULT");
    			RESULT = description.getText();
    			
    		} catch (DocumentException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			logger.error("获取医院门诊挂号号表失败");
    		}
            
            
            if(result.equals("200")&& RESULT.equals("0")){
            	result =  Constants.RESULT_MESSAGE_SUCCESS;
            }else{
            	result =  Constants.RESULT_MESSAGE_ERROR;
            }
            try {
                if (wsResult != null && !"".equals(wsResult)) {
                    list = Dom4JUtil.readXMLToJavaBean(new HospitalRegistPO(), wsResult);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
            	logger.error("获取医院门诊挂号号表失败");
                e.printStackTrace();
            }

            if (list != null && list.size() > 0) {
                 /*page = new PageModel<DepartmentPO>(list , this.pagesize);
                 list = page.getObjects(pageno);
         		page.setData(list);*/
            }
        }else{
        	logger.error(message);
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        return this.result(result, message, list);
    }

    /**
     * 查询余额
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisBalance_ht
     */
    @RequestMapping(value = "/getHisBalance_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisBalance_ht(@RequestBody HisRequstBean bean) {

        String cardtype = bean.getCardType();
        String cardno = bean.getCardNo();
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        //调用航天医院业务挂号号表接口获取数据
        HTHisIface iface = new HTHisIface();
        map = iface.getHisBalance(cardtype, cardno, result, message);
        wsResult = map.get("wsResult");
        result = map.get("result");
        message = map.get("message");
        if (result.equals("200")) {
            try {
                Document document = DocumentHelper.parseText(wsResult);
                Element element = document.getRootElement();
                Element description = element.element("MSG");
                Element RESULT = element.element("RESULT");
                if (RESULT.getText().equals("-1")) {
                    message = description.getText() == null || description.getText().equals("") ? "查询余额失败！" : description.getText();
                    result = Constants.RESULT_MESSAGE_ERROR;
                }
                if (RESULT.getText().equals("0")) {
	    			/*Document document1 = DocumentHelper.parseText(wsResult);
					Element element1 = document1.getRootElement();
		    		Element description1 = element1.element("YE");
		    		System.out.println(description1.getText());
		    		message= description1.getText();*/
                    Document document1;
                    try {
                        document1 = DocumentHelper.parseText(wsResult);
                        Element element1 = document1.getRootElement();
                        Element description1 = element1.element("MSG");
                        String msg = description1.asXML().toString();
                        document1 = DocumentHelper.parseText(msg);
                        element1 = document1.getRootElement();
                        description1 = element1.element("XMLTable");
                        msg = description1.asXML().toString();
                        document1 = DocumentHelper.parseText(msg);
                        element1 = document1.getRootElement();
                        description1 = element1.element("XMLRec");
                        //System.out.println(description1.asXML().toString());
                        String s1 = description1.asXML().toString();

                        Document document2 = DocumentHelper.parseText(s1);
                        Element element2 = document2.getRootElement();
                        Element description2 = element2.element("YE");
                        message = description2.getText();
                    } catch (DocumentException e) {
                    	logger.error("查询余额失败失败");
                    }
                }

            } catch (DocumentException e) {
                // TODO Auto-generated catch block
            	logger.error("查询余额失败！");
                e.printStackTrace();
            }
        }
        List<HospitalRegistPO> list = new ArrayList<HospitalRegistPO>();
        try {
            if (wsResult != null && !"".equals(wsResult)) {
                list = Dom4JUtil.readXMLToJavaBean(new HospitalRegistPO(), wsResult);
            }
        } catch (Exception e) {
        	logger.error("查询余额失败！");
            e.printStackTrace();
        }

        if (list != null && list.size() > 0) {
             /*page = new PageModel<DepartmentPO>(list , this.pagesize);
     		list = page.getObjects(pageno);
     		page.setData(list);*/
        }

        return this.result(result, message, list);
    }

    /**
     * 获取医院病人消费明细
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisConsumerDetail
     */
    @RequestMapping(value = "/getHisXFMX_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisXFMX_ht(@RequestBody HisRequstBean bean) {
        String cardType = bean.getCardType() == null ? "" : bean.getCardType();  //卡类型
        String cardNo = bean.getCardNo();  //卡号
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        //调用航天医院业务消费明细接口获取数据

        return this.result(result, message, wsResult);
    }

    /**
     * 终端缴费
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisConsumerDetail
     */
    @RequestMapping(value = "/getHisTerminalPay", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisTerminalPay(@RequestBody HisRequstPayBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处
        String xml = "";
        Map<String, String> map = new HashMap<String, String>();

        List<HisRequstPayBean> list3 = new ArrayList<HisRequstPayBean>();
        String yyjylsh = getHospTradeCode();
        bean.setOtherParams("10,K0996138X,42220119810923XXXX");
        bean.setCardType("02");
        bean.setYYL(yyjylsh);

        list3.add(bean);
        String[] oo = new String[2];
        oo[0] = "ROOT";
        oo[1] = "XMLTable";
        xml = Dom4JUtil.getStringByDocument(Dom4JUtil.writeXmlDocument(bean, list3, oo, "XMLRec"), oo, "XMLRec");
        //调用航天医院业务挂号号表接口获取数据
	/*	HTHisIface iface = new HTHisIface();
		map = iface.getHisTerminalPay(xml,message,result);
		wsResult = map.get("wsResult");
		result = map.get("result");
		message = map.get("message");*/

        List<HospitalRegistPO> list = new ArrayList<HospitalRegistPO>();

        return this.result(result, message, xml);
    }

    
    /**
     * 终端取号（现场）
     * 1、取号时HIS返回取号的顺序号，
     * 2、把顺序号修改到平台中
     * 3、取号之后，修改平台的状态（已取号）
     * <p/>
     * jzh method
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisDepartment
     */
    @RequestMapping(value = "/GetHisGhbr_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result GetHisGhbr_ht(@RequestBody HisRequstBean bean) {
        String jzh = bean.getJZH() == null ? "" : bean.getJZH();  //就诊号
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        //调用航天医院业务科室接口获取数据
        HTHisIface iface = new HTHisIface();
        map = iface.getHisGhbr(jzh, result, message);
        wsResult = map.get("wsResult");
        result = map.get("result");
        message = map.get("message");
        List<HospitalRegistPO> list = null;
      	if(result.equals(Constants.RESULT_MESSAGE_SUCCESS)){
      		 Document document;
             String RESULT = "";
             try {
                 document = DocumentHelper.parseText(wsResult);
                 Element element = document.getRootElement();
                 Element description = element.element("RESULT");
                 RESULT = description.getText();
                 
                 if (result.equals("200") && RESULT.equals("0")) {
     				result = Constants.RESULT_MESSAGE_SUCCESS;
     				try {
     					if (wsResult != null && !"".equals(wsResult)) {
     						list = Dom4JUtil.readXMLToJavaBean(new HospitalRegistPO(),wsResult);
     						message = list.size() + "";
     					}
     				} catch (Exception e) {
     					result = Constants.RESULT_MESSAGE_ERROR;
     					message = "解析his现场取号xml失败";
     				}
                      
     				if(!list.isEmpty()){
     					if(!list.get(0).getSXH().isEmpty()){
     						bean.setSXH(list.get(0).getSXH());
     						bean.setJZH(jzh);
     						bean.setId(bean.getId());
     						String upResult = updateGhbr(bean);
     						if(upResult.equals(Constants.RESULT_MESSAGE_ERROR)){
     							 result = Constants.RESULT_MESSAGE_ERROR;
     							 message = "终端取号（现场）修改平台状台失败！";
     						}
     					}
     				}
                  } else {
                      result = Constants.RESULT_MESSAGE_ERROR;
                  }

             } catch (DocumentException e1) {
                 e1.printStackTrace();
                 logger.error("终端取号（现场）失败");
             }
      	}else{
      		 logger.error(message);
             result = Constants.RESULT_MESSAGE_ERROR;
      	}
        return this.result(result, message, list);
    }
    

    /*
   @version
   @parameter
   @return
   @throws
   @ 取一周的时间
*/
    @RequestMapping(value = "/getWeekAllDate", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getWeekAllDate(HisRequstBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        List list = new ArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd EEE");
        for (int i = 0; i < 7; i++) {
            list.add(sdf.format(getAfterDate(i)));
        }
        Result r = this.result(result, message, list);
        return r;
    }

    /**
     * 获取当前日期n天后的日期
     *
     * @param n:返回当天后的第N天
     * @return 返回的日期
     */
    public static Date getAfterDate(int n) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, n);
        return c.getTime();
    }

    /*
    @version
    @parameter
    @return
    @throws
    @ 生成医院交易流水号
    @ 生成规则  本地时间（20150702223124）+医院编号（140034）+四位随机数（0250）
    @ 201507022231241400340250

    */
    public static String getHospTradeCode() {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String value = "";
        //生成本地系统时间；
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(currentTime);
        String current = formatter.format(date);

        //医院编码
        String hospitalCode = Constants.HT_HOSPITAL_CODE;

        //生成4位随机数；
        Random random = new Random();
        int randomValue = random.nextInt(9999);

        if (current == null || current == "") {
            logger.error("生成本地系统时间失败");
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "生成本地系统时间失败";
        } else if (hospitalCode == null || hospitalCode == "") {
            logger.error("获取医院编号失败");
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取医院编号失败";
        } else {
            value = current + hospitalCode + randomValue;
        }
        return value;
    }

    /*
    @version
    @parameter
    @return
    @throws
    @ 生成医院交易流水号
    @ 生成规则  本地时间（20150702223124）+医院编号（140034）+四位随机数（0250）
    @ 201507022231241400340250

    */
    public static String getHosMzzyId() {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String value = "";
        //生成本地系统时间；
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(currentTime);
        String current = formatter.format(date);
        //生成4位随机数；
        Random random = new Random();
        int randomValue = random.nextInt(9999);

        if (current == null || current == "") {
            logger.error("生成本地系统时间失败");
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "生成本地系统时间失败";
        } else {
            value = current + randomValue;
        }
        return value;
    }

    /**
     * @param bean
     * @return Result    返回类型
     * @throws IOException 读取卡信息拼成报文
     * @throws
     * @Title: getHisConsumerDetail
     */
    @RequestMapping(value = "/getHisPayCardXml", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisPayCardXml(@RequestBody HisRequstBean bean) {
        String xmlData = "";  //封装终端缴费数据
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        HisRequstBean beanVo = new HisRequstBean();
        List<HisRequstBean> list3 = new ArrayList<HisRequstBean>();
        String yyjylsh = getHospTradeCode();
        //String mzzylsh =  getHosMzzyId();
        beanVo.setYYJYLSH(yyjylsh);
        beanVo.setYYBH(Constants.HT_HOSPITAL_CODE);
        beanVo.setCZYBH("5000");
        beanVo.setMZZYLSH(yyjylsh.substring(0, 14));
        beanVo.setKSBH(bean.getKSBH());
        beanVo.setDJH("2");
        beanVo.setGHF(bean.getGHF());
        beanVo.setYLLB("11");
        beanVo.setYYSFXMNM("z1197");
        beanVo.setSFXMZXBM("110100001");
        beanVo.setYYSFXMMC("挂号费");
        beanVo.setKMM("123456");
        beanVo.setYLLX(bean.getYLLX());

        list3.add(beanVo);
        String[] oo = new String[2];
        oo[0] = "ROOT";
        oo[1] = "XMLTable";
        String xml = Dom4JUtil.getStringByDocument(Dom4JUtil.writeXmlDocument(bean, list3, oo, "XMLRec"), oo, "XMLRec");
        if (!xml.isEmpty()) {
            message = Constants.RESULT_MESSAGE_SUCCESS;
            xmlData = xml;
        } else {
            message = Constants.RESULT_MESSAGE_ERROR;
        }
        return this.result(result, message, xmlData);
    }

    /**
     * 验证参保人是否可挂号（所有的预约挂号前必须先调用此接口）
     * 一天一张社保卡只允许挂号三次
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: isRegister
     */
    @RequestMapping(value = "/checkRegister_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result checkRegister_ht(@RequestBody HisRequstBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String xmlData = "";  //封装数据
        try {
            StringBuffer status = new StringBuffer();
            status.append("'").append(Constants.HIS_HT_STATUS_01).append("',")
                    .append("'").append(Constants.HIS_HT_STATUS_02).append("',")
                    .append("'").append(Constants.HIS_HT_STATUS_03).append("',")
                    .append("'").append(Constants.HIS_HT_STATUS_04).append("'");
            //String status ="'"+Constants.HIS_HT_STATUS_01+"',"+"'"+Constants.HIS_HT_STATUS_02+"',"+"'"+Constants.HIS_HT_STATUS_03+"',"+"'"+Constants.HIS_HT_STATUS_04+"'";
            BusInfoHTPO po = new BusInfoHTPO();
            po.setSTATUS(status.toString());
            po.setIDENTITY(bean.getCernum());
            po.setNAME(bean.getName());
            po.setCARDNO(bean.getCardNo());
            po.setREGDATE(bean.getRegdate());
            xmlData = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.CHECKREGISTER_HT));
            Result irpo = JsonMapper.buildNormalMapper().fromJson(xmlData, Result.class);
            xmlData = String.valueOf(irpo.getData());
            result = irpo.getStatusCode();
            message = irpo.getMessage();
        } catch (IOException e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "验证挂号解析json出错";
            logger.error("----验证挂号解析json出错----", e);
        }
        return this.result(result, message, xmlData);
    }

    /**
     * 航天接口--大终端通知平台接口(东软缴费模式-大终端使用)--社保帐户缴费
     * <p/>
     * 流程：
     * 1.记录东软缴费成功的信息（在医院子系统中处理）
     * 2.调用航天医院webservice接口告知医院缴费成功，返回顺序号
     * 3.根据返回的结果，更新数据库记录（在医院子系统中处理）
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: sendHisSalSF
     */

    @RequestMapping(value = "/noticePlatfrom_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result noticePlatfrom_ht(@RequestBody HisRequestBusInfoBean bean) {
        insertTransLog(bean, Constants.BUSINESSCODE.get("noticePlatfrom_ht"));
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String xmlData = "";  //封装数据
        String wsResult = "";
        //解析前台传过来的数据
        String OtherParams = bean.getOtherParams();
        String yhc = bean.getYHC();
        if (OtherParams != null && !"".equals(OtherParams)) {
            String ops[] = OtherParams.split(",");
            if (ops.length > 0 && ops[0].equals("10")) {
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "OtherParams参数值不对";
                return this.result(result, message);
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "OtherParams参数值不对";
            return this.result(result, message);
        }
        if (yhc == null || "".equals(yhc)) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "YHC参数值不对";
            return this.result(result, message);
        }
        String yhcs[] = yhc.split("\\|");
        if (yhcs.length != 2) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "YHC参数值不对";
            return this.result(result, message);
        }

        //1.记录东软缴费成功信息
        String id = "0";
        BusInfoHTPO po = new BusInfoHTPO();
        try {
        	//fwf  HisRequestBusInfoBean  
           // po = copyProperties_social(bean);
        	 po = copyProperties(bean);
            po.setSTATUS(Constants.HIS_HT_STATUS_02);//状态 02 东软缴费成功
            po.setPAYTIME(CommUtil.getNowDateLongStr()); //缴费时间
            po.setTRANSNO(bean.getYYL());//医院交易流水号；
            po.setMZTRANSNO(bean.getJZH());//门诊住院流水号；

           // po.setREGFEE(String.valueOf(bean.getSJF()));
            po.setREGFEE(String.valueOf(bean.getZHZF()));
            po.setDOCTORTYPE(bean.getDOCTORTYPE());//挂号类型

            xmlData = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.INSERTBUSINFO_HT));
            Result irpo = JsonMapper.buildNormalMapper().fromJson(xmlData, Result.class);
            id = String.valueOf(irpo.getData());
            po.setID(Integer.parseInt(id));
            result = irpo.getStatusCode();
            message = irpo.getMessage();
        } catch (IOException e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "记录东软缴费成功信息失败";
            logger.error("记录东软缴费成功信息失败", e);
        }
        if (result == Constants.RESULT_MESSAGE_ERROR || Constants.RESULT_MESSAGE_ERROR.equals(result)) {
            return this.result(result, message);
        }
        //2.调用航天医院webservice接口告知医院缴费成功，返回顺序号
        //2.1封装XmlData报文
        try {
            Map resultmap = getXMLdata(bean, result, xmlData, message);
            result = resultmap.get("result").toString();
            message = resultmap.get("message").toString();
            wsResult = resultmap.get("wsResult").toString();
            //3.更新数据库记录
            if (result.equals(Constants.RESULT_MESSAGE_ERROR)) {
                message = "缴费成功告知医院缴费失败，请退费";
                po.setSTATUS(Constants.HIS_HT_STATUS_05);
                result = Constants.RESULT_MESSAGE_ERROR; //返回失败后，进行退费 update by 2015-9-5
            } else {
                if (StringUtils.isNotEmpty(wsResult)) {
                    Document document = DocumentHelper.parseText(wsResult);
                    Element element = document.getRootElement();
                    Element RESULT = element.element("RESULT");
                    Element MSG = element.element("MSG");
                    if (RESULT.getText().equals("0") && !MSG.equals("")) {
                        List<PayResultPO> list = new ArrayList<PayResultPO>();
                        PayResultPO busInfoHTPO = new PayResultPO();
                        list = Dom4JUtil.readXMLToJavaBean(busInfoHTPO, wsResult);
                        if (!list.isEmpty()) {
                            busInfoHTPO = list.get(0);
                            po.setVISID(busInfoHTPO.getJZH());
                            po.setSN(busInfoHTPO.getSXH());
                            po.setADDRESS(busInfoHTPO.getADR());
                            po.setSTATUS(Constants.HIS_HT_STATUS_03);//挂号成功
                        }else{
                        	result = Constants.RESULT_MESSAGE_ERROR;
                        }
                    } else result = Constants.RESULT_MESSAGE_ERROR;
                }
            }
            po.setREGSUCCTIME(CommUtil.getNowDateLongStr());// 挂号成功时间
            String updatehisResult = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
            Result updateResult = JsonMapper.buildNormalMapper().fromJson(updatehisResult, Result.class);
            String upResult = updateResult.getStatusCode();
            if(upResult.equals(Constants.RESULT_MESSAGE_ERROR)){
            	logger.info("挂号缴费通知平台修改状态失败!");
            	 result = Constants.RESULT_MESSAGE_ERROR;
            }else{
            	//挂号成功后调用短信接口
	           	 bean.setTYPE("1");
	           	 String smsResult =  smsSend(bean);
	           	 if(smsResult.equals("200")){
	           		 logger.info("预约挂号短信接口调用成功!");
	           	 }else{
	           		 logger.error("预约挂号短信接口调用失败!");
	           	 }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "更新数据库状态错误";
            logger.error("航天接口--大终端通知平台接口,更新数据库状态失败", e);
        }
        return this.result(result, message);
    }
    
    public static String smsSend(HisRequestBusInfoBean bean){
        String mobile = bean.getPhoneNo();
        String message="";
		SmsRequstBean smsBean = new SmsRequstBean();
		/*smsBean.setDeviceid(bean.getDeviceid());*/
		//smsBean.setGHF(bean.getSJF());
		smsBean.setGHF(bean.getZHZF());
		smsBean.setRegdate(bean.getREGDATE());
		smsBean.setNewPhoneNo(mobile);
		smsBean.setDEPARTMENTMC(bean.getDEPARTMENTMC());
		smsBean.setDOCTORMC(bean.getDOCTORMC());
		smsBean.setHOSPITALMC(bean.getHOSPITALMC());
	/*	smsBean.setChannelcode(bean.getChannelcode());
		smsBean.setChannelType(bean.getCHANNELTPYE());*/
		smsBean.setREGTIME(bean.getTIM().equals("AM")?"上午":"下午");
		smsBean.setName(bean.getUNM());
		smsBean.setType(bean.getTYPE());
		if(!mobile.isEmpty()){
			SmsSendUtil smsUtil = new SmsSendUtil();
			String smsResult = smsUtil.SmsSend(smsBean);
			if(smsResult.equals("200")){
				logger.info("短信接口调用成功!");
				message =Constants.RESULT_MESSAGE_SUCCESS;
			}else{
				logger.error("短信接口调用失败!");
				message=Constants.RESULT_MESSAGE_ERROR;
			}
		}else{
			logger.error("发送短信时，手机号码不能为空!");
		}
    	return message;
    }
    
    /*public BusInfoHTPO copyProperties_social(HisRequestBusInfoBean bean) {
        String OtherParams = bean.getOtherParamsBase();
        String cardNo = "";
        String cernum = "";
        String name = "";
        if (OtherParams != null && !"".equals(OtherParams)) {
            String ops[] = OtherParams.split(",");
            if (ops.length > 0) {
                if (ops[0].equals("10")) {
                    cardNo = ops[1];
                    cernum = ops[2];
                    name = ops[3];
                }
            }
        }
        BusInfoHTPO po = new BusInfoHTPO();
        po.setCARDNO(cardNo);
        po.setIDENTITY(cernum);
        po.setNAME(name);
        po.setTERMINALID(bean.getZDM());
        po.setCARDTYPE(bean.getCardType());
        po.setACCOUNTTYPE(bean.getZFLB());
        po.setSEX(bean.getSEX());
        po.setAGR(Integer.parseInt(bean.getAGE() == null || bean.getAGE().equals("") ? "0" : bean.getAGE()));
        po.setDEPARTMENT(bean.getDEPARTMENT());
        po.setFLOWID(bean.getFLOWID());
        po.setREGDATE(bean.getREGDATE());
        po.setREGTIME(bean.getTIM());
        po.setHOSID(Constants.HT_HOSPITAL_CODE);
        po.setCHANNELTPYE(bean.getCHANNELTPYE());
        
        po.setDEPARTMENTMC(bean.getDEPARTMENTMC());
        po.setHOSPITALMC(bean.getHOSPITALMC());
        po.setDOCTORMC(bean.getDOCTORMC());
        return po;
    }*/
    //fwf1 HisRequestBusInfoBean
    public BusInfoHTPO copyProperties(HisRequestBusInfoBean bean) {
        String OtherParams = bean.getOtherParams();
        String cardNo = "";
        String cernum = "";
        String name = "";
        if (OtherParams != null && !"".equals(OtherParams)) {
            String ops[] = OtherParams.split(",");
            if (ops.length > 0) {
                if (ops[0].equals("10")) {
                    cardNo = ops[1];
                    cernum = ops[2];
                    name = ops[3];
                }
            }
        }
        BusInfoHTPO po = new BusInfoHTPO();
        po.setCARDNO(cardNo);
        po.setIDENTITY(cernum);
        po.setNAME(name);
        po.setTERMINALID(bean.getZDM());
        po.setCARDTYPE(bean.getCardType());
        po.setACCOUNTTYPE(bean.getZFLB());
        po.setSEX(bean.getSEX());
        po.setAGR(Integer.parseInt(bean.getAGE() == null || bean.getAGE().equals("") ? "0" : bean.getAGE()));
        po.setDEPARTMENT(bean.getDEPARTMENT());
        po.setFLOWID(bean.getFLOWID());
        po.setREGDATE(bean.getREGDATE());
        po.setREGTIME(bean.getTIM());
        po.setHOSID(Constants.HT_HOSPITAL_CODE);
        po.setCHANNELTPYE(bean.getCHANNELTPYE());
        
        po.setDEPARTMENTMC(bean.getDEPARTMENTMC());
        po.setHOSPITALMC(bean.getHOSPITALMC());
        po.setDOCTORMC(bean.getDOCTORMC());
        return po;
    }

    //更新数据库
    public void updateBusInfo(BusInfoHTPO po) throws IOException {
        DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
    }

    /**
     * 航天接口--预约缴费接口-医院帐户缴费
     * <p/>
     * 流程：
     * 1.记录TSB预约挂号的信息（在医院子系统中处理）
     * 2.调用航天医院接口进行挂号缴费，返回顺序号
     * 3.根据返回的结果，更新数据库记录（在医院子系统中处理）
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: sendHisSalTSB
     */
    @RequestMapping(value = "/sendHisSal_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result sendHisSal_ht(@RequestBody HisRequestBusInfoBean bean) {
        insertTransLog(bean, Constants.BUSINESSCODE.get("sendHisSal_ht"));
        String statuCode = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "预约缴费成功";
        String result = "";//挂号成功返回数据
        BusInfoHTPO busInfoHTPO = new BusInfoHTPO();
        String yyjylsh = getHospTradeCode();
        String jzh = getHosMzzyId();
        try {
            //1 插入记录
            busInfoHTPO.setSUBTIME(CommUtil.getNowDateLongStr());

            //fwf2  HisRequestBusInfoBean
            busInfoHTPO = copyProperties(bean);
            busInfoHTPO.setSTATUS(Constants.HIS_HT_STATUS_01);//状态 01 
            busInfoHTPO.setPAYTIME(CommUtil.getNowDateLongStr()); //缴费时间

            busInfoHTPO.setTRANSNO(yyjylsh);
            // yyzf

            busInfoHTPO.setREGFEE(String.valueOf(bean.getYYZF()));
            //busInfoHTPO.setREGFEE(String.valueOf(bean.getSJF()));
            busInfoHTPO.setREGTIME(bean.getTIM());
            busInfoHTPO.setREGDATE(bean.getREGDATE());
            busInfoHTPO.setDOCTORTYPE(bean.getDOCTORTYPE());//挂号类别
            

            String insertbusResult = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(busInfoHTPO), DictionaryUtil.getHost(Constants.INSERTBUSINFO_HT));
            Result busResult = JsonMapper.buildNormalMapper().fromJson(insertbusResult, Result.class);
            statuCode = busResult.getStatusCode();
            busInfoHTPO.setID(Integer.parseInt(busResult.getData().toString()));
        } catch (IOException e) {
            logger.error("预约缴费 记录TSB预约挂号的信息失败:" + e.getMessage());
            statuCode = Constants.RESULT_MESSAGE_ERROR;
            e.printStackTrace();
        }
        if (statuCode.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
            try {
                //2 调用航天医院接口进行挂号缴费
                bean.setYYL(yyjylsh);
                bean.setYYD(new SimpleDateFormat("yyyyMMdd").format(new Date()));
                bean.setYYM(Constants.HT_HOSPITAL_CODE);
                bean.setJZH(jzh);

//            	bean = copyProperties(busInfoHTPO);
                Map map = getXMLdata(bean, statuCode, result, message);
                statuCode = map.get("result").toString();
                result = map.get("wsResult").toString();
                message = map.get("message").toString();
            } catch (Exception e) {
                logger.error("预约缴费 挂号失败:" + e.getMessage());
                statuCode = Constants.RESULT_MESSAGE_ERROR;
                e.printStackTrace();
            }
            try {
                //3 如果挂号成功则更新状态
                if (statuCode.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                    if (StringUtils.isNotEmpty(result)) {
                        Document document = DocumentHelper.parseText(result);
                        Element element = document.getRootElement();
                        Element RESULT = element.element("RESULT");
                        Element MSG = element.element("MSG");
                        if (RESULT.getText().equals("0") && !MSG.equals("")) {
                            List<PayResultPO> list = new ArrayList<PayResultPO>();
                            PayResultPO po = new PayResultPO();
                            list = Dom4JUtil.readXMLToJavaBean(po, result);
                            if (!list.isEmpty()) {
                                po = list.get(0);
                                busInfoHTPO.setVISID(po.getJZH());
                                busInfoHTPO.setSN(po.getSXH());
                                busInfoHTPO.setADDRESS(po.getADR());
                                busInfoHTPO.setSTATUS(Constants.HIS_HT_STATUS_03);//挂号成功
                                busInfoHTPO.setREGSUCCTIME(CommUtil.getNowDateLongStr());// 挂号成功时间
                                String updatehisResult = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(busInfoHTPO), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
                                Result updateResult = JsonMapper.buildNormalMapper().fromJson(updatehisResult, Result.class);
                                statuCode = updateResult.getStatusCode();
                              //3 如果挂号成功则更新状态后调用短信接口
                              //挂号成功后调用短信接口
                                bean.setTYPE("1");
                                String smsResult =  smsSend(bean);
                        		if(smsResult.equals("200")){
                        			logger.info("预约挂号短信接口调用成功!");
                        		}else{
                        			logger.error("预约挂号短信接口调用失败!");
                        		}
                            }

                        } else statuCode = Constants.RESULT_MESSAGE_ERROR;

                    }
                    
                } else {
                    //4 如果挂号失败 则修改记录为挂号失败
                    busInfoHTPO.setSTATUS(Constants.HIS_HT_STATUS_05);//挂号失败
                    busInfoHTPO.setREGSERRORTIME(CommUtil.getNowDateLongStr());// 挂号失败时间
                    DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(busInfoHTPO), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
                }
            } catch (Exception e) {
                logger.error("预约缴费 挂号成功后更新状态失败:" + e.getMessage());
                statuCode = Constants.RESULT_MESSAGE_ERROR;
                e.printStackTrace();
            }
        }
        return this.result(statuCode, message, result);
    }

    public Map getXMLdata(HisRequestBusInfoBean bean, String statuCode, String result, String message) {
        List<HisRequestBusInfoBean> list = new ArrayList<HisRequestBusInfoBean>();
        list.add(bean);
        String[] oo = new String[2];
        oo[0] = "ROOT";
        oo[1] = "XMLTable";
        String xmlData = Dom4JUtil.getStringByDocument(Dom4JUtil.writeXmlDocument(bean, list, oo, "XMLRec"), oo, "XMLRec");
        xmlData = (new StringBuilder("<XMLTable>")).append(xmlData).append("</XMLTable>").toString();
        HTHisIface iface = new HTHisIface();
        Map<String, String> map = iface.getHisTerminalPay(xmlData, statuCode, "");
        return map;
    }

    /**
     * 获取已经有效挂号的列表信息
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getRegisterInfoList_ht
     */
    @RequestMapping(value = "/getRegisterInfoList_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getRegisterInfoList_ht(@RequestBody HisRequstBean bean) {
        String cernum = bean.getCernum();
        String name = bean.getName();
        String cardNo = bean.getCardNo();
        String channelType = bean.getChannelType();//接口类型  01-德生宝 02-大终端
        String accounttype = bean.getAccounttype();
        String status = bean.getStatus();
        String hosId = bean.getHosid();
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        if (cernum == null || cernum.equals("") ||
                name == null || name.equals("") || cardNo.equals("") || cardNo == null || hosId ==null) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "传入的参数值不能为空";
            return this.result(result, message);
        }
        Object jsonData = "";
        JsonObject json = new JsonObject();
        json.addProperty("cernum", cernum);
        json.addProperty("name", name);
        json.addProperty("cardNo", cardNo);
        json.addProperty("status", status);
        json.addProperty("channelType", channelType);
        json.addProperty("accounttype", accounttype);
        json.addProperty("hosid", hosId);
        json.addProperty("pagesize", bean.getPagesize());
        json.addProperty("pageno", bean.getPageno());
        jsonData = DictionaryUtil.postClientRequest(json.toString(), DictionaryUtil.getHost(Constants.GETREGISTERINFOLIST_HT));
        Result irpo;
        try {
            irpo = JsonMapper.buildNormalMapper().fromJson(jsonData.toString(), Result.class);
            jsonData = irpo.getData();
            result = irpo.getStatusCode();
            message = irpo.getMessage();
        } catch (IOException e) {
        	 logger.error("获取已经有效挂号的列表解析数据失败", e);
            return this.result(Constants.RESULT_MESSAGE_ERROR, "获取已经有效挂号的列表解析数据出错");
        }

        return this.result(result, message, jsonData);
    }

    /**
     * 记录退费、取消挂号的信息
     * 需要调用此接口的情况：
     * 1.取消挂号
     * 2.东软缴费成功后，调用平台接口，平台调用his接口失败，此情况需要退费（大终端）
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: cancelRegister
     */
    @RequestMapping(value = "/cancelRegister_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result cancelRegister_ht(@RequestBody HisRequstBean bean) {
        insertTransLog(bean, Constants.BUSINESSCODE.get("cancelRegister_ht"));
        String id = bean.getId();
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        Object jsonData = "";
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        jsonData = DictionaryUtil.postClientRequest(json.toString(), DictionaryUtil.getHost(Constants.GETHISByID_HT));
        Result irpo;
        try {
            irpo = JsonMapper.buildNormalMapper().fromJson(jsonData.toString(), Result.class);
            List map = (List) irpo.getData();
            result = irpo.getStatusCode();
            message = irpo.getMessage();
            if (result == null || result.equals("") || result.equals(Constants.RESULT_MESSAGE_ERROR)) {
                return this.result(Constants.RESULT_MESSAGE_ERROR, "获取取消挂号数据出错");
            }
            BusInfoHTPO po = new BusInfoHTPO();
            setData(map, po);
            if (po != null && (po.getSTATUS().equals(Constants.HIS_HT_STATUS_02) || po.getSTATUS().equals(Constants.HIS_HT_STATUS_03)
                    || po.getSTATUS().equals(Constants.HIS_HT_STATUS_06)||po.getSTATUS().equals(Constants.HIS_HT_STATUS_10))) {
                //组装发送给医院
            	//fwf4NO  HisRequestBusInfoBean
                HisRequestBusInfoBean busInfoBean = copyProperties(po);
                String xmlData = "";
                Map resultmap = getXMLdata(busInfoBean, result, xmlData, message);
                result = resultmap.get("result").toString();
                message = resultmap.get("message").toString();
                if (result.equals(Constants.RESULT_MESSAGE_ERROR)) {
                    po.setSTATUS(Constants.HIS_HT_STATUS_08);
                } else {
                    po.setSTATUS(Constants.HIS_HT_STATUS_07);
                    //取消挂号成功后调用短信接口
                    
                    HisRequestBusInfoBean smsbean  = new HisRequestBusInfoBean();
                    String mobile="";
                    smsbean.setTYPE("2");
                    SecQueryBean secBean  = new SecQueryBean();
                    secBean.setName(po.getNAME());
                    secBean.setCernum(po.getIDENTITY());
                    NetUserInfoVO  netUserInfo  = netUserService.getPhoneNo(secBean);
                	if(netUserInfo!=null){
                	   mobile = netUserInfo.getMobile();
                	}
                	smsbean.setPhoneNo(mobile);
                	smsbean.setUNM(po.getNAME());
                	
                	
                	LinkedHashMap linkMap = (LinkedHashMap) map.get(0);
                	//smsbean.setSJF(po.getREGFEE());
                	smsbean.setREGDATE(po.getREGDATE());
                	smsbean.setTIM(linkMap.get("regtime").toString());
                	smsbean.setDEPARTMENTMC(linkMap.get("departmentmc").toString());
                	smsbean.setDOCTORMC(linkMap.get("doctormc").toString());
                	smsbean.setHOSPITALMC(linkMap.get("hospitalmc").toString());
                    String smsResult =  smsSend(smsbean);
            		if(smsResult.equals("200")){
            			logger.info("取消挂号短信接口调用成功!");
            		}else{
            			logger.error("取消挂号短信接口调用失败!");
            		}
                    
                }
                po.setCHANGERRORETIME(CommUtil.getNowDateLongStr());
                long busId = Long.valueOf(id).longValue();
                po.setID(busId);
                DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "该条记录不允许退费";
            }

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("获取取消挂号数据解析失败", e);
            return this.result(Constants.RESULT_MESSAGE_ERROR, "获取取消挂号数据解析出错");
        } catch (Exception e) {
        	 logger.error("消挂号时获取手机号码失败", e);
			e.printStackTrace();
		}

        return this.result(result, message);
    }

    private void setData(List map, BusInfoHTPO po) {
        if (!map.isEmpty()) {
            LinkedHashMap linkMap = (LinkedHashMap) map.get(0);
            po.setCARDNO(linkMap.get("cardno").toString());
            po.setIDENTITY(linkMap.get("identity").toString());
            po.setNAME(linkMap.get("name").toString());
            po.setCARDTYPE(linkMap.get("cardtype").toString());
            po.setSEX(linkMap.get("sex").toString());
            po.setAGR(Integer.parseInt(linkMap.get("agr").toString()));
            po.setTRANSNO(linkMap.get("transno").toString());
            po.setREGDATE(linkMap.get("regdate").toString());
            po.setTERMINALID(linkMap.get("terminalid").toString());
            po.setFLOWID(linkMap.get("flowid").toString());
            po.setREGFEE(linkMap.get("regfee").toString());
            po.setSTATUS(linkMap.get("status").toString());
            po.setACCOUNTTYPE(linkMap.get("accounttype").toString());

            po.setVISID(linkMap.get("visid").toString());
            //po.setREGTIME(linkMap.get("regtime").toString());// AM PM

        }

    }

    //fwf5NO HisRequestBusInfoBean
    public HisRequestBusInfoBean copyProperties(BusInfoHTPO po) {
        HisRequestBusInfoBean bean = new HisRequestBusInfoBean();
        StringBuffer otherParams = new StringBuffer();
        otherParams.append("10,").append(po.getCARDNO()).append(",")
                .append(po.getIDENTITY()).append(",")
                .append(po.getNAME()).append(",,,,,,,");
        bean.setOtherParams(otherParams.toString());
        bean.setCardType(po.getCARDTYPE());
        bean.setYHC(po.getCARDNO() + "|" + po.getIDENTITY());
        bean.setUNM(po.getNAME());
        bean.setSEX(po.getSEX());
        bean.setAGE(po.getAGR() + "");
        bean.setYYL(po.getTRANSNO());
        bean.setYYD(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        bean.setJBS("1"); //挂号
        bean.setTYPE("1");//退费
        bean.setTIM(po.getREGTIME());
        bean.setYYM(Constants.HT_HOSPITAL_CODE);
        bean.setZDM(po.getTERMINALID());
        bean.setQDM("1");//1为自助终端
        bean.setFLOWID(po.getFLOWID());
        bean.setSJF(po.getREGFEE()); //退号费
        bean.setZFLB(po.getACCOUNTTYPE());
        bean.setYYZF(Double.parseDouble((String) (po.getREGFEE() == null || po.getREGFEE().equals("") ? "0" : po.getREGFEE())));
        //XJZF>0.00</XJZF><ZHZF>0.00</ZHZF><TCZF>0.00</TCZF><YYZF>0.00</YYZF><ZPZF>0.00</ZPZF>
        bean.setXJZF("0.00");
        bean.setZHZF("0.00");
        bean.setTCZF("0.00");
        bean.setYYZF(0.00);
        bean.setZPZF("0.00");
        bean.setJZH(po.getVISID());

        return bean;
    }

    /**
     * 终端--打印凭证
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisZDQH
     */
    @RequestMapping(value = "/getHisZDQH_ht", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisZDQH_ht(@RequestBody HisRequstBean bean) {
        insertTransLog(bean, Constants.BUSINESSCODE.get("getHisZDQH_ht"));
        String id = bean.getId();
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        BusInfoHTPO po = new BusInfoHTPO();
        po.setSTATUS(Constants.HIS_HT_STATUS_10);
        po.setGETNO(CommUtil.getNowDateLongStr());//已取号时间
        long busId = Long.valueOf(id).longValue();
        po.setID(busId);
        String updatehisResult;
        try {
            updatehisResult = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
            Result updateResult = JsonMapper.buildNormalMapper().fromJson(updatehisResult, Result.class);
            result = updateResult.getStatusCode();
            message = "终端打印凭证成功！";
        } catch (IOException e) {
            message = "终端打印凭证修改状态失败！";
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("终端打印凭证修改状态失败！");
            e.printStackTrace();
        }
        return this.result(result, message);
    }

    
    
    /**
     * 终端取号
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title: getHisZDQH
     */
    public static String updateGhbr(HisRequstBean bean) {
        //insertTransLog(bean, Constants.BUSINESSCODE.get("getHisZDQH_ht"));
        String id = bean.getId(); //查询列表中的id,若id有多值，则以逗号隔开
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        BusInfoHTPO po = new BusInfoHTPO();
        po.setSTATUS(Constants.HIS_HT_STATUS_04);
        po.setGETNO(CommUtil.getNowDateLongStr());//已取号时间
        po.setSN(bean.getSXH());
        long busId = Long.valueOf(id).longValue();
        po.setID(busId);
        String updatehisResult;
        try {
            updatehisResult = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
            Result updateResult = JsonMapper.buildNormalMapper().fromJson(updatehisResult, Result.class);
            result = updateResult.getStatusCode();
        } catch (IOException e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("终端取号（现场）修改平台状台失败！");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param bean
     * @return Result    返回类型
     * @throws IOException 东软退费拼成报文方法
     * @throws
     * @Title: getHisConsumerDetail
     */
    @RequestMapping(value = "/getHisByID", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisByID(@RequestBody HisRequstBean bean) {

        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String id = bean.getId();
        String message = "";
        Object jsonData = "";
        JsonObject json = new JsonObject();
        json.addProperty("id", id);
        jsonData = DictionaryUtil.postClientRequest(json.toString(), DictionaryUtil.getHost(Constants.GETHISByID_HT));
        Result irpo;
        try {
            irpo = JsonMapper.buildNormalMapper().fromJson(jsonData.toString(), Result.class);
            jsonData = irpo.getData();
            result = irpo.getStatusCode();
            message = irpo.getMessage();
        } catch (IOException e) {
            logger.error("获取已经有效挂号的列表解析数据失败");
            return this.result(Constants.RESULT_MESSAGE_ERROR, "获取已经有效挂号的列表解析数据出错");
        }
        return this.result(result, message, jsonData);
    }

    /**
     * @param bean
     * @return Result    返回类型
     * @throws IOException 东软退费拼成报文方法
     * @throws
     * @Title: getHisConsumerDetail
     */
    @RequestMapping(value = "/getHisRefunCardXml", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisRefunCardXml(@RequestBody HisRequstBean bean) {
        String xmlData = "";  //封装终端缴费数据
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        HisRequstBean beanVo = new HisRequstBean();
        List<HisRequstBean> list3 = new ArrayList<HisRequstBean>();
        beanVo.setYYJYLSH(bean.getYYJYLSH());
        beanVo.setYYBH(Constants.HT_HOSPITAL_CODE);
        beanVo.setCZYBH("5000");
        beanVo.setMZZYLSH(bean.getMZZYLSH());
        beanVo.setKSBH(bean.getKSBH());
        beanVo.setDJH("2");
        beanVo.setGHF(bean.getGHF());
        beanVo.setYLLB("0");
        beanVo.setYYSFXMNM("z1197");
        beanVo.setSFXMZXBM("110100001");
        beanVo.setYYSFXMMC("挂号费");
        beanVo.setKMM("123456");
        beanVo.setYLLX(bean.getYLLX());

        list3.add(beanVo);
        String[] oo = new String[2];
        oo[0] = "ROOT";
        oo[1] = "XMLTable";
        String xml = Dom4JUtil.getStringByDocument(Dom4JUtil.writeXmlDocument(bean, list3, oo, "XMLRec"), oo, "XMLRec");
        if (!xml.isEmpty()) {
            message = Constants.RESULT_MESSAGE_SUCCESS;
            xmlData = xml;
        } else {
            message = Constants.RESULT_MESSAGE_ERROR;
        }
        return this.result(result, message, xmlData);
    }

    public void insertTransLog(HisRequestBusInfoBean bean, String funcName) {
        TransBean transBean = new TransBean();
        transBean.setUserid(bean.getId());
        transBean.setChannelcode(bean.getChannelcode());
        transBean.setBusinesscode(funcName);
        transBean.setDeviceid(bean.getDeviceid());
        ThreadPoolUtil.getThreadPool().execute(new ThreadPoolTask(transBean));
    }

    public void insertTransLog(HisRequstBean bean, String funcName) {
        TransBean transBean = new TransBean();
        transBean.setUserid(bean.getId());
        transBean.setChannelcode(bean.getChannelcode());
        transBean.setBusinesscode(funcName);
        transBean.setDeviceid(bean.getDeviceid());
        ThreadPoolUtil.getThreadPool().execute(new ThreadPoolTask(transBean));
    }
    
    
    
    /*
    @version
    @parameter
    @return 1表示下午；0表示上午；
    @throws
    @ 判断当前时间是上午还是下午；
 */
/*     @RequestMapping(value = "/checkDateIsAmPm", method = RequestMethod.POST, produces = "application/json")
     @ResponseBody
     public Result checkDateIsAmPm() {
         String result = Constants.RESULT_MESSAGE_SUCCESS;
         String message = "";
         int dataValue = 00;
         GregorianCalendar ca = new GregorianCalendar();  
         dataValue= ca.get(GregorianCalendar.AM_PM);
         Result r = this.result(result, message, dataValue);
         if(dataValue == 00){
        	 result = Constants.RESULT_MESSAGE_ERROR;
         }else{
        	 result = Constants.RESULT_MESSAGE_SUCCESS;
         }
         return r;
     }*/

}
