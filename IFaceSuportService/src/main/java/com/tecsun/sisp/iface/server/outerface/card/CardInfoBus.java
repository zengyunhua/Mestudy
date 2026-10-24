package com.tecsun.sisp.iface.server.outerface.card;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.card.CardAllInfo;
import com.tecsun.sisp.iface.common.vo.card.CardBaseInfo;
import com.tecsun.sisp.iface.common.vo.card.SettingCardQuery;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 *  省厅接口
* @ClassName: CardInfoBus 
*
 */
public class CardInfoBus extends CardInfoBusClient {
	
	public final static Logger logger = Logger.getLogger(CardInfoBus.class);
    //private static String TEST_URL = "http://58.54.132.3:3333/sisp/iface/cardInfo/";
    private static String TEST_URL = " http://10.128.8.73:7001/hbCardService/services/CardService";
	public CardInfoBus(){}
	
	/**
	 * 领卡启用
	* @Title: setStart 
	* @param  cardno  社保卡号
	* @param  cernum  身份证号码
	* @param  name    姓名 
	* @return String    00成功  / 01之前已经成功   /失败原因
	* @throws
	 */
	public String setStart(String cardno , String cernum , String name){
		try {
			String result = stub.setStart(USER, PASSWORD, cardno, cernum, name, CITYNO);
			return result;
		} catch (RemoteException e) {
			logger.error("***调用省厅--领卡启用--接口失败***" , e);
			return null;
		}
	}
	
	/**
	 * 临时挂失
	* @param  cardno  卡号
	* @param  cernum  身份证号码
	* @param  name    姓名
	* @return String    00成功/01之前已经挂失/挂失失败的原因
	* @throws
	 */
	public String setLoss(String cardno , String cernum , String name ){
		try {
			String result = stub.setLoss(USER, PASSWORD, cardno, cernum, name , "");
			return result;
		} catch (RemoteException e) {
			logger.error("***调用省厅--临时挂失--接口失败***" , e);
			return null;
		}
	}
	
	/**
	 * 解挂   -- 03为解挂
	* @param  cardno  卡号
	* @param  cernum  身份证号码
	* @param  name    姓名
	* @return String    00成功/01之前已经成功/失败原因
	* @throws
	 */
	public String setHanging( String cardno , String cernum , String name){
		String result = setCard("03", cardno, cernum, name, "", "", "");
		return result;
	}

	/**
	 * 设置卡的维护状态
	 * 1.当server为01临时挂失、02正式挂失、03解除挂失时，系统对bank、bankno、cityno不作判断，允许跨地区执行这些请求。
	 * 2.当server为04注销、11领卡启用时，系统不判断bankno，其他全部数据项都将做严格判断，不允许跨地区执行这两项请求。
	* @param  server  业务代码（2位，01临时挂失、02正式挂失、03解除挂失、04注销、11领卡启用）
	* @param  cardno  社保卡号（9位）
	* @param  cernum  身份证号
	* @param  name    姓名
	* @param  bank	服务银行
	* @param  bankno	银行卡号
	* @param  cityno	发卡城市代码
	* @return String  	00成功/01之前已经成功/失败原因
	* @throws
	 */
	public String setCard(String server , String cardno , String cernum , 
		String name , String bank , String bankno , String cityno){
		try {
			String result = stub.setCard(USER, PASSWORD, server, cardno, cernum, name, bank, bankno, cityno);
			return result;
		} catch (RemoteException e) {
			logger.error("***调用省厅--设置卡的维护状态--接口失败***" , e);
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		String cardno = "赵华友";
		String cernum = "110101199001010015";
		CardInfoBus bus = new CardInfoBus();
		CardAllInfo info = bus.getCardAllInfo("", cernum);
		String cardno1 = "赵华友";
	}
	
	/**
	 * 卡基础数据（不包含个人照片)  社保卡号和身份证号码至少有一项不为空
	* @Title: getCardBaseInfo 
	* @param @param cardno  社保卡号
	* @param @param cernum  身份证号码
	* @param @return    设定文件 
	* @return CardBaseInfo    返回类型 
	* @throws
	 */
	public CardBaseInfo getCardBaseInfo(String cardno , String cernum){
		CardBaseInfo info = new CardBaseInfo();
			try {
				String result = stub.getAC01(USER, PASSWORD, cardno, cernum);
				result = (new StringBuilder("<shengting>")).append(result).append("</shengting>").toString();
				Document document = DocumentHelper.parseText(result);
				Element element = document.getRootElement();
				Element description = element.element("ERR");
				String err = description.getText();
//				System.out.println(err);
				info.setErr(err);
				if(err.equals("OK")){
					info = getCardBaseInfo(element , err);
				}
			} catch (RemoteException e) {
				info.setErr("连接省厅接口出错");
				logger.error("***调用省厅--卡基础数据（不包含个人照片)--接口失败***" , e);
			} catch (DocumentException e) {
				info.setErr("解析报文出错");
				logger.error("***解析省厅--卡基础数据（不包含个人照片)--报文出错***" , e);
			}
		return info;
	}

	/**
	 * 卡基础数据（包含个人照片） 社保卡号和身份证号码至少有一项不为空
	* @Title: getCardAllInfo 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param cardno
	* @param @param cernum
	* @param @return    设定文件 
	* @return CardAllInfo    返回类型 
	* @throws
	 */
	public CardAllInfo getCardAllInfo(String cardno , String cernum) {
        CardAllInfo info = new CardAllInfo();
            try {
                String result = stub.getData(USER, PASSWORD, cardno, cernum);
                result = (new StringBuilder("<shengting>")).append(result).append("</shengting>").toString();
                Document document = DocumentHelper.parseText(result);
                Element element = document.getRootElement();
                Element description = element.element("ERR");
                String err = description.getText();
                info.setErr(err);
                if (err.equals("OK")) {
                    info = getCardBaseInfo(element, err);
                }
            } catch (RemoteException e) {
                System.out.println("catch1:***调用省厅--卡基础数据（包含个人照片)--接口失败***");
                info.setErr("连接省厅接口出错");
                info.setErr("连接省厅接口出错");
                logger.error("***调用省厅--卡基础数据（包含个人照片)--接口失败***", e);
            } catch (DocumentException e) {
                System.out.println("catch2");
                info.setErr("解析报文出错");
                logger.error("***解析省厅--卡基础数据（包含个人照片)--报文出错***", e);
            }
            return info;
    }
    /**
     * 获取卡数据  社保卡号和身份证号码至少有一项不为空
     * @Title: getCard
     * @param @param cardno  社保卡号
     * @param @param cernum  身份证号码
     * @param @return    设定文件
     * @return CardBaseInfo    返回类型
     * @throws
     */
    public CardAllInfo getCard(String cardno , String cernum){
        CardAllInfo allInfo = new CardAllInfo();
        try {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("cardNo", cardno);
            jsonObject.addProperty("id", cernum);
            String wsresult = (String)getWebClient("getCardAllInfo", jsonObject, String.class);
            JsonObject jsonsresult = new JsonParser().parse(wsresult).getAsJsonObject().getAsJsonObject("result");
            String wresult=new Gson().toJson(jsonsresult);
            allInfo= JsonMapper.buildNormalMapper().fromJson(wresult, CardAllInfo.class);
        } catch (RemoteException e) {
            allInfo.setErr("连接省厅接口出错");
            logger.error("***调用省厅--卡基础数据（不包含个人照片)--接口失败***" , e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allInfo;
    }
	/**
	 * 制卡进度查询
	* @Title: getCardProgressQuery 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return SettingCardQuery    返回类型 
	* @throws
	 */
	public SettingCardQuery getCardProgressQuery(String cernum , String name){
		SettingCardQuery info = new SettingCardQuery();
		try {
			String result = stub.getAZ03(USER, PASSWORD, cernum, name);
			result = (new StringBuilder("<shengting>")).append(result).append("</shengting>").toString();
			Document document = DocumentHelper.parseText(result);
			Element element = document.getRootElement();
			Element description = element.element("ERR");
			String err = description.getText();
//			System.out.println(err);
			info.setErr(err);
			if(err.equals("OK")){
				info = getSettingCardQuery(element , err);
			}
		} catch (RemoteException e) {
			info.setErr("连接省厅接口出错");
			logger.error("***解析省厅--制卡进度查询--报文出错***" , e);
		} catch (DocumentException e) {
			info.setErr("解析报文出错");
			logger.error("***解析省厅--制卡进度查询--报文出错***" , e);
		}
		return info;
	}
	
	//卡基础数据查询组装实体类
	private CardAllInfo getCardBaseInfo(Element element , String err){
		CardAllInfo po = new CardAllInfo ();
		po.setErr(err);
		//社会保障卡卡号
		String cardno = element.element("AAZ500").getText();
		po.setSbkh(cardno);
		//社会保障号码
		String cernum = element.element("AAC002").getText();
		po.setSfzh(cernum);
		//姓名
		String name = element.element("AAC003").getText();
		po.setName(name);
		//性别
		String sex = element.element("AAC004").getText();
		po.setSex(sex);
		//民族
		String nation = element.element("AAC005").getText();
		po.setEthnic(nation);
		//出生日期
		String birthday = element.element("AAC006").getText();
		po.setBirthday(birthday);
		//人员状态
		String person_status = element.element("AAC008").getText();
		po.setPersonstatus(person_status);
		//户口性质
		String AAC009 = element.element("AAC009").getText();
		po.setRegisttype(AAC009);
		//户口所在地址
		String address = element.element("AAC010").getText();
		po.setRegaddr(address);
		//联系手机
		String mobile = element.element("MOBILE").getText();
		po.setPhoneno(mobile);
		//联系电话
		String phone = element.element("AAE005").getText();
		po.setTelno(phone);
		//通讯地址
		String mobile_address = element.element("AAE006").getText();
		po.setMailaddr(mobile_address);
		//邮政编码
		String AAE007 = element.element("AAE007").getText();
		po.setZipcode(AAE007);
		//开卡银行
		String bankid = element.element("AAE008").getText();
		po.setBank(bankid);
		//银行卡号
		String bankno = element.element("AAE010").getText();
		po.setBankno(bankno);
		//邮箱地址
		String email = element.element("EMAIL").getText();
		po.setEmail(email);
		//单位编号
		String unitno = element.element("AAB001").getText();
		po.setDwbh(unitno);
		//单位名称
		String unitname = element.element("AAB004").getText();
		po.setDwmc(unitname);
		//照片  BASE64编码的相片数据
		if(element.element("PHOTO") != null){
			String PHOTO = element.element("PHOTO").getText();
			po.setPhoto(PHOTO);
		}
		return po;
	}
	
	//制卡进度查询组装实体类
	private SettingCardQuery getSettingCardQuery(Element element , String err){
		SettingCardQuery po = new SettingCardQuery();
		po.setErr(err);
		String cardno = element.element("AAZ500").getText();//社会保障卡卡号
		po.setSbkh(cardno);
		String cernum = element.element("AAC002").getText();//社会保障号码
		po.setSfzh(cernum);
		String name = element.element("AAC003").getText();//姓名
		po.setName(name);
		String CARDTYPE = element.element("CARDTYPE").getText();
		po.setCardtype(CARDTYPE);
		String TRANSACTTYPE = element.element("TRANSACTTYPE").getText();
		po.setTransacttype(TRANSACTTYPE);
		String BATCHNO = element.element("BATCHNO").getText();
		po.setBatchno(BATCHNO);
		String AAB301 = element.element("AAB301").getText();
		po.setCitycode(AAB301);
		String ORGANID = element.element("ORGANID").getText();
		po.setOrganid(ORGANID);
		String AAE008 = element.element("AAE008").getText();
		po.setBank(AAE008);
		String APPLYTIME = element.element("APPLYTIME").getText();
		po.setApplytime(APPLYTIME);
		String BANKTIME0 = element.element("BANKTIME0").getText();
		po.setBanktime0(BANKTIME0);
		String BANKFINISHTIME0 = element.element("BANKFINISHTIME0").getText();
		po.setBankfinishtime0(BANKFINISHTIME0);
		String INSURETIME = element.element("INSURETIME").getText();
		po.setInsuretime(INSURETIME);
		String INSUREFINISHTIME0 = element.element("INSUREFINISHTIME0").getText();
		po.setInsurefinishtime0(INSUREFINISHTIME0);
		String INSUREFINISHTIME = element.element("INSUREFINISHTIME").getText();
		po.setInsurefinishtime(INSUREFINISHTIME);
		String PROVINCETIME = element.element("PROVINCETIME").getText();
		po.setProvincetime(PROVINCETIME);
		String CITYTIME = element.element("CITYTIME").getText();
		po.setCitytime(CITYTIME);
		String GETTIME = element.element("GETTIME").getText();
		po.setGettime(GETTIME);
		String GETTIME1 = element.element("GETTIME1").getText();
		po.setGettime1(GETTIME1);
		String REMARKS = element.element("REMARKS").getText();
		po.setRemarks(REMARKS);
		String VALIDTAG = element.element("VALIDTAG").getText();
		po.setValidtag(VALIDTAG);
		return po;
	}
	
	/**
	 *  分析制卡进度查询数据获取卡状态
	* @Title: setPoStatus 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @return    设定文件 
	* @return SettingCardQuery    返回类型 
	* @throws
	 */
	public SettingCardQuery setPoStatus(SettingCardQuery po){
		String batchno = po.getBatchno(); //批次号
		String banktime0 = po.getBanktime0();//导出银行预开户
		String cardno = po.getSbkh();//卡号
		String bankfinishtime0 = po.getBankfinishtime0();//银行开户回盘时间
		
		
		String status = "";
		//1.若有批次号、BANKTIME0(导出银行预开户)为空，表示已加入批次;
		if(batchno!=null && !"".equals(batchno) && (banktime0 == null || "".equals(banktime0))){
			status = "已加入批次";
		}
		//2.若有批次号、卡号，银行开户回盘时间为空，BANKTIME0(导出银行预开户)不为空，表示已分配卡号;
		if(batchno!=null&&!batchno.equals("") && cardno!=null&&!"".equals(cardno) &&
				(bankfinishtime0 == null || bankfinishtime0.equals("")) && 
				banktime0!=null&&!"".equals(banktime0)){
			status = "已分配卡号";
		}
		//3.若有金融账户、个人账户、并且有预开户返回时间，表示开户成功;
		//4.若有导出制卡时间(INSURETIME)，制卡回盘时间为空的(INSUREFINISHTIME)，表示制卡中;
		//5.若经办机构接收时间不为空，且领卡时间为空，表示制卡成功;
		//6.若领卡时间不为空，表示银行领卡;
		//7.若金融账户为空 或者 个人账户为空、并且有银行预开户返回时间(即BANKFINISHTIME0不为空)，表示开户失败;

		po.setStatus(status);
		return po;
	}
    public static Object getWebClient(String url , JsonObject json , Class resultClass){
        url = TEST_URL + url ;
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, json.toString());
        client.destroy();

        return response.getEntity(resultClass);
    }
}
