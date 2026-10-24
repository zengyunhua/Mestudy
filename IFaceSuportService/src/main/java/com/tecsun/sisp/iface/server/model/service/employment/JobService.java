package com.tecsun.sisp.iface.server.model.service.employment;

import java.io.InputStream;

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
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetPolicyDocumentsVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetQzyxByQzzVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.GetZphInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.IsPhoneNumExistVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.JobBasicVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.JobOfferInfoVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.LookPictureeVo;
import com.tecsun.sisp.iface.common.vo.employment.param.job.ModifyJobSeekerInfoVo;
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

public interface JobService {

	Result getJobOffer(String url, QueryJobOfferVo param);

	Result getJobOfferInfo(String url, JobOfferInfoVo param);

	Result getPersonJob(String url, PersonJobVo param);

	Result addJobSeekerInfo(String url, AddJobSeekerInfoVo param);

	Result modifyJobSeekerInfo(String url, AddJobSeekerInfoVo param);

	Result personUserLogin(String url, PersonUserLoginVo param);

	Result isPhoneNumExist(String url, IsPhoneNumExistVo param);

	Result getAllDic(String url, GetAllDicVo param);

	Result getAllSecondDic(String url, GetAllSecondDicVo param);

	Result registerByIdCard(String url, RegisterByIdCardVo param);

	Result queryJobFairs(String url, QueryJobFairVo param);

	Result getZphInfo(String url, GetZphInfoVo param);

	Result queryCompanys(String url, QueryCompanyVo param);

	Result getCompanyInfo(String url, GetCompanyInfoVo param);

	Result getQzyxByQzz(String url, GetQzyxByQzzVo param);

	Result addJobDirection(String url, AddJobDirectionVo param);

	Result dropJobDirection(String url, DropJobDirectionVo param);

	Result getCompanyByZph(String url, GetCompanyByZphVo param);

	Result sendJobChoice(String url, SendJobChoiceVo param);

	Result getAllMajor(String url, GetCompanyByZphVo param);

	Result addIdCardInfo(String url, AddIdCardInfoVo param);

	Result getPolicyDocuments(String url, GetPolicyDocumentsVo param);

	Result smsSendCode(String url, SmsSendCodeVo param);

	Result queryPolicyDocument(String url, QueryPolicyDocumentVo param);

	Result queryAgencyIntroduction(String url, JobBasicVo param);

	Result querySalaryguidance(String url, QuerySalaryguidanceVo param);

//	Result getPdf(String url, GetPdfVo param);
	InputStream getPdf(String url, GetPdfVo param);

	Result addViewInfo(String url, AddViewInfoVo param);

	Result savePicture(String url, SavePictureVo param);

	Result lookPicture(String url, LookPictureeVo param);

}
