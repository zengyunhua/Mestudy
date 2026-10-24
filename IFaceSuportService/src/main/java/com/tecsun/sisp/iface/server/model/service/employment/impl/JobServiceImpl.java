package com.tecsun.sisp.iface.server.model.service.employment.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFImageWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Encoder;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.tecsun.sisp.iface.client.WebClient;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.util.PicUtil;
import com.tecsun.sisp.iface.common.vo.Result;
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
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetPdfVo2;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetPolicyDocumentsVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetQzyxByQzzVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetZphInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.IsPhoneNumExistVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.JobBasicVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.JobOfferInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.LookPictureeVo;
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
import com.tecsun.sisp.iface.common.vo.faceverify.ResultVerify;
import com.tecsun.sisp.iface.server.model.service.employment.JobService;
import com.tecsun.sisp.iface.server.util.Config;
import com.tecsun.sisp.iface.server.util.JobUtil;

@Service("jobServiceImpl")
public class JobServiceImpl implements JobService {

	private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	/**
	 * 公用的Post请求
	 */
	private Result commonJobService(String url, JobBasicVo param){
		try {
			JsonObject jsonObject = JobUtil.constructParam(param);
			String jsonStr = (String) WebClient.getWebClient(url, jsonObject, String.class);
			return JobUtil.convertValue(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("抱歉，网络请求失败",e);
			return new Result(Constants.RESULT_MESSAGE_EXCEPTION,"抱歉，网络请求失败",null);
		}
	}
	

	/**
	 * 公用的get请求
	 */
	private Result commonJobService_get(String url){
		try {
			String jsonStr = (String) WebClient.getWebClient_get(url,String.class);
			return JobUtil.convertValue(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("抱歉，网络请求失败",e);
			return new Result(Constants.RESULT_MESSAGE_EXCEPTION,"抱歉，网络请求失败",null);
		}
	}
	
	/**
	 * 公用的Post请求，简单取值（不对Result节点取值）
	 */
	private Result commonJobService_simple(String url, JobBasicVo param){
		try {
			JsonObject jsonObject = JobUtil.constructParam(param);
			String jsonStr = (String) WebClient.getWebClient(url, jsonObject, String.class);
			return JobUtil.convertValue_simple(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("抱歉，网络请求失败",e);
			return new Result(Constants.RESULT_MESSAGE_EXCEPTION,"抱歉，网络请求失败",null);
		}
	}
	
	/**
	 * 简单 get 请求
	 */
	private Result commonGetReq(String url){
		try {
			String jsonStr = (String) WebClient.getWebClient_get(url,String.class);
			JsonMapper mapper = new JsonMapper(Inclusion.ALWAYS);
			Map jsonMap = mapper.fromJson(jsonStr, Map.class);
			return new Result(Constants.RESULT_MESSAGE_SUCCESS,"ok",jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("抱歉，网络请求失败",e);
			return new Result(Constants.RESULT_MESSAGE_EXCEPTION,"抱歉，网络请求失败",null);
		}
	}
	
	
	@Override
	public Result getJobOffer(String url, QueryJobOfferVo param) {
		return commonJobService(url, param);
	}
	
	@Override
	public Result getJobOfferInfo(String url, JobOfferInfoVo param) {
		String jsonStr = (String) WebClient.getWebClient_get(url+"?GWID="+param.getGwid(), String.class);
		return JobUtil.convertValue(jsonStr);
	}

//	@Override
//	public Result getPersonJob(String url, PersonJobVo param) {
//		JsonObject jo = JobUtil.constructParam(param);
//		jo.addProperty("GRXXID", param.getGrxxid());
//		String jsonStr = (String) WebClient.getWebClient(url, jo, String.class);
//		return JobUtil.convertValue(jsonStr);
//	}

//	@Override
//	public Result addJobSeekerInfo(String url, AddJobSeekerInfoVo param) {
//		return commonJobService_simple(url, param);
//	}

//	@Override
//	public Result modifyJobSeekerInfo(String url, AddJobSeekerInfoVo param) {
//		return commonJobService_simple(url, param);
//	}

	@Override
	public Result isPhoneNumExist(String url, IsPhoneNumExistVo param) {
		return commonJobService_get(url+"?SJHM="+param.getSjhm());
	}

	@Override
	public Result getAllDic(String url, GetAllDicVo param) {
		return commonJobService_get(url+"?key="+param.getKey());
	}

	@Override
	public Result getAllSecondDic(String url, GetAllSecondDicVo param) {
		return commonJobService_get(url+"?key="+param.getKey());
	}

	@Override
	public Result registerByIdCard(String url, RegisterByIdCardVo param) {
		return commonJobService(url, param);
	}

	@Override
	public Result queryJobFairs(String url, QueryJobFairVo param) {
		return commonJobService(url, param);
	}

	@Override
	public Result getZphInfo(String url, GetZphInfoVo param) {
		return commonJobService_get(url+"?ZPHID="+param.getZphid());
	}

	@Override
	public Result queryCompanys(String url, QueryCompanyVo param) {
		return commonJobService(url, param);
	}

	@Override
	public Result getCompanyInfo(String url, GetCompanyInfoVo param) {
		return commonJobService_get(url+"?QYID="+param.getQyid());
	}

	@Override
	public Result getQzyxByQzz(String url, GetQzyxByQzzVo param) {
		return commonJobService(url, param);
	}

	@Override
	public Result addJobDirection(String url, AddJobDirectionVo param) {
		return commonJobService_simple(url, param);
	}

	@Override
	public Result dropJobDirection(String url, DropJobDirectionVo param) {
		return commonJobService_simple(url, param);
	}

	@Override
	public Result getCompanyByZph(String url, GetCompanyByZphVo param) {
		return commonJobService(url, param);
	}

	@Override
	public Result sendJobChoice(String url, SendJobChoiceVo param) {
		return commonJobService_simple(url, param);
	}

	@Override
	public Result getAllMajor(String url, GetCompanyByZphVo param) {
		return commonGetReq(url);
	}

	@Override
	public Result addIdCardInfo(String url, AddIdCardInfoVo param) {
		return commonJobService_simple(url, param);
	}

	@Override
	public Result getPolicyDocuments(String url, GetPolicyDocumentsVo param) {
		return commonJobService_get(url+"?ID="+param.getId()+"&PTLX="+param.getPtlx());
	}

//	@Override
//	public Result smsSendCode(String url, SmsSendCodeVo param) {
//		return commonJobService_simple(url, param);
//	}

	@Override
	public Result queryPolicyDocument(String url, QueryPolicyDocumentVo param) {
		return commonJobService(url, param);
	}

	@Override
	public Result queryAgencyIntroduction(String url, JobBasicVo param) {
		return commonJobService(url, param);
	}

	@Override
	public Result querySalaryguidance(String url, QuerySalaryguidanceVo param) {
		return commonJobService(url, param);
	}
	
	//把PDF转为了图片
	/*@Override
	public Result getPdf(String url, GetPdfVo param) {
		String statusCode = Constants.RESULT_MESSAGE_SUCCESS;
		String msg = "政策文件获取成功";
		Object data = null;
		try {
			InputStream is = requestPdf(url,param);
			data = convertSinglePagePdf2ImgStr(is,param.getCurrentPage());
		} catch (Exception e) {
			statusCode = Constants.RESULT_MESSAGE_ERROR;
			msg = "抱歉，政策文件获取失败";
			logger.error("政策文件获取失败",e.getMessage());
		}
		return new Result(statusCode, msg, data);
	}*/
	
	
	//直接从第三方请求PDF
	@Override
	public InputStream getPdf(String url, GetPdfVo param) {
		InputStream is = null;
		try {
			is = requestPdf(url,param);
		} catch (Exception e) {
			logger.error("政策文件获取失败",e.getMessage());
		}
		return is;
	}

	//得到指定页PDF照片
	private GetPdfVo2 convertSinglePagePdf2ImgStr(InputStream inputStream,Integer currentPage) throws IOException {
//		long currentTimeMillis = System.currentTimeMillis();
		String pdf = generatePdf(inputStream);//生成pdf
		GetPdfVo2 vo2 = pdfTojpg(pdf,currentPage);//转换成照片
//		System.out.println("花费："+(System.currentTimeMillis()-currentTimeMillis));
		return vo2;
	}

	private String generatePdf(InputStream inputStream) throws FileNotFoundException {
		String pdf = FileUtils.getTempDirectoryPath()+(int)(Math.random()*10000)+".pdf";
		OutputStream os = new FileOutputStream(pdf);
		InputStream is = inputStream;
		try {
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("创建pdf文件出错了",e);
		}finally{
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return pdf;
	}

	//从第三方请求PDF文件
	private InputStream requestPdf(String url, GetPdfVo param) {
		Client client = new Client();
		WebResource webResource = client.resource(url+"?WJLJ="+param.getWjlj());
		ClientResponse response = webResource.accept(MediaType.APPLICATION_OCTET_STREAM).type(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
		return response.getEntity(InputStream.class);
	}
	
    public  GetPdfVo2 pdfTojpg(String pdf,Integer currentPage) throws IOException{
    	String jpg_dir = FileUtils.getTempDirectoryPath()+(int)(Math.random()*10000);
		PDDocument document = PDDocument.load(pdf); 
		PDFImageWriter imageWriter = new PDFImageWriter();  
		GetPdfVo2 vo = new GetPdfVo2();
		if(imageWriter.writeImage(document, "jpg", null, currentPage, currentPage, jpg_dir, 1, 100)){
			String jpg = jpg_dir+currentPage+".jpg";
			//System.out.println(jpg);
			Thumbnails.of(jpg).size(400, 565).toFile(jpg);
			vo.setPicBase64Str(PicUtil.GetImageStr(jpg));
			vo.setCurrentPage(currentPage);
			vo.setTotalPage(document.getNumberOfPages());
			//删除临时PDF、JPG
			FileUtils.deleteQuietly(new File(pdf));
			FileUtils.deleteQuietly(new File(jpg));
		}
		return vo;
    }

	@Override
	public Result addViewInfo(String url, AddViewInfoVo param) {
		return commonJobService_simple(url, param);
	}

	/**
	 * 上传照片
	 */
	@Override
	public Result savePicture(String url, SavePictureVo vo) {
		try {
			return uploadPicture(url,vo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("抱歉，网络请求失败", e);
			return new Result(Constants.RESULT_MESSAGE_EXCEPTION, "抱歉，网络请求失败", null);
		}
	}
	
	
	public  Result uploadPicture(String urlStr,SavePictureVo vo) throws Exception {
		String imgStr = vo.getPicBase64Str();
		
		//把base64字符照片保存到本地，并保留照片路径（插入本地数据库）
        String pic_path = constructPicPathAndName(vo);
        vo.setSfzzplj(pic_path);
		PicUtil.GenerateImage(imgStr, pic_path);
		Thumbnails.of(pic_path).size(93,114).toFile(pic_path);//压缩照片，小于32k
		File file = new File(pic_path);
		
		Client client = Client.create();  
        client.setConnectTimeout(5000);  
        client.setReadTimeout(600000);
        
        WebResource webResource = client.resource(urlStr);
        
		//尝试过StreamDataBodyPart，但服务器端不接收文件或文件保存格式不对，改用FileDataBodyPart，因此上面要先保存照片
        FileDataBodyPart fdp = new FileDataBodyPart("pic", file, MediaType.APPLICATION_OCTET_STREAM_TYPE);  
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();  
        formDataMultiPart.bodyPart(fdp); 
        
        ClientResponse response = webResource.type(MediaType.MULTIPART_FORM_DATA).post(ClientResponse.class, formDataMultiPart);  
        String result = response.getEntity(String.class); 
        
        //删除临时照片
        //file.delete();
        
        return stringToJson(result);
	}
	

	/**
	 * 构造照片名称和路径
	 */
	private String constructPicPathAndName(SavePictureVo vo) {
		//名称
		String zjhm = vo.getZjhm();
		String timeStamp = new SimpleDateFormat("yyyMMddHHmmss").format(new Date());
		String pic_name = zjhm+"_"+timeStamp+".jpg";
		
		//路径
		String pic_path = Config.getInstance().get("jiuye_job_idcard_picture_dir");
		if(StringUtils.isBlank(pic_path)){
			return "没有配置就业模块身份证/社保卡照片保存路径";
		}
		File file = new File(pic_path);
		if(!file.exists()){
			file.mkdirs();
		}
		
        return pic_path += File.separator+pic_name;
	}

	
	private Result stringToJson(String str) {
		try {
			JsonMapper mapper = new JsonMapper(Inclusion.ALWAYS);
			Map jsonMap = mapper.fromJson(str, Map.class);

			boolean ok = (boolean) (jsonMap.get("IsOK").equals(true));
			Integer i = (Integer) jsonMap.get("RowCount");
			String msg = (String) jsonMap.get("Message");
			
			if (ok && i > 0) {
				return doStringToJson(mapper, jsonMap);
			} 
			return new ResultVerify(Constants.RESULT_MESSAGE_ERROR, msg, null, i);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResultVerify(Constants.RESULT_MESSAGE_EXCEPTION, "抱歉，数据转换出错了",null, 0);
		}
	}

	private Result doStringToJson(JsonMapper mapper, Map jsonMap) {
		String msg = (String) jsonMap.get("Message");
		Integer i = (Integer) jsonMap.get("RowCount");
		
		jsonMap.remove("IsOK");
		
		Object dz = jsonMap.get("Result");
		
		return new ResultVerify(Constants.RESULT_MESSAGE_SUCCESS, msg, dz, i);
	}

	@Override
	public Result lookPicture(String url, LookPictureeVo param) {
		try {
			Client client = new Client();
			url = url+"?TPDZ="+param.getTpdz()+"&TPLX="+param.getTplx();//图片地址要有文件名后缀，如.jpg
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_OCTET_STREAM).type(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
			InputStream is = response.getEntity(InputStream.class);
			
			String base64Str = inputStreamToBase64Str(is);
			
//			List<String> list = new ArrayList<String>();
//			list.add(base64Str);
			
			return new Result(Constants.RESULT_MESSAGE_SUCCESS, "图片请求成功", base64Str);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查看照片出错了",e);
			return new Result(Constants.RESULT_MESSAGE_EXCEPTION, "抱歉，网络请求失败", null);
		}
	}

	/**
	 * 把 照片流 转为 base64 字符串
	 * 
	 * @param is
	 * @return
	 */
	public String inputStreamToBase64Str(InputStream is) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			is.close();
			byte[] data = outStream.toByteArray();
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encode(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Result addJobSeekerInfo(String public_job_url, AddJobSeekerInfoVo param) {
		return addOrModifyJobSeekerInfo(public_job_url, param, public_job_url+"/JobSeeker/AddInfo");
	}
	
	@Override
	public Result modifyJobSeekerInfo(String public_job_url, AddJobSeekerInfoVo param) {
		return addOrModifyJobSeekerInfo(public_job_url, param, public_job_url+"/JobSeeker/ModifyInfo");
	}
	
	/**
	 * 添加或修改求职者信息
	 * <p>
	 * 本接口把 添加图片、刷身份证添加身份证信息、添加/修改求职者信息 三个接口方法写在一起
	 * 
	 * @param public_job_url
	 * @param param
	 * @param job_url_addOrModify 添加时，入参为 public_job_url+"/JobSeeker/AddInfo"；修改时，入参为 public_job_url+"/JobSeeker/ModifyInfo"
	 * @return
	 */
	public Result addOrModifyJobSeekerInfo(String public_job_url, AddJobSeekerInfoVo param,String job_url_addOrModify){

		SavePictureVo picVo = new SavePictureVo();
		AddIdCardInfoVo idCardVo = new AddIdCardInfoVo();
		BeanUtils.copyProperties(param, picVo);
		BeanUtils.copyProperties(param, idCardVo);
		
		//上传身份证照片
		Result picResult = savePicture(public_job_url+"/Picture/SavePicture", picVo);//{"statusCode":"200","message":"图片信息保存成功","data":{"DZ":"c00d82b7a03939b9416c790c5bdcf0c9"},"total":1}
		param.setSfzzplj(picVo.getSfzzplj());//本地保存的身份证照片路径，保存数据到本地数据库用到
		
		if(Constants.RESULT_MESSAGE_SUCCESS.equals(picResult.getStatusCode())){
			//获取照片路径
			Map map = (Map) picResult.getData();
			String dz = (String) map.get("DZ");
			idCardVo.setSfzzplj(dz+".jpg");
			
			//添加身份证信息
			Result idCardResult = addIdCardInfo(public_job_url+ "/IdCard/Add", idCardVo);
			
			if(Constants.RESULT_MESSAGE_SUCCESS.equals(idCardResult.getStatusCode())){
				//添加或修改求职者信息
				return commonJobService_simple(job_url_addOrModify, param);
			}
			//提示添加身份证信息失败情况
			return idCardResult;
		}
		//提示上传身份证照片失败情况
		return picResult;
	}
	

	@Override
	public Result personUserLogin(String url, PersonUserLoginVo param) {
		JsonObject jsonObject = JobUtil.constructParam(param);
		String jsonStr = (String) WebClient.getWebClient(url, jsonObject, String.class);
		
		try {
			JsonMapper mapper = new JsonMapper(Inclusion.ALWAYS);
			Map jsonMap = mapper.fromJson(jsonStr, Map.class);

			boolean ok = (boolean) (jsonMap.get("IsOK").equals(true));
			Integer i = (Integer) jsonMap.get("RowCount");
			String msg = (String) jsonMap.get("Message");
			if (ok && i > 0) {
				return convertUserLoginJson(mapper, jsonMap);
			} else if(ok && i == 0) {
				msg = "请求成功，但没有查到数据";
			}
			return new ResultVerify(Constants.RESULT_MESSAGE_ERROR, msg, null, 0);
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("抱歉，数据转换出错了",e);
			return new ResultVerify(Constants.RESULT_MESSAGE_EXCEPTION, "抱歉，数据转换出错了",null, 0);
		}
	}

	private Result convertUserLoginJson(JsonMapper mapper, Map jsonMap) {
		String msg = (String) jsonMap.get("Message");
		Integer total = (Integer) jsonMap.get("RowCount");
		Object data = null;
		
		jsonMap.remove("IsOK");
		jsonMap.remove("RowCount");
		jsonMap.remove("Message");
		
		LinkedHashMap<String, Object> tempMap = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, Object> tempMap_sub = null;
		Map<String, Object> resultMap= (Map<String, Object>) jsonMap.get("Result");
		Set<String> keySet = resultMap.keySet();
		for (String key : keySet) {
			Map<String, Object> subMap = (Map<String, Object>) resultMap.get(key);
			tempMap_sub = new LinkedHashMap<String, Object>();
			Set<String> subSet = subMap.keySet();
			for(String subKey:subSet){
				Object value = subMap.get(subKey);
				if (value instanceof String) {
					String s = JobUtil.decode(value);
					tempMap_sub.put(subKey, s);
				}else {
					tempMap_sub.put(subKey, value);
				}
			}
			tempMap.put(key, tempMap_sub);
		}
		resultMap.remove("Result");
		data = tempMap;
		
		return new ResultVerify(Constants.RESULT_MESSAGE_SUCCESS, msg, data, total);
	}
	
	@Override
	public Result smsSendCode(String url, SmsSendCodeVo param) {
		try {
			JsonObject jsonObject = JobUtil.constructParam(param);
			String jsonStr = (String) WebClient.getWebClient(url, jsonObject, String.class);
			JsonMapper mapper = new JsonMapper(Inclusion.ALWAYS);
			Map jsonMap = mapper.fromJson(jsonStr, Map.class);
			return JobUtil.doConvert__simple(mapper, jsonMap);
		} catch (IOException e) {
			e.printStackTrace();
			return new Result(Constants.RESULT_MESSAGE_EXCEPTION, "抱歉，网络请求失败", null);
		}
	}
	
	/*
	 * 增加 GWID（岗位ID） 入参，用于筛选 个人投递岗位的某个岗位信息。
	 * 
	 * GRXXID是必传项，用于从第三方请求数据；然后根据 GWID 筛选个人岗位（如果GWID为空，则不筛选）
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Result getPersonJob(String url, PersonJobVo param) {
		JsonObject jo = JobUtil.constructParam(param);
		String json = (String) WebClient.getWebClient(url, jo, String.class);
		Result rs = JobUtil.convertValue(json);
		//岗位ID不为空，则表示查询的是个人投递岗位的某个岗位信息
		String gwid = param.getGwid();
		if(StringUtils.isNotBlank(gwid)){
			ArrayList<LinkedHashMap<String,Object>> list = (ArrayList<LinkedHashMap<String,Object>>) rs.getData();
			for(LinkedHashMap<String,Object> map :list){
				Set<String> keySet = map.keySet();
				for(String key:keySet){
					if("JobInfo".equals(key)){
						LinkedHashMap<String,Object> jobInfoMap = (LinkedHashMap<String, Object>) map.get("JobInfo");
						if(gwid.equals(jobInfoMap.get("GWID"))){
							List arrList = new ArrayList();
							arrList.add(map);
							rs.setData(arrList);
							break;
						}
					}
				}
			}
		}
		return rs;
	}

	
}
