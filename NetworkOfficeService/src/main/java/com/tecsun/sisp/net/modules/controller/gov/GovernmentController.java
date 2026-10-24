package com.tecsun.sisp.net.modules.controller.gov;

import com.tecsun.sisp.net.common.Config;
import com.tecsun.sisp.net.common.Page;
import com.tecsun.sisp.net.common.vo.faceverify.Result;
import com.tecsun.sisp.net.modules.common.FileFromUrlUtil;
import com.tecsun.sisp.net.modules.controller.BaseController;
import com.tecsun.sisp.net.modules.entity.request.DictBean;
import com.tecsun.sisp.net.modules.entity.request.GovernmentBean;
import com.tecsun.sisp.net.modules.entity.request.NoticeBean;
import com.tecsun.sisp.net.modules.entity.response.*;
import com.tecsun.sisp.net.modules.service.impl.CommonService;
import com.tecsun.sisp.net.modules.service.impl.GovernmentService;

import com.tecsun.sisp.net.common.Constants;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * @version 创建时间：2017年4月21日 上午9:58:55
 * 类说明
 * 政务系统：新闻，通知，动态管理
 */
@Controller
@RequestMapping(value = "/adapter/notice")
public class GovernmentController  extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(GovernmentController.class);
	private static final String SBDT_FILEPATH = Config.getInstance().get("sbdt.file_savepath");
	@Autowired
	private GovernmentService governmentService;
	@Autowired
	private CommonService commonService;

	/**
	 * 查询列表信息
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getNoticeList", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getNoticeList(@RequestBody GovernmentBean bean) throws Exception{
		//只传二级编码，不传一级编码
		if(bean.getCode() != "" && bean.getGroupId() == "" ){
			return resultVerify(Constants.RESULT_MESSAGE_PARAM, "请输入一级编码","");
		}
		String statusCode = Constants.RESULT_MESSAGE_ERROR;
		String message = "操作失败";
		try {
			Page<GovernmentVO> list = governmentService.getNoticeList4Cssp(bean);
			if (list.getData() != null && list.getData().size()>0) {
				message = "操作成功";
			}else{
				message = "暂无数据";
			}
			statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			return resultVerify(statusCode, message, list);
		} catch (Exception e) {
			logger.error("查询列表出错：", e);
			statusCode = Constants.RESULT_MESSAGE_ERROR;
		}

		return resultVerify(statusCode, message, null);
	}
//
//	/**
//	 * 查询详情
//	 * @param bean
//	 * @return
//	 */
//	@RequestMapping(value = "/getGovernmentDetail", method = RequestMethod.POST, produces = "application/json")
//	@ResponseBody
//	public Result getGovernmentDetail(@RequestBody NoticeBean bean) {
//		String statusCode  = Constants.RESULT_MESSAGE_ERROR;
//		String message = "操作失败";
//		NoticeDetailVO detailVo=null;
//		// 检验新闻id是否为空
//		if (bean.getId()==0) return new Result(Constants.RESULT_MESSAGE_PARAM, "参数有误", "");
//		try {
//			detailVo = governmentService.getNoticeDetailById4Cssp(bean);
//			statusCode = Constants.RESULT_MESSAGE_SUCCESS;
//			if(detailVo!=null) {
//				message = "操作成功";
//			}else{
//				message = "暂无数据";
//			}
//		} catch (Exception e) {
//			logger.error("查看社保动态详情异常", e);
//		}
//		return new Result(statusCode,message,detailVo);
//	}

	//查询数据字典
	@RequestMapping(value = "/getAllDictionary", method = RequestMethod.POST,produces = "application/json")
	@ResponseBody
	public Result getAllDictionary(@RequestBody DictBean bean) throws Exception {
		if (bean.getGroupId() == null || bean.getGroupId() == "") {
			return resultVerify(Constants.RESULT_MESSAGE_PARAM, "请输入组名","");
		}
		String statusCode = Constants.RESULT_MESSAGE_ERROR;
		String message = "操作失败";
		try {
			List<DictVO> list = commonService.getDictionaryById4Sisp(bean.getGroupId());
			if (list != null && list.size() > 0) {
				statusCode = Constants.RESULT_MESSAGE_SUCCESS;
				message = "查询成功";
				return resultVerify(statusCode, message, list);
			} else message = "暂无该数据字典";
		} catch (Exception e) {
			logger.error("获取字典组数据出错：" + e.getMessage());
		}
		return resultVerify(statusCode, message,"");
	}

	/**
	 * 查询详情
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = "/getGovernmentDetail", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getGovernmentDetail(@RequestBody NoticeBean bean) {
		String statusCode  = Constants.RESULT_MESSAGE_ERROR;
		String message = "操作失败";
		NoticeDetailVO detailVo=null;
		// 检验新闻id是否为空
		if (bean.getId()==0) return resultVerify(Constants.RESULT_MESSAGE_PARAM, "请输入id");
		try {
			detailVo = governmentService.getNoticeDetailById4Cssp(bean);
			statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			if(detailVo!=null) {
				message = "操作成功";
			}else{
				message = "暂无数据";
			}
		} catch (Exception e) {
			logger.error("查看社保动态详情异常", e);
		}
		return resultVerify(statusCode,message,detailVo);
	}

	/**
	 * 下载文件
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/downloadNoticeFile", method = RequestMethod.GET)
	@ResponseBody
	public Result downloadNoticeFile(HttpServletRequest request, HttpServletResponse response)  throws Exception {
		String id = request.getParameter("id");
		String fileId = request.getParameter("fileId");
		if (StringUtils.isBlank(id)||"0".equals(id)||StringUtils.isBlank(fileId)||"0".equals(fileId))
			return error("请传入附件信息", null);
		NoticePathVO vo=new NoticePathVO();
		vo.setId(id);
		vo.setFileId(Long.valueOf(fileId));
		vo=governmentService.getNoticeFileById4cssp(vo);
		if(vo==null||StringUtils.isBlank(vo.getNewFilePath())){
			return error("暂无附件", null);
		}
		String filePath = SBDT_FILEPATH + File.separator + id + File.separator + vo.getNewFilePath();
		int status = FileFromUrlUtil.downloadFile(request, response,filePath, null);
		if (status == 200) {
			return ok("下载成功", null);
		} else if (status == 1) {
			return error("暂无附件", null);
		} else
			return error("下载失败", null);
	}
}
