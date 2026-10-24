package com.tecsun.sisp.iface.server.controller.card;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;

import com.tecsun.sisp.iface.server.util.Config;
import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.CardInfoBean;
import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.TransBean;
import com.tecsun.sisp.iface.common.vo.card.CardAllInfo;
import com.tecsun.sisp.iface.common.vo.card.CardBaseInfo;
import com.tecsun.sisp.iface.common.vo.card.SettingCardQuery;
import com.tecsun.sisp.iface.server.controller.BaseController;
import com.tecsun.sisp.iface.server.outerface.card.CardInfoBus;
import com.tecsun.sisp.iface.server.util.DictionaryUtil;
import com.tecsun.sisp.iface.server.util.ThreadPoolTask;
import com.tecsun.sisp.iface.server.util.ThreadPoolUtil;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Social-security card services: card data and card-production queries, plus temporary
 * loss reporting, suspension removal and card activation.
 *
 * <p>Every operation is forwarded to the provincial card-management gateway
 * ({@code CARDBUESS_URL}); this class only validates the request, translates dictionary
 * codes into display names, and wraps the outcome in the standard {@code Result}
 * envelope.
 *
 * <p><strong>Required input.</strong> Most operations need at least one of card number or
 * ID number; the mutating operations (activate, report lost, remove suspension) need card
 * number, ID number <em>and</em> name.
 *
 * <p><strong>Status semantics.</strong> A gateway reply of {@code "00"} means the operation
 * succeeded now, {@code "01"} means it had already succeeded earlier, anything else is a
 * failure whose text is passed through in {@code message}.
 *
 * @author thl
 */
@Controller
@RequestMapping(value = "/iface/cardInfo")
public class CardInfoController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(CardInfoController.class);

  /*  @Autowired
    private NetUserServiceImpl netUserService;*/

    /**
     * SISP_IFACE_CARD_001 — card base data, excluding the personal photo.
     *
     * <p>At least one of card number or ID number must be supplied. Dictionary-coded
     * fields (sex, ethnicity, person status, registration type) are expanded to display
     * names before the response is returned.
     *
     * @param bean request carrying the card number and/or ID number
     * @return the card base data, or a failure envelope when neither identifier is present
     *         or the gateway rejects the lookup
     * @throws Exception if the gateway call fails
     */
    @RequestMapping(value = "/getCardInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getCardInfo(@RequestBody CardInfoBean bean)throws Exception{
        insertTransLog(bean,Constants.BUSINESSCODE.get("getCardInfo"));
        String cardno = bean.getCardNo()==null?"":bean.getCardNo();
        String cernum = bean.getId()==null?"":bean.getId();
        String result = "";
        String message = "";
        if ((cardno != null && !cardno.equals("")) || (cernum != null && !cernum.equals(""))) {
            CardInfoBus bus = new CardInfoBus();
            CardBaseInfo info = bus.getCardBaseInfo(cardno,cernum);
            if (info != null && info.getErr().equals("OK")) {
                result = Constants.RESULT_MESSAGE_SUCCESS;
                message = "Card base data retrieved (personal photo excluded).";
                info.setSex(DictionaryUtil.getDictName(Constants.SEX_GROUP, info.getSex()));
                info.setEthnic(DictionaryUtil.getDictName(Constants.PARAM_NATION_GROUP, info.getEthnic()));
                info.setPersonstatus(DictionaryUtil.getDictName(Constants.PERATATUS_GROUP, info.getPersonstatus()));
                info.setRegisttype(DictionaryUtil.getDictName(Constants.REGNATURE_GROUP, info.getRegisttype()));
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = info.getErr();
            }
            return this.result(result, message, info);
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "At least one of card number or ID number is required.";
            return this.result(result, message, null);
        }

    }

    
    
    @RequestMapping(value = "/getCardBaseInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getCardBaseInfo(@RequestBody CardInfoBean bean)throws Exception{
        insertTransLog(bean,Constants.BUSINESSCODE.get("getCardInfo"));
        String cardno = bean.getCardNo()==null?"":bean.getCardNo();
        String cernum = bean.getId()==null?"":bean.getId();
        String result = "";
        String message = "";
        if ((cardno != null && !cardno.equals("")) || (cernum != null && !cernum.equals(""))) {
            CardInfoBus bus = new CardInfoBus();
            CardBaseInfo info = bus.getCardBaseInfo(cardno,cernum);
            if (info != null && info.getErr().equals("OK")) {
                result = Constants.RESULT_MESSAGE_SUCCESS;
                message = "Card base data retrieved (personal photo excluded).";
                info.setSex(info.getSex());
                info.setEthnic(info.getEthnic());
                info.setPersonstatus(info.getPersonstatus());
                info.setRegisttype(info.getRegisttype());
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = info.getErr();
            }
            return this.result(result, message, info);
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "At least one of card number or ID number is required.";
            return this.result(result, message, null);
        }

    }
    /**
     * SISP_IFACE_CARD_002 — card base data including the personal photo.
     *
     * <p>At least one of card number or ID number must be supplied.
     */
    @RequestMapping(value = "/getCardAllInfo", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getCardAllInfo(@RequestBody CardInfoBean bean)throws Exception{
        insertTransLog(bean,Constants.BUSINESSCODE.get("getCardAllInfo"));
        String cardno = bean.getCardNo()==null?"":bean.getCardNo();
        String cernum = bean.getId()==null?"":bean.getId();
        String result = "";
        String message = "";
        if ((cardno != null && !cardno.equals("")) || (cernum != null && !cernum.equals(""))) {
            CardInfoBus bus = new CardInfoBus();
            CardAllInfo info = bus.getCardAllInfo(cardno, cernum);
            if (info != null && info.getErr().equals("OK")) {
                info.setSex(DictionaryUtil.getDictName(Constants.SEX_GROUP, info.getSex()));
                info.setEthnic(DictionaryUtil.getDictName(Constants.PARAM_NATION_GROUP, info.getEthnic()));
                info.setPersonstatus(DictionaryUtil.getDictName(Constants.PERATATUS_GROUP, info.getPersonstatus()));
                info.setRegisttype(DictionaryUtil.getDictName(Constants.REGNATURE_GROUP, info.getRegisttype()));
                result = Constants.RESULT_MESSAGE_SUCCESS;
                message = "Card base data retrieved.";
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = info.getErr();
            }
            return this.result(result, message, info);
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "At least one of card number or ID number is required.";
            return this.result(result, message, null);
        }

    }
    /**
     * Card data. At least one of card number or ID number must be supplied.
     */
   /* @RequestMapping(value = "/getCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getCard(@RequestBody CardInfoBean bean)throws Exception{
        //insertTransLog(bean,Constants.BUSINESSCODE.get("getCardAllInfo"));
        String cardno = bean.getCardNo()==null?"":bean.getCardNo();
        String cernum = bean.getId()==null?"":bean.getId();
        String result = "";
        String message = "";
        if ((cardno != null && !cardno.equals("")) || (cernum != null && !cernum.equals(""))) {
            CardInfoBus bus = new CardInfoBus();
            CardAllInfo allInfo = bus.getCard(cardno, cernum);
           // CardAllInfo allInfo=bus.getCardAllInfo(cardno,cernum);
            if (allInfo != null && allInfo.getErr().equals("OK")) {//the person exists at the provincial gateway
                result = Constants.RESULT_MESSAGE_SUCCESS;
                message = "Card data retrieved.";
                return result(result,message,allInfo);
             }
            else{//no such person at the provincial gateway
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "The name you entered is incorrect. Please try again.";
                logger.error("No such person at the provincial gateway.");
                return result(result, message,null);
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "At least one of card number or ID number is required.";
            return this.result(result, message, null);
        }
    }*/

    /**
     * SISP_IFACE_CARD_003 — set the card maintenance status.
     *
     * <ol>
     *   <li>When {@code server} is 01 (temporary loss), 02 (permanent loss) or 03 (remove
     *       loss), the gateway does not validate {@code bank}, {@code bankno} or
     *       {@code cityno}, so these requests may cross regions.</li>
     *   <li>When {@code server} is 04 (cancel) or 11 (activate on collection), every field
     *       except {@code bankno} is validated strictly, so these requests may not cross
     *       regions.</li>
     * </ol>
     *
     * @param bean request carrying the operation code and card identifiers
     * @return {@code message} is the gateway reply: {@code 00} on success, {@code 01} if
     *         the operation had already succeeded, otherwise the failure reason
     */
//    @RequestMapping(value = "/setCard", method = RequestMethod.POST, produces = "application/json")
//    public Result setCardStatus(CardInfoBean bean) {
//        String server = bean.getServer();
//        String cardno = bean.getCardNo();
//        String cernum = bean.getId();
//        String name = bean.getName();
//        String bank = bean.getBank();
//        String cityno = bean.getCityno();
//        String bankno = bean.getBankno();
//        CardInfoBus bus = new CardInfoBus();
//        String message = bus.setCard(server, cardno, cernum, name, bank, bankno, cityno);
//        String result = Constants.RESULT_MESSAGE_SUCCESS;
//        if (message == null) {
//            result = Constants.RESULT_MESSAGE_ERROR;
//            message = "Provincial gateway call failed.";
//        }
//        return this.result(result, message, null);
//    }

    /**
     * SISP_IFACE_CARD_004 — card production progress query.
     *
     * <p>TSB terminals send the ID number only; large-format terminals and WeChat send both
     * ID number and name. When the name is missing it is looked up from the card record
     * first.
     */
    @RequestMapping(value = "/getCardStatus", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result getCardStatus(@RequestBody CardInfoBean bean)throws Exception{
        insertTransLog(bean,Constants.BUSINESSCODE.get("getCardStatus"));
        String cernum = bean.getId();
        String name = bean.getName();
        String result = "";
        String message = "";
        CardInfoBus bus = new CardInfoBus();
        if(StringUtils.isEmpty(name)){
            name=bus.getCardAllInfo("", cernum).getName();
        }
        if ((name != null && !name.equals("")) && (cernum != null && !cernum.equals(""))) {
            SettingCardQuery info = bus.getCardProgressQuery(cernum, name);
            if (info != null && info.getErr().equals("OK")) {
                info.setCardtype(DictionaryUtil.getDictName(Constants.CARDTYPE_GROUP, info.getCardtype()));
                info.setTransacttype(DictionaryUtil.getDictName(Constants.APPLYTYPE_GROUP, info.getTransacttype()));
                info.setBank(DictionaryUtil.getDictName(Constants.BANK_GROUP, info.getBank()));
                result = Constants.RESULT_MESSAGE_SUCCESS;
                message = "Card production progress retrieved.";
            } else {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = info.getErr();
            }
            return this.result(result, message, info);
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            if(Constants.TSB.equals(bean.getChannelcode())){
                message = "Please enter a valid ID number.";
            }else{
                message = "Please enter a valid ID number and name.";
            }
            return this.result(result, message, null);
        }
    }

    /**
     * SISP_IFACE_CARD_005 — activate a collected card.
     *
     * @param bean request carrying card number, ID number and name
     * @return <ol>
     *   <li>{@code statusCode} 200 means the gateway call succeeded; read {@code message}
     *       for the outcome.</li>
     *   <li>{@code message}: {@code 00} success, {@code 01} already succeeded earlier,
     *       otherwise the failure reason.</li>
     *   <li>{@code statusCode} 0 means the gateway call failed and {@code message} holds
     *       the reason.</li>
     * </ol>
     */
    @RequestMapping(value = "/setStart", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result setStart(@RequestBody CardInfoBean bean) {
        insertTransLog(bean,Constants.BUSINESSCODE.get("setStart"));
        String cardno = bean.getCardNo();
        String cernum = bean.getId();
        String name = bean.getName();
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        if ((!"".equals(cardno) && cardno != null) && (!"".equals(cernum) && cernum != null)
                && (!"".equals(name) && name != null)) {
            CardInfoBus bus = new CardInfoBus();
            message = bus.setStart(cardno, cernum, name);
            if (message == null || "".equals(message)) {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "Provincial gateway call failed.";
            }else if(message.equals("00")){
            	result = Constants.RESULT_MESSAGE_SUCCESS;
            	message = "Card activated.";
            }else if(message.equals("01")){
            	 result = Constants.RESULT_MESSAGE_ERROR;
                 message = "Card was already activated.";
            }else{
            	result = Constants.RESULT_MESSAGE_ERROR;
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "Card number, ID number and name are all required.";
        }
        return this.result(result, message, null);
    }

    /**
     * SISP_IFACE_CARD_006 — report the card lost (temporary).
     *
     * @param bean request carrying card number, ID number and name
     * @return same contract as {@link #setStart}: {@code 00} success, {@code 01} already
     *         reported lost, otherwise the failure reason
     */
    @RequestMapping(value = "/setLoss", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result setLoss(@RequestBody CardInfoBean bean) {
        insertTransLog(bean,Constants.BUSINESSCODE.get("setLoss"));
        String cardno = bean.getCardNo();
        String cernum = bean.getId();
        String name = bean.getName();
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        if ((!"".equals(cardno) && cardno != null) && (!"".equals(cernum) && cernum != null)
                && (!"".equals(name) && name != null)) {
            CardInfoBus bus = new CardInfoBus();
            message = bus.setLoss(cardno, cernum, name);
            if (message == null || "".equals(message)) {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "Provincial gateway call failed.";
            }else if(message.equals("00")){
            	result = Constants.RESULT_MESSAGE_SUCCESS;
            	message = "Card reported lost (temporary).";
            }else if(message.equals("01")){
            	 result = Constants.RESULT_MESSAGE_ERROR;
                 message = "Card was already reported lost.";
            }else{
            	result = Constants.RESULT_MESSAGE_ERROR;
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "Card number, ID number and name are all required.";
        }
        return this.result(result, message, null);
    }

    /**
     * SISP_IFACE_CARD_007 — remove a suspension.
     *
     * @param bean request carrying card number, ID number and name
     * @return same contract as {@link #setStart}: {@code 00} success, {@code 01} already
     *         removed, otherwise the failure reason
     */
    @RequestMapping(value = "/setHanging", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Result setHanging(@RequestBody CardInfoBean bean) {
        insertTransLog(bean,Constants.BUSINESSCODE.get("setHanging"));
        String cardno = bean.getCardNo();
        String cernum = bean.getId();
        String name = bean.getName();
        String result = Constants.RESULT_MESSAGE_SUCCESS;
        String message = "";
        if ((!"".equals(cardno) && cardno != null) && (!"".equals(cernum) && cernum != null)
                && (!"".equals(name) && name != null)) {
            CardInfoBus bus = new CardInfoBus();
            message = bus.setHanging(cardno, cernum, name);
            if (message == null || "".equals(message)) {
                result = Constants.RESULT_MESSAGE_ERROR;
                message = "Provincial gateway call failed.";
            }else if(message.equals("00")){
            	result = Constants.RESULT_MESSAGE_SUCCESS;
            	message = "Suspension removed.";
            }else if(message.equals("01")){
            	 result = Constants.RESULT_MESSAGE_ERROR;
                 message = "Suspension was already removed.";
            }else{
            	result = Constants.RESULT_MESSAGE_ERROR;
            }
        } else {
            result = Constants.RESULT_MESSAGE_ERROR;
            message = "Card number, ID number and name are all required.";
        }
        return this.result(result, message, null);
    }
    
    
    /**
     * Card status query against the provincial card gateway.
     *
     * <p>Calls the {@code getCard} SOAP operation directly with Axis rather than going
     * through {@code CardInfoBus}, because this endpoint predates that facade.
     *
     * @param bean request carrying card number, ID number and name — all three required
     * @return {@code data} is the gateway's raw reply when it equals {@code "OK"};
     *         otherwise a failure envelope describing the problem
     * @author fuweifeng
     * @version V.1.0.0
     */
    @RequestMapping(value = "/getCard", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
	public   Result getCard(@RequestBody CardInfoBean bean){
    	 insertTransLog(bean,Constants.BUSINESSCODE.get("getCard"));
		String result  = Constants.RESULT_MESSAGE_SUCCESS;
		String message = "Success.";
		String wsResult = "";
		if(bean!=null){
			String idCard = bean.getId();//ID number
			String cardNo = bean.getCardNo();//card number
			String name = bean.getName();//name
			
			if((idCard!=null && idCard.length()>0) &&(cardNo!=null && cardNo.length()>0) && (name!=null && cardNo.length()>0)){
				Service service = new Service();
				Call call;
				Map<String , String > map = new HashMap<String , String>();
				try {
					String url = Config.getInstance().get("CARDBUESS_URL");//provincial card gateway
					call = (Call) service.createCall();
		            call.setTargetEndpointAddress( new URL(url));
		            call.setOperationName( new QName("http://ws.apache.org/axis2","getCard"));//operation to invoke
		            call.setSOAPActionURI("http://ws.apache.org/axis2" + "getCard");
		            call.addParameter(new QName("http://ws.apache.org/axis2", "user"), XMLType.SOAP_STRING, ParameterMode.IN);//declare an input parameter
		            call.addParameter(new QName("http://ws.apache.org/axis2", "pass"), XMLType.SOAP_STRING, ParameterMode.IN);//declare an input parameter
		            call.addParameter(new QName("http://ws.apache.org/axis2", "aaz500"), XMLType.SOAP_STRING, ParameterMode.IN);//declare an input parameter
		            call.addParameter(new QName("http://ws.apache.org/axis2", "aac002"), XMLType.SOAP_STRING, ParameterMode.IN);//declare an input parameter
		            call.addParameter(new QName("http://ws.apache.org/axis2", "aac003"), XMLType.SOAP_STRING, ParameterMode.IN);//declare an input parameter
		            call.setReturnType(XMLType. SOAP_STRING); //return type
		            wsResult = (String) call.invoke(new String[]{"szytj","szytj12333",cardNo, idCard, name});///invoke with parameters
		            if((wsResult!=null && wsResult.length()>0) && wsResult.equals("OK")){
		            	 return this.result(result, message, wsResult);
		            }else{
		            	 return this.result(result, message);
		            }
				} catch (Exception e) {
					result =  Constants.RESULT_MESSAGE_ERROR;
					message = "getCard call failed: " + e.getMessage();
				}
				 return this.result(result, message);
			}else{
				result =  Constants.RESULT_MESSAGE_ERROR;
				message ="Card number, ID number and name are all required.";
				return this.result(result, message);
			}
			
		}else{
			result =  Constants.RESULT_MESSAGE_ERROR;
			message = "getCard requires a request body.";
			return this.result(result, message);
		}
		

	}

    /**
     * Queues an asynchronous transaction record for the business-analytics subsystem.
     *
     * <p>Runs on the shared thread pool, so a failure here never fails the business call —
     * it is logged and swallowed.
     *
     * @param bean     the request being served, used to populate the audit record
     * @param funcName business code recorded against the transaction
     */
    public void insertTransLog(CardInfoBean bean,String funcName){
        try {
            TransBean transBean=new TransBean();
            transBean.setUserid(bean.getId());
            transBean.setChannelcode(bean.getChannelcode());
            transBean.setBusinesscode(funcName);
            transBean.setDeviceid(bean.getDeviceid());
            ThreadPoolUtil.getThreadPool().execute(new ThreadPoolTask(transBean));
        } catch (Exception e) {
            logger.error("Failed to queue the card transaction log: " + e.getMessage());
        }
    }

}
