package com.tecsun.sisp.iface.server.controller.cssp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.ImageChangeUtil;
import com.tecsun.sisp.iface.common.util.PicUtil;
import com.tecsun.sisp.iface.common.vo.ApplyCardVO;
import com.tecsun.sisp.iface.common.vo.BusRcmBinVO;
import com.tecsun.sisp.iface.common.vo.BusRcmBoxVO;
import com.tecsun.sisp.iface.common.vo.BusRcmCardVO;
import com.tecsun.sisp.iface.common.vo.NewPicInfo;
import com.tecsun.sisp.iface.common.vo.NewPicInfoPO;
import com.tecsun.sisp.iface.common.vo.PersonalApply;
import com.tecsun.sisp.iface.common.vo.PicInfoPO;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;
import com.tecsun.sisp.iface.common.vo.SfzInfo;
import com.tecsun.sisp.iface.common.vo.picInfo;
import com.tecsun.sisp.iface.common.vo.picInfo1;
import com.tecsun.sisp.iface.common.vo.card.TsjbxxVO;
import com.tecsun.sisp.iface.common.vo.cssp.Applybean;
import com.tecsun.sisp.iface.common.vo.cssp.BasicPersonInfoBean;
import com.tecsun.sisp.iface.common.vo.cssp.BusApplyPicVO;
import com.tecsun.sisp.iface.common.vo.cssp.LoginVO;
import com.tecsun.sisp.iface.common.vo.cssp.PersonInfoVO;
import com.tecsun.sisp.iface.common.vo.cssp.PicVO;
import com.tecsun.sisp.iface.common.vo.cssp.ResultBusMakeDetal;
import com.tecsun.sisp.iface.common.vo.cssp.ResultLoginvo;
import com.tecsun.sisp.iface.common.vo.cssp.ResultPersonApplyVO;
import com.tecsun.sisp.iface.common.vo.cssp.ResultUpVO;
import com.tecsun.sisp.iface.common.vo.cssp.ResultXhcVO;
import com.tecsun.sisp.iface.common.vo.cssp.UseInfoBean;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.CsspQueryServiceImpl;
import com.tecsun.sisp.iface.server.model.service.SecQueryServiceImpl;
import com.tecsun.sisp.iface.server.model.service.SispPublicQueryServiceImpl;
import com.tecsun.sisp.iface.server.util.CommUtil;
import com.tecsun.sisp.iface.server.util.Config;
import com.tecsun.sisp.iface.server.util.JedisUtil;

/**
 * Created by hhl on 2016/6/16.
 */
@Controller
@RequestMapping(value = "/iface/cssp")
public class CsspBusController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(CsspBusController.class);
    private final String collectPhotoPath1 = "/home/disk/voicePrint/updatePic1/";
   // private final String collectPhotoPath1 = "D:\\test1\\";
    private final String collectPhotoPath2 = "/home/disk/voicePrint/updatePic2/";
  //  private final String collectPhotoPath2 = "D:\\test2\\";
    private final String collectPhotoPath3 = "/home/disk/voicePrint/updatePic3/";
    private final String sfzPhotoPath = "/home/disk/sfz/";
    //private final String collectPhotoPath3 = "G:\\imagetest\\";
    private  final  String  cardmaxsize=Config.getInstance().get("cardmaxsize");
    private  final  String  carboxcount=Config.getInstance().get("carboxcount");

    private static boolean showBoxFullMsg = true;//是否提示盒子已装满的消息（滞留卡登记功能），true_提示，false_不提示
    
    @Autowired
    private SecQueryServiceImpl secQueryService;

    @Autowired
    private CsspQueryServiceImpl csspQueryService;

    @Autowired
    private SispPublicQueryServiceImpl sispPublicQueryService;

    //系统内部调用 根据用户名，身份证获取用户唯一 个人编号
    public TsjbxxVO getGrbh(String sfzh, String xm) throws Exception {
        TsjbxxVO tsjbxxVO = new TsjbxxVO();
        tsjbxxVO.setSfzh(sfzh);
        tsjbxxVO.setXm(xm);
        return secQueryService.getTsjbxxVO(tsjbxxVO);
    }
    /*********************************  新申领 start   *****************************************/

    public UseInfoBean getInfo() throws Exception {
        try{
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String deviceId = request.getParameter("deviceid");
           // String deviceId="864881020737852";//864881020737852
            //System.out.println("deviceId:"+deviceId);
            UseInfoBean bean = sispPublicQueryService.getInfo(deviceId);
            if(bean != null){
                System.out.println("orgid:"+bean.getOrgId());
                //  System.out.println("===" +bean.getDevcode());
            }
            return bean;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "/canApply", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result canApply(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "查询成功";
        String cernum = bean.getId();
        String name = bean.getName();
        if(StringUtils.isEmpty(cernum) && StringUtils.isEmpty(name)){
            return error("参保人姓名和身份证号不能为空","参保人姓名和身份证号不能为空");
        }
        String msg = "";
        try {
            TsjbxxVO vo = getGrbh(cernum, name);
            List<BasicPersonInfoBean> personInfo = csspQueryService.getPersonInfo(name,cernum);
            if ( vo != null || (personInfo.size()>0 && !personInfo.isEmpty())) {//(vo != null ||
                if(personInfo.size()>0 && !personInfo.isEmpty()){
                    String date=new SimpleDateFormat("yyyy-MM-dd").format(personInfo.get(0).getAddDate());
                    msg = "您于"+date+"时间已完成申领，请勿重复申领";
                    result="201";
                }
                else {
                    message="可以申领";
                    msg ="可以申领";
                }


            }else {
                message="可以申领";
                msg = "可以申领";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "查询参保人信息异常：" + e.getMessage();
        }
        return result(result, message, msg);
    }

    @RequestMapping(value = "/applyCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result applyCard(@RequestBody Applybean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "成功";
        UseInfoBean  useInfoBean = getInfo();

        if(useInfoBean == null || (useInfoBean.getOrgId() == null || useInfoBean.getOrgId() == 0)){
            return error("请先到后台把德生宝和社保网点关联起来","请先到后台把德生宝和社保网点关联起来");
        }

        Integer orgid = useInfoBean.getOrgId();
        String cernum = bean.getCertNum();
        //参保人基本信息表

        bean.setCertType("03");//身份证
        bean.setStatus("00");//已参保
        bean.setIsEnabled("01");//有效
        bean.setAddUserId(orgid);

        //bean.setAddUserId(1);
        bean.setOperatorId(orgid);
        System.out.println("pname:"+bean.getPhotoName());
        if(StringUtils.isEmpty(bean.getPhotoName())){
            return error("参保人相片不能为空","参保人相片不能为空");
        }
        PicVO picVO = null;
        if(StringUtils.isNotEmpty(bean.getPhotoName())){//参保人相片名字
            picVO = csspQueryService.queryPicByPicName(bean.getPhotoName());
        }

        String picPath = null;

        String signName = bean.getSignName();
        String pic = bean.getBase64String();


        if (StringUtils.isBlank(pic) || StringUtils.isBlank(signName)){
            return error("电子签名不能为空", "电子签名不能为空");
        }

        File file = new File(collectPhotoPath3);//collectPhotoPath3
        if(!file.exists()){
            file.mkdirs();
        }

        String signPhotoPath = collectPhotoPath3 + "QM_" +signName +"_"+ new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
        if (signPhotoPath != null) {
            OutputStream os = null;
            try {
                os = new FileOutputStream(new File(signPhotoPath));
                os.write(Base64.getMimeDecoder().decode(pic));
            } catch (Exception e) {
                e.printStackTrace();
                return error("电子签名读取错误", "电子签名读取错误");
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(StringUtils.isEmpty(signPhotoPath)){
            return error("参保人电子签名不能为空","参保人电子签名不能为空");
        }
        bean.setSignPhoto(signPhotoPath);


        String orgAddress = csspQueryService.getOrgAddress(orgid);

        bean.setTsbAddress(orgAddress);

       /* if(StringUtils.isEmpty(bean.getAddress())){
            bean.setAddress(orgAddress);
        }*/
        bean.setAddress(bean.getAddress());
        bean.setPhotoUrl(picPath);
        //参保人历史记录表
        bean.setBusType("01");//申领
        bean.setBusStatus("103");//业务已受理
        bean.setHisOperatorId(useInfoBean.getOrgId());//操作人
        // bean.setHisOperatorId(1);//操作人
        //个人申领业务表
        bean.setBusDataType("00");//正常发起业务数据
        bean.setBankOpenType("01");//银行开户类型 -- 新申领/新开户
        bean.setProcessDataType("01");//表单数据处理 -- 本网点录入
        bean.setMakeCardType("00");//非现场制卡
        bean.setApStatus("03");//业务已受理
        bean.setNetType("01");//网点类型 -- 社保机构
        bean.setNetId(orgid);//所属网点id
        //bean.setNetId(1);//所属网点id
        bean.setChannel("TSB");
        if(StringUtils.isNotEmpty(bean.getAgentCertNo())){
            bean.setApplyType("02");//别人代办
        }else {
            bean.setApplyType("01");//自行办理
        }

        if(useInfoBean.getCsspBankId() != null || useInfoBean.getCsspBankId() != 0){
            bean.setBankId(useInfoBean.getCsspBankId());//制卡银行id
        }
        // bean.setBankId(181);//制卡银行id
        bean.setIswritedata(1);//是否网点已对申领表单录入数据 -- 已经录入
        bean.setCardIssueNetType("01");//领卡网点类型 -- 社保机构
        bean.setCardIssueNetId(orgid);//领卡网点ID
        //bean.setCardIssueNetId(181);//领卡网点ID
        String name = bean.getName();
        if(StringUtils.isEmpty(cernum) && StringUtils.isEmpty(name)){
            return error("参保人姓名和身份证号不能为空","参保人姓名和身份证号不能为空");
        }

        if(StringUtils.isEmpty(bean.getSex())){
            return error("性别不能为空","性别不能为空");
        }
        bean.setSex(Constants.SEX.get(bean.getSex().trim()));


        String birth = getBirth(cernum);
        bean.setBirthday(birth);

        if(StringUtils.isEmpty(bean.getPersonType())){
            return error("人员类别不能为空","人员类别不能为空");
        }
        if(StringUtils.isEmpty(bean.getNation())){
            return error("民族不能为空","民族不能为空");
        }
        if(StringUtils.isNotEmpty(bean.getNation())){
            bean.setNation(Constants.NATION_Code.get(bean.getNation().trim()));
        }

        if(StringUtils.isEmpty(bean.getGuoji())){
            return error("国籍不能为空","国籍不能为空");
        }

        if(StringUtils.isEmpty(bean.getMobile())){
            return error("手机号不能为空","手机号不能为空");
        }

        if(StringUtils.isEmpty(bean.getTsbAddress())){
            return error("申领人通讯地址不能为空","申领人通讯地址不能为空");
        }
        ApplyCardVO applyCardVO=new ApplyCardVO();
        applyCardVO.setCertNum(cernum);//身份证
        applyCardVO.setName(bean.getName());//姓名
        applyCardVO.setMobile(bean.getMobile());//手机号
        String d=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        applyCardVO.setApplytime(d);//时间
        applyCardVO.setPic(pic);//电子签名
        applyCardVO.setOrgAddress(orgAddress);//领卡地址
        String devcode =useInfoBean.getDevcode();
        applyCardVO.setDevcode(devcode);
        bean.setDevcode(devcode);
        //    System.out.println("======"+applyCardVO.getDevcode());
        if(bean.getPersonType().equals("1")){
            applyCardVO.setPersonType("城镇职工");
        }
        else if(bean.getPersonType().equals("2")){
            applyCardVO.setPersonType("城镇居民");
        }
        else if(bean.getPersonType().equals("3")){
            applyCardVO.setPersonType("农村居民");
        }
        else{
            applyCardVO.setPersonType("其他");
        }
        bean.setDevcode(devcode);

        String data = "";
        try {
            Integer rs =  csspQueryService.insertBasePersonInfo(bean);//返回个人信息表主键id
            System.out.println("rs:"+rs);
            if(rs != null){
                if(picVO != null){
                    if(picVO.getIsCheck().equals("04")){
                        Map map = new HashMap();
                        map.put("id",rs);
                        map.put("photoUrl",picVO.getPicPath());
                        csspQueryService.updatePersonPic(map);
                        //System.out.println("su.......");
                    }
                }
                bean.setPersonId(rs);

                Integer r = csspQueryService.insertBusPersonalApply(bean);//返回业务表主键id
                if(r != null){
                    bean.setBusId(r);
                    Integer t = csspQueryService.insertBusHisInfo(bean);
                    if(t >0){

                        return result(result, message, applyCardVO);

                    }else {
                        return error("失败","");
                    }
                }

            }
            // return error("失败","");
        } catch (Exception e) {
            e.printStackTrace();
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "查询参保人信息异常：" + e.getMessage();
        }
        return result(result, message, data);
    }

    public String getBirth(String certNum){
        String birth = "";
        String year = certNum.substring(6,10);
        String mouth = certNum.substring(10,12);
        String day = certNum.substring(12,14);
        birth = year+"-"+mouth+"-"+day;
        return birth;
    }
    /**
     * 1.德生宝传照片
     * 2.参数： 照片名 、 照片64位数据
     * 3.需要加的字段 ：  身份证、姓名
     * @param bean
     * @return
     */
    @RequestMapping(value = "/uploadPicInfoByTSB", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object uploadPicInfoByTSB(@RequestBody picInfo bean) {
        String picName = bean.getPicName();
        String pic = bean.getBase64String();
        String cernum = bean.getCernum();
        String name = bean.getName();
      /*  System.out.println("picName==:"+picName);
        System.out.println("cernum==:"+cernum);*/
        if (StringUtils.isBlank(pic) || StringUtils.isBlank(picName)){
            return result("201", "上传图片信息不能为空");
        }

        if (StringUtils.isBlank(cernum) || StringUtils.isBlank(name)){
            return result("201", "参保人身份证不能为空");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        File file = new File(collectPhotoPath1);
        if(!file.exists()){
            file.mkdirs();
        }
        String PhotoPath2 = collectPhotoPath1 + picName + ".jpg";
        map.put("picName", picName);
        map.put("picPath", PhotoPath2);
        map.put("cernum", cernum);
        map.put("name", name);
        if (PhotoPath2 != null) {
            OutputStream os = null;
            try {
                os = new FileOutputStream(new File(PhotoPath2));
                os.write(Base64.getMimeDecoder().decode(pic));
            } catch (Exception e) {
                e.printStackTrace();
                return this.result("302", "图片读取错误");
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        csspQueryService.insertPic(map);
        return result("200", "上传图片成功");
    }

    /**
     * 轮询接口
     * 图片处理；获取未处理图片的照片信息；状态为01
     * @param
     * @return
     */
    @RequestMapping(value = "/getPicInfoByPC", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getPicInfoByPC() {
        // System.out.println("coming.....");
        List<PicInfoPO> list = csspQueryService.getPicIsCheck();
        if(list != null){
            // System.out.println("list=================="+list.size());
            List<picInfo1> list1 = new ArrayList<picInfo1>();
            if (list.size() == 0) {
                //System.out.println("300.......");
                return this.result("300", "尚未存在图片资料");
            }
            String photoPath = list.get(0).getPicPath();
            String picName = list.get(0).getPicName();
            String isCheck = list.get(0).getIsCheck();
            if (photoPath == null) {
                return this.result("300", "尚未存在图片资料");
            }
            String strPic = PicUtil.GetImageStr(photoPath);
            picInfo1 photo = new picInfo1();
            photo.setBase64String(strPic);
            photo.setIsCheck(isCheck);
            photo.setPicName(picName);
            list1.add(photo);
            return result("200", "获取图片成功", list1);
        }else {
            return this.result("300", "尚未存在图片资料");
        }
    }

    /**
     * PICServer预处理返回接口
     * @param info
     * @return
     */
    @RequestMapping(value = "/pretreatmentByPC", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object pretreatmentByPC(@RequestBody PicInfoPO info) {
        String picName = info.getPicName();
        String isCheck = info.getIsCheck();  //02—成功；03—失败
        String msg = info.getMsg();
        if(isCheck.equals("05")){
            csspQueryService.updatePicStatus(picName);
            // System.out.println("----预处理超时---"+picName.substring(0,18));
            return result("304","预处理超时!");
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("picName", picName);
        map.put("isCheck", isCheck);
        map.put("msg", msg);
        csspQueryService.updatePic(map);
        return result("200", "预处理成功", null);
    }

    /**
     * TSB轮询预处理返回接口
     * 1.TSB根据照片名轮询照片
     * @param bean
     * @return
     */
    @RequestMapping(value = "/getNewPicInfoByTSB", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object getNewPicInfoByTSB(@RequestBody picInfo bean) {


        String picName = bean.getPicName();
        List<NewPicInfoPO> list = csspQueryService.getUpdatePic(picName);
        if(list != null){
            List<NewPicInfo> list1 = new ArrayList<NewPicInfo>();
            NewPicInfo photo = new NewPicInfo();
            if (list.size() == 0) {
                return this.result("300", "尚未存在图片资料");
            }
            String photoPath = list.get(0).getPicPath();
            String picName1 = list.get(0).getPicName();
            String msg = list.get(0).getMsg();
            String isCheck = list.get(0).getIsCheck();
            if (photoPath == null) {
                return this.result("300", "尚未存在图片资料");
            }
            photo.setPicName(picName1);
            photo.setMsg(msg);
            photo.setIsCheck(isCheck);
            if (isCheck.equals(Constants.STATUSMAP.get("STATUSMAP_01"))){
                return result("301", "图片还没处理");
            } else if (isCheck.equals(Constants.STATUSMAP.get("STATUSMAP_03"))){
                photo.setBase64String("");
                return result("303","图片预处理失败");
            }else if (isCheck.equals(Constants.STATUSMAP.get("STATUSMAP_05"))){
                System.out.println("====="+isCheck);
                return result("304","预处理超时！");
            }
    /*       *//* ==========*//*
           else if (isCheck.equals(Constants.STATUSMAP.get("STATUSMAP_02"))){
               // System.out.println("====="+isCheck);
                return result("306","图片未审核！");
            }

            *//*=======*/

            else if(isCheck.equals(Constants.STATUSMAP.get("STATUSMAP_02"))) {//2
                File file = new File(photoPath);
                FileInputStream is = null;

                if (file.exists()) {
                    try {
                        is = new FileInputStream(file);
                        byte[] buffer = new byte[is.available()];
                        is.read(buffer);
                        photo.setBase64String(Base64.getEncoder().encodeToString(buffer));
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return this.result("302", "图片读取错误");
                    }
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            list1.add(photo);
            return result("200", "获取图片成功", list1);
        }else {
            return this.result("300", "尚未存在图片资料");
        }

    }


    /**
     *  PICServer图片处理返回接口
     *  04--处理成功 ； 05 -- 处理失败
     */
    @RequestMapping(value = "/auditByTSB", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object auditByTSB(@RequestBody picInfo bean){
        String isCheck = bean.getIsCheck();
        String picName = bean.getPicName();
        // System.out.println("测试"+picName);
        String msg = bean.getMsg();
        String base46String = bean.getBase64String();
        if(StringUtils.isBlank(picName) || StringUtils.isBlank(isCheck)
                || (!isCheck.equals(Constants.STATUSMAP.get("STATUSMAP_04"))&& !isCheck.equals(Constants.STATUSMAP.get("STATUSMAP_05")))){
            return result("201", "传入参数值有误");
        }
        //查询

        List<NewPicInfoPO> list = csspQueryService.getUpdatePic(picName);
        if (list == null || list.isEmpty()) {
            return result("300", "尚未存在图片资料");
        }
        String photoPath = list.get(0).getPicPath();
        if(StringUtils.isEmpty(photoPath)) {
            return this.result("300", "尚未存在图片资料");
        }
        picName = list.get(0).getPicName();
        String check = list.get(0).getIsCheck();
        String name = list.get(0).getName();
        // System.out.println("picName=="+picName);
        // System.out.println("name=="+name);
        if(check != null && check.equals(Constants.STATUSMAP.get("STATUSMAP_02"))){ //照片处理成功
            File file = new File(collectPhotoPath2);
            System.out.println("1");
            if(!file.exists()){
                file.mkdirs();
            }
            String path2= collectPhotoPath2 + picName+".jpg";
            System.out.println("path2=="+path2);
            OutputStream os = null;
            try {
                os = new FileOutputStream(new File(path2));
                os.write(Base64.getMimeDecoder().decode(base46String));
                System.out.println("2");
            } catch (Exception e) {
                e.printStackTrace();
                return this.result("302", "图片读取错误");
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("picName", picName);
            map.put("isCheck", isCheck);
            map.put("msg", msg);
            map.put("picPath",path2);
            csspQueryService.updatePic(map);
            ///修改BASIC_PERSON_INFO照片路径
            Applybean info = new Applybean();
            String certNum = picName.substring(0, 18);
            info.setCertNum(certNum);
            info.setName(name);
            Applybean applybean = csspQueryService.queryPhotoUrl(name,certNum);
            if(applybean != null && StringUtils.isEmpty(applybean.getPhotoUrl())){
                csspQueryService.updatePhotoUrl(name,certNum, path2);
                //  System.out.println("success....");
            }
            return result("200", "审核成功");
        }else return this.result("300", "图片未处理或处理失败");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public void uploadSfzByTSB(@Context HttpServletRequest request) throws Exception {
        String picName = request.getParameter("picName");
        String name = request.getParameter("name");
        name = new String(name.toString().getBytes("ISO-8859-1"),"UTF-8");
        Applybean info = new Applybean();
        info.setCertNum(picName);
        info.setName(name);
        //Applybean applybean = csspQueryService.queryPhotoUrl(name,picName);
        //System.out.println("photoUrl"+photoUrl+"=====");
    }

    /**
     * 德生宝扫描身份证上传
     * @param bean
     * @return
     */
    @RequestMapping(value = "/uploadSfzByTSB", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object uploadSfzByTSB(@RequestBody SfzInfo bean) {
        String cerNum = bean.getCerNum();
        String name = bean.getName();
        String sex = bean.getSex();
        String photo = bean.getBase64String();
        String birthday = bean.getBrithday();

        if (StringUtils.isBlank(cerNum) || StringUtils.isBlank(name)){
            return result("201", "身份证信息不能为空");
        }

        if (StringUtils.isBlank(photo)){
            return result("201", "参保人照片不能为空");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        File file = new File(sfzPhotoPath);
        if(!file.exists()){
            file.mkdirs();
        }
        String photoPath = sfzPhotoPath + cerNum + ".jpg";
        map.put("cerNum", cerNum);
        map.put("photoPath", photoPath);
        map.put("name", name);
        map.put("sex", sex);
        map.put("birthday", birthday);
        if (photoPath != null) {
            OutputStream os = null;
            try {
                os = new FileOutputStream(new File(photoPath));
                os.write(Base64.getMimeDecoder().decode(photo));
            } catch (Exception e) {
                e.printStackTrace();
                return this.result("302", "图片读取错误");
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        csspQueryService.insertPic(map);
        return result("200", "上传图片成功");
    }

    /*********************************  新申领 end   *****************************************/

    /*********************************  滞留卡 start   *****************************************/
    /**
     * 登录验证
     * @param bean
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result login (@RequestBody LoginVO bean)throws Exception {
        String message = "登录成功";
        String data = "";
        if (StringUtils.isNotEmpty(bean.getPassword()) && !" ".equals(bean.getLoginName()) && StringUtils.isNotEmpty(bean.getLoginName()) && !" ".equals(bean.getLoginName())) {
            String password= CommUtil.generateValue(bean.getPassword()).toUpperCase();
            bean.setPassword(password);
            //System.out.println("===="+bean.getPassword());
            ResultLoginvo clist = csspQueryService.login(bean);
            if (clist !=null  ) {
                JedisUtil.setValue("username",bean.getLoginName());
                return result("200", message);
            } else {
                message = "账号或密码错误";
                return result("0", message);
            }
        } else {
            message = "账号密码不能为空";
            return result("0", message);
        }

    }

    /**
     * 滞留卡登记
     * @param bean
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result register (@RequestBody BusRcmCardVO bean)throws Exception {

        if (StringUtils.isNotEmpty(bean.getCertNum()) && !" ".equals(bean.getCertNum())) {
            BusRcmBoxVO boxVO = new BusRcmBoxVO();
            BusRcmBinVO binVO = new BusRcmBinVO();
            UseInfoBean  useInfoBean =getInfo();
            if(useInfoBean == null || (useInfoBean.getOrgId() == null || useInfoBean.getOrgId() == 0)){
                return result("301","请先到后台把德生宝和社保网点关联起来");
            }
            String username=JedisUtil.getValue("username");
            bean.setADDUSER(username);
            Integer id=useInfoBean.getOrgId();
            String orgAddress=csspQueryService.getrcmWdname(id);
            bean.setSoName(orgAddress);
            boxVO.setOrgAddress(orgAddress);
            binVO.setOrgAddress(orgAddress);
            bean.setOrgid(id);
            List<BusRcmCardVO> List = csspQueryService.getCardinfo(bean.getCertNum());
            if (List.isEmpty() && List.size() <= 0) {
                List<ResultXhcVO> xhcList = csspQueryService.getCardku(orgAddress);
                if ((xhcList.isEmpty() && xhcList.size() == 0) || (xhcList.get(0).getXcount() >= Integer.parseInt(carboxcount) && xhcList.get(0).getNowCount() >= Integer.parseInt(cardmaxsize))) {//全部为0/10/50的时候
                    if (xhcList.isEmpty() && xhcList.size() == 0) {
                        binVO.setBinNo("1");
                    } else {
                        Integer XHSJ = (Integer.parseInt(xhcList.get(0).getBinNo()) + 1);
                        String XH = XHSJ.toString();
                        binVO.setBinNo(XH);
                    }
                    binVO.setCount(1);
                    binVO.setStatus(0);//文档未定义
                    binVO.setRemark("0");
                    try {
                        csspQueryService.insertBin(binVO);//插入箱子
                        if (xhcList.isEmpty() && xhcList.size() == 0) {
                            boxVO.setBoxSn(1);
                        } else {

                            boxVO.setBoxSn(xhcList.get(0).getBoxsn() + 1);
                        }
                        boxVO.setBoxNo("1");
                        boxVO.setStatus(0);
                        boxVO.setCount(1);
                        boxVO.setNowCount(1);
                        boxVO.setBinId(binVO.getId());//插入箱子成功后，MyBatis返回的id
                        boxVO.setRemark("0");
                        boxVO.setStoreOrgType("01");
                        boxVO.setStreamStatus(0);
                        csspQueryService.insertBox(boxVO);
                        bean.setRegisStatus("1");//0，已发；1，未发
                        bean.setBoxId(boxVO.getId());//插入盒子成功后，MyBatis返回的id
                        bean.setStatus(0);//0：在盒中1：分散中2：已发放
                        bean.setCardSn(1);
                        csspQueryService.insertCard(bean);
                        return result(Constants.RESULT_MESSAGE_SUCCESS, "登记成功", 0);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                        return error("0","登记失败");
                    }

                }
                if (1 <= xhcList.get(0).getXcount() && xhcList.get(0).getXcount() <= Integer.parseInt(carboxcount)
                        && 1 <= xhcList.get(0).getHcount() && xhcList.get(0).getNowCount() < Integer.parseInt(cardmaxsize)) {
                    List<BusRcmBoxVO> boxList = csspQueryService.queryBox(orgAddress);//找最新的盒子的id
                    bean.setRegisStatus("1");//代发放//0，已发；1，未发
                    bean.setBoxId(boxList.get(0).getId());
                    bean.setStatus(0);//0：在盒中1：分散中2：已发放
                    bean.setCardSn(xhcList.get(0).getNowCount() + 1);
                    try{
                        csspQueryService.insertCard(bean);
                        boxVO.setCount(xhcList.get(0).getHcount()+1);
                        boxVO.setNowCount(xhcList.get(0).getNowCount()+1);
                        boxVO.setId(bean.getBoxId());
                        csspQueryService.updateBox(boxVO);
                        return result(Constants.RESULT_MESSAGE_SUCCESS, "登记成功", 0);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        return error("0","登记失败");
                    }
                }
                if (1 <= xhcList.get(0).getXcount() && xhcList.get(0).getXcount() < Integer.parseInt(carboxcount)
                        && xhcList.get(0).getNowCount() == Integer.parseInt(cardmaxsize)) {
                	/**当盒子装满时，第一次先提示已装满，第二次再装下一盒。因此，前端需请求两次本方法，才能完成数据操作**/
                	if(showBoxFullMsg){
                		showBoxFullMsg = false;
                		return result("301", "本盒已装满，请装下一盒", null);
                	}else{
                		showBoxFullMsg = true;
                		Integer a = Integer.parseInt(xhcList.get(0).getBoxno()) + 1;
                        String HH = a.toString();
                        boxVO.setBoxNo(HH);
                        boxVO.setBoxSn(xhcList.get(0).getBoxsn() + 1);
                        boxVO.setCount(1);
                        boxVO.setNowCount(1);
                        boxVO.setBinId(xhcList.get(0).getBinId());
                        boxVO.setStatus(0);
                        boxVO.setRemark("0");
                        boxVO.setStoreOrgType("01");
                        boxVO.setStreamStatus(0);
                        try {
                            csspQueryService.insertBox(boxVO);
                            binVO.setCount(xhcList.get(0).getXcount() + 1);
                            binVO.setId(xhcList.get(0).getBinId());
                            csspQueryService.updateBin(binVO);
                            List<BusRcmBoxVO> boxList = csspQueryService.queryBox(orgAddress);//找最新的盒子的id
                            bean.setRegisStatus("1");//代发放//0，已发；1，未发
                            bean.setStatus(0);//0：在盒中1：分散中2：已发放
                            bean.setBoxId(boxList.get(0).getId());
                            bean.setCardSn(1);
                            csspQueryService.insertCard(bean);
                            return result(Constants.RESULT_MESSAGE_SUCCESS, "登记成功", 0);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            return  error("0","登记失败");
                        }
                	}
                }
            } else {
                return result(Constants.RESULT_MESSAGE_ERROR,"已登记过", 1);//1:表示已经登记过
            }
        } else {
            return result(Constants.RESULT_MESSAGE_ERROR,"未读到有效信息", "未读到有效信息");
        }
        return null;

    }

    /*********************************  滞留卡 end   *****************************************/

    /*********************************  精准发卡 start  *****************************************/


    /*=========================位置和资格 start===================================*/
    /**
     * 查找是否满足申领条件和卡位置查询
     * @param bean
     * @return
     */
    @RequestMapping(value = "/getApplyCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getApplyCard (@RequestBody PersonInfoVO bean)throws Exception {
        ResultBusMakeDetal resultBusMakeDetal=new ResultBusMakeDetal();
        String[] data= new String[]{};
        String kwz="";
        if(StringUtils.isNotEmpty(bean.getCertNum()) && !" ".equals(bean.getCertNum())) {
            //先去滞留卡中找卡位置
            resultBusMakeDetal = csspQueryService.getCardwz(bean.getCertNum()); //用身份证号到滞留卡表中找条件regis为1对应的盒ID,箱ID，卡编号
            if (resultBusMakeDetal != null) {
                if (resultBusMakeDetal.getOrgAddress() != null && resultBusMakeDetal.getBinNo() != null && resultBusMakeDetal.getBoxNo() != null && resultBusMakeDetal.getCardsn() != null) {
                    if(resultBusMakeDetal.getOrgAddress().endsWith("网点")){
                        resultBusMakeDetal.setOrgAddress(resultBusMakeDetal.getOrgAddress().substring(0,resultBusMakeDetal.getOrgAddress().length() -2));
                    }
                    kwz = resultBusMakeDetal.getOrgAddress() + "网点" + resultBusMakeDetal.getBinNo() + "箱" + resultBusMakeDetal.getBoxNo() + "盒" + resultBusMakeDetal.getCardsn() + "张";
                    if(resultBusMakeDetal.getBATCHNO() != null){
                        kwz = resultBusMakeDetal.getOrgAddress() + "网点" + resultBusMakeDetal.getBinNo() + "箱" + resultBusMakeDetal.getBoxNo() + "盒" + resultBusMakeDetal.getCardsn() + "张"+resultBusMakeDetal.getBATCHNO()+"批次";
                    }
                    String name=resultBusMakeDetal.getName();
                    String certnum=resultBusMakeDetal.getCertNum();
                    if (resultBusMakeDetal.getRegisStatus().equals("1")) {
                        String kzt = "未申领";
                        String kzg = "是";
                        data = new String[]{kwz, kzt, kzg,name,certnum};

                        return result("200", "查找成功", data);
                        //System.out.println("==="+page.getData().get(0)+page.getData().get(1)+page.getData().get(2));
                    } else {
                        //  List<BusApplyPicVO> applybeans=csspQueryService.getApplytime(bean.getCertNum());
                        String kzt = "已申领";
                        String kzg = "否";
                        data = new String[]{kwz, kzt, kzg,name,certnum};
                        return result("201", "卡已发放", data);
                        // System.out.println("==="+page.getData().get(0)+page.getData().get(1)+page.getData().get(2));
                    }
                } else {
                    return result("202", "查找失败");
                }
            }
            else {
                return result("202", "卡片不存在");
            }
        }
        else {
            return result("0", "未发现身份证信息！");
        }
    }


     /*=========================位置和资格 end===================================*/

    /**
     * 1.精准发卡上传照片
     * 2.参数： 照片名 、 照片64位数据
     * @param bean
     * @return
     */
    @RequestMapping(value = "/uploadCardPicInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object uploadCardPicInfo (@RequestBody BusApplyPicVO bean) throws Exception {
        String ppic = bean.getBase64String();
        // String qmpic = bean.getBase64Stringqmz();
        String agentName = bean.getAgentName();
        String name = bean.getName();
        String certNum = bean.getCertNum();
        String agencertNum=bean.getAgentcertNum();
       /* if (StringUtils.isBlank(ppic) || StringUtils.isBlank(qmpic)) {
            return result("0", "上传签名和人的图片信息不能为空");
        }*/
        if (bean.getNo().equals("1")) {// 0：本人，1：代他人办理
            if (StringUtils.isBlank(agencertNum) && StringUtils.isBlank(agentName)) {
                return result("0", "代办人身份证号、姓名不能为空");
            }
            //这个是手动录入身份证号（新生儿与未办理身份证的学生等等，无法校验身份证的真实性）
            else if(StringUtils.isBlank(agencertNum)){
            	return result("0", "代办人身份证号不能为空");
            }
        }
        if (bean.getNo().equals("0")) {// 0：本人，1：代他人办理
            if (StringUtils.isBlank(certNum) || StringUtils.isBlank(name)) {
                return result("0", "参保人身份证不能为空");
            }
        }
        UseInfoBean  useInfoBean =getInfo();
        if(useInfoBean == null || (useInfoBean.getOrgId() == null || useInfoBean.getOrgId() == 0)){
            return result("301","请先到后台把德生宝和社保网点关联起来");
        }
        Integer id = useInfoBean.getOrgId();
        String data = csspQueryService.getOrgAddress(id);
        String orgAddress = csspQueryService.getrcmWdname(id);
        File file = new File(sfzPhotoPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String photoPath1,photoPath2;
        if(bean.getNo().equals("0")) {
            photoPath1 = sfzPhotoPath + "person" +"_" + certNum +new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+ ".jpg";
            // photoPath2 = sfzPhotoPath + "signature" +"_" + certNum  +new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+ ".jpg";
        }
        else {
            photoPath1 = sfzPhotoPath + "person" + "_" + agencertNum +new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".jpg";
            /// photoPath2 = sfzPhotoPath + "signature" + "_" + agencertNum +new SimpleDateFormat("yyyyMMddhhmmss").format(new Date())+ ".jpg";
        }
       /* PicUtil.GenerateImage(PicUtil.GetImageStr(bean.getBase64Stringqmz()), photoPath1);
        PicUtil.GenerateImage(PicUtil.GetImageStr(bean.getBase64Stringqmz()), photoPath2);*/
        try {
            PicUtil.GenerateImage(ppic, photoPath1);
            //   PicUtil.GenerateImage(qmpic, photoPath2);
            bean.setOrgid(orgAddress);
            bean.setRphotoPath(photoPath1);
            //  bean.setQmphotoPath(photoPath2);
            bean.setSoCardNum(bean.getSoCardNum());//2016/9/20xz
            bean.setSoname(data);
            csspQueryService.insertApplyPic(bean);
        } catch (Exception e) {
            e.printStackTrace();
            return result("0", "领卡失败（上传图片失败）");
        }
        ResultUpVO resultUpVO=csspQueryService.getboxid(bean.getSoCardNum());
        //   BasicCardVO basicCardVOs=csspQueryService.getbasicpid(bean.getSoCardNum());

        // String s=bean.getSoCardNum();
        if(resultUpVO != null){
         /*   if(basicCardVOs != null) {
                long cardid=basicCardVOs.getId();
                csspQueryService.updataBox(resultUpVO.getBoxid());
                csspQueryService.updataRcm(bean.getSoCardNum());
                csspQueryService.updateBasicstatus(bean.getSoCardNum());
                csspQueryService.updatePesonxx(basicCardVOs.getId());
                csspQueryService.updategryw(cardid);
                return result("200", "领卡成功",data);

            else {*/
            csspQueryService.updataRcm(bean.getSoCardNum());
            csspQueryService.updateCard(resultUpVO);
            csspQueryService.updataBox(resultUpVO.getBoxid());

            return result("200", "领卡成功",data);
        }

        //更新基本信息表
        //}

      /*  else
        {

            //BasicCardVO basicCardVOs=csspQueryService.getbasicpid(bean.getSoCardNum());
            if(basicCardVOs != null){
                long cardid=basicCardVOs.getId();
                csspQueryService.updateBasicstatus(bean.getSoCardNum());
                csspQueryService.updatePesonxx(basicCardVOs.getId());
                csspQueryService.updategryw(cardid);
                return result("200", "领卡成功",data);

            }
            else {
                return result("0", "领卡失败(更新卡数据失败)",data);
            }*/

        //}

        else {
            return result("0", "领卡失败",data);
        }



    }




    /*********************************  精准发卡 end  *****************************************/


    /*********************************  社保卡业务 申领记录查询  *****************************************/
    /**
     * 1.申领记录查询
     */
    @RequestMapping(value = "/applyhistory", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result applyhistory (@RequestBody BasicPersonInfoBean bean) throws Exception {
        String certnum=bean.getCertNum();
        List<ResultPersonApplyVO> list=new ArrayList<ResultPersonApplyVO>();
        list=csspQueryService.getapplyhistory(certnum);
        if(list.size() > 0 && ! list.isEmpty()){
            return result("200","查询成功",list);
        }
        else {
            return result("0","没有记录");
        }
    }
    /*********************************  社保卡业务 申领记录查询  end*****************************************/



    /********************************** 滞留卡需求变更 start********************************* */
    /**
     * 获取网点名称
     */
    @RequestMapping(value = "/getsoname", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getsoname () throws Exception {
        UseInfoBean  useInfoBean =getInfo();
        if(useInfoBean == null || (useInfoBean.getOrgId() == null || useInfoBean.getOrgId() == 0)){
            return result("301","请先到后台把德生宝和社保网点关联起来");
        }
        Integer id = useInfoBean.getOrgId();
        String orgAddress = csspQueryService.getrcmWdname(id);
        if(orgAddress != null && !" ".equals(orgAddress)){
            return  result("200",orgAddress);
        }
        return  result("0","未找到网点名称");
    }

    /**
     * 装箱情况查询
     */
    @RequestMapping(value = "/getCardSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getCardSum(@RequestBody BusRcmCardVO bean) throws Exception {
        if(bean.getBoxno() == null  && bean.getBoxno().equals(" ")){
            return error("请输入盒号","请输入盒号");
        }
        if(bean.getBinno() == null  && bean.getBinno().equals(" ")){
            return error("请输入箱号","请输入箱号");
        }
        if(bean.getOrgAddress() == null  && bean.getOrgAddress().equals(" ")){
            return error("请输入网点名称","请输入网点名称");
        }
        if(bean.getNowcount() == null  && bean.getNowcount().equals(" ")){
            return error("请输入装盒数量","请输入装盒数量");
        }
        List<BusRcmCardVO> busList=csspQueryService.getHnowcount(bean);
        if(busList.size()>0 && ! busList.isEmpty()){
            if(Integer.parseInt(busList.get(0).getNowcount()) >= Integer.parseInt(bean.getNowcount())){
                return result("201","不可装卡");
            }
            else {
                Integer card=Integer.parseInt(bean.getNowcount())-Integer.parseInt(busList.get(0).getNowcount());

                return result("200","本盒已装"+busList.get(0).getNowcount()+"张卡，还可以装"+card+"张卡");
            }
        }
        else {
            return result("0","没有找到所要查的装卡位置");
        }
    }
    /**
     * 滞留卡——零散卡登记
     */
    @RequestMapping(value = "/registerNew", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result registerNew (@RequestBody BusRcmCardVO bean) throws Exception {
        if(bean.getCertNum().isEmpty()){
            return  result(Constants.RESULT_MESSAGE_ERROR,"身份证号码不能为空");
        }
        UseInfoBean  useInfoBean =getInfo();
        if(useInfoBean == null || (useInfoBean.getOrgId() == null || useInfoBean.getOrgId() == 0)){
            return result("301","请先到后台把德生宝和社保网点关联起来");
        }
        List<BusRcmCardVO> List = csspQueryService.getCardinfo(bean.getCertNum());
        if(List.size() <=0 && List.isEmpty()){
            List<BusRcmCardVO> busList=csspQueryService.getHnowcount(bean);
            if(Integer.parseInt(busList.get(0).getNowcount()) < Integer.parseInt(bean.getNowcount())){
                String username=JedisUtil.getValue("username");
                bean.setADDUSER(username);
                bean.setRegisStatus("1");//代发放//0，已发；1，未发
                bean.setStatus(0);//0：在盒中1：分散中2：已发放
                bean.setBoxId(busList.get(0).getBoxId());
                bean.setCardSn(Integer.parseInt(busList.get(0).getNowcount()) + 1);
                bean.setSoName(bean.getOrgAddress());
                bean.setOrgid(useInfoBean.getOrgId());
                csspQueryService.insertCard(bean);
                //更新盒子卡片数量和卡片实时数量
                csspQueryService.updateboxnew(busList.get(0).getBoxId());
                return result(Constants.RESULT_MESSAGE_SUCCESS, "登记成功", 0);
            }
            else {
                return  result(Constants.RESULT_MESSAGE_ERROR,"本盒已装满,请重新选择装卡位置", 1);
            }
        }
        else {
            return  result(Constants.RESULT_MESSAGE_ERROR,"已登记过", 1);
        }
    }
    
    

    /**
     * 
     * @Title:
     * @company :TECSUN
     * @developer: ZENGYUNHUA
     *	@version 1.0
     */
    @SuppressWarnings("unused")
	@RequestMapping(value = "/querybyPersonMessage", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result querybyPersonMessage (@RequestBody PersonalApply vo) throws Exception {
    	logger.info("---------------CsspBusController: querybyPersonMessage---------------");
    	String statusCode = Constants.RESULT_MESSAGE_ERROR;
		String message = "未获取到相关数据";
		if (StringUtils.isBlank(vo.getCertNum())) {
	            return  result(Constants.RESULT_MESSAGE_ERROR,"身份证号不能为空");
	        }
		 try {
			 	//转base64
	            PersonalApply list = csspQueryService.querybyPersonMessage(vo.getCertNum());
	    	 	String imageStr = ImageChangeUtil.getImageStr(list.getRphotopath());
	    	 	list.setRphotopath(imageStr);
	                if (list != null) {
	            	statusCode = Constants.RESULT_MESSAGE_SUCCESS;
	    			message = "获取发卡记录成功";
	    			 return result(statusCode, message, list);
	    		} else {
	    			statusCode = Constants.RESULT_MESSAGE_SUCCESS;
	    			message = "未找到相关的数据!";
	    		}
	            	
	        } catch (Exception e) {
	            logger.error("获取发卡记录出错：" + e.getMessage());
	        }
			
		return result(statusCode, message);
	}
    
    
}
