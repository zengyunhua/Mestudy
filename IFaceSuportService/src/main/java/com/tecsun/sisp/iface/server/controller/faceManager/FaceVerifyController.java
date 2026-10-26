package com.tecsun.sisp.iface.server.controller.faceManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.client.HttpClientUtil;
import com.tecsun.sisp.iface.client.WebClient;
import com.tecsun.sisp.iface.common.vo.Page;
import com.tecsun.sisp.iface.common.vo.PageModel;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.faceverify.BusinessInfoPO;
import com.tecsun.sisp.iface.common.vo.faceverify.DRPersonPO;
import com.tecsun.sisp.iface.common.vo.faceverify.FafangInfo;
import com.tecsun.sisp.iface.common.vo.faceverify.HistoryMsgBean;
import com.tecsun.sisp.iface.common.vo.faceverify.HistoryResult;
import com.tecsun.sisp.iface.common.vo.faceverify.IC09PO;
import com.tecsun.sisp.iface.common.vo.faceverify.PersonLoginBean;
import com.tecsun.sisp.iface.common.vo.faceverify.RegistBean;
import com.tecsun.sisp.iface.common.vo.faceverify.ResultVerify;
import com.tecsun.sisp.iface.common.vo.faceverify.UploadPhoto;
import com.tecsun.sisp.iface.common.vo.faceverify.XGLoginBean;
import com.tecsun.sisp.iface.common.vo.faceverify.XGPersonMsg;
import com.tecsun.sisp.iface.common.vo.faceverify.XGPersonPO;
import com.tecsun.sisp.iface.common.vo.faceverify.XGPhotoBean;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.faceVerify.impl.PersonInfoServiceImpl;
import com.tecsun.sisp.iface.server.util.JsonHelp;

/**
 * Created by Sandwich on 2015/12/10.
 */
@Controller
@RequestMapping(value = "/iface/faceVerify")
public class FaceVerifyController extends BaseController {
    public final static Logger logger = Logger.getLogger(FaceVerifyController.class);

    @Autowired
    public PersonInfoServiceImpl personInfoService;

    @RequestMapping(value = "/userLogin")
    @ResponseBody
    public Result insuredInfo(@RequestBody PersonLoginBean bean){
        String idCard = bean.getIdCard();
        String name = bean.getName();
        String token = this.createToken();
        Map<String,String> map = new HashMap<String, String>();
        map.put("idCard",idCard);
        map.put("name",name);
        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
        if (list_xg.size()<=0){
            return this.result("303", "用户名不存在，请检查您的用户名是否正确！");
        }
        String dbPwd = list_xg.get(0).getPassword();
        Map<String,String> loginMap = new HashMap<String, String>();
        loginMap.put("idCard",idCard);
        loginMap.put("token",token);
        if (!personInfoService.updateToken(loginMap)){
            return this.result("301","token 更新不成功！");
        }
        //以姓名和身份证号，查找东软参保人数据库,获取人员编号和企业编号
        List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
        String aac001 = list_dr.get(0).getGRBH();
        String aab001 = list_dr.get(0).getDWBH();
        //向照片服务器获取照片
        JsonObject jsonPic = new JsonObject();
        jsonPic.addProperty("idCard", idCard);
        jsonPic.addProperty("district", "xg");
        String picResult = (String) WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/getPhoto", jsonPic, String.class);
        Map<String,Object> picMap = JsonHelp.jsonToMap(picResult);
        String str = picMap.get("data").toString();//base64String
        //向客户端返回信息
        XGPersonMsg personMsg = new XGPersonMsg();
        personMsg.setPhoto(str);
        personMsg.setIdCard(idCard);
        personMsg.setSex(list_xg.get(0).getSex());
        personMsg.setName(list_xg.get(0).getPerson_name());
        personMsg.setSys_token(list_xg.get(0).getToken());
        personMsg.setAddr(list_xg.get(0).getAddress());
        personMsg.setCompany(list_xg.get(0).getCompany());
        personMsg.setAac001(aac001);
        personMsg.setAab001(aab001);
        List<XGPersonMsg>listResult = new ArrayList<XGPersonMsg>();
        listResult.add(personMsg);
        return this.result("200", "登录成功", listResult);
    }

    @RequestMapping(value = "/registration")
    @ResponseBody
    public Result registration(@RequestBody RegistBean bean) throws Exception {
        String name = bean.getName();
        String idCard = bean.getIdCard();
        String cjPhoto = bean.getPhoto();
        String aac001;
        String aab001;
        String loginRe;
        String xg_token;
        String loginStatus;
        String loginMsg;
        String rylb;//离休或遗嘱
        String isLx;//0离休，1遗嘱
        Map<String,String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("idCard",idCard);
        //向孝感数据库获取信息
        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
        if (list_xg.size()==0){//不存在，则向东软获取
            //查找东软参保人数据库
            List<DRPersonPO>  list_dr = personInfoService.getDRPersonInfo(map);
            if (list_dr.size()<= 0){
                return result("303","不存在该用户");
            }
            aac001 = list_dr.get(0).getGRBH();//个人编号
            aab001 = list_dr.get(0).getDWBH();//企业编号
            rylb = list_dr.get(0).getRYLB();//离退休或遗嘱
            if (rylb.equals("离退休")){
                isLx = "0";
            }else if (rylb.equals("遗嘱")){
                isLx = "1";
            }else {
                return result("304","不符合认证标准");
            }
            //向省厅获取数据 1.登陆获取token 2.获取信息
            String loginUrl = "https://219.138.205.201:443/sisp/iface/user/checkLogin";
            XGLoginBean beans = new XGLoginBean();
            beans.setType("TSB");
            beans.setNetpassword(MD5("TSB").toLowerCase());
            beans.setUserName(MD5("TSB").toLowerCase());
            loginRe = HttpClientUtil.getData(loginUrl, beans, true, true, 443).toString();
            xg_token = JsonHelp.jsonToMap(loginRe).get("data").toString();
            loginStatus = JsonHelp.jsonToMap(loginRe).get("statusCode").toString();
            loginMsg = JsonHelp.jsonToMap(loginRe).get("message").toString();
            if (!loginStatus.equals("200")){//孝感登陆失败
                logger.error(">>>>>>>login xg happen error ,msg："+loginMsg+",statusCode is:"+loginStatus+"<<<<<<<");
                return result("305","服务器异常");
            }
            //向省卡管获取
            String url = "https://219.138.205.201/sisp/iface/cardInfo/getCardAllInfo?tokenId="+xg_token;
            XGPhotoBean photoBean = new XGPhotoBean();
            photoBean.setId(idCard);
            photoBean.setChannelcode("TSB");
            String result = HttpClientUtil.getData(url, photoBean, true, true, 443).toString();
            String statusCode = JsonHelp.jsonToMap(result).get("statusCode").toString();
            //状态码为0则是不存在该用户，且为遗嘱，则提示到服务点采集照片
            if (statusCode.equals("0") && isLx.equals("1")) {
                return this.result("306", "遗嘱人员请到服务点采集照片");
            }
            //存在则向孝感数据库插入参保人员信息，isCheck为等待未审核，不通过，再删除
            Map<String,String> data = (LinkedHashMap<String,String>)JsonHelp.jsonToMap(result).get("data");
            String password = MD5(idCard.substring(idCard.length()-4,idCard.length())).toLowerCase();
            String sys_token = this.createToken();
            String msg = JsonHelp.jsonToMap(result).get("message").toString();
            String base64Str = data.get("photo").toString();
            String sex = data.get("sex").toString();
            name = data.get("name").toString();
            String company = data.get("dwmc").toString();
            String tel = data.get("telno").toString();
            String address = data.get("regaddr").toString();
            String sexs =(sex.equals("男")?"01":"02");
            //新增用户信息
            Map<String, String> newMap = new HashMap<String, String>();
            String cjPath = "/home/disk/voicePrint/xg_sbPic/"+idCard+".jpg";//采集照片路径
            newMap.put("password",password);
            newMap.put("person_name", name);
            newMap.put("role","0");
            newMap.put("idCard", idCard);
            newMap.put("sex",sexs);
            newMap.put("company", company);
            newMap.put("tel", tel);
            newMap.put("address", address);
            newMap.put("token", sys_token);
            newMap.put("isCheck", "01");//注册信息未审核
            newMap.put("cj_sbPhotoPath",cjPath);
            personInfoService.insertPersonInfo(newMap);//向孝感数据库插入参保人员信息，isCheck为未审核
            //调用认证平台认证
            String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");//作为用户认证与认证平台的唯一标识
            String identifyId = uuid+"-"+idCard+"-"+aab001+"-"+aac001;
            JsonObject json = new JsonObject();
            json.addProperty("sbBase64String",base64Str);//省厅的照片
            json.addProperty("base64String1", cjPhoto);//注册照片
            json.addProperty("base64String2", cjPhoto);
            json.addProperty("base64String3", cjPhoto);
            json.addProperty("identifyId",identifyId);
            json.addProperty("userid",idCard);
            json.addProperty("distinct", "XG");
            json.addProperty("businesstype", "SB");
            json.addProperty("justCheck","01");//00只进行人工审核，01机器+人工
            String wsResult = (String)WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/ImgFilter", json, String.class);
            Map<String,Object> reMap = JsonHelp.jsonToMap(wsResult);
            String code = reMap.get("statusCode").toString();
            if (code.equals("01")){//比对通过，定时器获取人工审核结果
                getResigt(cjPhoto, idCard,identifyId);//执行定时器，获取审核结果,并将数据写入东软视图
                return this.result("200", "注册图片上传成功，请稍后！");
            }else{//比对不通过，删除记录
                personInfoService.delPersonInfo(idCard);
                return result("307","用户注册图片不通过");
            }
        }
        return result("201","用户已注册");
    }

    @RequestMapping(value = "/verify")
    @ResponseBody
    public Result verify(@RequestBody UploadPhoto bean) throws Exception {
        final String idCard = bean.getIdCard();
        String photo1 = bean.getBase64String1();
        String photo2 = bean.getBase64String2();
        String photo3 = bean.getBase64String3();
        String justCheck = bean.getJustCheck();
        String name = bean.getName();
        String aac001 = bean.getAac001();
        String aab001 = bean.getAab001();
        Map<String,String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("idCard",idCard);
        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
        String dbToken = "";
        if (list_xg.size() ==0){
            return this.result("302", "用户尚未注册");
        }
        if (list_xg.size()>0) {
            dbToken = list_xg.get(0).getToken();
        }
        //再判断是否有未审核的照片
        String isCheck = list_xg.get(0).getIsCheck().trim();
        String personId = list_xg.get(0).getId();
        if (isCheck!= null && isCheck.equals("01")){
            return this.result("201", "用户照片尚未审核");
        }
        //调用认证平台取得社保卡图片
        JsonObject jsonPic = new JsonObject();
        jsonPic.addProperty("idCard",idCard);
        jsonPic.addProperty("district", "xg");//照片路径为"/home/disk/voicePrint/"+district+"_sbPic/"+idCard+".jpg";
        String picResult = (String)WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/getPhoto", jsonPic, String.class);
        Map<String,Object> picMap = JsonHelp.jsonToMap(picResult);
        String str = picMap.get("data").toString();//base64String

        //调用认证平台认证
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");//作为用户认证与认证平台的唯一标识
        String identifyId = uuid+"-"+idCard+"-"+aab001+"-"+aac001;
        JsonObject json = new JsonObject();
        json.addProperty("sbBase64String",str);
        json.addProperty("base64String1", photo1);
        json.addProperty("base64String2", photo2);
        json.addProperty("base64String3", photo3);
        json.addProperty("identifyId",identifyId);
        json.addProperty("userid",idCard);
        json.addProperty("distinct", "XG");
        json.addProperty("businesstype", "SB");
        json.addProperty("justCheck",justCheck);
		String wsResult = (String)WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/ImgFilter", json, String.class);
        Map<String,Object> reMap = JsonHelp.jsonToMap(wsResult);
        String code = reMap.get("statusCode").toString();
        List<IC09PO> icList = personInfoService.getIC09Info(aab001, aac001,"","");//IC09
        int icSize = icList.size();
        if (code.equals("01")){//比对通过，等待人工审核
            personInfoService.updateXGPersonFPStatus("01",idCard);
            getResult(identifyId,personId,idCard,icSize);//执行定时器，获取审核结果,并将数据写入东软视图
            return this.result("200", "图片上传成功");
        }
        //不为01则比对失败
        //personInfoService.insertBusinessFaceRe(personId, "1", "人脸比对失败", identifyId);//业务表新增认证记录
        personInfoService.removeXGPersonFPStatus("1", idCard);//修改认证状态和修改审核状态为已审核，不再经过人工审核
        if (icSize == 0){//不存在该记录，则IC09表新增一条记录
            Calendar acl = Calendar.getInstance();
            int year = acl.get(Calendar.YEAR);
            personInfoService.insertView(aac001,aab001,Integer.toString(year),"","","","","1","","",personId,identifyId);
        }
        return this.result("301", "图片比对失败");
    }
    /**
     * 认证历史纪录
     * @param bean
     * @return
     */
//    @RequestMapping(value="/verifyHistory", method=RequestMethod.POST , produces ="application/json")
//    @ResponseBody
//    public Object verifyHistory(@RequestBody HistoryMsgBean bean) {
//        String idCard = bean.getIdCard();
//        String token = bean.getToken();
//        String pageNos = bean.getPageno();
//        String pageSizes = bean.getPagesize();
//        String verifyType = bean.getVerifyType();
//        String startTime = bean.getStartTime();
//        String endTime = bean.getEndTime();
//        Date dt=new Date();
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置当前时间显示格式
//        if (startTime == null){
//            startTime = "2015-06-01";
//        }
//        if (endTime == null){
//            endTime = df.format(dt);
//        }
//        int pageNo = 1;
//        int pageSize = 10;
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("idCard",idCard);
//        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
//        if(!list_xg.get(0).getToken().equals(token)){
//            return this.result("301", "身份认证失败，请重新登录");
//        }
//        if (pageNos != null){
//            pageNo = Integer.parseInt(pageNos);
//        }
//        if (pageSizes != null){
//            pageSize = Integer.parseInt(pageSizes);
//        }
//        String personId = list_xg.get(0).getId();
//        String personName = list_xg.get(0).getPerson_name();
//        List<BusinessInfoPO>list = personInfoService.verifyHistory(personId,startTime,endTime);
//        List<HistoryResult> resultList = new ArrayList<HistoryResult>();
//        for (BusinessInfoPO historyBean:list){
//            HistoryResult history = new HistoryResult();
//            history.setBusiness_time(historyBean.getTimes());
//            history.setId_card(idCard);
//            history.setPerson_name(personName);
//            history.setStatus(historyBean.getStatus());
//            history.setType("01");//人脸认证
//            resultList.add(history);
//        }
//        PageModel page = new PageModel();
//        page.init(resultList, pageSize);
//        return result("200", "查询成功", page.getObjects(pageNo));
//    }

    /**
     * APP轮询照片是否审核
     * @param bean
     * @return
     */
    @RequestMapping(value="/isCheck", method=RequestMethod.POST , produces = "application/json")
    @ResponseBody
    public Object isCheck(@RequestBody UploadPhoto bean) {
        String idCard = bean.getIdCard();
        String token = bean.getToken();
        Map<String,String> map = new HashMap<String, String>();
        map.put("idCard",idCard);
        List<XGPersonPO>  list_xg = personInfoService.getXGPersonInfo(map);
        if(list_xg.size()==0){//不存在该条记录
            return result("203","注册不通过");
        }
        if(!list_xg.get(0).getToken().equals(token)){
            return this.result("301", "身份认证失败，请重新登录");
        }
        String isCheck = list_xg.get(0).getIsCheck().trim();
        if(isCheck == null){//为了确保旧数据为空的问题
            return result("201","尚未审核");
        }
        if (isCheck.equals("01")){//01则未审核，0照片审核认证通过，1照片审核认证不通过,2注册通过
            return result("201", "尚未审核");
        }
        if (isCheck.equals("0")) {
            return result("200", "认证通过");
        } else if (isCheck.equals("1")){
            return result("202","认证不通过");
        } else {//2注册通过
            return result("204","注册通过");
        }
    }
    
    /**
     *  离退休或遗嘱 养老金发放信息查询接口
     */
    @RequestMapping(value="/getFafangInfo", method=RequestMethod.POST , produces = "application/json")
    @ResponseBody
    public ResultVerify getFafangInfo(@RequestBody PersonLoginBean bean){
    	try{
    		String idCard = bean.getIdCard();
    		String name = bean.getName();
    	    Map<String,Object> map = new HashMap<String, Object>();
            map.put("idCard",idCard);
            map.put("name",name);
            //1. 先获取个人编号
            List<DRPersonPO>  list_xg = personInfoService.getDRPersonInfo(map);
            if(list_xg.isEmpty()){
            	return resultVerify("303", "个人信息查询失败，查无此人");
            }
            String grbh = list_xg.get(0).getGRBH();
            //2.查询养老金发放信息
            bean.setGrbh(grbh);
            Page<FafangInfo> page =  new Page<FafangInfo>(bean.getPageno(),bean.getPagesize());
            page = personInfoService.getFafangInfo(page,bean);
            return ok(page.getCount(), "", page.getData());
    	}catch(Exception e){
    		return error("养老金发放信息查询接口失败", null);
    	}
    }

    /**
     * @param token
     * @param idCard 身份证号
     * @param aac001 个人编号
     * @param aab001 单位编号
     * @return
     */
    public Result getMsgSave(String token, String idCard,String aac001,String aab001) {
        String result = null;
        String statusCode = null;
        String msg = null;
        String base64Str = null;
        String name = null;
        String sex = null;
        String company = null;
        String tel = null;
        String address = null;
        String sys_token = this.createToken();
        String password = MD5(idCard.substring(idCard.length() - 4, idCard.length())).toLowerCase();
        List<XGPersonMsg>listResult = new ArrayList<XGPersonMsg>();
        //向省卡管获取
        String url = "https://219.138.205.201/sisp/iface/cardInfo/getCardAllInfo?tokenId="+token;
        XGPhotoBean photoBean = new XGPhotoBean();
        photoBean.setId(idCard);
        photoBean.setChannelcode("TSB");
        try {
            result = HttpClientUtil.getData(url, photoBean, true, true, 443).toString();
            statusCode = JsonHelp.jsonToMap(result).get("statusCode").toString();
            //状态码为0则是不存在该用户
            if (statusCode.equals("0")) {
                return this.result("303", "不存在该用户");
            }
            Map<String,String> data = (LinkedHashMap<String,String>)JsonHelp.jsonToMap(result).get("data");
            msg = JsonHelp.jsonToMap(result).get("message").toString();
            base64Str = data.get("photo").toString();
            sex = data.get("sex").toString();
            name = data.get("name").toString();
            company = data.get("dwmc").toString();
            tel = data.get("telno").toString();
            address = data.get("regaddr").toString();
            String sexs =(sex.equals("男")?"01":"02");
            //新增用户信息
            Map<String, String> map = new HashMap<String, String>();
            map.put("password",password);
            map.put("person_name", name);
            map.put("role","0");
            map.put("idCard", idCard);
            map.put("sex",sexs);
            map.put("company", company);
            map.put("tel", tel);
            map.put("address", address);
//            map.put("verify_time","");
            map.put("token", sys_token);
//            map.put("face_Status","");
//            map.put("isCheck","");
            personInfoService.insertPersonInfo(map);//向孝感数据库插入参保人员信息
            //add图片上传接口，调用认证平台
            JsonObject jsonPic = new JsonObject();
            jsonPic.addProperty("idCard",idCard);
            jsonPic.addProperty("district", "xg");
            jsonPic.addProperty("base64Str", base64Str);
            String picResult = (String) WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/uploadPhoto", jsonPic, String.class);
            Map<String,Object> picMap = JsonHelp.jsonToMap(picResult);
            String code = picMap.get("statusCode").toString();
            if (code.equals("302")){
                logger.error(">>>>>insert pic into local happen error idCard is"+idCard+"<<<<<");
                return this.result("302","服务器异常");
            }
            //向客户端返回数据
            XGPersonMsg personMsg = new XGPersonMsg();
            personMsg.setPhoto(base64Str);
            personMsg.setIdCard(idCard);
            personMsg.setSex(sex);
            personMsg.setName(name);
            personMsg.setXg_token(token);
            personMsg.setSys_token(sys_token);
            personMsg.setAddr(address);
            personMsg.setCompany(company);
            personMsg.setAac001(aac001);
            personMsg.setAab001(aab001);
            listResult.add(personMsg);
            if (statusCode.equals("200")){
                return this.result("200","查询成功",listResult);
            }else{
                logger.info(">>>getMsg from XG Server happen error ,statusCode is:" + statusCode + ",MSG:" + msg+"idCrad is "+idCard);
                return this.result("302",msg);
            }
        } catch (Exception e) {
            logger.error(">>>promgram happen error ,idCrad is " + idCard + "<<<");
            e.printStackTrace();
            return this.result("302","服务器异常");
        }
    }

    /**
     *  获取审核结果
     * @param identifyId
     * @param personId
     * @param idCard
     * @param size
     */
    public void getResult(final String identifyId,final String personId, final String idCard, final int size){
        //定时器每2秒扫描一次数据库
        final  java.util.Timer timer;
        timer = new Timer(true);
        String []str = identifyId.split("-");
        final String aab001 = str[2];//企业编号
        final String aac001 = str[3];//个人编号
        timer.schedule(
                new java.util.TimerTask() {
                    public void run() {
                        JsonObject json = new JsonObject();
                        json.addProperty("businessid", identifyId);
                        String wsResult = (String) WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/getVerifyResult", json, String.class);
                        Map<String, Object> map = JsonHelp.jsonToMap(wsResult);
                        String verifyStatus = map.get("statusCode").toString();
                        //200则已经审核,有记录
                        if (verifyStatus.equals("200")) {
                            Map<String,String> data = (LinkedHashMap<String,String>)map.get("data");
                            String verify = data.get("verifystate").toString();//人工审核结果
                            if (verify.equals("00")) {
                               // personInfoService.insertBusinessFaceRe(personId, "0", "人脸比对成功", identifyId);//业务表新增认证记录
                            } else {
                                String failreason = data.get("failreason").toString();//失败原因
                                //personInfoService.insertBusinessFaceRe(personId, "1", failreason, identifyId);//业务表新增认证记录
                            }
                            personInfoService.removeXGPersonFPStatus(verify.equals("00") ? "0" : "1", idCard);//个人认证表
                            if (size == 0){//不存在该记录，则新增一条记录
                                Calendar acl = Calendar.getInstance();
                                int year = acl.get(Calendar.YEAR);
                                personInfoService.insertView(aac001,aab001,Integer.toString(year),"","","","",verify.equals("00") ? "0" : "1","","",personId,identifyId);
                            }
                            //先判断是否存在，再向东软插入认证历史
//                            List<IC09PO> lsIC = personInfoService.getIC09Info(aab001, aac001);
//                            if (lsIC.size()<=0){
//                                personInfoService.insertView(aac001, aab001, "", "", new Date().toString(),
//                                        new Date().toString(), "faceVerify", "pass", "", "", personId, identifyId);
//                            }
                            timer.cancel();//审核完，则定时器取消
                        }
                        logger.info(">>>>>>Timer is running ,idCard is "+idCard+",identifyId is "+identifyId+"<<<<<");
                    }
                }, 0, 2 * 1000);
    }

    /**
     * 获取注册结果
     * @param idCard
     * @param str
     */
    public void getResigt(final String str, final String idCard ,final String identifyId){
        //定时器每2秒扫描一次数据库
        final  java.util.Timer timer;
        timer = new Timer(true);
        timer.schedule(
                new java.util.TimerTask() {
                    public void run() {
                        JsonObject json = new JsonObject();
                        json.addProperty("businessid", identifyId);
                        String wsResult = (String) WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/getVerifyResult", json, String.class);
                        Map<String, Object> map = JsonHelp.jsonToMap(wsResult);
                        String verifyStatus = map.get("statusCode").toString();
                        //200则已经审核,有记录
                        if (verifyStatus.equals("200")) {
                            Map<String,String> data = (LinkedHashMap<String,String>)map.get("data");
                            String verify = data.get("verifystate").toString();//人工审核结果
                            if (verify.equals("00")) {//审核通过则新增人员信息表
                                personInfoService.updateXGPersonFPStatus("2",idCard);
                                //保存照片
                                //add图片上传接口，调用认证平台
                                JsonObject jsonPic = new JsonObject();
                                jsonPic.addProperty("idCard",idCard);
                                jsonPic.addProperty("district", "xg");
                                jsonPic.addProperty("base64Str", str);
                                try {
                                    String picResult = (String) WebClient.getWebClient("http://192.168.1.52:8084/Verify/iface/business/uploadPhoto", jsonPic, String.class);
                                }catch (Exception e){
                                    logger.error(">>>>save photo happen error<<<<<"+idCard);
                                }
                            } else {
                                personInfoService.delPersonInfo(idCard);
                            }
                            timer.cancel();//审核完，则定时器取消
                        }
                        logger.info(">>>>>>Timer is running ,idCard is "+idCard+",identifyId is "+identifyId+"<<<<<");
                    }
                }, 0, 2 * 1000);
    }
}
