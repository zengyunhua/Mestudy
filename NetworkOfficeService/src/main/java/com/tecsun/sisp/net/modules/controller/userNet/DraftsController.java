package com.tecsun.sisp.net.modules.controller.userNet;

import com.alibaba.fastjson.JSON;
import com.tecsun.sisp.net.common.*;
import com.tecsun.sisp.net.common.vo.faceverify.Result;
import com.tecsun.sisp.net.modules.controller.BaseController;
import com.tecsun.sisp.net.modules.entity.request.*;
import com.tecsun.sisp.net.modules.entity.response.*;
import com.tecsun.sisp.net.modules.service.impl.AreaServiceImpl;
import com.tecsun.sisp.net.modules.service.impl.CommonServiceImpl;
import com.tecsun.sisp.net.modules.service.impl.DraftsServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userNet/drafts")
public class DraftsController extends BaseController{

	
	private static Logger logger = LoggerFactory
			.getLogger(DraftsController.class);

	@Autowired
	private DraftsServiceImpl draftsService;
	@Autowired
	private CommonServiceImpl commonService;

	@Autowired
	private AreaServiceImpl areaService;
	

	// 查询办理成功件
	@RequestMapping(value = "/findbySuccessMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findbySuccessMessage(@RequestBody DraftsBean2 bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			logger.debug("-----打印参数------" + bean.toString());
			Page<DraftsVo> list = draftsService.findbySuccessMessage(bean);
			logger.debug("打印出参=======》》》" + JsonHelper.javaBeanToJson(list));
			if (list.getData() != null && list.getData().size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);
	}

	
	

	// 查询办理成功详细信息
	@RequestMapping(value = "/findbySuccessDetailedMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findbySuccessDetailedMessage(@RequestBody DraftsBean bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if (StringUtils.isBlank(bean.getOfficeId())) {
			return resultVerify("400!", "参数不能为空");
		}
		try {
			logger.debug("-----打印参数------" + bean.toString());
			DraftsVo draftsVo = draftsService
					.findbySuccessDetailedMessage(bean);
			logger.debug("打印出参=======》》》" + JsonHelper.javaBeanToJson(draftsVo));
			if (draftsVo != null ) {
				return resultVerify(statusCode, message, draftsVo);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 办理失败件
	@RequestMapping(value = "/findbyFailMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findbyFailMessage(@RequestBody DraftsBean2 bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			logger.debug("-----打印参数------" + bean.toString());
			Page<DraftsVo> list = draftsService.findbyFailMessage(bean);
			logger.debug("打印出参=======》》》" + JsonHelper.javaBeanToJson(list));
			if (list.getData() != null && list.getData().size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 办理失败件详细查询
	@RequestMapping(value = "/findbyFailDetailedMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findbyFailDetailedMessage(@RequestBody DraftsBean bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if (StringUtils.isBlank(bean.getOfficeId())) {
			return resultVerify("400!", "参数不能为空");
		}
		try {
			logger.debug("-----打印参数------" + bean.toString());
			List<DraftsVo> list = draftsService.findbyFailDetailedMessage(bean);
			logger.debug("打印出参=======》》》" + JsonHelper.javaBeanToJson(list));
			if (list != null && list.size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 正在办理件
	@RequestMapping(value = "/findbyCurrentMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findbyCurrentMessage(@RequestBody DraftsBean2 bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			logger.debug("打印入参=======》》》" + bean.toString());
			Page<DraftsVo> list = draftsService.findbyCurrentMessage(bean);
			logger.debug("打印出参=======》》》" + JsonHelper.javaBeanToJson(list));
			if (list.getData() != null && list.getData().size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 正在办理件详细查询
	@RequestMapping(value = "/findbyCurrentDetailedMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findbyCurrentDetailedMessage(@RequestBody DraftsBean bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if (StringUtils.isBlank(bean.getOfficeId())) {
			return resultVerify("400!", "参数不能为空");
		}
		try {
			logger.debug("-----打印参数------" + bean.toString());
			List<DraftsVo> list = draftsService
					.findbyCurrentDetailedMessage(bean);
			logger.debug("打印出参=======》》》" + JsonHelper.javaBeanToJson(list));
			if (list != null && list.size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 查询办件进度
	@RequestMapping(value = "/queryByOfficeStatus", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryByOfficeStatus(@RequestBody DraftsBean bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if (StringUtils.isBlank(bean.getOfficeId())) {
			return resultVerify("400!", "参数有误!");
		}
		try {
			logger.debug("-----打印参数------" + bean.toString());
			List<DraftsVo> list = draftsService.queryByOfficeStatus(bean);
			logger.debug("打印出参=======》》》" + JsonHelper.javaBeanToJson(list));
			if (list != null && list.size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 提交草稿
	@RequestMapping(value = "/commitDraft", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result commitDraft(@RequestBody DraftsListVo bean) throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if (bean.getOfficeId() == null || bean.getOfficeId() == "") {
			return resultVerify("400!", "参数有误!");
		}

		long commitDraft = draftsService.commitDraft(bean);
		if (commitDraft > 0) {
			result(statusCode, message);
			logger.info("提交成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "提交失败!";
		}

		return resultVerify(statusCode, message);
	}

	//保存草稿
	@RequestMapping(value = "/addDraftsMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result addDraftsMessage(@RequestBody DraftsBean bean)
			throws Exception {

		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		long num=draftsService.addOfficeMessage(bean.getOfficeVo());
		if(num>0){
			bean.setOfficeId(bean.getOfficeVo().getoId());
			draftsService.addDraftsMessage(bean);
			result(statusCode, message);
			logger.info("保存到草稿箱成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "保存到草稿箱失败!";
		}
		return resultVerify(statusCode, message);
	}

	@RequestMapping(value = "/deleteDraftsMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result deleteDraftsMessage(@RequestBody Map<String,Object> map)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";

		if (StringUtils.isBlank(map.get("tId").toString())) {
			return resultVerify("400", "参数不可为空!");
		}
		long row = draftsService.deleteDraftsMessage(map);
		if (row > 0) {
			deleteOfficeMessage(map);
			result(statusCode, message);
			logger.info("删除草稿成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "删除草稿失败!";
		}
		return resultVerify(statusCode, message);
	}
	//删除办件
	public void deleteOfficeMessage(Map map){
		 draftsService.deleteOfficeMessage(map);
	}

	@RequestMapping(value = "/updateDraftsMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result updateDraftsMessage(@RequestBody DraftsListVo bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";

		if (StringUtils.isBlank(bean.gettId())) {
			return resultVerify("400", "请输入完整的参数!");
		}
		long row = draftsService.updateDraftsMessage(bean);
		if (row > 0) {
			result(statusCode, message);
			logger.info("修改草稿成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "修改草稿失败!";
		}

		return resultVerify(statusCode, message);
	}

	// 查询草稿箱
	@RequestMapping(value = "/findbyDraftsMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findbyDraftsMessage(@RequestBody DraftsBean2 bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			logger.debug("打印入参=======" + bean.toString());
			Page<DraftsVo> list = draftsService.findbyDraftsMessage(bean);
			logger.debug("打印出参=======" + JsonHelper.javaBeanToJson(list));
			if (list.getData() != null && list.getData().size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 查询草稿箱详细
	@RequestMapping(value = "/findAllDraftsMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result findAllDraftsMessage(@RequestBody DraftsBean bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if (bean.getOfficeId() == "" || bean.getOfficeId() == null) {
			return resultVerify("400", "参数非法!");
		}
		try {

			BanjianVo banjianVo = draftsService.findAllDraftsMessage(bean);
			if (banjianVo != null) {
				return resultVerify(statusCode, message, banjianVo);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 获取孝感市区县
	@RequestMapping(value = "/queryByArea", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryByArea() throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			List<AreaVo> list = areaService.queryByArea();
			if (list != null && list.size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 人员状态
	@RequestMapping(value = "/queryPersonStatus", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryPersonStatus(@RequestBody DictionaryBean bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		if (StringUtils.isBlank(bean.getGroupid())) {
			return resultVerify(Constants.RESULT_MESSAGE_PARAM, "参数为空");
		}
		try {

			List<DictionaryVo> list = areaService.queryPersonStatus(bean);
			if (list != null && list.size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 事项查询
	@RequestMapping(value = "/queryByItems", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryByItems(@RequestBody ItemListBean bean) throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";

		try {

			Page<ItemBean> list = draftsService.queryByItems(bean);
			if (list.getData() != null && list.getData().size() > 0) {
				return resultVerify(statusCode, message, list);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 查询明细
	@RequestMapping(value = "/queryByItemDetails", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryByItemDetails(@RequestBody ItemVo bean) throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		/*if (bean.getId() <= 0) {
			return result(Constants.RESULT_MESSAGE_PARAM, "参数非法!");
		}*/
		try {

			ItemVo itemVo = draftsService.queryByItemDetails(bean);
			if (itemVo != null) {
				return resultVerify(statusCode, message, itemVo);
			} else {
				return resultVerify(Constants.RESULT_MESSAGE_SUCCESS, "暂未获取到相关信息");
			}

		} catch (Exception e) {
			e.printStackTrace();
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "获取失败";
			logger.error("数据出现异常", e);
		}

		return resultVerify(statusCode, message);

	}

	// 添加人员信息
	@RequestMapping(value = "/addPersonMessages", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result addPersonMessages(@RequestBody ItemVo bean) throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";

		long row = draftsService.addPersonMessages(bean);

		if (row > 0) {
			result(statusCode, message);
			logger.info("添加成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "添加失败!";
		}

		return resultVerify(statusCode, message);
	}

	// 事项修改
	@RequestMapping(value = "/updateByItemDetails", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result updateByItemDetails(@RequestBody ItemVo bean)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";

		if (bean.getId() <= 0) {
			return resultVerify("参数非法", null);
		}
		long row = draftsService.updateByItemDetails(bean);
		if (row > 0) {
			result(statusCode, message);
			logger.info("修改成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "修改失败!";

		}

		return resultVerify(statusCode, message);
	}

	// 事项删除
	@RequestMapping(value = "/deleteItemDetails", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result deleteItemDetails(@RequestBody ItemVo bean) throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";

		if (bean.getId() <= 0) {
			return resultVerify("参数非法", null);
		}
		long row = draftsService.deleteItemDetails(bean);
		if (row > 0) {
			result(statusCode, message);
			logger.info("删除成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "删除失败!";
		}

		return resultVerify(statusCode, message);
	}

	// 调用电子证件库
	// 个人信息
	@RequestMapping(value = "/getByPersonMessage", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getPersonMessage(@RequestBody PersonBeanRequest bean)
			throws Exception {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		logger.info("获取个人信息接口，入参bean:\t..........."
				+ JsonHelper.javaBeanToJson(bean));
		try {
			// 获取接口地址；
			String url = Config.getInstance().get("PERSONFILENUMBER");

			// 处理接口入参各字段是否为必填项
			StringBuffer sb = new StringBuffer();
			// 接口名和 必填参数
			sb.append(url.toString());
			if (bean.getHolderCode() != null
					&& bean.getHolderCode().length() > 0) {
				sb.append("?holderCode=" + bean.getHolderCode());
			}
			// 调用接口方法
			String ps = (String) DictionaryUtil.getClientRequest(sb.toString());
			Object datas = JSON.parse(ps);

			// 调用接口方法，用get请求方式调用
			if (ps != null && ps.length() > 0) {
				logger.info("获取人员信息接口，出参bean:\t..........." + datas);
				return resultVerify(result, message, datas);
			} else {
				message = "暂未获取到相关的数据";
				result = Constants.RESULT_MESSAGE_ERROR;
				logger.error("调用接口失败，返回值空");
				return resultVerify(result, message);
			}
		} catch (Exception e) {
			message = "暂未获取到相关的数据";
			result = Constants.RESULT_MESSAGE_ERROR;
			e.printStackTrace();
			logger.error("获取个人信息接口失败");
		}
		return resultVerify(result, message);
	}

	// 获取证照id编号
	@RequestMapping(value = "/getImageInfo", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getImageInfo(@RequestBody PersonBeanRequest bean)
			throws Exception {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		logger.info("获取个人信息接口，入参bean:\t..........."
				+ JsonHelper.javaBeanToJson(bean));
		try {
			// 获取接口地址；
			String url = Config.getInstance().get("XGFILENUMBER");

			// 处理接口入参各字段是否为必填项
			StringBuffer sb = new StringBuffer();
			// 接口名和 必填参数
			sb.append(url.toString());
			if (bean.getFileNumber() != null
					&& bean.getFileNumber().length() > 0) {
				sb.append("?fileNumber=" + bean.getFileNumber());
			}
			// 调用接口方法
			String ps = (String) DictionaryUtil.getClientRequest(sb.toString());
			Object datalist = JSON.parse(ps);

			// 调用接口方法，用get请求方式调用
			if (ps != null && ps.length() > 0) {
				logger.info("获取人员信息接口，出参bean:\t..........." + datalist);
				return resultVerify(result, message, datalist);
			} else {
				message = "暂未获取到相关的数据";
				result = Constants.RESULT_MESSAGE_ERROR;
				logger.error("调用接口失败，返回值空");
				return resultVerify(result, message);
			}
		} catch (Exception e) {
			message = "暂未获取到相关的数据";
			result = Constants.RESULT_MESSAGE_ERROR;
			e.printStackTrace();
			logger.error("获取个人信息接口失败");
		}

		return resultVerify(result, message);

	}

	// 获取指定文件流图片
	@RequestMapping(value = "/getImageFile", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getImageFile(@RequestBody PersonBeanRequest bean)
			throws Exception {
		String result = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		logger.info("获取个人信息接口，入参bean:\t..........."
				+ JsonHelper.javaBeanToJson(bean));
		try {
			// 获取接口地址；
			String url = Config.getInstance().get("XGIMAGEFILEID");

			// 处理接口入参各字段是否为必填项
			StringBuffer sb = new StringBuffer();
			// 接口名和 必填参数
			sb.append(url.toString());
			if (bean.getFileId() != null && bean.getFileId().length() > 0) {
				sb.append("?fileId=" + bean.getFileId());
			}
			// 调用接口方法
			String str = (String) DictionaryUtil
					.getClientRequest(sb.toString());
			byte[] bytes = str.getBytes();// 可能转换出问题的地方
			String encode = Base64Util.encode(bytes);

			// 调用接口方法，用get请求方式调用
			if (encode != null && encode.length() > 0) {
				logger.info("获取图片信息接口，此处代表已成功,文件流不打印了.");
				return resultVerify(result, message, encode);
			} else {
				message = "暂未获取到相关的数据";
				result = Constants.RESULT_MESSAGE_ERROR;
				logger.error("调用接口失败，返回值空");
				return resultVerify(result, message);
			}
		} catch (Exception e) {
			message = "暂未获取到相关的数据";
			result = Constants.RESULT_MESSAGE_ERROR;
			e.printStackTrace();
			logger.error("获取图片流接口失败");
		}
		return resultVerify(result, message);
	}

	// 审核对所有办理的初步审核
	@RequestMapping(value = "/audMatters", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result audMatters(@RequestBody AuditVo bean) throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";

		int userId = bean.getUserId();
		UserVO user = areaService.getUserById(userId);
		try {

			if (user != null) {
				bean.setAuditor(user.getName());// 审核人
			}
			long count = draftsService.insertAudit(bean);
			long row = 0;
			if (count > 0) {
				row = draftsService.updateTimeAndState(bean);
				Map<String,Object> map =new HashMap<>();
				map.put("state",bean.getStatus());
				map.put("id",bean.gettId());
				draftsService.updateDraftstate(map);
			}
			if (row > 0) {
				message = "success";
				statusCode = Constants.RESULT_MESSAGE_SUCCESS;
			} else {
				message = "操作失败!";
				statusCode = Constants.RESULT_MESSAGE_ERROR;
				logger.error("操作失败!");

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("出现了一个异常");
		}
		return resultVerify(statusCode, message);
	}

    /** Role SQL was never supplied. Keep the URL, but do not execute an unbound mapper. */
    @Deprecated
    @RequestMapping(value = "/insertUserRol", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public Result insertUserRol(@RequestBody UserRolVo bean) {
        return new Result("501", "角色接口尚未实现：需要确认角色表结构和 SQL", null);
    }

    /** Retains the legacy spelling and returns an explicit, non-success HTTP/JSON contract. */
    @Deprecated
    @RequestMapping(value = "/queryUserRloe", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public Result queryUserRloe(@RequestBody UserRoleBean bean) {
        return new Result("501", "角色接口尚未实现：需要确认角色表结构和 SQL", null);
    }
	
		
	//保存权限
	@RequestMapping(value = "/inserttMatteRole", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result inserttMatteRole(@RequestBody MatterRoleBean bean) throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		long row = areaService.inserttMatteRole(bean);
		if (row > 0) {
			result(statusCode, message);
			logger.info("保存成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "保存失败!";
		}
		return resultVerify(statusCode, message);
	}


	// 添加用户事项关联//2018/07/31需要修改
	@RequestMapping(value = "/insertMatterAndRole", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result insertMatterAndRole(@RequestBody List<UserRoleList> list)
			throws Exception {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "操作成功";
		try {
			Map<String,Object> map =new HashMap();
			map.put("userId", list.get(0).getUserId());
			/*先删除所有的权限*/
		areaService.deleteUserMatter(map);
		long row = areaService.insertMatterAndRole(list);

		if (row >0) {
			result(statusCode, message);
			logger.info("添加成功!");
		} else {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "添加失败!";

		}
		} catch (Exception e) {
			message="添加失败!";
			statusCode = Constants.RESULT_MESSAGE_ERROR;
		}
		return resultVerify(statusCode, message);
	}
	
	
	
	//文件上传
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	@ResponseBody
    public Result upload(HttpServletRequest request,
                         @RequestParam(value="description",required=false) String description,
                         @RequestParam("file") MultipartFile file,@RequestParam("sfzh") String sfzh
                        ) throws Exception {
			String message="操作成功";
			String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
        //System.out.println(description);
        //如果文件不为空，写入上传路径
		if( sfzh ==null || sfzh == ""){
			message = "没有获取到身份证到";
			return resultVerify(statusCode, message);
		}
        if(!file.isEmpty()) {
            //上传文件路径
            String path = Config.getInstance().get("SCFILEPATH");
            //上传文件名
            String filename = file.getOriginalFilename();
            String newName = sfzh + filename.substring(filename.lastIndexOf("."));
            File filepath = new File(path,newName);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            file.transferTo(new File(path + File.separator + newName));
           // long l = MyBatisBatchItemWriter<T>();
            FileBean bean = new FileBean();
            bean.setFilename(newName);
            draftsService.insertFileName(bean);
            long id = bean.getId();
            return resultVerify(statusCode, message,bean);
        } else {
            return resultVerify(Constants.RESULT_MESSAGE_ERROR, "上传失败!");
        }
    }

	/**
	 * 查询孝感的所有统筹区的名称和编号
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryalltcq", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryalltcq() throws Exception {
		String message="查询成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		List<TcqVo> list=new ArrayList<TcqVo>();
		try {
			list=draftsService.queryalltcq();
			if (list.size() >0){
				return resultVerify(statusCode, message, list);
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "查询失败";
			logger.error("查询所有统筹区名称和编码：", e);
		}
		return resultVerify(statusCode, message);
	}

	/**
	 * 根据统筹区编码查询参保单位名称和编码
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryunit", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result queryunit(@RequestBody Map<String, Object> map) throws Exception {
		String message="查询成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		if (org.springframework.util.StringUtils.isEmpty(map.get("tcqcode"))) return resultVerify(Constants.RESULT_MESSAGE_PARAM, "参数为空");
		TcqVo bean=new TcqVo();
		try {
			bean=draftsService.queryunit(map);
			if ( bean != null){
				return resultVerify(statusCode, message, bean);
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "查询失败";
			logger.error("查询所有统筹区名称和编码：", e);
		}
		return resultVerify(statusCode, message);
	}


	@RequestMapping(value = "/querydystatus", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result querydystatus() throws Exception {
		String message="查询成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		List<DictVO> list=new ArrayList<DictVO>();
		try {
			list=areaService.querydystatus();
			if (list.size() >0){
				return resultVerify(statusCode, message, list);
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "查询失败";
			logger.error("查询所有医疗待遇类别：", e);
		}
		return resultVerify(statusCode, message);
	}

	/**
	 * 新增事项
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/additem", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result additem(@RequestBody MatterBean bean) throws Exception {
		String message="新增事项成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		//获取事项编码
		bean.setSxbm(draftsService.getBjNum());
		if (org.springframework.util.StringUtils.isEmpty(bean.getId())) return resultVerify(Constants.RESULT_MESSAGE_PARAM, "参数为空");
		if (org.springframework.util.StringUtils.isEmpty(bean.getSxbm())) return resultVerify(Constants.RESULT_MESSAGE_PARAM, "参数为空");
		try {
			int num=draftsService.additem(bean);
			if (num >0){
				return resultVerify(statusCode, message,bean.getSxbm());
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "新增事项失败";
			logger.error("新增事项：", e);
		}
		return resultVerify(statusCode, message);
	}

	/**
	 * 新增事项的文件
	 * @param list
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addmatterfile", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result addmatterfile(@RequestBody List<MatterFileBean> list) throws Exception {
		String message="新增成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		//获取事项编码
		try {
			int num=draftsService.addmatterfile(list);
			if (num >0){
				return resultVerify(statusCode, message);
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "新增事项的文件失败";
			logger.error("新增事项：", e);
		}
		return resultVerify(statusCode, message);
	}


	@RequestMapping(value="/uploadmatterfile",method=RequestMethod.POST,produces = "application/json")
	@ResponseBody
	public Result uploadmatterfile(@RequestBody MultipartFile file) throws Exception {
		String message="操作成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		if(!file.isEmpty()) {
			//上传文件路径
			String path = Config.getInstance().get("MATTERSCFILEPATH");
			//上传文件名
			String filename = file.getOriginalFilename();
			String filecode=draftsService.getBjNum();
			String newName = filecode + filename.substring(filename.lastIndexOf("."));
			File filepath = new File(path,newName);
			//判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}
			//将上传文件保存到一个目标文件当中
			file.transferTo(new File(path + File.separator + newName));
			MatterFile bean = new MatterFile();
			bean.setFilecode(newName);
			bean.setName(filename);
			return resultVerify(statusCode, message,bean);
		} else {
			return resultVerify(Constants.RESULT_MESSAGE_ERROR, "上传失败!");
		}
	}

	/**
	 * 删除事项
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteitem", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result deleteitem(@RequestBody Map<String,Object> map) throws Exception {
		String message="删除事项成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		if (org.springframework.util.StringUtils.isEmpty(map.get("id"))) return resultVerify(Constants.RESULT_MESSAGE_PARAM, "参数为空");
		try {
			int num=draftsService.deleteitem(map);
			if (num >0){
				draftsService.deletefiles(map);
				List<String> filecodes=draftsService.getfilecodelist(map);
				for (int i = 0; i < filecodes.size() ; i++) {
					deleteFile(filecodes.get(i));
				}
				return resultVerify(statusCode, message);
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "删除事项的失败";
			logger.error("删除事项：", e);
		}
		return resultVerify(statusCode, message);
	}

	/**
	 * 删除事项的单个文件
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deletefilebyid", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result deletefilebyid(@RequestBody Map<String,Object> map) throws Exception {
		String message="删除文件成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		if (StringUtils.isEmpty(map.get("filecode").toString())) return resultVerify(Constants.RESULT_MESSAGE_PARAM, "参数为空");
		try {
			int num=draftsService.deletefilebyid(map);
			if (num >0){
				deleteFile(map.get("filecode").toString());
				return resultVerify(statusCode, message);
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "删除事项的文件失败";
			logger.error("删除事项的文件：", e);
		}
		return resultVerify(statusCode, message);
	}

	/**
	 * 查询事项列表
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getitemsbyterm", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result getitemsbyterm(@RequestBody ItemBean bean) throws Exception {
		String message="查询事项列表成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		Page<ItemMatters> page=new Page<>(bean.getPageno(),bean.getPagesize());
		List<ItemMatters> list=new ArrayList<>();
		try {
			list=draftsService.getitemsbyterm(bean);
			if (list.size() >0){
				page.setCount(list.size());
				page.setData(list);
				return resultVerify(statusCode, message,page);
			}else {
				message="查询事项列表暂无";
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "查询事项列表失败";
			logger.error("查询事项列表：", e);
		}
		return resultVerify(statusCode, message);
	}

	/**
	 * 修改事项
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateitembyid", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result updateitembyid(@RequestBody MatterBean bean) throws Exception {
		String message="修改事项成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		try {
			int num=draftsService.updateitembyid(bean);
			if (num >0){
				return resultVerify(statusCode, message);
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "修改事项失败";
			logger.error("修改事项：", e);
		}
		return resultVerify(statusCode, message);
	}



	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public HttpServletResponse downloadFile( HttpServletResponse response,HttpServletRequest request,String pathUrl, String name)
	{
		try
		{
			//这里获取一个 response 对象，如果不知道怎么获取的话，那么你也可以把   HttpServletResponse response 当做一个参数传递到这个方法中，直接使用即可
//			HttpServletResponse response = getResponse();

			if ((pathUrl == null) && ("".equals(pathUrl))) {
				return null;
			}
			File file = new File(pathUrl);
			//这里获取服务器的绝对路径。如果不会用的话，用下面注释掉的方法就可以
//			String filePath = getServletContext().getRealPath("/");
			//用下面的方法前，需要把HttpServletRequest request当做一个参数传递到本方法中，直接使用即可
			String filePath = request.getSession().getServletContext().getRealPath("/");

			InputStream fis = new BufferedInputStream(new FileInputStream(filePath + pathUrl));

			byte[] buffer = new byte[fis.available()];

			fis.read(buffer);

			fis.close();

			response.reset();

			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());

			response.addHeader("Content-Disposition", "attachment;filename=" + new String(name.getBytes(), "ISO-8859-1"));

			toClient.write(buffer);

			toClient.flush();

			toClient.close();

			response.setContentType("application/octet-stream");
			//这里的addHeader方法，如果报错，请使用下方注释掉的方法。
			response.addHeader("Content-Length", "" + file.length());
			//把第二个参数更改为String 类型即可
			//response.addHeader("Content-Length", String.valueOf(file.length()));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}

		return null;
	}


	public HttpServletResponse download(String path, HttpServletResponse response) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。
//			String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return response;
	}


	//删除单个文件 （传filecode）
	public static boolean deleteFile(String fileName) {
		String path = Config.getInstance().get("MATTERSCFILEPATH");
		String newName = path + "/" + fileName;
		File file = new File(newName);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("删除单个文件" + fileName + "失败！");
				return false;
			}
		} else {
			System.out.println("删除单个文件失败：" + fileName + "不存在！");
			return false;
		}
	}

	@RequestMapping(value = "/deleteMatterFile", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Result deleteMatterFile(@RequestBody Map<String,Object> map) throws Exception {
		String message="删除成功";
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		try {
			String filecode=map.get("filecode").toString();
			Boolean flag=deleteFile(filecode);
			if (flag){
				return resultVerify(statusCode, message);
			}else {
				statusCode = Constants.RESULT_MESSAGE_ERROR;
				message = "删除文件失败";
			}
		}catch (Exception e){
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			message = "删除文件失败";
			logger.error("删除文件：", e);
		}
		return resultVerify(statusCode, message);
	}
}
