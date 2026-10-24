package com.tecsun.sisp.iface.server.controller.bank;

import com.sun.jersey.api.representation.Form;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.SecQueryBean;
import com.tecsun.sisp.iface.common.vo.SecretKeyBean;
import com.tecsun.sisp.iface.common.vo.TransBean;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.NetUserServiceImpl;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import com.tecsun.sisp.iface.server.util.JedisUtil;
import com.tecsun.sisp.iface.server.util.ThreadPoolTask;
import com.tecsun.sisp.iface.server.util.ThreadPoolUtil;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;

/**
 * 孝感农商行服务接口
 * ClassName: RCCBankController
 * Description:
 * CreateTime： 2015年06月25日 09时:16分
 */

@Controller
@RequestMapping(value = "/iface/bank")
public class BankController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private NetUserServiceImpl netUserService;
    
    
    
    public void insertTransLog(String deviceid,String funcName){
        try {
            TransBean transBean=new TransBean();
            transBean.setUserid("");
            transBean.setChannelcode("TSB");
            transBean.setBusinesscode(funcName);
            transBean.setDeviceid(deviceid);
            ThreadPoolUtil.getThreadPool().execute(new ThreadPoolTask(transBean));
        } catch (Exception e) {
            logger.error("添加卡业务操作记录到业务分析子系统出错：" + e.getMessage());
        }
    }
    
    

    /**
     *  缴费 签到（大终端）
     * devId 设备 ID （对应 设备管理表的 equipmentNo ），根据此 ID 获取，改设备是否需要更新工作密钥
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getWorkSecretKey", method = RequestMethod.POST)
    @ResponseBody
    public Result getWorkSecretKey(HttpServletRequest request) throws Exception {
        SecretKeyBean keyBean = new SecretKeyBean();
        String devId=request.getParameter("deviceid");
        String result = Constants.RESULT_MESSAGE_ERROR;
        try {
            if (StringUtils.isNotEmpty(devId)) {
                //1.根据mac地址检测改设备是否需要更新键盘秘钥
                String isupdatewk = DictionaryUtil.getClientRequest(DictionaryUtil.getHost(Constants.ISUPDATEWK_URI) + "/" + devId);
                keyBean.setIsupdatewk(isupdatewk);
                //2.查询此设备对应的银行编码，终端号，商户号
                String devresult = DictionaryUtil.getClientRequest(DictionaryUtil.getHost(Constants.GETBANK_URI) + "/" + devId);
                if (StringUtils.isNotEmpty(devresult)) {
                    HashMap devmap = JsonMapper.buildNormalMapper().fromJson(devresult, HashMap.class);
                    String bank_code = devmap.get("bank_code").toString();
                    String shh = devmap.get("shh").toString();
                    String zdh = devmap.get("zdh").toString();
                    String key = Constants.SECRETWK.replace("bank_code", bank_code).replace("zdh", zdh).replace("shh", shh);
                    String seckey = JedisUtil.getValue(key);
                    //3.如果获取不到redis数据，调用签到业务
                    if (StringUtils.isEmpty(seckey)) {
                        SecretKeyBean postBean = new SecretKeyBean();
                        postBean.setSeq(netUserService.getSequence().toString());
                        postBean.setZdh(zdh);
                        postBean.setShh(shh);
                        postBean.setBank_code(bank_code);
                        String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.FEESIGNON4HEX));
                        HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                        seckey = remap.get("data").toString();
                    }
                    if (StringUtils.isNotEmpty(seckey)) {
                        //4.如果需要更新秘钥，设置pik,mak,tdk属性，否则返回其余四个属性供前台组装报文
                        String[] keyArray = seckey.split(";");
                        //pik;mak;tdk;patchNo;zdh;shh;seq
                        keyBean.setPatchNo(keyArray[3]);
                        keyBean.setZdh(keyArray[4]);
                        keyBean.setShh(keyArray[5]);
                        keyBean.setSeq(keyArray[6]);
                        keyBean.setBodyHexStr(keyArray[7]);
                        if ("1".equals(isupdatewk)) {
                            if (keyArray.length == 8) {
                                keyBean.setPik(keyArray[0]);
                                keyBean.setMak(keyArray[1]);
                                keyBean.setTdk(keyArray[2]);
                            }
                        }
                        result = Constants.RESULT_MESSAGE_SUCCESS;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return this.result(result, null, keyBean);
    }

    /**
     * 获取秘钥后，更新改设备为已更新 isupdatewk=0
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/updWorkSecretKey", method = RequestMethod.POST)
    @ResponseBody
    public Result updWorkSecretKey(HttpServletRequest request) throws Exception {
        String devId=request.getParameter("deviceid");
        if (StringUtils.isNotEmpty(devId)) {
            DictionaryUtil.getClientRequest(DictionaryUtil.getHost(Constants.UPDATEWKBYMAC_URI) + "/" + devId);
        }
        return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, null);
    }

    /**
     * 居民医疗缴费    获取缴费列表
     *
     * @param cardNo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTrementLevel", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getTrementLevel(@RequestBody String cardNo) throws Exception {
        if (StringUtils.isNotEmpty(cardNo)) {
            List list = new ArrayList();
            for (int i = 0; i < 3; i++) {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", "张三" + i);
                map.put("id", "420666666666");
                map.put("level", "10" + i);
                list.add(map);
            }
            return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, list);
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }

    /**
     * 查询余额
     * hex 的字符串，通常给 银行报文使用 java 封装的子系统使用，如：德生宝
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getBalance4HexFee", method = RequestMethod.POST)
    @ResponseBody
    public Result getBalance4HexFee(HttpServletRequest request) throws Exception {
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                Form form = new Form();
                form.add("hexStr", hexStr);
                String resultObj = DictionaryUtil.postClientRequest(form, DictionaryUtil.getHost(Constants.QUERY4HEX));
                System.out.println("=="+resultObj);
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                if(!remap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_EXCEPTION)) {
                    LinkedHashMap lmap = (LinkedHashMap) remap.get("data");
                    lmap.remove("processOver");
                    lmap.remove("result");
                    lmap.remove("message");
                    return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, lmap);
                }
            }
        } catch (IOException e) {
            logger.error("查询余额 出错：" + e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }

    
    /**
     * 商家模式   根据设备编码获取商家的商户号，终端号
     *
     * @param request 设备编码
     * @return
     */
    @RequestMapping(value = "/getMerchantInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result getMerchantInfo(HttpServletRequest request) throws Exception {
        String result = Constants.RESULT_MESSAGE_ERROR;
        String devId=request.getParameter("deviceid");
        SecretKeyBean keyBean = new SecretKeyBean();
        try {
            if (StringUtils.isNotEmpty(devId)) {
                String devresult = DictionaryUtil.getClientRequest(DictionaryUtil.getHost(Constants.GETMERCHANT_URI) + "/" + devId);
                if (StringUtils.isNotEmpty(devresult)) {
                    HashMap devmap = JsonMapper.buildNormalMapper().fromJson(devresult, HashMap.class);
                    String shh = devmap.get("shh").toString();
                    String zdh = devmap.get("zdh").toString();
                    keyBean.setShh(shh);
                    keyBean.setZdh(zdh);
                    result = Constants.RESULT_MESSAGE_SUCCESS;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return this.result(result,null,keyBean);
    }

    /**
     * 商家模式 余额查询  平台返回银行报文
     * @param request 设备编码，16进制签到报文
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryBalance4HexMerchant", method = RequestMethod.POST)
    @ResponseBody
    public Result queryBalance4HexMerchant(HttpServletRequest request) throws Exception {
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                SecretKeyBean postBean = new SecretKeyBean();
                postBean.setBodyHexStr(hexStr);
                String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.QUERYBALANCE4HEXMERCHANT));
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                if(!remap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_EXCEPTION)) {
                    LinkedHashMap lmap = (LinkedHashMap) remap.get("data");
                    lmap.remove("processOver");
                    lmap.remove("result");
                    lmap.remove("message");
                    return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, lmap);
                }
            }
        } catch (IOException e) {
            logger.error("商家模式 余额查询失败："+e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }

    /**
     * 商家模式 取款
     * @param request 设备编码，16进制签到报文
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/drawCash4Hex", method = RequestMethod.POST)
    @ResponseBody
    public Result drawCash4Hex(HttpServletRequest request) {
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                SecretKeyBean postBean = new SecretKeyBean();
                postBean.setDevId(request.getParameter("deviceid"));
                postBean.setBodyHexStr(hexStr);
                String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.DRAWCASH4HEX));
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                if(!remap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_EXCEPTION)){
                    LinkedHashMap lmap=(LinkedHashMap)remap.get("data");
                    lmap.remove("processOver");
                    lmap.remove("result");
                    lmap.remove("message");
                    return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, lmap);
                }
            }
        } catch (IOException e) {
            logger.error("商家模式 取款失败："+e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }

    /**
     * 业务办理交易结果查询接口
     * @param request 16进制签到报文
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTransResult", method = RequestMethod.POST)
    @ResponseBody
    public Result getTransResult(HttpServletRequest request) {
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                SecretKeyBean postBean = new SecretKeyBean();
                postBean.setBodyHexStr(hexStr);
                String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.GETTRANSRESULT));
                System.out.println("==============" + DictionaryUtil.getHost(Constants.GETTRANSRESULT));
                System.out.println(resultObj);
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                if(!remap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_EXCEPTION)) {
                    LinkedHashMap lmap = (LinkedHashMap) remap.get("data");
                    lmap.remove("processOver");
                    lmap.remove("result");
                    lmap.remove("message");
                    return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, lmap);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("业务办理交易结果查询 失败：" + e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }

 //-----------金融部分孝感目前正在用的部分-----------------------------------------------------------------------------------------------------------------------------------------------   
    /**
     * 商家模式 签到
     * @param request 设备编码，16进制签到报文
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/signOn4hex", method = RequestMethod.POST)
    @ResponseBody
    public Result signOn4hex(HttpServletRequest request) throws Exception {
    	insertTransLog(request.getParameter("deviceid"), Constants.BUSINESSCODE.get("signOn4hex"));
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                SecretKeyBean postBean = new SecretKeyBean();
                postBean.setBodyHexStr(hexStr);
                String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.MERCHANTSIGNON4HEX));
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, remap.get("data"));
            }
        } catch (IOException e) {
            logger.error("商家模式 签到失败："+e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }
    
    

    /**
     * 社保卡缴费交易（湖北） add by 20160601
     * 商家模式 消费  平台返回银行报文
     * @param request 设备编码，16进制签到报文
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/consumer4HexMerchant", method = RequestMethod.POST)
    @ResponseBody
    public Result consumer4HexMerchant(HttpServletRequest request) throws Exception {
    	insertTransLog(request.getParameter("deviceid"), Constants.BUSINESSCODE.get("consumer4HexMerchant"));
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                SecretKeyBean postBean = new SecretKeyBean();
                postBean.setDevId(request.getParameter("deviceid"));
                postBean.setBodyHexStr(hexStr);
                String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.CONSUMER4HEXMERCHANT));
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                if(!remap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_EXCEPTION)) {
                    LinkedHashMap lmap = (LinkedHashMap) remap.get("data");
                    lmap.remove("processOver");
                    lmap.remove("result");
                    lmap.remove("message");
                    return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, lmap);
                }
            }
        } catch (IOException e) {
            logger.error("商家模式 消费失败："+e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }
    
    
    
    /**
     * 地税缴费 消费 -社保卡缴费交易（湖北） add by 20160601
     *
     * @param request 16进制的字符串
     * @return
     */
    @RequestMapping(value = "/consumer4HexFee", method = RequestMethod.POST)
    @ResponseBody
    public Result consumer4HexFee(HttpServletRequest request) {
    	insertTransLog(request.getParameter("deviceid"), Constants.BUSINESSCODE.get("consumer4HexFee"));
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                SecretKeyBean postBean = new SecretKeyBean();
                postBean.setBodyHexStr(hexStr);
                postBean.setDevId(request.getParameter("deviceid"));
                String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.CONSUMER4HEXFEE));
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                if(!remap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_EXCEPTION)) {
                    LinkedHashMap lmap = (LinkedHashMap) remap.get("data");
                    lmap.remove("processOver");
                    lmap.remove("result");
                    lmap.remove("message");
                    return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, lmap);
                }
            }
        } catch (IOException e) {
            logger.error("消费 出错："+e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }
    
    /**
     * 社保卡查询交易（湖北） add by 20160601  新增加的方法 调用银行工程中的方法也需要新写
     *
     * @param request 16进制的字符串
     * @return
     */
    @RequestMapping(value = "/queryTransPayInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result queryTransPayInfo(HttpServletRequest request) {
    	insertTransLog(request.getParameter("deviceid"), Constants.BUSINESSCODE.get("queryTransPayInfo"));
        String hexStr=request.getParameter("hexStr");
        try {
            if (StringUtils.isNotEmpty(hexStr)) {
                SecretKeyBean postBean = new SecretKeyBean();
                postBean.setBodyHexStr(hexStr);
                postBean.setDevId(request.getParameter("deviceid"));
                String resultObj = DictionaryUtil.postClientRequest(JsonMapper.buildNormalMapper().toJson(postBean), DictionaryUtil.getHost(Constants.CONSUMER4HEXFEE));
                HashMap remap = JsonMapper.buildNormalMapper().fromJson(resultObj, HashMap.class);
                if(!remap.get("statusCode").toString().equals(Constants.RESULT_MESSAGE_EXCEPTION)) {
                    LinkedHashMap lmap = (LinkedHashMap) remap.get("data");
                    lmap.remove("processOver");
                    lmap.remove("result");
                    lmap.remove("message");
                    return this.result(Constants.RESULT_MESSAGE_SUCCESS, null, lmap);
                }
            }
        } catch (IOException e) {
            logger.error("消费 出错："+e.getMessage());
        }
        return this.result(Constants.RESULT_MESSAGE_ERROR, null, null);
    }
}
