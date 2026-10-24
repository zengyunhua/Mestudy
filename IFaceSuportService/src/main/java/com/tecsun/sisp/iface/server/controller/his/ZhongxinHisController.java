package com.tecsun.sisp.iface.server.controller.his;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.HisRequstBean;
import com.tecsun.sisp.iface.common.vo.HisZxRequstBean;
import com.tecsun.sisp.iface.common.vo.NetUserInfoVO;
import com.tecsun.sisp.iface.common.vo.Page;
import com.tecsun.sisp.iface.common.vo.PageModel;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;
import com.tecsun.sisp.iface.common.vo.SmsRequstBean;
import com.tecsun.sisp.iface.common.vo.his.BusInfoHTPO;
import com.tecsun.sisp.iface.common.vo.his.DepartmentPO;
import com.tecsun.sisp.iface.common.vo.his.zx.HospitalZxOrderPO;
import com.tecsun.sisp.iface.common.vo.his.zx.HospitalZxRegistPO;
import com.tecsun.sisp.iface.common.vo.his.zx.ZxHosInfoRegistPO;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.util.CommUtil;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import com.tecsun.sisp.iface.server.util.Dom4JUtil;
import com.tecsun.sisp.iface.server.util.SmsSendUtil;
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
import com.tecsun.sisp.iface.server.model.service.NetUserServiceImpl;
import  com.tecsun.sisp.iface.server.outerface.his.ZXHisIface;
import com.tecsun.sisp.iface.common.util.ZxHisUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/**
 * Created by Administrator on 2015/8/12.
 */
@Controller
@RequestMapping(value = "/iface/his/zhongxinhis")
public class ZhongxinHisController extends BaseController {
    public static final Logger logger = Logger.getLogger(ZhongxinHisController.class);

    static ZxHisUtil zhHis = new ZxHisUtil();
    static ZXHisIface iface = new ZXHisIface();
    @Autowired
    private NetUserServiceImpl netUserService;

    /**
     * 1.1获取医院信息
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getHospitalList_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHospitalList_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getHospitalListXml(bean);
        String zxHosXml = null;
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getHospitalList_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        message = map.get("message");
        List<ZxHosInfoRegistPO> list = new ArrayList<ZxHosInfoRegistPO>(0);
        try {
            if (xml != null && !"".equals(xml)) {
                int returncod = Dom4JUtil.checkWsReturnCode(xml);
                message  = Dom4JUtil.getMessage(xml);
                if(result.equals(Constants.RESULT_MESSAGE_SUCCESS)){
                    if(returncod ==1){
                        list = Dom4JUtil.readXMLToJavaBean_Zx(new ZxHosInfoRegistPO(), xml);
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                    }else{
                        result = Constants.RESULT_MESSAGE_ERROR;
                    }
                }else{
                    logger.error("获取【中心医院】医院信息暂无数据！");
                    result = Constants.RESULT_MESSAGE_ERROR;
                }

            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("获取【中心医院】医院信息失败：getHospitalList_zx--"+e.getMessage());
        }

        return this.result(result, message, list);
    }

    /**
     * 1.2 获取科室信息
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getDeptmentList_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getDeptmentList_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getDeptmentListXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getDeptmentList_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        int count =0;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        message = map.get("message");
        List<ZxHosInfoRegistPO> list = null;
        Page<ZxHosInfoRegistPO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            if (xml != null && !"".equals(xml)) {
                int returncod = Dom4JUtil.checkWsReturnCode(xml);
                message  = Dom4JUtil.getMessage(xml);
                if(result.equals(Constants.RESULT_MESSAGE_SUCCESS) && returncod ==1){
                    list = Dom4JUtil.readXMLToJavaBean_Zx(new ZxHosInfoRegistPO(), xml);
                    if(!list.isEmpty() && list.size()>0){
                        count = Dom4JUtil.checkDeptmentCont(xml);
                        page.setCount(count);
                        page.setData(list);
                    }
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                }else{
                    result = Constants.RESULT_MESSAGE_ERROR;
                    logger.error("获取【中心医院】科室信息失败！");
                }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("获取【中心医院】科室信息失败：getDeptmentList_zx--"+e.getMessage());
        }
        return this.result(result, message, page);
    }

    /**
     * 1.3 获取医生信息
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getDoctorList_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getDoctorList_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getDoctorListXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getDoctorList_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        message = map.get("message");
        List<ZxHosInfoRegistPO> list = new ArrayList<ZxHosInfoRegistPO>(0);
        List<ZxHosInfoRegistPO> doctorlist =  new ArrayList<ZxHosInfoRegistPO>(0);
        try {
            if (xml != null && !"".equals(xml)) {
                int returncod = Dom4JUtil.checkWsReturnCode(xml);
                message  = Dom4JUtil.getMessage(xml);
                if(result.equals(Constants.RESULT_MESSAGE_SUCCESS) && returncod ==1){
                    list = Dom4JUtil.readXMLToJavaBean_Zx(new ZxHosInfoRegistPO(), xml);
                    if (!list.isEmpty() && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            if(list.get(i).getScheduleflag().equals(Constants.SCHEDULEFLAGVALUE_1)){
                                list.get(i).setScheduleflag(DictionaryUtil.getDictName(Constants.SCHEDULEFLAG_GROUP, list.get(i).getScheduleflag()));
                                doctorlist.add(list.get(i));
                            }
                        }
                    }
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                }else{
                    result = Constants.RESULT_MESSAGE_ERROR;
                    logger.error("获取【中心医院】医生信息失败！");
                }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("获取【中心医院】医生信息失败：getDoctorList_zx--"+e.getMessage());
        }

        return this.result(result, message, doctorlist);
    }

    /**
     * 1.4 获取指定医生的当前可约状态
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getDoctorScheduleFlag_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getDoctorScheduleFlag_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getDoctorScheduleFlagXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getDoctorScheduleFlag_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        message = map.get("message");
        List<ZxHosInfoRegistPO> list = new ArrayList<ZxHosInfoRegistPO>(0);
        try {
            if (xml != null && !"".equals(xml)) {
                int returncod = Dom4JUtil.checkWsReturnCode(xml);
                message  = Dom4JUtil.getMessage(xml);
                if(result.equals(Constants.RESULT_MESSAGE_SUCCESS) && returncod ==1){
                    list = Dom4JUtil.readXMLToJavaBean_Zx(new ZxHosInfoRegistPO(), xml);
                    result = Constants.RESULT_MESSAGE_ERROR;
                }else{
                    logger.error("获取【中心医院】指定医生的当前可约状态失败！result!=200");
                    result = Constants.RESULT_MESSAGE_ERROR;
                }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("获取【中心医院】指定医生的当前可约状态失败：getDoctorScheduleFlag_zx--"+e.getMessage());
        }
        return this.result(result, message, list);
    }

    /**
     * 1.5 取一个医生的排班列表
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getDoctorSchedule_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getDoctorSchedule_zx(@RequestBody HisZxRequstBean bean) {
        PageModel<HospitalZxRegistPO> page=null;
        String mxl = zhHis.getDoctorScheduleXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getDoctorSchedule_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        List<HospitalZxRegistPO> list = new ArrayList<HospitalZxRegistPO>(0);
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
            message = map.get("message");
            try {
                if (xml != null && !"".equals(xml)) {
                    int returncod = Dom4JUtil.checkWsReturnCode(xml);
                    message  = Dom4JUtil.getMessage(xml);
                    if(result.equals(Constants.RESULT_MESSAGE_SUCCESS) && returncod ==1){
                        list = Dom4JUtil.readXMLToJavaBean_Zx(new HospitalZxRegistPO(), xml);
                        if (!list.isEmpty() && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setReserveFlag(DictionaryUtil.getDictName(Constants.RESERVEFLAG_GROUP, list.get(i).getReserveFlag()));
                                list.get(i).setTimeInterval(DictionaryUtil.getDictName(Constants.TIMEINTERVAL_GROUP, list.get(i).getTimeInterval()));
                            }
                        }
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                    }else{
                        result = Constants.RESULT_MESSAGE_ERROR;
                        logger.error("获取【中心医院】一个医生的排班列表失败！");
                    }
                   /* if (list != null && list.size() > 0) {
                 		int pageRecorders = bean.getPagesize()==0?list.size():bean.getPagesize();
                    	int pageno = bean.getPageno()==0?list.size():bean.getPageno();
                    	page = new PageModel<HospitalZxRegistPO>(list , pageRecorders);
                    	if(page.isNext()){
                    		list = page.getObjects(pageno);
                    		page.setData(list);
                    	}
                    }*/
                }
            } catch (Exception e) {
                result = Constants.RESULT_MESSAGE_ERROR;
                logger.error("获取【中心医院】一个医生的排班列表失败：getDoctorSchedule_zx--"+e.getMessage());
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        return this.result(result, message, list);
    }

    /**
     * 1.6获取具体日期门诊安排的号源情况(列表)
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getScheduleList_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getScheduleList_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getScheduleListXml(bean);
        String zxHosXml = zhHis.setBase64EncoderXml(mxl.getBytes());
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getScheduleList_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        try {
            document = DocumentHelper.parseText(wsResult);
            Element element = document.getRootElement();
            Element description = element.element("RESULT");
            RESULT = description.getText();
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("获取【中心医院】具体日期门诊安排的号源情况(列表)失败--"+e.getMessage());
        }
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS) && RESULT.equals("0")) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        List<DepartmentPO> list = new ArrayList<DepartmentPO>(0);
        return this.result(result, message, list);
    }

    /**
     * 1.7取一个日排班的分时信息
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getSchedulePartTime_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getSchedulePartTime_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getSchedulePartTimeXml(bean);
        String zxHosXml = zhHis.setBase64EncoderXml(mxl.getBytes());
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getSchedulePartTime_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        try {
            document = DocumentHelper.parseText(wsResult);
            Element element = document.getRootElement();
            Element description = element.element("RESULT");
            RESULT = description.getText();
        } catch (DocumentException e1) {
            e1.printStackTrace();
            logger.error("获取【中心医院】取一个日排班的分时信息失败--"+e1.getMessage());
        }
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS) && RESULT.equals("0")) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        List<DepartmentPO> list = new ArrayList<DepartmentPO>(0);
        return this.result(result, message, list);
    }

    /**
     * 1.8生成挂号订单并申请锁号
     * 新建成功，成功后先检查是是否锁定，如果是锁定状态去更新订单记录，如果不是锁定状态调用取消订单；
     * 新建失败，直接返回结果；
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getNewOrder_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getNewOrder_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getNewOrderXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getNewOrder_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        List<HospitalZxRegistPO> list = new ArrayList<HospitalZxRegistPO>(0);
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
            message = map.get("message");
            try {
                if (xml != null && !"".equals(xml)) {
                    int returncod = Dom4JUtil.checkWsReturnCode(xml);
                    list = Dom4JUtil.readNewOrderXMLToJavaBean(new HospitalZxRegistPO(), xml);
                    message  = Dom4JUtil.getMessage(xml);
                    if( returncod ==1){
                        //申请(新建)成功，调用 查询锁号是否成功的接口
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                        if (!list.isEmpty() && list.size() > 0) {
                            String orderid = list.get(0).getOrderid();//订单号
                            String pinkey = list.get(0).getPinkey();//数字订单
                            bean.setOrderid(orderid);//订单号
                            bean.setPinkey(pinkey);//数字订单
                            result = checkLock_zx(bean);
                            if(result.equals(Constants.RESULT_MESSAGE_SUCCESS)){
                                result = Constants.RESULT_MESSAGE_SUCCESS;
                            }else if(result.equals(Constants.RESULT_MESSAGE_EXCEPTION)){
                                result = Constants.RESULT_MESSAGE_EXCEPTION;
                                message="【中心医院】挂号失败，已取消本次订单";
                                logger.error("生成【中心医院】挂号订单失败，已取消订单");
                            }else{
                                result = Constants.RESULT_MESSAGE_ERROR;
                                message="请到未完成订单页面取消";
                                logger.error("查询【中心医院】锁号是否成功-失败");
                            }
                        }
                    }else{ //申请(新建)失败,直接调用取消订单
                        result = Constants.RESULT_MESSAGE_ERROR;
                        logger.error("生成【中心医院】挂号订单并申请锁号失败--xml:returncode!=1");
                    }
                }
            } catch (Exception e) {
                result = Constants.RESULT_MESSAGE_ERROR;
                logger.error("生成【中心医院】挂号订单并申请锁号失败" + e.getMessage());
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("生成【中心医院】挂号订单失败:iface.getNewOrder_zx--result!=200");
        }
        return this.result(result, message, list);
    }

    /**
     * 1.9查询锁号是否成功
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
   /* @RequestMapping(value = "/checkLock_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody*/
    public static String checkLock_zx(HisZxRequstBean bean) {
        String mxl = zhHis.checkLockXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.checkLock_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        int returnLockflag;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
            returnLockflag = Dom4JUtil.checkLockflag(xml);
            if(returnLockflag !=1){
                Thread thread = Thread.currentThread();
                thread.sleep(5000);//查询锁号失败，暂停5秒后程序继续查询
                map = iface.checkLock_zx(zxHosXml, result, message);
                wsResult = map.get("wsResult");
                xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
                logger.error("【中心医院】第一次锁号失败，等待第二次");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = map.get("result");
        // List<HospitalZxRegistPO> list = new ArrayList<HospitalZxRegistPO>(0);
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
            try {
                if (xml != null && !"".equals(xml)) {
                    //list = Dom4JUtil.readNewOrderXMLToJavaBean(new HospitalZxRegistPO(), xml);
                    returnLockflag = Dom4JUtil.checkLockflag(xml);
                    String errorMessage = Dom4JUtil.getMessage(xml);
                    if (returnLockflag == 1) {
                        message = updateOrder_zx(bean);//是锁定状态，去更新订单记录
                        if (message.equals(Constants.RESULT_MESSAGE_SUCCESS)) {//是锁定状态
                            result = Constants.RESULT_MESSAGE_SUCCESS;
                        } else if (message.equals(Constants.RESULT_MESSAGE_EXCEPTION)) {
                            result = Constants.RESULT_MESSAGE_EXCEPTION;
                            logger.error("【中心医院】锁号成功checkLock_zx：更新订单信息失败,取消订单成功");
                        } else {
                            bean.setCancelreason(errorMessage);
                            message = cancelOrderMessage_zx(bean);//取消订单
                            if (message.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                                result = Constants.RESULT_MESSAGE_EXCEPTION;
                                logger.error("查询【中心医院】锁号是否成功checkLock_zx-锁号成功,更新订单失败，取消订单成功");
                            }else {
                                result = Constants.RESULT_MESSAGE_ERROR;
                                logger.error("查询【中心医院】锁号是否成功checkLock_zx-锁号成功,更新订单失败，取消订单失败");
                            }
                        }
                    } else {//检查锁定失败
                        bean.setCancelreason(errorMessage);
                        message = cancelOrderMessage_zx(bean);//取消订单
                        if (message.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                            result = Constants.RESULT_MESSAGE_EXCEPTION;
                            logger.error("查询【中心医院】锁号是否成功checkLock_zx-锁号失败,取消订单成功");
                        }else {
                            result = Constants.RESULT_MESSAGE_ERROR;
                            logger.error("查询【中心医院】锁号是否成功checkLock_zx-锁号失败，取消订单失败");
                        }
                    }
                }
            } catch (Exception e) {
                result = Constants.RESULT_MESSAGE_ERROR;
                logger.error("查询【中心医院】锁号是否成功--失败" + e.getMessage());
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("查询【中心医院】锁号是否成功-失败：iface.checkLock_zx失败");
        }
        return result;
    }

    /**
     * 1.10更新订单信息
     * GetHospitalList
     * 更新订单失败后，调用取消订单cancelOrderMessage_zx();
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/updateOrder_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public static String updateOrder_zx(HisZxRequstBean bean) {
        String mxl = zhHis.updateOrderXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.updateOrder_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
            try {
                if (xml != null && !"".equals(xml)) {
                    //list = Dom4JUtil.readNewOrderXMLToJavaBean(new HospitalZxRegistPO(), xml);
                    int returncod = Dom4JUtil.checkWsReturnCode(xml);
                    String errorMessage  = Dom4JUtil.getMessage(xml);
                    if(returncod==1){
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                    }else{
                        result  = cancelOrderMessage_zx(bean);//取消订单
                        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                            result = Constants.RESULT_MESSAGE_EXCEPTION;
                        }else {
                            logger.error("【中心医院】更新订单信息:取消订单失败-" + errorMessage);
                        }
                    }
                }
            } catch (Exception e) {
                result = Constants.RESULT_MESSAGE_ERROR;
                logger.error("【中心医院】更新订单信息失败" + e.getMessage());
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("【中心医院】更新订单信息失败:iface.updateOrder_zx失败");
        }
        return result;
    }

    /**
     * 1.11提交订单生效
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/confirmOrder_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result confirmOrder_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.confirmOrderXml(bean);
        String zxHosXml = zhHis.setBase64EncoderXml(mxl.getBytes());
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.confirmOrder_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        try {
            document = DocumentHelper.parseText(wsResult);
            Element element = document.getRootElement();
            Element description = element.element("RESULT");
            RESULT = description.getText();
        } catch (DocumentException e1) {
            e1.printStackTrace();
            logger.error("【中心医院】提交订单生效信息失败:confirmOrder_zx--"+e1.getMessage());
        }
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS) && RESULT.equals("0")) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        List<DepartmentPO> list = new ArrayList<DepartmentPO>(0);
        return this.result(result, message, list);
    }

    /**
     * 1.12取历史订单列表
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getOrderInfo_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getOrderInfo_zx(@RequestBody HisZxRequstBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        bean.setIdcard(bean.getPatientidcard());//身份证
        String requestid=bean.getRequestid();
        bean.setRequestid("110");
        Result result1=getPatientInfo_zx(bean);
        try {
            if(( Constants.RESULT_MESSAGE_SUCCESS).equals(result1.getStatusCode())){
                bean.setContactphone(result1.getMessage());
            }else {
                message = "查不到该患者信息";
                logger.error("【中心医院】取历史订单列表:获取患者信息--获取手机号失败");
                result = Constants.RESULT_MESSAGE_ERROR;
                return this.result(result, message, null);
            }
        }catch (Exception e) {
            message = "查不到该患者信息";
            logger.error("【中心医院】取历史订单列表--获取手机号失败--"+e.getMessage());
            result=Constants.RESULT_MESSAGE_ERROR;
            return this.result(result, message, null);
        }
        bean.setRequestid(requestid);
        String mxl = zhHis.getOrderInfoXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getOrderInfo_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        int count = 0;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        message = map.get("message");
        List<HospitalZxOrderPO> list = new ArrayList<HospitalZxOrderPO>(0);
        Page<HospitalZxOrderPO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            if (xml != null && !"".equals(xml)) {
                int returncod = Dom4JUtil.checkWsReturnCode(xml);
                if (returncod == 1&& result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                    list = Dom4JUtil.readXMLToJavaBean_Zx(new HospitalZxOrderPO(), xml);
                    if (!list.isEmpty() && list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setTimeinterval(DictionaryUtil.getDictName( Constants.TIMEINTERVAL_GROUP, list.get(i).getTimeinterval()));
                        }
                        count = Dom4JUtil.checkDeptmentCont(xml);
                        page.setCount(count);
                        page.setData(list);
                        message = "【中心医院】取历史订单列表成功";
                    }
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                } else {
                    message = Dom4JUtil.getMessage(xml);
                    result = Constants.RESULT_MESSAGE_ERROR;
                    logger.error("【中心医院】取历史订单列表失败:returncode!=1||result!=200");
                }
            }
        }catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("【中心医院】取历史订单列表失败:getOrderInfo_zx"+e.getMessage());
        }
        return this.result(result, message, page);
    }
    /**
     * 1.13取消订单
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    public static String  cancelOrderMessage_zx(HisZxRequstBean bean) {
        String mxl = zhHis.cancelOrderXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.cancelOrder_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        // List<HospitalZxRegistPO> list = null;
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
            try {
                if (xml != null && !"".equals(xml)) {
                    //list = Dom4JUtil.readNewOrderXMLToJavaBean(new HospitalZxRegistPO(), xml);
                    int returncod = Dom4JUtil.checkWsReturnCode(xml);
                    String errorMessage  = Dom4JUtil.getMessage(xml);
                    if(returncod==1){
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                    }else{
                        result = Constants.RESULT_MESSAGE_ERROR;
                        logger.error("【中心医院】取消订单失败--returncode!=1--" + errorMessage);
                    }
                }
            } catch (Exception e) {
                result = Constants.RESULT_MESSAGE_ERROR;
                logger.error("【中心医院】取消订单失败" + e.getMessage());
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("【中心医院】取消订单失败：iface.cancelOrder_zx--result!=200");
        }
        return result;
    }

    /**
     * 1.13取消订单
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/cancelOrder_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result cancelOrder_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.cancelOrderXml(bean);
        String zxHosXml = "";
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.cancelOrder_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        try {
            if (xml != null && !"".equals(xml)) {
                int returncod = Dom4JUtil.checkWsReturnCode(xml);
                if(result.equals(Constants.RESULT_MESSAGE_SUCCESS) && returncod ==1){
                    message = Dom4JUtil.getMessage(xml);
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                } else {
                    message  = Dom4JUtil.getMessage(xml);
                    result = Constants.RESULT_MESSAGE_ERROR;
                    logger.error("【中心医院】取消订单失败cancelOrder_zx:returncode!=1"+message);
                }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("【中心医院】取消订单失败:cancelOrder_zx--"+e.getMessage());
        }
        return this.result(result, message);
    }

    /**
     * 1.14确定退费
     * GetHospitalList
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/refund_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result refund_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.refundXml(bean);
        String zxHosXml = zhHis.setBase64EncoderXml(mxl.getBytes());
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.refund_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        try {
            document = DocumentHelper.parseText(wsResult);
            Element element = document.getRootElement();
            Element description = element.element("RESULT");
            RESULT = description.getText();
        } catch (DocumentException e1) {
            e1.printStackTrace();
            logger.error("【中心医院】确定退费失败--"+ e1.getMessage());
        }
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS) && RESULT.equals("0")) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        List<DepartmentPO> list = new ArrayList<DepartmentPO>(0);
        return this.result(result, message, list);
    }

    /**
     * 1.15取短信文本
     * GetHospitalList
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getSms_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getSms_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getSmsXml(bean);
        String zxHosXml = zhHis.setBase64EncoderXml(mxl.getBytes());
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getSms_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        try {
            document = DocumentHelper.parseText(wsResult);
            Element element = document.getRootElement();
            Element description = element.element("RESULT");
            RESULT = description.getText();
        } catch (DocumentException e1) {
            e1.printStackTrace();
            logger.error("【中心医院】确定退费失败--"+e1.getMessage());
        }
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS) && RESULT.equals("0")) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        List<DepartmentPO> list = new ArrayList<DepartmentPO>(0);
        return this.result(result, message, list);
    }
    /**
     * 1.16获取指定排班的当前可约状态
     * GetHospitalList
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getScheduleFlag_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getScheduleFlag_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getScheduleFlagXml(bean);
        String zxHosXml = zhHis.setBase64EncoderXml(mxl.getBytes());
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getScheduleFlag_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        try {
            document = DocumentHelper.parseText(wsResult);
            Element element = document.getRootElement();
            Element description = element.element("RESULT");
            RESULT = description.getText();
        } catch (DocumentException e1) {
            e1.printStackTrace();
            logger.error("获取【中心医院】指定排班的当前可约状态失败--"+ e1.getMessage());
        }
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS) && RESULT.equals("0")) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        List<DepartmentPO> list = new ArrayList<DepartmentPO>(0);
        return this.result(result, message, list);
    }

    /**
     * 1.17 获取退费记录接口
     * GetHospitalList
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
    @RequestMapping(value = "/getRefundList_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getRefundList_zx(@RequestBody HisZxRequstBean bean) {
        String mxl = zhHis.getRefundListXml(bean);
        String zxHosXml = zhHis.setBase64EncoderXml(mxl.getBytes());
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        map = iface.getRefundList_zx(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        wsResult = zhHis.getBase64DecoderXml(wsResult);
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        try {
            document = DocumentHelper.parseText(wsResult);
            Element element = document.getRootElement();
            Element description = element.element("RESULT");
            RESULT = description.getText();
        } catch (DocumentException e1) {
            e1.printStackTrace();
            logger.error("获取【中心医院】退费记录接口失败--"+ e1.getMessage());
        }
        if (result.equals(Constants.RESULT_MESSAGE_SUCCESS) && RESULT.equals("0")) {
            result = Constants.RESULT_MESSAGE_SUCCESS;
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        List<DepartmentPO> list = new ArrayList<DepartmentPO>(0);
        return this.result(result, message, list);
    }

    /**
     * 1.18  提交订单生效（包含支付信息)
     * GetHospitalList
     * @param bean
     * @return Result    返回类型
     * 前台调东软动态库缴费成功后，
     * 先通知平台（noticePlatfrom_zx），
     * 再调支付（confirmOrderPay_zx），
     * 支付成功或失败都要修改平台状态（updateBusInfoHTPOSatus）
     * 支付失败调取消订单
     * @Title:
     */
    @RequestMapping(value = "/confirmOrderPay_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result confirmOrderPay_zx(@RequestBody  HisZxRequstBean bean) {
        BusInfoHTPO  busInfoHTPO = this.noticePlatfrom_zx(bean);
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        List<ZxHosInfoRegistPO> list = null;
        if(busInfoHTPO!=null){
            String mxl = zhHis.confirmOrderPayXml(bean);
            String zxHosXml = null;
            try {
                zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Map<String, String> map = new HashMap<String, String>();
            map = iface.confirmOrderPay_zx(zxHosXml, result, message);
            wsResult = map.get("wsResult");
            String xml = null;
            try {
                xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            result = map.get("result");
            message = map.get("message");
            try {
                if (xml != null && !"".equals(xml)) {
                    int returncod = Dom4JUtil.checkWsReturnCode(xml);
                    if(result.equals(Constants.RESULT_MESSAGE_SUCCESS)){
                        message  = Dom4JUtil.getMessage(xml);
                        if(returncod ==1){
                            busInfoHTPO.setSTATUS(Constants.HIS_HT_STATUS_03);//挂号成功
                            busInfoHTPO.setREGSUCCTIME(CommUtil.getNowDateLongStr());// 挂号成功时间
                            //支付成功后，修改平台记录数据状态
                            String statuCode = updateBusInfoHTPOSatus(busInfoHTPO);
                            if(statuCode.equals(Constants.RESULT_MESSAGE_SUCCESS)){
                                smsSend(bean);
                                logger.error("【中心医院】支付成功后，修改平台记录数据状态成功-03！");
                            }else{
                                logger.error("【中心医院】支付成功后，修改平台记录数据状态失败-03！");
                            }
                            result = Constants.RESULT_MESSAGE_SUCCESS;
                        }else{
                            message  = cancelOrderMessage_zx(bean);//取消订单
                            result = Constants.RESULT_MESSAGE_ERROR;
//                            result  = cancelOrderMessage_zx(bean);//取消订单
                            busInfoHTPO.setREGSERRORTIME(CommUtil.getNowDateLongStr());// 挂号失败时间
                            busInfoHTPO.setSTATUS(Constants.HIS_HT_STATUS_05);//挂号失败
                            String statuCode = updateBusInfoHTPOSatus(busInfoHTPO);
                            if(statuCode.equals(Constants.RESULT_MESSAGE_SUCCESS)){
                                //result = Constants.RESULT_MESSAGE_SUCCESS;
                            }else{
                                //result = Constants.RESULT_MESSAGE_ERROR;
                                logger.error("【中心医院】支付成功后，修改平台记录数据状态失败！-05挂号失败");
                            }
                            logger.error("【中心医院】提交订单生效失败！iface.confirmOrderPay_zx-returncode!=1");
                        }
                    } else{
                        String errorMessage  = Dom4JUtil.getMessage(xml);
                        bean.setCancelreason(errorMessage);
                        message  = cancelOrderMessage_zx(bean);//取消订单
                        result = Constants.RESULT_MESSAGE_ERROR;
                        logger.error("【中心医院】confirmOrderPay_zx提交订单生效失败--iface.confirmOrderPay_zx失败");
                    }
                }
            } catch (Exception e) {
                result = Constants.RESULT_MESSAGE_ERROR;
                logger.error("【中心医院】提交订单生失败confirmOrderPay_zx--"+e.getMessage());
                message ="【中心医院】提交订单生效失败";
            }
        }else{
            result = Constants.RESULT_MESSAGE_ERROR;
        }
        return this.result(result, message, list);
    }
    /**
     * 1.18  提交订单生效（包含支付信息)
     * GetHospitalList
     *
     * @param bean
     * @return Result    返回类型
     * @throws
     * @Title:
     */
   /* @RequestMapping(value = "/noticePlatfrom_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody*/
    public  static BusInfoHTPO  noticePlatfrom_zx(@RequestBody  HisZxRequstBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        List<ZxHosInfoRegistPO> list = null;
        String xmlData = "";  //封装数据
        String id = "0";
        BusInfoHTPO po = new BusInfoHTPO();
        po.setSTATUS(Constants.HIS_HT_STATUS_02);//状态 02 东软缴费成功
        po.setPAYTIME(CommUtil.getNowDateLongStr()); //缴费时间
        po.setTRANSNO(bean.getPayType());//医院交易流水号；
        po.setVISID(bean.getPayid());//单据号
        po.setREGFEE(String.valueOf(bean.getExt8()));//挂号费
        //po.setTERMINALID(bean.getTermId());//设备终端号
        po.setTERMINALID(bean.getZdm());//设备终端号
        po.setIDENTITY(bean.getExt4());//身份证号
        po.setCARDTYPE(bean.getCardType());//接口类型(如：大众端-02、德生宝-01)
        po.setCARDNO(bean.getCardNO());//社保卡号
        po.setACCOUNTTYPE(bean.getZflb());//支付类别
        po.setNAME(bean.getExt2());//姓名
        po.setSEX(bean.getExt3().equals("M")?"1":"2");//性别
        long day=zhHis.getDays(bean.getExt5(), zhHis.getStringDate());
        long age=day/365;
        int ageValue = new Long(age).intValue();
        po.setAGR(ageValue);
        po.setDEPARTMENT(bean.getDeptcode());//科室编码
        po.setREGDATE(bean.getOutpdate());//挂号日期
        if(bean.getTimeinterval().equals(Constants.TIMEINTERVALAM)){
            po.setREGTIME(Constants.TIMEINTERVALAMCODE);//挂号时段
        }else{
            po.setREGTIME(Constants.TIMEINTERVALPMCODE);//挂号时段
        }
        //po.setREGTIME(bean.getTimeinterval());//挂号时段
        po.setHOSID(bean.getHospitalid());//医院代码
        po.setCHANNELTPYE(bean.getChanneltpye());//接口类型
        po.setDEPARTMENTMC(bean.getDeptname());//科室名称
        po.setHOSPITALMC(bean.getHospitalname());//医院名称
        po.setDOCTORMC(bean.getDoctorname());//医生姓名     
        po.setDOCTORTYPE(bean.getDoctortype());//挂号类型（专家号，普通号）
        Random random = new Random();
        int randomValue = random.nextInt(9999);
        po.setFLOWID(randomValue+"");//号表
        try {
            xmlData = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(po), DictionaryUtil.getHost(Constants.INSERTBUSINFO_HT));
            Result irpo = JsonMapper.buildNormalMapper().fromJson(xmlData, Result.class);
            id = String.valueOf(irpo.getData());
            po.setID(Integer.parseInt(id));
            result = irpo.getStatusCode();
            message = irpo.getMessage();
            if(result.equals(Constants.RESULT_MESSAGE_SUCCESS)){
            }else{
                po = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("【中心医院】提交订单生效失败noticePlatfrom_zx--"+e.getMessage());
        }
        return po;
        // return this.result(result, message, list);
    }

    public static String smsSend(HisZxRequstBean bean){
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        SmsRequstBean smsBean = new SmsRequstBean();
        String mobile = bean.getContactphone();

        smsBean.setGHF(bean.getExt8());//挂号费
        smsBean.setRegdate(bean.getOutpdate());//挂号时间
        smsBean.setNewPhoneNo(mobile);
        smsBean.setDEPARTMENTMC(bean.getDeptname());//科室名称
        smsBean.setDOCTORMC(bean.getDoctorname());//医生名称
        smsBean.setHOSPITALMC(bean.getHospitalname());//医院名称
        smsBean.setREGTIME(bean.getTimeinterval().equals("AM")?"上午":"下午");//挂号时段
        smsBean.setName(bean.getExt2());
        smsBean.setType("1");
        if(!mobile.isEmpty()){
            SmsSendUtil smsUtil = new SmsSendUtil();
            String smsResult = smsUtil.SmsSend(smsBean);
            if(smsResult.equals("200")){
                logger.info("短信接口调用成功!");
                result =Constants.RESULT_MESSAGE_SUCCESS;
            }else{
                logger.error("短信接口调用失败!");
                result=Constants.RESULT_MESSAGE_ERROR;
            }
        }else{
            logger.error("发送短信时，手机号码不能为空!");
        }
        return result;
    }

    /**
     * @param bean
     * @return Result    返回类型
     * @throws IOException 调东软缴费成功后，通知平台-调支付，支付成功后，更新状态；
     * @throws
     * @Title: noticePlatfrom_zx
     */
    public static String updateBusInfoHTPOSatus(BusInfoHTPO bean){
        String statuCode = Constants.RESULT_MESSAGE_SUCCESS;
        String updatehisResult;
        try {
            updatehisResult = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(bean), DictionaryUtil.getHost(Constants.UPDATEBUSINFO_HT));
            Result updateResult = JsonMapper.buildNormalMapper().fromJson(updatehisResult, Result.class);
            statuCode = updateResult.getStatusCode();
            if(statuCode.equals(Constants.RESULT_MESSAGE_SUCCESS)){
                statuCode = Constants.RESULT_MESSAGE_SUCCESS;
            }else{
                statuCode = Constants.RESULT_MESSAGE_ERROR;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statuCode;
    }

    public BusInfoHTPO copyProperties(HisZxRequstBean bean){
        BusInfoHTPO po = new BusInfoHTPO();
        return po;
    }

    /**
     * @param bean
     * @return Result    返回类型
     * @throws IOException 读取卡信息拼成报文
     * @throws
     * @Title: getHisConsumerDetail
     */
    @RequestMapping(value = "/getHisPayCardXml_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHisPayCardXml_zx(@RequestBody HisRequstBean bean) {
        String xmlData = "";  //封装终端缴费数据
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        HisRequstBean beanVo = new HisRequstBean();
        List<HisRequstBean> list3 = new ArrayList<HisRequstBean>();
        String yyjylsh = getHospTradeCode(bean.getYYBH());
        beanVo.setYYJYLSH(yyjylsh);
        beanVo.setYYBH(bean.getYYBH());
        beanVo.setCZYBH("5000");
        String mzzylsh = null;
        try {
            mzzylsh = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(bean), DictionaryUtil.getHost(Constants.GETVISIDSEQ_HT));
        } catch (IOException e) {
            logger.error("获取就诊号出错");
            e.printStackTrace();
        }
        beanVo.setMZZYLSH(mzzylsh);
        beanVo.setKSBH(bean.getKSBH());
        beanVo.setDJH("2");
        beanVo.setGHF(bean.getGHF());
        beanVo.setYLLB("11");
        beanVo.setYYSFXMNM("z1197");
        beanVo.setSFXMZXBM("F00000005649");
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

    /*
	@version
	@parameter
	@return
	@throws
	@ 生成医院交易流水号
	@ 生成规则  本地时间（20150702223124）+医院编号（140034）+四位随机数（0250）
	@ 201507022231241400340250
	*/
    public static String getHospTradeCode(String yybh) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String value = "";
        //生成本地系统时间；
        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(currentTime);
        String current = formatter.format(date);
//	    //医院编码
//	    String hospitalCode = Constants.HT_HOSPITAL_CODE;
        //生成4位随机数；
        Random random = new Random();
        int randomValue = random.nextInt(9999);
        if (current == null || current == "") {
            logger.error("生成本地系统时间失败");
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "生成本地系统时间失败";
        } else if (yybh == null || yybh == "") {
            logger.error("获取医院编号失败");
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取医院编号失败";
        } else {
            //value = current + yybh + randomValue;
            value = current + yybh;
        }
        return value;
    }

    /**
     * 检查诊疗卡是否绑定接口
     * @param bean
     * @return
     */
    @RequestMapping(value = "/checkCardSign", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result checkCardSign(@RequestBody HisZxRequstBean bean) {
        ZxHisUtil zhHis = new ZxHisUtil();
        String mxl = zhHis.getCheckCardSignXml(bean);
        String zxHosXml = null;
        try {
            zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        Map<String, String> map = new HashMap<String, String>();
        ZXHisIface iface = new ZXHisIface();
        map = iface.getCheckCardSign(zxHosXml, result, message);
        wsResult = map.get("wsResult");
        String xml = null;
        try {
            xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = map.get("result");
        message = map.get("message");
        Document document;
        String RESULT = "";
        List<ZxHosInfoRegistPO> list = null;
        try {
            if (xml != null && !"".equals(xml)) {
                list = Dom4JUtil.readXMLToJavaBean_Zx(new ZxHosInfoRegistPO(), xml);
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("获取'检查诊疗卡是否绑定接口'失败--"+e.getMessage());
        }
        return this.result(result, message, list);
    }

    /**
     * 新患者HIS绑定接口
     * @param bean
     * @return
     */
    @RequestMapping(value = "/cardSignNewPatient_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result cardSignNewPatient_zx(@RequestBody HisZxRequstBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        SecQueryBean queryBean = new SecQueryBean();
        queryBean.setCernum(bean.getIdcard());//身份证
        queryBean.setName(bean.getPatientname());//患者名称；
        queryBean.setCardno(bean.getCardNO());//社保卡号
        try {
            String bankno=zhHis.getBankNo("",bean.getIdcard());//服务器
//        NetUserInfoVO bankno =  netUserService.getPseronBank(queryBean);//本地
            if (bankno != null && !bankno.equals("")) {
                bean.setBankno(bankno);
//                bean.setBankno(bankno.getBankno());
                String mxl = zhHis.getCardSignNewPatientXml(bean);
                String zxHosXml = null;
                try {
                    zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String wsResult = "";  //iface上不做任何的业务处理
                Map<String, String> map = new HashMap<String, String>();
                map = iface.cardSignNewPatient(zxHosXml, result, message);
                wsResult = map.get("wsResult");
                String xml = null;
                try {
                    xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result = map.get("result");
                if (xml != null && !"".equals(xml)) {
                    int returncod = Dom4JUtil.checkWsReturnCode(xml);
                    if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                        if (returncod == 1) {
                            result = Constants.RESULT_MESSAGE_SUCCESS;
                        } else {
                            result = Constants.RESULT_MESSAGE_ERROR;
                            message = Dom4JUtil.getMessage(xml);
                            logger.error("获取'新患者HIS绑定接口'失败returncode!=1" + message);
                        }
                    } else {
                        result = Constants.RESULT_MESSAGE_ERROR;
                        logger.error("获取'新患者HIS绑定接口'失败iface.cardSignNewPatient失败");
                    }
                }
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "患者银行帐号不能为空！";
                logger.error("cardSignNewPatient_zx:获取患者银行信息失败");
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("获取'新患者HIS绑定接口'失败--" + e.getMessage());
        }
        return this.result(result, message, null);
    }

    /*
     * 获取患者信息
     * @param bean
     * @return
     */
    @RequestMapping(value = "/getPatientInfo_zx", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPatientInfo_zx(@RequestBody HisZxRequstBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        String wsResult = "";  //iface上不做任何的业务处理
        List<ZxHosInfoRegistPO> list = null;
        SecQueryBean queryBean = new SecQueryBean();
        queryBean.setCernum(bean.getIdcard());//身份证
        queryBean.setName(bean.getPatientname());//患者名称；
        queryBean.setCardno(bean.getCardNO());//社保卡号
        try {
            String bankno=zhHis.getBankNo("",bean.getIdcard());//服务器
//            NetUserInfoVO bankno = netUserService.getPseronBank(queryBean);
            if (bankno != null && !bankno.equals("")) {
                bean.setBankno(bankno);
//                bean.setBankno(bankno.getBankno());
                String mxl = zhHis.getPatientInfoXml(bean);
                String zxHosXml = null;
                try {
                    zxHosXml = zhHis.setBase64EncoderXml_Zx(mxl.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Map<String, String> map = new HashMap<String, String>();
                map = iface.getPatientInfo(zxHosXml, result, message);
                wsResult = map.get("wsResult");
                String xml = null;
                try {
                    xml = new String(zhHis.getBase64DecoderXml_Zx(wsResult), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                result = map.get("result");
                message = map.get("message");
                if (xml != null && !"".equals(xml)) {
                    int returncod = Dom4JUtil.checkWsReturnCode(xml);
                    if (result.equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                        message = Dom4JUtil.getMessage(xml);
                        if (returncod == 1) {
                            list = Dom4JUtil.readXMLToJavaBean_Zx(new ZxHosInfoRegistPO(), xml);
                            message = list.get(0).getExt1();
                            result = Constants.RESULT_MESSAGE_SUCCESS;
                        } else {
                            result = Constants.RESULT_MESSAGE_EXCEPTION;
                            logger.error("获取获取患者信息接口失败 returncode!=1" + message);
                        }
                    } else {
                        message = "获取患者信息失败！";
                        logger.error("获取获取患者信息接口失败 iface.cardSignNewPatient失败");
                    }
                }
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "患者银行帐号不能为空！";
                logger.error("getPatientInfo_zx:获取患者银行信息失败");
            }
        } catch (Exception e1) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取患者信息失败！";
            logger.error("getPatientInfo_zx:获取患者信息失败--" + e1.getMessage());
        }
        return this.result(result, message, list);
    }
}
