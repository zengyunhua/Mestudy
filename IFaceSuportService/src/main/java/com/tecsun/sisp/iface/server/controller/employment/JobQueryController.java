package com.tecsun.sisp.iface.server.controller.employment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.TransBean;
import com.tecsun.sisp.iface.common.vo.employment.param.job.AddIdCardInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.AddJobDirectionVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.AddJobSeekerInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.AddViewInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.DropJobDirectionVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetAllDicVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetAllSecondDicVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetCompanyByZphVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetCompanyInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetPdfVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetPolicyDocumentsVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetQzyxByQzzVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetZphInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.IsPhoneNumExistVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.JobBasicVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.JobOfferInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.LookPictureeVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.NoNullRegisteVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.PersonJobVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.PersonUserLoginVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.QueryCompanyVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.QueryJobFairVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.QueryJobOfferVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.QueryPolicyDocumentVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.QuerySalaryguidanceVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.RegisterByIdCardVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.SavePictureVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.SendJobChoiceVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.SmsSendCodeVo;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.model.service.NetUserServiceImpl;
import com.tecsun.sisp.iface.server.model.service.employment.JobService;
import com.tecsun.sisp.iface.server.util.Config;
import com.tecsun.sisp.iface.server.util.JobUtil;
import com.tecsun.sisp.iface.server.util.ThreadPoolTask;
import com.tecsun.sisp.iface.server.util.ThreadPoolUtil;

/**
 * 这个类中的方法来自 《孝感自助一体机前后端交互接口.doc》 接口文档
 * 
 */

@Controller
@RequestMapping(value = "/iface/job")
public class JobQueryController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(JobQueryController.class);
	private static String PUBLIC_JOB_URL = Config.getInstance().get("public_job_url");//公共就业服务
	
	@Autowired
	private JobService jobServiceImpl;
    @Autowired
    private NetUserServiceImpl netUserService;

	/***********************************************招聘岗位*******开始***************************************************/
	
	/**
	 * 获得招聘岗位列表
	 * 
	 */
	@RequestMapping(value = "/getJobOffer", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getJobOffer(@RequestBody QueryJobOfferVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getJobOffer"));
		Result rs = JobUtil.checkParamIsEmpty(param);
		if(Constants.RESULT_MESSAGE_ERROR.equals(rs.getStatusCode())){
			return rs;
		}
		return jobServiceImpl.getJobOffer(PUBLIC_JOB_URL+"/JobOffer/Query",param);
	}
	
	/**
	 * 查看指定招聘岗位信息
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/getJobOfferInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getJobOfferInfo(@RequestBody JobOfferInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getJobOfferInfo"));
		if(StringUtils.isBlank(param.getGwid())){
			return error("岗位ID不能为空", null);
		}
		return jobServiceImpl.getJobOfferInfo(PUBLIC_JOB_URL + "/JobOffer/GetJobOfferInfo",param);
	}
	
	/**
	 * 获得求职者投递的岗位信息
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/getPersonJob", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getPersonJob(@RequestBody PersonJobVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getPersonJob"));
		Result rs = JobUtil.checkParamIsEmpty(param);
		if(Constants.RESULT_MESSAGE_ERROR.equals(rs.getStatusCode())){
			return rs;
		}
		if(StringUtils.isBlank(param.getGrxxid())){
			return error("个人信息ID不能为空", null);
		}
		return jobServiceImpl.getPersonJob(PUBLIC_JOB_URL + "/JobOffer/GetPersonJob",param);
	}
	
	/***********************************************招聘岗位*******结束***************************************************/
	
	/**
	 * 求职者注册流程：12.1刷身份证添加身份证信息 ---> 2.1添加求职者信息 ---> 5.2新建求职志愿
	 */
	
	/**
	 * 添加求职者信息
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/addJobSeekerInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result addJobSeekerInfo(@RequestBody AddJobSeekerInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("addJobSeekerInfo"));
        if(StringUtils.isBlank(param.getPicBase64Str())){
            return error("base64字符串（picBase64Str），不能为空 ", null);
		}
		if(StringUtils.isBlank(param.getZjlxid())||StringUtils.isBlank(param.getZjhm())||StringUtils.isBlank(param.getSblx())){
			return error("证件类型ID(字典表的ID)、证件号码、设备类型(3:自助一体机)，不能为空", null);
		}
		Result result_err = checkRegistVoIsEmpty(param);
		if(Constants.RESULT_MESSAGE_ERROR.equals(result_err.getStatusCode())){
			return result_err;
		}
        Result result=jobServiceImpl.addJobSeekerInfo(PUBLIC_JOB_URL, param);
        if(Constants.RESULT_MESSAGE_SUCCESS.equals(result.getStatusCode())){
            Map map = (Map) result.getData();
            param.setQzzid(String.valueOf(map.get("ID")));
            try {
                netUserService.registerBySISP(param);
            } catch (Exception e) {
                logger.error(param.getSfzh()+" 信息未插入数据库"+e.getMessage());
            }
        }
        return result;
	}
	
	
	 //校验本地数据库注册信息必填项
	public Result checkRegistVoIsEmpty(AddJobSeekerInfoVo param) {
		NoNullRegisteVo registeVo = new NoNullRegisteVo();
		BeanUtils.copyProperties(param, registeVo);
		StringBuffer sb = new StringBuffer();
		boolean ok = true;
		for (Field f : registeVo.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				if (f.get(registeVo) == null) {
					sb.append(f.getName() + ",");
					ok = false;
				}
			} catch (Exception e) {
			}
		}
		if (ok) {
			return ok("ok", null);
		}
		return error(sb.toString() + "不能为空", null);
	}
	
	/**
	 * 编辑求职者信息
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/modifyJobSeekerInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result modifyJobSeekerInfo(@RequestBody AddJobSeekerInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("modifyJobSeekerInfo"));
		if(StringUtils.isBlank(param.getQzzid())){
			return error("求职者ID不能为空", null);
		}
		if(StringUtils.isBlank(param.getPicBase64Str())){
			return error("base64字符串（picBase64Str），不能为空 ", null);
		}
        Result result_err = checkRegistVoIsEmpty(param);
        if(Constants.RESULT_MESSAGE_ERROR.equals(result_err.getStatusCode())){
            return result_err;
        }
		Result result = jobServiceImpl.modifyJobSeekerInfo(PUBLIC_JOB_URL, param); 
        try {//如果本地数据库有该用户信息则修改，若无则添加
            if(netUserService.getRegistrationInfo(param)!=null){
                netUserService.updateRegistrationInfo(param);
            }else {
            	netUserService.registerBySISP(param);
            }
        } catch (Exception e) {
            logger.error(param.getSfzh()+" 信息操作数据库失败" +e.getMessage());
        }
        return result;
	}
	
	/**
	 * 求职者登录
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/personUserLogin", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result personUserLogin(@RequestBody PersonUserLoginVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("personUserLogin"));
		if(StringUtils.isBlank(param.getDllx())||StringUtils.isBlank(param.getZjhm())){
			return error("证件号码、登录类型(1:账号  2：证件号  3：手机号码)，不能为空", null);
		}
		return jobServiceImpl.personUserLogin(PUBLIC_JOB_URL+ "/Login/PersonUserLogin", param);
	}
	
	/**
	 * 判断求职者手机号码是否已存在
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/isPhoneNumExist", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result isPhoneNumExist(@RequestBody IsPhoneNumExistVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("isPhoneNumExist"));
		if(StringUtils.isBlank(param.getSjhm())){
			return error("求职者手机号码不能为空", null);
		}
		return jobServiceImpl.isPhoneNumExist(PUBLIC_JOB_URL+ "/JobSeeker/IsPhoneNumExist", param);
	}
	
	/***********************************************字典表*******开始***************************************************/
	
	/**
	 * 第一字典表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/getAllDic", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getAllDic(@RequestBody GetAllDicVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getAllDic"));
		if(StringUtils.isBlank(param.getKey())){
			return error("关键字不能为空", null);
		}
		return jobServiceImpl.getAllDic(PUBLIC_JOB_URL+ "/Dictionarys/GetAllDic", param);
	}
	
	/**
	 * 第二字典表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping(value = "/getAllSecondDic", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getAllSecondDic(@RequestBody GetAllSecondDicVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getAllSecondDic"));
		if(StringUtils.isBlank(param.getKey())){
			return error("关键字不能为空", null);
		}
		return jobServiceImpl.getAllSecondDic(PUBLIC_JOB_URL+ "/SecondDictionarys/GetAllSecondDic", param);
	}
	

	
	/***********************************************招聘会*******开始***************************************************/
	
	/**
	 * 获得招聘会列表
	 */
	@RequestMapping(value = "/queryJobFairs", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryJobFairs(@RequestBody QueryJobFairVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("queryJobFairs"));
		Result rs = JobUtil.checkParamIsEmpty(param);
		if(Constants.RESULT_MESSAGE_ERROR.equals(rs.getStatusCode())){
			return rs;
		}
		return jobServiceImpl.queryJobFairs(PUBLIC_JOB_URL+ "/JobFair/Query", param);
	}
	
	/**
	 * 查看指定招聘会信息
	 */
	@RequestMapping(value = "/getZphInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getZphInfo(@RequestBody GetZphInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getZphInfo"));
		if(StringUtils.isBlank(param.getZphid())){
			return error("招聘会ID不能为空", null);
		}
		return jobServiceImpl.getZphInfo(PUBLIC_JOB_URL+ "/JobFair/GetZphInfo", param);
	}
	/***********************************************招聘会*******结束***************************************************/
	
	
	/**
	 * 获得企业列表
	 */
	@RequestMapping(value = "/queryCompanys", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryCompanys(@RequestBody QueryCompanyVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("queryCompanys"));
		Result rs = JobUtil.checkParamIsEmpty(param);
		if(Constants.RESULT_MESSAGE_ERROR.equals(rs.getStatusCode())){
			return rs;
		}
		return jobServiceImpl.queryCompanys(PUBLIC_JOB_URL+ "/Company/Query", param);
	}

	/**
	 * 查看指定企业信息
	 */
	@RequestMapping(value = "/getCompanyInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getCompanyInfo(@RequestBody GetCompanyInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getCompanyInfo"));
		if(StringUtils.isBlank(param.getQyid())){
			return error("企业ID,不能为空", null);
		}
		return jobServiceImpl.getCompanyInfo(PUBLIC_JOB_URL+ "/Company/GetCompanyInfo", param);
	}
	
	/**
	 * 获得企业的图片及视频信息
	 */
	@RequestMapping(value = "/getCompanyImgAndVideo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getCompanyImgAndVideo(@RequestBody GetCompanyInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getCompanyImgAndVideo"));
		if(StringUtils.isBlank(param.getQyid())){
			return error("企业ID,不能为空", null);
		}
		return jobServiceImpl.getCompanyInfo(PUBLIC_JOB_URL+ "/Company/GetImgAndVideo", param);
	}

	/**
	 * 获得求职者的求职志愿列表
	 */
	@RequestMapping(value = "/getQzyxByQzz", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getQzyxByQzz(@RequestBody GetQzyxByQzzVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getQzyxByQzz"));
		Result rs = JobUtil.checkParamIsEmpty(param);
		if(Constants.RESULT_MESSAGE_ERROR.equals(rs.getStatusCode())){
			return rs;
		}
		if(StringUtils.isBlank(param.getQzzid())){
			return error("求职者ID,不能为空", null);
		}
		return jobServiceImpl.getQzyxByQzz(PUBLIC_JOB_URL+ "/JobDirection/GetQzyxByQzz", param);
	}

	/**
	 * 新建求职志愿
	 */
	@RequestMapping(value = "/addJobDirection", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result addJobDirection(@RequestBody AddJobDirectionVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("addJobDirection"));
		return jobServiceImpl.addJobDirection(PUBLIC_JOB_URL+ "/JobDirection/Add", param);
	}
	
	/**
	 * 删除求职志愿
	 */
	@RequestMapping(value = "/dropJobDirection", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result dropJobDirection(@RequestBody DropJobDirectionVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("dropJobDirection"));
		if(StringUtils.isBlank(param.getIds())){
			return error("求职志愿列表ID，不能为空", null);
		}
		return jobServiceImpl.dropJobDirection(PUBLIC_JOB_URL+ "/JobDirection/Drop", param);
	}

	/**
	 * 根据招聘会获得参会企业信息
	 */
	@RequestMapping(value = "/getCompanyByZph", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getCompanyByZph(@RequestBody GetCompanyByZphVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getCompanyByZph"));
		Result rs = JobUtil.checkParamIsEmpty(param);
		if(Constants.RESULT_MESSAGE_ERROR.equals(rs.getStatusCode())){
			return rs;
		}
		if(StringUtils.isBlank(param.getZphid())){
			return error("招聘会ID,不能为空", null);
		}
		return jobServiceImpl.getCompanyByZph(PUBLIC_JOB_URL+ "/JoinMeetingCompany/GetCompanyByZph", param);
	}
	
	
	/**
	 * 添加图片<p>
	 */
	@RequestMapping(value = "/savePicture", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result savePicture(@RequestBody SavePictureVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("savePicture"));
		if(StringUtils.isBlank(param.getPicBase64Str())){
			return error("base64字符串（picBase64Str），不能为空 ", null);
		}
		return jobServiceImpl.savePicture(PUBLIC_JOB_URL+ "/Picture/SavePicture", param);
	}
	
	
	/**
	 * 查看照片<p>
	 */
	@RequestMapping(value = "/lookPicture", method = RequestMethod.POST)
	@ResponseBody
	public Result lookPicture(@RequestBody LookPictureeVo param, HttpServletResponse response) {
		insertTransLog(param,Constants.BUSINESSCODE.get("lookPicture"));
		if(StringUtils.isBlank(param.getTpdz())||StringUtils.isBlank(param.getTplx())){
			return error("图片地址、图片类型（1：一般图片，2：企业图片），不能为空 ", null);
		}
		return jobServiceImpl.lookPicture(PUBLIC_JOB_URL+ "/Picture/LookPicture", param);
//		writeOutputSream(is,response);
//		return ok("照片获取成功", null);
	}

	
	/**
	 * 公用方法，写文件流到客户端（德生宝、浏览器）
	 * 
	 */
	private void writeOutputSream(InputStream is, HttpServletResponse response) {
		OutputStream os = null;
        BufferedInputStream bis = null;
		
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(String.valueOf(Math.random()*1000000+1)));
			
			bis = new BufferedInputStream(is);
			os = new BufferedOutputStream(response.getOutputStream());
			
			int len;
            byte[] bytes = new byte[10240];
            while (-1 != (len = bis.read(bytes))) {
                os.write(bytes, 0, len);
            }
            os.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			 try {
                 if (os != null) {
                     os.close();
                 }
                 if (bis != null) {
                     bis.close();
                 }
             } catch (Exception e) {
             }
		}
	}
	
	//输出PDF
	private void writePdf(InputStream is, HttpServletResponse response) {
		OutputStream os = null;
		BufferedInputStream bis = null;
		try {
			response.setContentType("application/pdf");
			bis = new BufferedInputStream(is);
			os = new BufferedOutputStream(response.getOutputStream());
			int len;
			byte[] bytes = new byte[10240];
			while (-1 != (len = bis.read(bytes))) {
				os.write(bytes, 0, len);
			}
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if (os != null) {
					os.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (Exception e) {
			}
		}
	}
	
	/**
	 * 获得所有的专业
	 */
	@RequestMapping(value = "/getAllMajor", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getAllMajor(@RequestBody GetCompanyByZphVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getAllMajor"));
		return jobServiceImpl.getAllMajor(PUBLIC_JOB_URL+ "/Major/GetAllMajor", param);
	}
	
	
	/**
	 * 投递岗位
	 */
	@RequestMapping(value = "/sendJobChoice", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result sendJobChoice(@RequestBody SendJobChoiceVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("sendJobChoice"));
		if(StringUtils.isBlank(param.getGrxxid())||StringUtils.isBlank(param.getQyids())||StringUtils.isBlank(param.getGwids())){
			return error("个人信息ID、企业ID、岗位ID，皆不能为空", null);
		}
		return jobServiceImpl.sendJobChoice(PUBLIC_JOB_URL+ "/SendJobDirection/SendJobChoice", param);
	}
	
	/**
	 * 刷身份证添加身份证信息
	 */
	@RequestMapping(value = "/addIdCardInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result addIdCardInfo(@RequestBody AddIdCardInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("addIdCardInfo"));
		return jobServiceImpl.addIdCardInfo(PUBLIC_JOB_URL+ "/IdCard/Add", param);
	}
	
	/**
	 * 查看政策文件
	 * <p>
	 * 此接口，不可用	2017-01-05
	 */
	@RequestMapping(value = "/getPolicyDocuments", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getPolicyDocuments(@RequestBody GetPolicyDocumentsVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getPolicyDocuments"));
		if(StringUtils.isBlank(param.getId())||StringUtils.isBlank(param.getPtlx())){
			return error("政策文件ID、平台类型(1：查询平台)，不能为空", null);
		}
		return jobServiceImpl.getPolicyDocuments(PUBLIC_JOB_URL+ "/PolicyDocuments/GetPolicyDocuments", param);
	}
	
	/**
	 * 获取办事流程信息
	 * <p>
	 * 这个方法使用了 getPolicyDocuments 方法的 Vo 和 service 方法 
	 */
	@RequestMapping(value = "/getJobLineInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getJobLineInfo(@RequestBody GetPolicyDocumentsVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("getJobLineInfo"));
		if(StringUtils.isBlank(param.getId())||StringUtils.isBlank(param.getPtlx())){
			return error("政策文件ID、平台类型(1：查询平台)，不能为空", null);
		}
		return jobServiceImpl.getPolicyDocuments(PUBLIC_JOB_URL+ "/JobLine/GetJobLineInfo", param);
	}

	/**
	 * 短信验证
	 */
	@RequestMapping(value = "/smsSendCode", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result smsSendCode(@RequestBody SmsSendCodeVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("smsSendCode"));
		if(StringUtils.isBlank(param.getSjhm())||StringUtils.isBlank(param.getYhlx())||StringUtils.isBlank(param.getYzlx())){
			return error("手机号码、用户类型（1.求职者2.企业用户）、验证类型（1.登录验证，2.注册验证），不能为空", null);
		}
		return jobServiceImpl.smsSendCode(PUBLIC_JOB_URL+ "/MsgValidate/SendCode", param);
	}
	
	
	/**
	 * 刷身份证注册
	 */
	@RequestMapping(value = "/registerByIdCard", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result registerByIdCard(@RequestBody RegisterByIdCardVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("registerByIdCard"));
		return jobServiceImpl.registerByIdCard(PUBLIC_JOB_URL+ "/Register/RegisterByIdCard", param);
	}
	
	/**
	 * 获取政策法规列表
	 */
	@RequestMapping(value = "/queryPolicyDocument", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryPolicyDocument(@RequestBody QueryPolicyDocumentVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("queryPolicyDocument"));
		return jobServiceImpl.queryPolicyDocument(PUBLIC_JOB_URL+ "/PolicyDocument/Query", param);
	}
	
	/**
	 * 获取机构介绍列表
	 */
	@RequestMapping(value = "/queryAgencyIntroduction", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryAgencyIntroduction(@RequestBody JobBasicVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("queryAgencyIntroduction"));
		return jobServiceImpl.queryAgencyIntroduction(PUBLIC_JOB_URL+ "/AgencyIntroduction/Query", param);
	}
	
	/**
	 * 获取工资指导价列表
	 */
	@RequestMapping(value = "/querySalaryguidance", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result querySalaryguidance(@RequestBody QuerySalaryguidanceVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("querySalaryguidance"));
		if(StringUtils.isBlank(param.getWjlx())){
			return error("工资指导价文件类型 (1.职业供求分析 2.最新工资价位)，不能为空", null);
		}
		return jobServiceImpl.querySalaryguidance(PUBLIC_JOB_URL+ "/Salaryguidance/Query", param);
	}
	
	
	/**
	 * 获取pdf文件
	 */
	@RequestMapping(value = "/getPdf", method = RequestMethod.GET)
	@ResponseBody
	public Result getPdf(HttpServletRequest req, HttpServletResponse response) {
		String wjlj = req.getParameter("WJLJ");
		String channelcode = req.getParameter("channelcode");
		if(StringUtils.isBlank(wjlj) || StringUtils.isBlank(channelcode)){
			return error("文件路径、渠道编码，不能为空", null);
		}
		GetPdfVo param = new GetPdfVo();
		param.setWjlj(wjlj);
		param.setChannelcode(channelcode);
		insertTransLog(param,Constants.BUSINESSCODE.get("getPdf"));
		
		try {
			InputStream in = jobServiceImpl.getPdf(PUBLIC_JOB_URL+ "/PdfOperation/GetPdf", param);
			writePdf(in,response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("抱歉，获取PDF出错了",e.getMessage());
			return error("抱歉，获取PDF出错了", null);
		}
        return ok("ok", null);
	}
	
	/**
	 * 记录访问信息
	 */
	@RequestMapping(value = "/addViewInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result addViewInfo(@RequestBody AddViewInfoVo param) {
		insertTransLog(param,Constants.BUSINESSCODE.get("addViewInfo"));
		if(StringUtils.isBlank(param.getSblx())||StringUtils.isBlank(param.getYhlx())||StringUtils.isBlank(param.getMknr())){
			return error("设备类型 (3:自助一体机)、模块内容、用户类型（ 1.求职者 2.企业 3.游客），不能为空", null);
		}
		return jobServiceImpl.addViewInfo(PUBLIC_JOB_URL+ "/ViewInfo/Add", param);
	}

	public void insertTransLog(JobBasicVo bean, String funcName) {
		try {
			TransBean transBean = new TransBean();
			transBean.setChannelcode(bean.getChannelcode());
			transBean.setBusinesscode(funcName);
			transBean.setDeviceid(bean.getDeviceid());
			ThreadPoolUtil.getThreadPool().execute(
					new ThreadPoolTask(transBean));
		} catch (Exception e) {
			logger.error("添加卡业务操作记录到业务分析子系统出错：" + e.getMessage());
		}
	}
}
