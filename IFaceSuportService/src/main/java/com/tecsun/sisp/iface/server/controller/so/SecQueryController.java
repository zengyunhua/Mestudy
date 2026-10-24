package com.tecsun.sisp.iface.server.controller.so;

import com.tecsun.sisp.iface.common.util.AESUtil;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.*;
import com.tecsun.sisp.iface.common.vo.card.*;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.NetUserServiceImpl;
import com.tecsun.sisp.iface.server.model.service.SecQueryServiceImpl;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 社保查询业务接口、社保卡业务接口
 * Created by jerry on 2015/5/31.
 */
@Controller
@RequestMapping(value = "/iface/rest")
public class SecQueryController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SecQueryController.class);

    @Autowired
    private SecQueryServiceImpl secQueryService;

    @Autowired
    private NetUserServiceImpl netUserService;

    //查询全部可用状态的人员信息
    @RequestMapping("/person")
    @ResponseBody
    public Result findAllByPersonState(Integer personState) throws Exception {
    	String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
		List<PersonVO> list = netUserService.findAllByPersonState(1);
		if(!list.isEmpty() && list.size()>0){
			 result = Constants.RESULT_MESSAGE_SUCCESS;
			 message = "操作成功";
		}else{
			 result = Constants.RESULT_MESSAGE_ERROR;
			 message = "暂无数据";
		}
		return result(result, message,list);
	}
    //系统内部调用 根据用户名，身份证获取用户唯一 个人编号
    public TsjbxxVO getGrbh(String sfzh, String xm) throws Exception {
        TsjbxxVO tsjbxxVO = new TsjbxxVO();
        tsjbxxVO.setSfzh(sfzh);
        tsjbxxVO.setXm(xm);
        return secQueryService.getTsjbxxVO(tsjbxxVO);
    }

    //18.获取个人基本信息	SISP_IFACE_SO_018
    @RequestMapping(value = "/getSelfInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getSelfInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsjbxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO vo = getGrbh(bean.getId(), bean.getName());
            if (vo != null) {
                vo.setXb(DictionaryUtil.getDictName(Constants.SEX_GROUP, vo.getXb()));
                vo.setMz(DictionaryUtil.getDictName(Constants.PARAM_NATION_GROUP, vo.getMz()));
                vo.setRyzt(DictionaryUtil.getDictName(Constants.FIVEPERATATUS_GROUP, vo.getRyzt()));
                vo.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getCbzt()));
                List<TsjbxxVO> list = new ArrayList<TsjbxxVO>(1);
                list.add(vo);
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "查询个人基本信息异常：" + e.getMessage();
        }
        return result(result, message, page);
    }

    //17.修改手机号码	SISP_IFACE_SO_01
    @RequestMapping(value = "/changePhoneNo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result changePhoneNo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        try {
            if(StringUtils.isNotEmpty(bean.getId())&&StringUtils.isNotEmpty(bean.getNewPhoneNo())) {
                netUserService.updateMobile(bean);
            } else {
                logger.error("身份证,新手机号码不能为空");
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "修改手机号码异常：身份证，新手机号码不能为空";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("修改手机号码失败");
            message = "修改手机号码异常：" + e.getMessage();
        }
        return result(result, message, null);
    }

    //1.查询个人社保基本信息	SISP_IFACE_SO_002
    @RequestMapping(value = "/getPersonSureInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPersonSureInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsgrcbxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TsgrcbxxVO> list = secQueryService.getPersonSureInfo(bean);
                if (!list.isEmpty()) {
                    for (TsgrcbxxVO vo : list) {
                        vo.setCbxz(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getCbxz()));
                        vo.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getCbzt()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "查询个人社保基本信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //2.个人缴费基数信息	SISP_IFACE_SO_003
    @RequestMapping(value = "/getPersonPay", method = RequestMethod.POST)
    @ResponseBody
    public Result getPersonPay(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsgrjfjsxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                page.setData(secQueryService.getPersonPay(bean));
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取个人缴费基数信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //3.个人养老保险缴费信息	SISP_IFACE_SO_004
    @RequestMapping(value = "/getPersonPesionPay", method = RequestMethod.POST)
    @ResponseBody
    public Result getPersonPesionPay(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsgrjfxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TsgrjfxxVO> list = secQueryService.getPersonPesionPay(bean);
                if (!list.isEmpty()) {
                    for (TsgrjfxxVO vo : list) {
                        vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                        vo.setJfbz(DictionaryUtil.getDictName(Constants.PAYSTATUS_GROUP, vo.getJfbz()));
                        vo.setKx(DictionaryUtil.getDictName(Constants.MONEYITEMTYPE_GROUP, vo.getKx()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取个人养老保险缴费信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //4.个人工伤保险基本信息	SISP_IFACE_SO_017
    @RequestMapping(value = "/getHurtInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHurtInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsgsbxjbxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TsgsbxjbxxVO> list = secQueryService.getHurtInfo(bean);
                if (!list.isEmpty()) {
                    for (TsgsbxjbxxVO vo : list) {
                        vo.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getCbzt()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取个人工伤保险基本信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //5.失业保险基本信息	SISP_IFACE_SO_005
    @RequestMapping(value = "/getLoseFee", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getLoseFee(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TslybxjbxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TslybxjbxxVO> list = secQueryService.getLoseFee(bean);
                if (!list.isEmpty()) {
                    for (TslybxjbxxVO vo : list) {
                        vo.setRmgbz(DictionaryUtil.getDictName(Constants.REGNATURE_GROUP, vo.getRmgbz()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取个人工伤保险基本信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //6.医疗保险费用结算信息	SISP_IFACE_SO_007
    @RequestMapping(value = "/getHealthFee", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHealthFee(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsmtbxfyjsxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TsmtbxfyjsxxVO> list = secQueryService.getHealthFee(bean);
                if (!list.isEmpty()) {
                    for (TsmtbxfyjsxxVO vo : list) {
                        vo.setYllb(DictionaryUtil.getDictName(Constants.MEDICALCLASS_GROUP, vo.getYllb()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取医疗保险费用结算信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //7.医疗保险个人帐户信息	SISP_IFACE_SO_008
    @RequestMapping(value = "/getHealthAccount", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHealthAccount(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsmtbxgrjbxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TsmtbxgrjbxxVO> list = secQueryService.getHealthAccount(bean);
                if (!list.isEmpty()) {
                    for (TsmtbxgrjbxxVO vo : list) {
                        vo.setXb(DictionaryUtil.getDictName(Constants.SEX_GROUP, vo.getXb()));
                        vo.setZgzt(DictionaryUtil.getDictName(Constants.FIVEPERATATUS_GROUP, vo.getZgzt()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取医疗保险个人帐户信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //8.医疗保险个人缴费信息	SISP_IFACE_SO_009
    @RequestMapping(value = "/getHealthPay", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHealthPay(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsmtbxgrjfmxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TsmtbxgrjfmxVO> list = secQueryService.getHealthPay(bean);
                if (!list.isEmpty()) {
                    for (TsmtbxgrjfmxVO vo : list) {
                        vo.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getCbzt()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "医疗保险个人缴费信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //9.生育保险基本信息	SISP_IFACE_SO_010
    @RequestMapping(value = "/getBirthInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getBirthInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        logger.info("=====生育保险基本信息=====bean.getName():"+bean.getName());
        Page<TssybxjbxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TssybxjbxxVO> list = secQueryService.getBirthInfo(bean);
                if (!list.isEmpty()) {
                    for (TssybxjbxxVO vo : list) {
                        vo.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getCbzt()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取生育保险基本信息异常：" + e.getMessage();
            logger.error(message);
        }
        return result(result, message, page);
    }

    //10.失业人员待遇支付明细表	SISP_IFACE_SO_006
    @RequestMapping(value = "/getLosePay", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getLosePay(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TssydyffxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                page.setData(secQueryService.getLosePay(bean));
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取失业人员待遇支付明细表异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //11.养老保险个人缴费明细	SISP_IFACE_SO_013
    @RequestMapping(value = "/getPesionTreatment", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPesionTreatment(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsylbxgrjfmxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                page.setData(secQueryService.getPesionTreatment(bean));
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取养老保险个人缴费明细异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //12.养老保险基本信息	SISP_IFACE_SO_015
    @RequestMapping(value = "/getPesionInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPesionInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsylbxjbxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                List<TsylbxjbxxVO> list = secQueryService.getPesionInfo(bean);
                if (!list.isEmpty()) {
                    for (TsylbxjbxxVO vo : list) {
                        vo.setXb(DictionaryUtil.getDictName(Constants.SEX_GROUP, vo.getXb()));
                        vo.setJfzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getJfzt()));
                        vo.setLtxlb(DictionaryUtil.getDictName(Constants.RETIREDCATAGORY_GROUP, vo.getLtxlb()));
                        vo.setGrzt(DictionaryUtil.getDictName(Constants.FIVEPERATATUS_GROUP, vo.getGrzt()));
                    }
                }
                page.setData(list);
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取养老保险基本信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //13.离退休人员待遇信息	SISP_IFACE_SO_016
    @RequestMapping(value = "/getQuitPay", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getQuitPay(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsylbxltxrydyxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                page.setData(secQueryService.getQuitPay(bean));
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取离退休人员待遇信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //14.养老保险待遇支付信息	SISP_IFACE_SO_014
    @RequestMapping(value = "/getPesionPay", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPesionPay(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsyldyffxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                page.setData(secQueryService.getPesionPay(bean));
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取养老保险待遇支付信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //15.在职人员养老保险个人年帐户	SISP_IFACE_SO_012
    @RequestMapping(value = "/getPesionYearAccount", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPesionYearAccount(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsylgrzhxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                page.setData(secQueryService.getPesionYearAccount(bean));
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取在职人员养老保险个人年帐户异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    //16.养老保险月账户	SISP_IFACE_SO_011
    @RequestMapping(value = "/getPesionMouthAccount", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPesionMouthAccount(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsylyzhxxVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = getGrbh(bean.getId(), bean.getName());
            if (tsjbxxVO != null) {
                bean.setGrbh(tsjbxxVO.getGrbh());
                bean.setPage(page);
                page.setData(secQueryService.getPesionMouthAccount(bean));
            } else {
                throw new Exception("不存在身份证号：" + bean.getId() + ",姓名：" + bean.getName());
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "获取养老保险月账户异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 五险查询
    */
    @RequestMapping(value = "/getPersonInsure", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPersonInsure(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            List<TsInsuranceVO> list = secQueryService.getPersonInsure(bean);
            if (!list.isEmpty()) {
                for (TsInsuranceVO vo : list) {
                    vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                    vo.setKx(DictionaryUtil.getDictName(Constants.MONEYITEMTYPE_GROUP, vo.getKx()));
                }
            }
            page.setData(list);
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "五险查询信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 参保个人信息
    */
    @RequestMapping(value = "/getPersonInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPersonInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        //险别的基本信息；
        bean.setAac002(bean.getId());
        List<TsInsuranceVO> list = secQueryService.getPersonInfo(bean);
        if(!list.isEmpty() && list.size()>0){
            tsInsuranceVO.setXm(list.get(0).getXm());//姓名；
            tsInsuranceVO.setDwmc(list.get(0).getDwmc());//单位名称
            tsInsuranceVO.setXb(DictionaryUtil.getDictName(Constants.SEX_GROUP, list.get(0).getXb()));//性别
            tsInsuranceVO.setSfzh(list.get(0).getSfzh());//身份证号码
            tsInsuranceVO.setRyzt(DictionaryUtil.getDictName(Constants.FIVEPERATATUS_GROUP, list.get(0).getRyzt()));//人员状态
            tsInsuranceVO.setMz(DictionaryUtil.getDictName(Constants.PARAM_NATION_GROUP, list.get(0).getMz()));//民族
            tsInsuranceVO.setGrsf(list.get(0).getGrsf());
            tsInsuranceVO.setGrbh(list.get(0).getGrbh());
            tsInsuranceVO.setCjgzsj(list.get(0).getCjgzsj());
        }
        return result(result, message, tsInsuranceVO);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 养老保险缴费合计
    */
    @RequestMapping(value = "/getPersonIncureSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPersonIncureSum(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        //险别的基本信息；
        bean.setAac002(bean.getId());
        List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(bean);
        if (grbhList.size() == 1) {
            tsInsuranceVO.setXm(grbhList.get(0).getXm());//姓名；
            bean.setAac001(grbhList.get(0).getGrbh());
            List<TsInsuranceVO> cbztlist = secQueryService.getPersonCvrgInfo(bean);
            if (!cbztlist.isEmpty() && cbztlist.size() > 0) {
                tsInsuranceVO.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, cbztlist.get(0).getCbzt()));//参保状态
            }
        }
        //养老缴费合计
        List<TsInsuranceVO> sumList =  secQueryService.getPaySum(bean);
        if(!sumList.isEmpty() && sumList.size()>0){
            for(int i = 0;i<sumList.size();i++){
                if(Constants.DW_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setDwjfhj(sumList.get(i).getSjje());
                }else if(Constants.GR_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setGrjfhj(sumList.get(i).getSjje());
                }
            }

        }
        return result(result, message, tsInsuranceVO);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 医疗保险缴费合计
    */
    @RequestMapping(value = "/getHealthIncureSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHealthIncureSum(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        //险别的基本信息；
        bean.setAac002(bean.getId());
        List<TsInsuranceVO> list = secQueryService.getHealthIncureySum(bean);
        if (!list.isEmpty() && list.size() > 0) {
            tsInsuranceVO.setXm(list.get(0).getXm());//姓名；
            tsInsuranceVO.setSfzh(list.get(0).getSfzh());//身份证号码；
            if (list.get(0).getYblx().startsWith(Constants.RYLB_CODE)) {//医保类型；
                tsInsuranceVO.setYblx(Constants.RYLB_VALUE1);
            } else {
                tsInsuranceVO.setYblx(Constants.RYLB_VALUE2);
            }
        }
        //医疗缴费合计
        List<TsInsuranceVO> sumList = secQueryService.getPaySum(bean);
        if (!sumList.isEmpty() && sumList.size() > 0) {
            for (int i = 0; i < sumList.size(); i++) {
                //基金来源=10，款项=20  为单位实缴； 基金来源=10，款项=10 为个人；  基金来源=20，款项=10 为 财政；
                if ("10".equals(sumList.get(i).getJjly()) && "20".equals(sumList.get(i).getKx())) { //单位实缴
                    tsInsuranceVO.setDwjfhj(sumList.get(i).getSjje());
                } else if ("10".equals(sumList.get(i).getJjly()) && "10".equals(sumList.get(i).getKx())) { //财政
                    tsInsuranceVO.setGrjfhj(sumList.get(i).getSjje());
                } else if ("20".equals(sumList.get(i).getJjly()) && "10".equals(sumList.get(i).getKx())) { //个人实缴
                    tsInsuranceVO.setCzhj(Double.valueOf(sumList.get(i).getSjje()));
                }
            }
        }
        //个人医疗帐户余额；
        SecQueryBean queryBean = new SecQueryBean();
        queryBean.setAac002(bean.getAac002());
        List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
        String grbh="";
        if(!grbhList.isEmpty()&& grbhList.size()>0){
            for(int j=0;j<grbhList.size();j++){
                if(j==grbhList.size()-1){
                    grbh+="'"+grbhList.get(j).getGrbh()+"'";
                }else{
                    grbh+="'"+grbhList.get(j).getGrbh()+"',";
                }
            }
            bean.setAac001(grbh);
            List<TsInsuranceVO> accountList = secQueryService.getylAccountYe(bean);
            if (!accountList.isEmpty() && accountList.size() > 0) {
                tsInsuranceVO.setYlye(accountList.get(0).getYlye());
            }
        }
        //参保状态
        if (grbhList.size() == 1) {
            bean.setAac001(grbhList.get(0).getGrbh());
            List<TsInsuranceVO> cbxzlist = secQueryService.getPersonCvrgInfo(bean);
            if(!cbxzlist.isEmpty() && cbxzlist.size()>0){
                tsInsuranceVO.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, cbxzlist.get(0).getCbzt()));
            }
        }
        return result(result, message, tsInsuranceVO);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 失业保险缴费合计
    */
    @RequestMapping(value = "/getLoseFeeIncureSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getLoseFeeIncureSum(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        //险别的基本信息；
        bean.setAac002(bean.getId());
        List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(bean);
        if (grbhList.size() == 1) {
            bean.setAac001(grbhList.get(0).getGrbh());
            List<TsInsuranceVO> list = secQueryService.getPersonCvrgInfo(bean);
            if (!list.isEmpty() && list.size() > 0) {
                tsInsuranceVO.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, list.get(0).getCbzt()));//参保状态
                tsInsuranceVO.setXm(list.get(0).getXm());//姓名；
            }
        }
        //失业保险缴费合计
        List<TsInsuranceVO> sumList =  secQueryService.getPaySum(bean);
        if(!sumList.isEmpty() && sumList.size()>0){
            for(int i = 0;i<sumList.size();i++){
                if(Constants.DW_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setDwjfhj(sumList.get(i).getSjje());
                }else if(Constants.GR_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setGrjfhj(sumList.get(i).getSjje());
                }
            }
        }
        return result(result, message, tsInsuranceVO);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 生育保险缴费合计
    */
    @RequestMapping(value = "/getBirthInfoIncureSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getBirthInfoIncureSum(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        String cbzt="";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        //险别的基本信息；
        bean.setAac002(bean.getId());
        List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(bean);
        if (grbhList.size() == 1) {
            bean.setAac001(grbhList.get(0).getGrbh());
            List<TsInsuranceVO> list = secQueryService.getPersonCvrgInfo(bean);
            if (!list.isEmpty() && list.size() > 0) {
                tsInsuranceVO.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, list.get(0).getCbzt()));//参保状态
                tsInsuranceVO.setXm(list.get(0).getXm());//姓名；
            }
        }
        //生育保险缴费合计
        List<TsInsuranceVO> sumList =  secQueryService.getPaySum(bean);
        if(!sumList.isEmpty() && sumList.size()>0){
            for(int i = 0;i<sumList.size();i++){
                if(Constants.DW_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setDwjfhj(sumList.get(i).getSjje());
                }else if(Constants.GR_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setGrjfhj(sumList.get(i).getSjje());
                }
            }
        }
        return result(result, message, tsInsuranceVO);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 工伤保险缴费合计
    */
    @RequestMapping(value = "/getHurtInfoIncureSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHurtInfoIncureSum(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        String cbzt="";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        //险别的基本信息；
        bean.setAac002(bean.getId());
        List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(bean);
        if (grbhList.size() == 1) {
            bean.setAac001(grbhList.get(0).getGrbh());
            List<TsInsuranceVO> list = secQueryService.getPersonCvrgInfo(bean);
            if (!list.isEmpty() && list.size() > 0) {
                tsInsuranceVO.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, list.get(0).getCbzt()));//参保状态
                tsInsuranceVO.setXm(list.get(0).getXm());//姓名；
            }
        }
        //工伤保险缴费合计
        List<TsInsuranceVO> sumList =  secQueryService.getPaySum(bean);
        if(!sumList.isEmpty() && sumList.size()>0){
            for(int i = 0;i<sumList.size();i++){
                if(Constants.DW_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setDwjfhj(sumList.get(i).getSjje());
                }else if(Constants.GR_CODE.equals(sumList.get(i).getKx())){
                    tsInsuranceVO.setGrjfhj(sumList.get(i).getSjje());
                }
            }
        }
        return result(result, message, tsInsuranceVO);
    }

    /*
    @date     2015-7-8
    @version
    @return
    @throws
    @ 查询参保信息险种
    */
    @RequestMapping(value = "/getPersonCvrgList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPersonCvrgList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        String grbh = "";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean = new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() == 1) {
                bean.setAac001(grbhList.get(0).getGrbh());
                List<TsInsuranceVO> list = secQueryService.getPersonCvrgInfo(bean);
                System.out.println(DictionaryUtil.getDictName(Constants.CHANNELCODE,"TSB"));
                if (!list.isEmpty()) {
                    for (TsInsuranceVO vo : list) {
                    	System.out.println("1翻译前："+vo.getCbzt());
                        vo.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getCbzt()));
                        System.out.println("1翻译后："+vo.getCbzt());
                        
                        System.out.println("2翻译前："+vo.getXzlx());
                        vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                        System.out.println("2翻译后："+vo.getXzlx());
                    }
                }
                page.setData(list);
            } else  if (grbhList.size() == 0) {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "不存在个人编号";
            }else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "查询参保险种信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 养老保险列表查询
    */
    @RequestMapping(value = "/getPersonIncureList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPersonIncureList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TsInsuranceVO> list = secQueryService.getPersonIncureList(bean);
                if (!list.isEmpty()) {
                    for (TsInsuranceVO vo : list) {
                        vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                        vo.setKx(DictionaryUtil.getDictName(Constants.MONEYITEMTYPE_GROUP, vo.getKx()));
                    }
                }
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "养老保险列表查询信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 医疗保险列表查询
    */
    @RequestMapping(value = "/getHealthIncureList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHealthIncureList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TsInsuranceVO> list = secQueryService.getHealthIncureyList(bean);
                if (!list.isEmpty()) {
                    for (TsInsuranceVO vo : list) {
                        vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                        vo.setKx(DictionaryUtil.getDictName(Constants.MONEYITEMTYPE_GROUP, vo.getKx()));
                    }
                }
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "医疗保险列表查询信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 失业保险列表查询
    */
    @RequestMapping(value = "/getLoseFeeIncureList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getLoseFeeIncureList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TsInsuranceVO> list = secQueryService.getLoseFeeIncureyList(bean);
                if (!list.isEmpty()) {
                    for (TsInsuranceVO vo : list) {
                        vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                        vo.setKx(DictionaryUtil.getDictName(Constants.MONEYITEMTYPE_GROUP, vo.getKx()));
                    }
                }
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "失业保险列表查询信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }
    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 生育保险列表查询
    */
    @RequestMapping(value = "/getBirthInfoIncureList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getBirthInfoIncureList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TsInsuranceVO> list = secQueryService.getBirthInfoIncureyList(bean);
                if (!list.isEmpty()) {
                    for (TsInsuranceVO vo : list) {
                        vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                        vo.setKx(DictionaryUtil.getDictName(Constants.MONEYITEMTYPE_GROUP, vo.getKx()));
                    }
                }
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "生育保险列表查询信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 工伤保险列表查询
    */
    @RequestMapping(value = "/getHurtInfoIncureList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHurtInfoIncureList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TsInsuranceVO> list = secQueryService.getHurtInfoIncureyList(bean);
                if (!list.isEmpty()) {
                    for (TsInsuranceVO vo : list) {
                        vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                        vo.setKx(DictionaryUtil.getDictName(Constants.MONEYITEMTYPE_GROUP, vo.getKx()));
                    }
                }
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "工伤保险列表查询信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 医疗帐户明细列表
    */
    @RequestMapping(value = "/getHealthAcountList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getHealthAcountList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TsInsuranceVO> list = secQueryService.getHealthAcountList(bean);
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        }catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "养老保险列表查询信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 城镇退休人员待遇信息
    */
    @RequestMapping(value = "/getQuitPayList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getQuitPayList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            //List<TsInsuranceVO> grcountList = this.getPersonGrbh(bean);
            List<TsInsuranceVO> grcountList = secQueryService.getQuitPayCode(bean);
            if(!grcountList.isEmpty() && grcountList.size()>0){
                bean.setAac001(grcountList.get(0).getGrbh());
                bean.setPage(page);
                List<TsInsuranceVO> list = secQueryService.getQuitPayList(bean);
                if(!list.isEmpty() && list.size()>0){
                    for(int i=0;i<list.size();i++){
                    	if(list.get(i).getSfzh()!=null && list.get(i).getSfzh().length()>0){
                    		list.get(i).setFfzh(this.getPersonAccount(list.get(i).getFfzh()));
                    	}
                    }
                    page.setData(list);
                }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "城镇退休人员列表查询信息异常：" + e.getMessage();
            e.printStackTrace();
        }
        return result(result, message, page);
    }

    public static String getPersonAccount(String str){
        String account = "";
        if(!str.isEmpty()&&str.length()>0){
            int length=str.length();
            account=str.substring(0, length-11)+"*******"+str.substring(length-2);
        }
        return account;
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 城镇退休人员待遇合计
    */
    @RequestMapping(value = "/getQuitPaySum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getQuitPaySum(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            //个人医疗帐户余额；
            List<TsInsuranceVO> grcountList = this.getPersonGrbh(bean);
            if(!grcountList.isEmpty() && grcountList.size()>0){
                bean.setAac001(grcountList.get(0).getGrbh());
                List<TsInsuranceVO> list = null;
                list = secQueryService.getQuitPaySum(bean);
                if(!list.isEmpty() && list.size()>0){
                    tsInsuranceVO.setXm(list.get(0).getXm());
                    tsInsuranceVO.setFfjehj(list.get(0).getFfjehj());
                    tsInsuranceVO.setDwmc(list.get(0).getDwmc());
                }
            }else{
                List<TsInsuranceVO> grbhcountList = this.getCheckPersonGrbh(bean);
                if(!grbhcountList.isEmpty() && grbhcountList.size()>0){
                    bean.setAac001(grbhcountList.get(0).getGrbh());
                    List<TsInsuranceVO> grbhlist = null;
                    grbhlist = secQueryService.getQuitPaySum(bean);
                    if(!grbhlist.isEmpty() && grbhlist.size()>0){
                        tsInsuranceVO.setXm(grbhlist.get(0).getXm());
                        tsInsuranceVO.setFfjehj(grbhlist.get(0).getFfjehj());
                        tsInsuranceVO.setDwmc(grbhlist.get(0).getDwmc());
                    }
                }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "城镇退休人员列表查询信息异常：" + e.getMessage();
            tsInsuranceVO.setXm(bean.getName());
            tsInsuranceVO.setFfjehj(0);
        }
        return result(result, message, tsInsuranceVO);

    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 权益单  个人基本信息
    */
    @RequestMapping(value = "/getRightPersInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getRightPersInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        //险别的基本信息；
        bean.setAac002(bean.getId());
        List<TsInsuranceVO> list = secQueryService.getPersonInfo(bean);
        if(!list.isEmpty() && list.size()>0){
            tsInsuranceVO.setDwmc(list.get(0).getDwmc());//单位名称
        }
        return result(result, message, tsInsuranceVO);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 权益单  个人基本信息
    */
    @RequestMapping(value = "/getRightPersCvrgInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getRightPersCvrgInfo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        String grbh="";
        Page<TsInsuranceVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            TsjbxxVO tsjbxxVO = new TsjbxxVO();
            tsjbxxVO.setSfzh(bean.getId());
            List<TsjbxxVO> grbhList = secQueryService.getGrbhList(tsjbxxVO);
            if(!grbhList.isEmpty()&& grbhList.size()>0){
                for(int j=0;j<grbhList.size();j++){
                    if(j==grbhList.size()-1){
                        grbh+="'"+grbhList.get(j).getGrbh()+"'";
                    }else{
                        grbh+="'"+grbhList.get(j).getGrbh()+"',";
                    }
                }
            }
            bean.setAac001(grbh);
            bean.setPage(page);
            List<TsInsuranceVO> list = secQueryService.getPersonCvrgInfo(bean);
            if (!list.isEmpty()) {
                for (TsInsuranceVO vo : list) {
                    vo.setCbzt(DictionaryUtil.getDictName(Constants.PERSONINSUREDSTATE_GROUP, vo.getCbzt()));
                    vo.setXzlx(DictionaryUtil.getDictName(Constants.INSURECANCETYPE_GROUP, vo.getXzlx()));
                }
            }
            page.setData(list);
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "查询参保险种信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
    @date     2015-7-7
    @version
    @return
    @throws
    @ 权益单 个人帐户有历年缴费汇总；
    */
    @RequestMapping(value = "/getRightSum", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getRightSum(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        String grbh = "";
        TsInsuranceVO tsInsuranceVO = new TsInsuranceVO();
        bean.setAac002(bean.getId());
        try {
            //个人医疗帐户余额；
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(bean);
            if (!grbhList.isEmpty() && grbhList.size() > 0) {
                for (int j = 0; j < grbhList.size(); j++) {
                    if (j == grbhList.size() - 1) {
                        grbh += "'" + grbhList.get(j).getGrbh() + "'";
                    } else {
                        grbh += "'" + grbhList.get(j).getGrbh() + "',";
                    }
                }
                bean.setAac001(grbh);
                List<TsInsuranceVO> accountList = secQueryService.getylAccountYe(bean);
                if (!accountList.isEmpty() && accountList.size() > 0) {
                    tsInsuranceVO.setYlye(accountList.get(0).getYlye());
                }
            }
            //养老缴费合计
            bean.setAae140(Constants.PesionCvrgTypeUrl);
            List<TsInsuranceVO> ageList = secQueryService.getPaySum(bean);
            if (!ageList.isEmpty() && ageList.size() > 0) {
                for (int i = 0; i < ageList.size(); i++) {
                    if (Constants.DW_CODE.equals(ageList.get(i).getKx())) {
                        tsInsuranceVO.setAgedUnitSum(Double.valueOf(ageList.get(i).getSjje()));
                    } else if (Constants.GR_CODE.equals(ageList.get(i).getKx())) {
                        tsInsuranceVO.setAgedPserSum(Double.valueOf(ageList.get(i).getSjje()));
                    }
                }
            }
            //医疗缴费合计:基金来源=10，款项=20  为单位实缴； 基金来源=10，款项=10 为个人；  基金来源=20，款项=10 为 财政；
            bean.setAae140(Constants.HealthCvrgTypeUrl);
            List<TsInsuranceVO> medicalList = secQueryService.getPaySum(bean);
            if (!medicalList.isEmpty() && medicalList.size() > 0) {
                for (int i = 0; i < medicalList.size(); i++) {
                    if ("10".equals(medicalList.get(i).getJjly()) && Constants.DW_CODE.equals(medicalList.get(i).getKx())) { //单位实缴
                        tsInsuranceVO.setMedicalPserSum(Double.valueOf(medicalList.get(i).getSjje()));
                    } else if ("10".equals(medicalList.get(i).getJjly()) && Constants.GR_CODE.equals(medicalList.get(i).getKx())) { //财政
                        tsInsuranceVO.setMedicalGovSum(Double.valueOf(medicalList.get(i).getSjje()));
                    } else if ("20".equals(medicalList.get(i).getJjly()) && Constants.GR_CODE.equals(medicalList.get(i).getKx())) { //个人实缴
                        tsInsuranceVO.setMedicalUnitSum(Double.valueOf(medicalList.get(i).getSjje()));
                    }
                }
            }
            //失业缴费合计
            bean.setAae140(Constants.LoseFeeCvrgTypeUrl);
            List<TsInsuranceVO> lossList = secQueryService.getPaySum(bean);
            if (!lossList.isEmpty() && lossList.size() > 0) {
                for (int i = 0; i < lossList.size(); i++) {
                    if (Constants.DW_CODE.equals(lossList.get(i).getKx())) {
                        tsInsuranceVO.setLossUnitSum(Double.valueOf(lossList.get(i).getSjje()));
                    } else if (Constants.GR_CODE.equals(lossList.get(i).getKx())) {
                        tsInsuranceVO.setLossPserSum(Double.valueOf(lossList.get(i).getSjje()));
                    }
                }
            }
            //工伤缴费合计
            bean.setAae140(Constants.HurtInfoCvrgTypeUrl);
            List<TsInsuranceVO> injuryList = secQueryService.getPaySum(bean);
            if (!injuryList.isEmpty() && injuryList.size() > 0) {
                for (int i = 0; i < injuryList.size(); i++) {
                    if (Constants.DW_CODE.equals(injuryList.get(i).getKx())) {
                        tsInsuranceVO.setInjuryUnitSum(Double.valueOf(injuryList.get(i).getSjje()));
                    } else if (Constants.GR_CODE.equals(injuryList.get(i).getKx())) {
                        tsInsuranceVO.setInjuryUnitSum(Double.valueOf(injuryList.get(i).getSjje()));
                    }
                }
            }
            //生育缴费合计
            bean.setAae140(Constants.BirthInfoCvrgTypeUrl);
            List<TsInsuranceVO> fertilityList = secQueryService.getPaySum(bean);
            if (!fertilityList.isEmpty() && fertilityList.size() > 0) {
                for (int i = 0; i < fertilityList.size(); i++) {
                    if (Constants.DW_CODE.equals(fertilityList.get(i).getKx())) {
                        tsInsuranceVO.setFertilityUnitSum(Double.valueOf(fertilityList.get(i).getSjje()));
                    } else if (Constants.GR_CODE.equals(fertilityList.get(i).getKx())) {
                        tsInsuranceVO.setFertilityPserSum(Double.valueOf(fertilityList.get(i).getSjje()));
                    }
                }
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "查询参保险种信息异常：" + e.getMessage();
            e.printStackTrace();
            logger.error(message);
        }
        return result(result, message, tsInsuranceVO);
    }

    //系统内部调用 根据用户名，身份证获取用户唯一 个人编号
    public List getPersonGrbh(@RequestBody SecQueryBean bean) throws Exception {
        return  secQueryService.getPersonGrbh(bean);
    }

    //系统内部调用 根据用户名，临时解决有些社保卡没有异地安置标志字段
    public List getCheckPersonGrbh(@RequestBody SecQueryBean bean) throws Exception {
        return  secQueryService.getCheckPersonGrbh(bean);
    }

    //获取手机号码
    @RequestMapping(value = "/getPhoneNo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getPhoneNo(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        String data ="";
        try {
            if(StringUtils.isNotEmpty(bean.getCernum())&&StringUtils.isNotEmpty(bean.getName())) {
                NetUserInfoVO  netUserInfo  = netUserService.getPhoneNo(bean);
                if(netUserInfo!=null){
                    if(netUserInfo.getMobile()!=null && !"".equals(netUserInfo.getMobile())){
                    	data =netUserInfo.getMobile() ;
                    }else{
                    	 message ="查询出的手机号码为空！";
                    	 logger.error("查询出的手机号码为空！");
                    }
                }/*else{
                	 result = Constants.RESULT_MESSAGE_ERROR;
                	 message ="暂无此人信息！";
                	 logger.error("暂无此人信息！");
                }*/
            } else {
            	 result = Constants.RESULT_MESSAGE_ERROR;
            	 message ="获取手机号码时入参身份证和姓名不能为空！";
            	 logger.error("获取手机号码时入参身份证和姓名不能为空！");
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            logger.error("获取手机号码失败SecQueryController-getPhoneNo");
            message = "获取手机号码异常：SecQueryController-getPhoneNo" + e.getMessage();
        }
        return result(result, message, data);
    }

//    public Result queryJBXX(@RequestBody SecQueryBean bean) {
//        String result = Constants.RESULT_MESSAGE_SUCCESS;
//        String message = "";
//        String url = Constants.DXSERVEICURL;
//        // DataExchange服务登录用户名，默认为admin
//        String username = Constants.SNBusername;
//        // DataExchange服务登录用户名，默认为1
//        String password =Constants.SNBpassword;
//        // 任务OID，定义的数据查询任务业务标识，可以到“任务”页面查看
//        String taskOId =Constants.SNBJBXXtaskOId;
//        // 添加查询条件
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("aac003", bean.getAac003());
//        params.put("aac002", bean.getAac002());
//        // 客户端应用名称，可以为空
//        String appName = Constants.SNBappName;
//        // 构造数据服务客户端实例
//        DataServiceClient client = DataExchangeClientFactory
//                .createDataServiceClient(url, username, password);
//        // 说明数据来自哪儿个应用系统，为监控展现使用
//        client.setClientAppName(appName);
//        // 从服务端查询数据
//        String xml = client.get(taskOId, params);
//        List<SNBVo> list = null;
//        if (xml != null && !"".equals(xml)) {
//            xml = xml.replace("<![CDATA[", "").replaceAll("]]>", "");
//            try {
//                list = Dom4JUtil.readXMLToJavaBean_SNB(new SNBVo(), xml);
//            } catch (Exception e1) {
//                result = Constants.RESULT_MESSAGE_ERROR;
//                logger.error("省农保-个人基本参保信息查询失败");
//            }
//        }
//        return this.result(result, message, list);
//    }

//    /**
//     * 省农保-变更信息查询
//     *
//     * @param bean
//     * @return
//     */
//    @RequestMapping(value = "/queryBGXX", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Result queryBGXX(@RequestBody SecQueryBean bean) {
//        String result = Constants.RESULT_MESSAGE_SUCCESS;
//        String message = "";
//        String url = Constants.DXSERVEICURL;
//        // DataExchange服务登录用户名，默认为admin
//        String username = Constants.SNBusername;
//        // DataExchange服务登录用户名，默认为1
//        String password =Constants.SNBpassword;
//        // 任务OID，定义的数据查询任务业务标识，可以到“任务”页面查看
//        String taskOId =Constants.SNBBGXXtaskOId; //
//        // 添加查询条件
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("aac001", bean.getAac001());
//        // 客户端应用名称，可以为空
//        String appName = Constants.SNBappName;
//        // 构造数据服务客户端实例
//        DataServiceClient client = DataExchangeClientFactory
//                .createDataServiceClient(url, username, password);
//        // 说明数据来自哪儿个应用系统，为监控展现使用
//        client.setClientAppName(appName);
//        // 从服务端查询数据
//        String xml = client.get(taskOId, params);
//        List<SNBVo> list = null;
//        if (xml != null && !"".equals(xml)) {
//            xml = xml.replace("<![CDATA[", "").replaceAll("]]>", "");
//            try {
//                list = Dom4JUtil.readXMLToJavaBean_SNB(new SNBVo(), xml);
//            } catch (Exception e1) {
//                result = Constants.RESULT_MESSAGE_ERROR;
//                logger.error("省农保-变更信息查询失败");
//                e1.printStackTrace();
//            }
//        }
//        return this.result(result, message, list);
//    }

//    /**
//     * 省农保-缴费信息查询
//     *
//     * @param bean
//     * @return
//     */
//    @RequestMapping(value = "/queryJFXX", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Result queryJFXX(@RequestBody SecQueryBean bean) {
//        String result = Constants.RESULT_MESSAGE_SUCCESS;
//        String message = "";
//        String url = Constants.DXSERVEICURL;
//        // DataExchange服务登录用户名，默认为admin
//        String username = Constants.SNBusername;
//        // DataExchange服务登录用户名，默认为1
//        String password =Constants.SNBpassword;
//        // 任务OID，定义的数据查询任务业务标识，可以到“任务”页面查看
//        String taskOId =Constants.SNBJFXXtaskOId; //
//        // 添加查询条件
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("aac001", bean.getAac001());
//        // 客户端应用名称，可以为空
//        String appName = Constants.SNBappName;
//        // 构造数据服务客户端实例
//        DataServiceClient client = DataExchangeClientFactory
//                .createDataServiceClient(url, username, password);
//        // 说明数据来自哪儿个应用系统，为监控展现使用
//        client.setClientAppName(appName);
//        // 从服务端查询数据
//        String xml = client.get(taskOId, params);
//        List<SNBVo> list = null;
//        if (xml != null && !"".equals(xml)) {
//            xml = xml.replace("<![CDATA[", "").replaceAll("]]>", "");
//            try {
//                list = Dom4JUtil.readXMLToJavaBean_SNB(new SNBVo(), xml);
//            } catch (Exception e1) {
//                result = Constants.RESULT_MESSAGE_ERROR;
//                logger.error("省农保-缴费信息查询失败");
//                e1.printStackTrace();
//            }
//        }
//        return this.result(result, message, list);
//    }

//    /**
//     * 省农保-个人年账户信息查询
//     *
//     * @param bean
//     * @return
//     */
//    @RequestMapping(value = "/queryNZH", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Result queryNZH(@RequestBody SecQueryBean bean) {
//        String result = Constants.RESULT_MESSAGE_SUCCESS;
//        String message = "";
//        String url = Constants.DXSERVEICURL;
//        // DataExchange服务登录用户名，默认为admin
//        String username = Constants.SNBusername;
//        // DataExchange服务登录用户名，默认为1
//        String password =Constants.SNBpassword;
//        // 任务OID，定义的数据查询任务业务标识，可以到“任务”页面查看
//        String taskOId = Constants.SNBNZHtaskOId;
//        // 添加查询条件
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("aac001", bean.getAac001());
//        // 客户端应用名称，可以为空
//        String appName = Constants.SNBappName;
//        // 构造数据服务客户端实例
//        DataServiceClient client = DataExchangeClientFactory
//                .createDataServiceClient(url, username, password);
//        // 说明数据来自哪儿个应用系统，为监控展现使用
//        client.setClientAppName(appName);
//        // 从服务端查询数据
//        String xml = client.get(taskOId, params);
//        List<SNBVo> list = null;
//        if (xml != null && !"".equals(xml)) {
//            xml = xml.replace("<![CDATA[", "").replaceAll("]]>", "");
//            try {
//                list = Dom4JUtil.readXMLToJavaBean_SNB(new SNBVo(), xml);
//            } catch (Exception e1) {
//                result = Constants.RESULT_MESSAGE_ERROR;
//                logger.error("省农保-个人年账户信息查询失败");
//                e1.printStackTrace();
//            }
//        }
//        return this.result(result, message, list);
//    }

//    /**
//     * 省农保-待遇支付查询
//     *
//     * @param bean
//     * @return
//     */
//    @RequestMapping(value = "/queryDYBFXX", method = RequestMethod.POST, produces = "application/json")
//    @ResponseBody
//    public Result queryDYBFXX(@RequestBody SecQueryBean bean) {
//        String result = Constants.RESULT_MESSAGE_SUCCESS;
//        String message = "";
//        String url = Constants.DXSERVEICURL;
//        // DataExchange服务登录用户名，默认为admin
//        String username = Constants.SNBusername;
//        // DataExchange服务登录用户名，默认为1
//        String password =Constants.SNBpassword;
//        // 任务OID，定义的数据查询任务业务标识，可以到“任务”页面查看
//        String taskOId =Constants.SNBDYBFXXtaskOId;
//        // 添加查询条件
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("aac001", bean.getAac001());
//        // 客户端应用名称，可以为空
//        String appName = Constants.SNBappName;
//        // 构造数据服务客户端实例
//        DataServiceClient client = DataExchangeClientFactory
//                .createDataServiceClient(url, username, password);
//        // 说明数据来自哪儿个应用系统，为监控展现使用
//        client.setClientAppName(appName);
//        // 从服务端查询数据
//        String xml = client.get(taskOId, params);
//        List<SNBVo> list = null;
//        if (xml != null && !"".equals(xml)) {
//            xml = xml.replace("<![CDATA[", "").replaceAll("]]>", "");
//            try {
//                list = Dom4JUtil.readXMLToJavaBean_SNB(new SNBVo(), xml);
//            } catch (Exception e1) {
//                result = Constants.RESULT_MESSAGE_ERROR;
//                logger.error("省农保-待遇支付查询失败");
//            }
//        }
//        return this.result(result, message, list);
//    }

//    20160405新增接口 start
    /*
    @date     2016-4-5
    @param bean
    @return
    @throws
    @ 工伤认定信息列表查询
    */
    @RequestMapping(value = "/getInjuryIdenList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getInjuryIdenList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TswxaddVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TswxaddVO> list = secQueryService.getInjuryIdenList(bean);
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "工伤认定信息列表查询信息异常：" + e.getMessage();
            logger.error(message);
        }
        return result(result, message, page);
    }
 /*
    @date     2016-4-6
    @param bean
    @return
    @throws
    @ 工伤定期待遇信息
    */
    @RequestMapping(value = "/getInjuryTreatmentList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getInjuryTreatmentList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TswxaddVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TswxaddVO> list = secQueryService.getInjuryTreatmentList(bean);
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "工伤定期待遇信息列表查询信息异常：" + e.getMessage();
            logger.error(message);
        }
        return result(result, message, page);
    }
     /*
    @date     2016-4-6
    @param bean
    @return
    @throws
    @ 工伤医疗费结算记录查询
    */
    @RequestMapping(value = "/getInjuryMedicalList", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getInjuryMedicalList(@RequestBody SecQueryBean bean) throws Exception {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TswxaddVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TswxaddVO> list = secQueryService.getInjuryMedicalList(bean);
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "工伤医疗费结算记录列表查询信息异常：" + e.getMessage();
            logger.error(message);
        }
        return result(result, message, page);
    }

    /*
     @date     2016-4-6
     @param bean
     @return
     @throws
     @劳动能力鉴定信息
     */
    @RequestMapping(value="/getWorkAbilityCheckList",method =RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Result getWorkAbilityCheckList(@RequestBody SecQueryBean bean){
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TswxaddVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TswxaddVO> list = secQueryService.getWorkAbilityCheckList(bean);
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "劳动能力鉴定信息列表查询信息异常：" + e.getMessage();
            logger.error(message);
        }
        return result(result, message, page);
    }
    /*
     @date     2016-4-19
     @param bean
     @return
     @throws
     @生育保险待遇信息查询
     */
    @RequestMapping(value="/getFertilityTreatList",method =RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Result getFertilityTreatList(@RequestBody SecQueryBean bean){
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TswxaddVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TswxaddVO> list = secQueryService.getFertilityTreatList(bean);
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "生育保险待遇信息查询信息异常：" + e.getMessage();
            logger.error(message);
        }
        return result(result, message, page);
    }
    /*
   @date     2016-4-18
   @param bean
   @return
   @throws
   @生育费用结算信息查询
   */
    @RequestMapping(value="/getFertilityMedicalList",method =RequestMethod.POST,produces = "application/json")
    @ResponseBody
    public Result getFertilityMedicalList(@RequestBody SecQueryBean bean){
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "操作成功";
        Page<TswxaddVO> page = new Page(bean.getPageno(), bean.getPagesize());
        try {
            bean.setAac002(bean.getId());
            bean.setPage(page);
            SecQueryBean queryBean=new SecQueryBean();
            queryBean.setAac002(bean.getAac002());
            List<TsInsuranceVO> grbhList = secQueryService.getPersonCount(queryBean);
            if (grbhList.size() <= 1) {
                List<TswxaddVO> list = secQueryService.getFertilityMedicalList(bean);
                page.setData(list);
            } else {
                result = Constants.RESULT_MESSAGE_EXCEPTION;
                message = bean.getAac002() + "存在多个个人编号，请到窗口合并";
            }
        } catch (Exception e) {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "生育医疗费用结算信息查询信息异常：" + e.getMessage();
            logger.error(message);
        }
        return result(result, message, page);
    }
    //    20180830新增接口省农保 end
    public static final String SOAPACTION = "http://entrance.platform.gov.cn/";
    long time=System.currentTimeMillis();
    Date date =new Date(time);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    String dateNowStr = sdf.format(date);
    /*获取个人编号的签名*/
    public String getSignGrbh(String aac003,String aac002) throws Exception {
        String inputData=getXmlGrbh(aac003, aac002);
        String busi_name=Constants.HBNBusername;
        String busi_pwd=Constants.HBNBpassword;
        String sign=  bytes2Hex(AESUtil.md5(URLEncoder.encode(inputData, "utf-8") + time + busi_name + busi_pwd));
        String url="?busi_name=" + busi_name + "&timestamp="+ time +"&sign=" + sign;
        return  url;
    }
    public static String bytes2Hex(byte[] src) {
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] res = new char[src.length * 2]; // 每个byte对应两个字符
        final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >> 4 & 0x0f]; // 先存byte的高4位
            res[j++] = hexDigits[src[i] & 0x0f]; // 再存byte的低4位
        }
        return new String(res);
    }
    //获取个人编号的xml
    public String getXmlGrbh(String aac003, String aac002){
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
                "<request>" +
                "<head>" +
                "<fun>GGYWDW_HBCXJB_009</fun>" +
                "<datetime>" + dateNowStr + "</datetime>" +
                "<operatorName>si_enterprise</operatorName>" +
                "<reciver>"+ Constants.HBNBusername +"</reciver>" +
                "<sender>"+ Constants.HBNBpassword +"</sender>" +
                "<ver>1.0</ver>" +
                "</head>" +
                "<body>" +
                "<name>" + aac003 + "</name>" +
                "<idNumber>" + aac002 + "</idNumber>" +
                "</body>" +
                "</request>" ;
        return xml;
    }

    //通过身份证和姓名获取个人编号
    public long getJbGrbh(String aac003 ,String aac002) throws Exception {
        Service service = new Service();
        Call call;
        String wsResult = "";
        String sign= getSignGrbh(aac003, aac002);
        long grbh =0;
        try {
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(Constants.HBNBSERVEICURL + sign );
            call.setOperationName(new QName(SOAPACTION, "funcInterface"));//设置要调用哪个方法
            call.addParameter( new QName(SOAPACTION, "parameters"),XMLType.XSD_STRING, ParameterMode.IN);//设置传入参数的属性
            call.setReturnType(XMLType.XSD_STRING);//要返回的数据类型（自定义类型）
            call.setTimeout(30000);
            wsResult = (String) call.invoke(new Object[] { getXmlGrbh(aac003, aac002) }); //调用方法并传递参数
            if(wsResult !="" && wsResult != null ){
                wsResult= wsResult.replace("<?xml version=\"1.0\" encoding=\"GBK\" standalone='yes'?>", "");
                XMLSerializer xmlSerializer = new XMLSerializer();
                String resutStr = xmlSerializer.read(wsResult).toString();
                JSONObject result = JSONObject.fromObject(resutStr);
                String code = (String) JSONObject.fromObject(result.get("head")).get("code");
                if (code.equals("1")){
                    grbh = Long.parseLong(JSONObject.fromObject(result.get("body")).get("personNumber").toString()) ;
                }
            }
        } catch (Exception e) {
            logger.error("---获取个人编号接口失败---" , e.getCause());
        }
        return grbh;
    }

    /*获取签名*/
    public String getSign(String method,long personId) throws UnsupportedEncodingException {
        String inputData=getXml(method,personId);
        String busi_name =Constants.HBNBusername;
        String busi_pwd=Constants.HBNBpassword;
        String sign=BaseController.MD5(URLEncoder.encode(inputData, "utf-8") + time + busi_name + busi_pwd);
        String url="?busi_name=" + busi_name + "&timestamp="+ time +"&sign=" + sign;
        return  url;
    }

    //获取入参的xml
    public String getXml(String method,long personId){
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"+
                "<request>" +
                "<head>" +
                "<fun>" + method + "</fun>" +
                "<datetime>" + dateNowStr + "</datetime>" +
                "<operatorName>si_enterprise</operatorName>" +
                "<reciver>"+ Constants.HBNBusername +"</reciver>" +
                "<sender>"+ Constants.HBNBpassword +"</sender>" +
                "<ver>1.0</ver>" +
                "</head>" +
                "<body>" +
                "<personId>" + personId + "</personId>" +
                "</body>" +
                "</request>" ;
        return xml;
    }

    //调用第三方接口 传方法名和个人编号
    public String getHisKS(long aac001,String  method) throws UnsupportedEncodingException {
        Service service = new Service();
        Call call;
        String wsResult = "";
        String sign= getSign(method,aac001);
        Map<String , String > map = new HashMap<String , String>();
        try {
            call = (Call) service.createCall();
            call.setTargetEndpointAddress(Constants.HBNBSERVEICURL + sign );
            call.setOperationName(new QName(SOAPACTION, "funcInterface"));//设置要调用哪个方法
            call.addParameter( new QName(SOAPACTION, "parameters"),XMLType.XSD_STRING, ParameterMode.IN);//设置传入参数的属性
            call.setReturnType(XMLType.XSD_STRING);//要返回的数据类型（自定义类型）
            call.setTimeout(30000);
            wsResult = (String) call.invoke(new Object[] { getXml(method,aac001) }); //调用方法并传递参数
        } catch (Exception e) {
            logger.error("---调用第三方接口接口失败---" , e.getCause());
        }
        return wsResult;
    }

    /**
     * 省农保-个人基本参保信息查询
     * @param bean
     * @return
     */
    @RequestMapping(value = "/queryJBXX", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result queryJBXX( @RequestBody SecQueryBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "查询成功";
        String  method =Constants.HBNBJBXXtaskOId;
        try {
          long grbh=getJbGrbh(bean.getAac003(),bean.getAac002());
            if (grbh ==0){
                message="没有查询到数据";
                result = Constants.RESULT_MESSAGE_ERROR;
            }else {
                String wsResult=getHisKS(grbh,method);
                if( wsResult != null && wsResult != "" ){
                    wsResult= wsResult.replace("<?xml version=\"1.0\" encoding=\"GBK\" standalone='yes'?>", "");
                    XMLSerializer xmlSerializer = new XMLSerializer();
                    String resutStr = xmlSerializer.read(wsResult).toString();
                    JSONObject json = JSONObject.fromObject(resutStr);
                    String code = (String) JSONObject.fromObject(json.get("head")).get("code");
                    if (code.equals("0")){
                        message="没有查询到数据";
                        result = Constants.RESULT_MESSAGE_ERROR;
//                        grbh=(long) JSONObject.fromObject(result.get("body")).get("personNumber");
                    }else if(code.equals("1")){
                        JSONObject object = JSONObject.fromObject(json.get("body"));
                        SNBVo snbVo =new SNBVo();
                        snbVo.setAAC001(object.get("personId").toString());
                        snbVo.setAAC002(object.get("idNumber").toString());
                        snbVo.setAAC003(object.get("name").toString());
                        snbVo.setAAC010(object.get("location").toString());
                        JSONArray list = JSONArray.fromObject(object.get("list")) ;
                        if(list.size()>0){
                            JSONObject object1= JSONObject.fromObject( list.get(0));
                            snbVo.setAAc049(object1.get("insureTime").toString());
                            if(object1.get("insureType").toString().equals("1")){
                                snbVo.setAAC008("正常参保");
                            } else if(object1.get("insureType").toString().equals("0")){
                                snbVo.setAAC008("暂停参保");
                            }
                        }else if(list.size()>1){
                            logger.error("有多个参保信息");
                        }
                        List<SNBVo> list1=new ArrayList<>();
                        list1.add(snbVo);
                        return result(result,message,list1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result(result, message);
    }

    /**
     * 省农保-变更信息查询
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "/queryBGXX", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result queryBGXX(@RequestBody SecQueryBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "查询成功";
        String  method =Constants.HBNBBGXXtaskOId;
//        Page<SNBVo> page = new Page<>(bean.getPageno(),bean.getPagesize());
        try {
//            long grbh=getJbGrbh(bean.getName(),bean.getSfzh());
//            if (grbh ==0){
//                message="没有查询到数据";
//                result = Constants.RESULT_MESSAGE_ERROR;
//            }else {
                String wsResult=getHisKS(Long.parseLong(bean.getAac001()),method);
                if( wsResult != null && wsResult != "" ){
                    wsResult= wsResult.replace("<?xml version=\"1.0\" encoding=\"GBK\" standalone='yes'?>", "");
                    XMLSerializer xmlSerializer = new XMLSerializer();
                    String resutStr = xmlSerializer.read(wsResult).toString();
                    JSONObject json = JSONObject.fromObject(resutStr);
                    String code = (String) JSONObject.fromObject(json.get("head")).get("code");
                    if (code.equals("0")){
                        message="没有查询到数据";
                        result = Constants.RESULT_MESSAGE_ERROR;
                    }else if(code.equals("1")){
                        JSONObject object = JSONObject.fromObject(json.get("body"));
                        List<SNBVo> snbVos= new ArrayList<>();

                        JSONArray list = JSONArray.fromObject(object.get("list")) ;
                        if(list.size()>0){
                            for (int i = 0; i <list.size() ; i++) {
                                JSONObject object1= JSONObject.fromObject( list.get(i));
                                SNBVo snbVo =new SNBVo();
                                snbVo.setAAC001(object1.get("personId").toString());
                                snbVo.setAAC003(object1.get("name").toString());
                                snbVo.setAAC002(object1.get("idNumber").toString());
                                snbVo.setAAC050(object1.get("changeType").toString());
                                snbVo.setAAE035(object1.get("changeTime").toString());
                                snbVo.setAAE160(object1.get("changeReason").toString());
                                snbVos.add(snbVo);
                            }
//                            ListPageUtil<SNBVo> page1 = new ListPageUtil(snbVos,bean.getPageno(), bean.getPagesize());
//                            page.setCount(page1.getTotalCount());
//                            page.setData(page1.getPagedList());
                        }else if(list.size()>1){
                            logger.error("有多个参保信息");
                        }
                        return result(result,message,snbVos);
                    }
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result(result, message);
    }

    /**
     * 省农保-缴费信息查询
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "/queryJFXX", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result queryJFXX(@RequestBody SecQueryBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "查询成功";
        String  method =Constants.HBNBJFXXtaskOId;
//        Page<SNBVo> page = new Page<>(bean.getPageno(),bean.getPagesize());
        try {
//            long grbh=getJbGrbh(bean.getName(),bean.getSfzh());
//            if (grbh ==0){
//                message="没有查询到数据";
//                result = Constants.RESULT_MESSAGE_ERROR;
//            }else {
                String wsResult=getHisKS(Long.parseLong(bean.getAac001()),method);
                if( wsResult != null && wsResult != "" ){
                    wsResult= wsResult.replace("<?xml version=\"1.0\" encoding=\"GBK\" standalone='yes'?>", "");
                    XMLSerializer xmlSerializer = new XMLSerializer();
                    String resutStr = xmlSerializer.read(wsResult).toString();
                    JSONObject json = JSONObject.fromObject(resutStr);
                    String code = (String) JSONObject.fromObject(json.get("head")).get("code");
                    if (code.equals("0")){
                        message="没有查询到数据";
                        result = Constants.RESULT_MESSAGE_ERROR;
                    }else if(code.equals("1")){
                        JSONObject object = JSONObject.fromObject(json.get("body"));
                        List<SNBVo> snbVos= new ArrayList<>();
                        JSONArray list = JSONArray.fromObject(object.get("list")) ;
                        if(list.size()>0){
                            for (int i = 0; i <list.size() ; i++) {
                                JSONObject object1= JSONObject.fromObject( list.get(i));
                                SNBVo snbVo =new SNBVo();
                                snbVo.setAAC001(object1.get("personId").toString());
                                snbVo.setAAE001(object1.get("year").toString());
                                snbVo.setAAC002(object1.get("idNumber").toString());
                                snbVo.setAAA115(object1.get("paymentType").toString());
                                snbVo.setAAE341(object1.get("financeType").toString());
                                snbVo.setGRJF(object1.get("personPayment").toString());
                                snbVo.setJTBZ(object1.get("collectiveSubsidy").toString());
                                snbVo.setQTZZ(object1.get("otherSubsidy").toString());
                                snbVo.setZFDJ(object1.get("governmentPayment").toString());
                                snbVo.setAAE025(object1.get("provincialSubsidy").toString());
                                snbVo.setAAE026(object1.get("municipalSubsidy").toString());
                                snbVo.setAAE027(object1.get("countySubsidy").toString());
                                snbVos.add(snbVo);
                            }
//                            ListPageUtil<SNBVo> page1 = new ListPageUtil(snbVos,bean.getPageno(), bean.getPagesize());
//                            page.setCount(snbVos.size());
//                            page.setData(snbVos);
                        }
                        return result(result,message,snbVos);
                    }
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result(result, message);
    }

    /**
     * 省农保-个人年账户信息查询  (需要修改)
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "/queryNZH", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result queryNZH(@RequestBody SecQueryBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "查询成功";
        String  method =Constants.HBNBNZHtaskOId;
        try {
//            long grbh=getJbGrbh(bean.getName(),bean.getSfzh());
//            if (grbh ==0){
//                message="没有查询到数据";
//                result = Constants.RESULT_MESSAGE_ERROR;
//            }else {
                String wsResult=getHisKS(Long.parseLong(bean.getAac001()),method);
                if( wsResult != null && wsResult != "" ){
                    wsResult= wsResult.replace("<?xml version=\"1.0\" encoding=\"GBK\" standalone='yes'?>", "");
                    XMLSerializer xmlSerializer = new XMLSerializer();
                    String resutStr = xmlSerializer.read(wsResult).toString();
                    JSONObject json = JSONObject.fromObject(resutStr);
                    String code = (String) JSONObject.fromObject(json.get("head")).get("code");
                    if (code.equals("0")){
                        message="没有查询到数据";
                        result = Constants.RESULT_MESSAGE_ERROR;
                    }else if(code.equals("1")){
                        JSONObject object = JSONObject.fromObject(json.get("body"));
                        List<SNBVo> snbVos= new ArrayList<>();

                        JSONArray list = JSONArray.fromObject(object.get("list")) ;
                        if(list.size()>0){
                            for (int i = 0; i <list.size() ; i++) {
                                JSONObject object1= JSONObject.fromObject( list.get(i));
                                SNBVo snbVo =new SNBVo();
                                snbVo.setAAC001(object1.get("personId").toString());
                                snbVo.setAAE001(object1.get("year").toString());
                                snbVo.setAAC002(object1.get("idNumber").toString());
                                snbVo.setAAA115(object1.get("paymentType").toString());
                                snbVo.setAAE341(object1.get("financeType").toString());
                                snbVo.setGRJF(object1.get("personPayment").toString());
                                snbVo.setJTBZ(object1.get("collectiveSubsidy").toString());
                                snbVo.setQTZZ(object1.get("otherSubsidy").toString());
                                snbVo.setZFBT(object1.get("governmentPayment").toString());
                                snbVo.setAAE025(object1.get("provincialSubsidy").toString());
                                snbVo.setAAE026(object1.get("municipalSubsidy").toString());
                                snbVo.setAAE027(object1.get("countySubsidy").toString());
                                snbVos.add(snbVo);
                            }
                        }
                        return result(result,message,snbVos);
                    }
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result(result, message);
    }

    /**
     * 省农保-待遇支付查询
     *
     * @param bean
     * @return
     */
    @RequestMapping(value = "/queryDYBFXX", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result queryDYBFXX(@RequestBody SecQueryBean bean) {
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "查询成功";
        String  method =Constants.HBNBDYBFXXtaskOId;
//        Page<SNBVo> page = new Page<>(bean.getPageno(),bean.getPagesize());
        try {
//            long grbh=getJbGrbh(bean.getName(),bean.getSfzh());
//            if (grbh ==0){
//                message="没有查询到数据";
//                result = Constants.RESULT_MESSAGE_ERROR;
//            }else {
                String wsResult=getHisKS(Long.parseLong(bean.getAac001()),method);
                if( wsResult != null && wsResult != "" ){
                    wsResult= wsResult.replace("<?xml version=\"1.0\" encoding=\"GBK\" standalone='yes'?>", "");
                    XMLSerializer xmlSerializer = new XMLSerializer();
                    String resutStr = xmlSerializer.read(wsResult).toString();
                    JSONObject json = JSONObject.fromObject(resutStr);
                    String code = (String) JSONObject.fromObject(json.get("head")).get("code");
                    if (code.equals("0")){
                        message="没有查询到数据";
                        result = Constants.RESULT_MESSAGE_ERROR;
                    }else if(code.equals("1")){
                        JSONObject object = JSONObject.fromObject(json.get("body"));
                        List<SNBVo> snbVos= new ArrayList<>();

                        JSONArray list = JSONArray.fromObject(object.get("list")) ;
                        if(list.size()>0){
                            for (int i = 0; i <list.size() ; i++) {
                                JSONObject object1= JSONObject.fromObject( list.get(i));
                                SNBVo snbVo =new SNBVo();
                                if(object1.get("personId")!="" && object1.get("personId")!=null){
                                    snbVo.setAAC001(object1.get("personId").toString());
                                }
                                if(object1.get("grantTime")!="" && object1.get("grantTime")!=null){
                                    snbVo.setAAE002(object1.get("grantTime").toString());
                                }
                                if(object1.get("basicPension")!="" && object1.get("basicPension")!=null){
                                    snbVo.setJC(object1.get("basicPension").toString());
                                }
                                if(object1.get("accountPension")!="" && object1.get("accountPension")!=null){
                                    snbVo.setZH(object1.get("accountPension").toString());
                                }
//                                snbVo.setAAE341(object1.get("yearPension").toString());
                                if(object1.get("reissue")!="" && object1.get("reissue")!=null){
                                    snbVo.setBF(object1.get("reissue").toString());
                                }
                                if(object1.get("Retreat")!="" && object1.get("Retreat")!=null){
                                    snbVo.setTF(object1.get("Retreat").toString());
                                }
                                if(object1.get("sumMonth")!="" && object1.get("sumMonth")!=null){
                                    snbVo.setAAE019(object1.get("sumMonth").toString());
                                }
                                snbVos.add(snbVo);
                            }
//                            ListPageUtil<SNBVo> page1 = new ListPageUtil(snbVos,bean.getPageno(), bean.getPagesize());
//                            page.setCount(page1.getTotalCount());
//                            page.setData(page1.getPagedList());
                        }
                        return result(result,message,snbVos);
                    }
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result(result, message);
    }
//    public Map<String, String> getHisKS1(long aac001) throws UnsupportedEncodingException {
//        Service service = new Service();
//        Call call;
//        String wsResult = "";
//        String  method =Constants.HBNBBGXXtaskOId;
//        String sign= getSign(method,aac001);
//        Map<String , String > map = new HashMap<String , String>();
//        try {
//            call = (Call) service.createCall();
//            call.setTargetEndpointAddress(Constants.HBNBSERVEICURL + sign );
//            call.setOperationName(new QName(SOAPACTION, "funcInterface"));//设置要调用哪个方法
//            call.addParameter( new QName(SOAPACTION, "parameters"),XMLType.XSD_STRING, ParameterMode.IN);//设置传入参数的属性
//            call.setReturnType(XMLType.XSD_STRING);//要返回的数据类型（自定义类型）
//            call.setTimeout(30000);
//            wsResult = (String) call.invoke(new Object[] { getXml(method,aac001) }); //调用方法并传递参数
//        } catch (Exception e) {
//            logger.error("---获取【航天医院】门诊科室webservice接口失败---" , e.getCause());
//        }
//        map.put("wsResult", wsResult);
//        return map;
//    }
//
//    public Map<String, String> getHisKS2(long aac001) throws UnsupportedEncodingException {
//        Service service = new Service();
//        Call call;
//        String wsResult = "";
//        String  method =Constants.HBNBJFXXtaskOId;
//        String sign= getSign(method,aac001);
//        Map<String , String > map = new HashMap<String , String>();
//        try {
//            call = (Call) service.createCall();
//            call.setTargetEndpointAddress(Constants.HBNBSERVEICURL + sign );
//            call.setOperationName(new QName(SOAPACTION, "funcInterface"));//设置要调用哪个方法
//            call.addParameter( new QName(SOAPACTION, "parameters"),XMLType.XSD_STRING, ParameterMode.IN);//设置传入参数的属性
//            call.setReturnType(XMLType.XSD_STRING);//要返回的数据类型（自定义类型）
//            call.setTimeout(30000);
//            wsResult = (String) call.invoke(new Object[] { getXml(method,aac001) }); //调用方法并传递参数
//        } catch (Exception e) {
//            logger.error("---获取【航天医院】门诊科室webservice接口失败---" , e.getCause());
//        }
//        map.put("wsResult", wsResult);
//        return map;
//    }
//
//    public Map<String, String> getHisKS3(long aac001) throws UnsupportedEncodingException {
//        Service service = new Service();
//        Call call;
//        String wsResult = "";
//        String  method =Constants.HBNBNZHtaskOId;
//        String sign= getSign(method,aac001);
//        Map<String , String > map = new HashMap<String , String>();
//        try {
//            call = (Call) service.createCall();
//            call.setTargetEndpointAddress(Constants.HBNBSERVEICURL + sign );
//            call.setOperationName(new QName(SOAPACTION, "funcInterface"));//设置要调用哪个方法
//            call.addParameter( new QName(SOAPACTION, "parameters"),XMLType.XSD_STRING, ParameterMode.IN);//设置传入参数的属性
//            call.setReturnType(XMLType.XSD_STRING);//要返回的数据类型（自定义类型）
//            call.setTimeout(30000);
//            wsResult = (String) call.invoke(new Object[] { getXml(method,aac001) }); //调用方法并传递参数
//        } catch (Exception e) {
//            logger.error("---获取【航天医院】门诊科室webservice接口失败---" , e.getCause());
//        }
//        map.put("wsResult", wsResult);
//        return map;
//    }
//
//    public Map<String, String> getHisKS4(long aac001) throws UnsupportedEncodingException {
//        Service service = new Service();
//        Call call;
//        String wsResult = "";
//        String  method =Constants.HBNBDYBFXXtaskOId;
//        String sign= getSign(method,aac001);
//        Map<String , String > map = new HashMap<String , String>();
//        try {
//            call = (Call) service.createCall();
//            call.setTargetEndpointAddress(Constants.HBNBSERVEICURL + sign );
//            call.setOperationName(new QName(SOAPACTION, "funcInterface"));//设置要调用哪个方法
//            call.addParameter( new QName(SOAPACTION, "parameters"),XMLType.XSD_STRING, ParameterMode.IN);//设置传入参数的属性
//            call.setReturnType(XMLType.XSD_STRING);//要返回的数据类型（自定义类型）
//            call.setTimeout(30000);
//            wsResult = (String) call.invoke(new Object[] { getXml(method,aac001) }); //调用方法并传递参数
//        } catch (Exception e) {
//            logger.error("---获取【航天医院】门诊科室webservice接口失败---" , e.getCause());
//        }
//        map.put("wsResult", wsResult);
//        return map;
//    }

}
