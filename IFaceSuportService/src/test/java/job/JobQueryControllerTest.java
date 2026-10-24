package job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.client.WebClient;

/**
 * @date 2016年12月16日 上午11:11:19 
 */
public class JobQueryControllerTest {

	private static String basic_url = "http://127.0.0.1:8081/IFaceSuportService-xg-temp/iface/job";
	
	/***********************************************招聘岗位*******开始***************************************************/
	@Test
	public void getJobOffer(){
		String test_url = basic_url+""+"/getJobOffer";
		JsonObject jo = new JsonObject();
		jo.addProperty("pxzd", "QYMC,GWMC");
		jo.addProperty("pxfs", "ASC,DESC");
		jo.addProperty("ym", "1");
		jo.addProperty("myhs", "2");
		jo.addProperty("deviceid", "214353315145");
		jo.addProperty("channelcode", "TSB");
		jo.addProperty("channelType", "01");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	@Test
	public void getJobOfferInfo(){
		String test_url = basic_url+""+"/getJobOfferInfo";//fee78004-40e5-0f51-b62f-951574007281
		JsonObject jo = new JsonObject();
		jo.addProperty("gwid", "fee78004-40e5-0f51-b62f-951574007281");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	@Test
	public void getPersonJob(){
		String test_url = basic_url+""+"/getPersonJob";//fee78004-40e5-0f51-b62f-951574007281
		JsonObject jo = new JsonObject();
		jo.addProperty("pxzd", "QYMC,GWMC");
		jo.addProperty("pxfs", "ASC,DESC");
		jo.addProperty("ym", "1");
		jo.addProperty("myhs", "2");
		jo.addProperty("grxxid", "fee78004-40e5-0f51-b62f-951574007281");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	/***********************************************招聘岗位*******结束***************************************************/
	
	@Test
	public void addJobSeekerInfo(){
		String test_url = basic_url+""+"/addJobSeekerInfo";
		JsonObject jo = new JsonObject();
		jo.addProperty("zjlxid", "01");//身份证
		jo.addProperty("zjhm", "123456789123456789");//51062319740829644X，张玉
		jo.addProperty("sblx", "3");//设备类型(3:自助一体机)
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}

	@Test
	public void modifyJobSeekerInfo(){
		String test_url = basic_url+""+"/modifyJobSeekerInfo";
		JsonObject jo = new JsonObject();
		jo.addProperty("qzzid", "421e2e6f-737e-938b-5094-5987ec3ed8ee");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	@Test
	public void personUserLogin(){
		String test_url = basic_url+""+"/personUserLogin";
		JsonObject jo = new JsonObject();
		jo.addProperty("dllx", "2");
		jo.addProperty("zjhm", "123456789123456789");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	@Test
	public void isPhoneNumExist(){
		String test_url = basic_url+""+"/isPhoneNumExist";
		JsonObject jo = new JsonObject();
		jo.addProperty("sjhm", "13481070000");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	
	
	/***********************************************字典表*******开始***************************************************/
	
	@Test
	public void getAllDic(){
		String test_url = basic_url+""+"/getAllDic";
		JsonObject jo = new JsonObject();
		jo.addProperty("key", "GZ");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	@Test
	public void getAllSecondDic(){
		String test_url = basic_url+""+"/getAllSecondDic";
		JsonObject jo = new JsonObject();
		jo.addProperty("key", "GWFL");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	/***********************************************字典表*******结束***************************************************/
	
	/***********************************************求职者刷身份证注册*******开始***************************************************/
	
	@Test
	public void registerByIdCard(){
		String test_url = basic_url+""+"/registerByIdCard";
		JsonObject jo = new JsonObject();
		jo.addProperty("sfzh", "123456789123456799");
		jo.addProperty("xm", "aaa");
		jo.addProperty("xbid", "1");
		jo.addProperty("mz", "汉");
		jo.addProperty("csny", "2016-12-20");
		jo.addProperty("jtzz", "天堂小区2号509");
		jo.addProperty("zxzz", "天堂小区2号509");
		jo.addProperty("qfjg", "天堂公安大队分队");
		jo.addProperty("qssj", "2016-12-20");
		jo.addProperty("jssj", "2016-12-20");
//		jo.addProperty("sfzzplj", "c:/");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	/***********************************************求职者求职者刷身份证注册*******结束***************************************************/
	
	/**
	 * 获得招聘会列表
	 */
	@Test
	public void queryJobFairs(){
		String test_url = basic_url+""+"/queryJobFairs";
		JsonObject jo = new JsonObject();
		jo.addProperty("pxzd", "ZPHID");
		jo.addProperty("pxfs", "ASC");
		jo.addProperty("ym", "1");
		jo.addProperty("myhs", "2");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	/**
	 * 查看指定招聘会信息
	 */
	@Test
	public void getZphInfo(){
		String test_url = basic_url+""+"/getZphInfo";
		JsonObject jo = new JsonObject();
		jo.addProperty("zphid", "293da8cd-bb94-e629-d34d-9220d55a0052");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}

	/**
	 * 获得企业列表
	 */
	@Test
	public void queryCompanys(){
		String test_url = basic_url+""+"/queryCompanys";
		JsonObject jo = new JsonObject();
		jo.addProperty("pxzd", "MC");
		jo.addProperty("pxfs", "ASC");
		jo.addProperty("ym", "1");
		jo.addProperty("myhs", "2");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}

	/**
	 * 查看指定企业信息
	 */
	@Test
	public void getCompanyInfo(){
		String test_url = basic_url+""+"/getCompanyInfo";
		JsonObject jo = new JsonObject();
		jo.addProperty("qyid", "6f536cce-f3b8-4226-ebea-d7d2d3e65acc");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}

	/**
	 * 获得企业的图片及视频信息
	 */
	@Test
	public void getCompanyImgAndVideo(){
		String test_url = basic_url+""+"/getCompanyImgAndVideo";
		JsonObject jo = new JsonObject();
		jo.addProperty("qyid", "6f536cce-f3b8-4226-ebea-d7d2d3e65acc");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	/**
	 * 获得求职者的求职志愿列表
	 */
	@Test
	public void getQzyxByQzz(){
		String test_url = basic_url+""+"/getQzyxByQzz";
		JsonObject jo = new JsonObject();
		jo.addProperty("pxzd", "ZYSM");
		jo.addProperty("pxfs", "ASC");
		jo.addProperty("ym", "1");
		jo.addProperty("myhs", "2");
		jo.addProperty("qzzid", "421e2e6f-737e-938b-5094-5987ec3ed8ee");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}

	/**
	 * 新建求职志愿
	 * 
	 * zy、gz 没有在字典中查到信息
	 */
	@Test
	public void addJobDirection(){
		String test_url = basic_url+""+"/addJobDirection";
		JsonObject jo = new JsonObject();
		jo.addProperty("grxxid", "fee78004-40e5-0f51-b62f-951574007281");
		jo.addProperty("zy", "ASC");//??
		jo.addProperty("gz", "1");//??
		jo.addProperty("gwfl", "g02000");//计算机软件
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	/**
	 * 删除求职志愿
	 */
	@Test
	public void dropJobDirection(){
		String test_url = basic_url+""+"/dropJobDirection";
		JsonObject jo = new JsonObject();
		jo.addProperty("IDS", "");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}

	/**
	 * 根据招聘会获得参会企业信息
	 */
	@Test
	public void getCompanyByZph(){
		String test_url = basic_url+""+"/getCompanyByZph";
		JsonObject jo = new JsonObject();
		jo.addProperty("pxzd", "QYMC");
		jo.addProperty("pxfs", "ASC");
		jo.addProperty("ym", "1");
		jo.addProperty("myhs", "2");
		jo.addProperty("zphid", "293da8cd-bb94-e629-d34d-9220d55a0052");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	/**
	 * 获得所有的专业
	 */
	@Test
	public void getAllMajor(){
		String test_url = basic_url+""+"/getAllMajor";
		JsonObject jo = new JsonObject();
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
	/**
	 * 投递岗位
	 */
	@Test
	public void sendJobChoice(){
		String test_url = basic_url+""+"/sendJobChoice";
		JsonObject jo = new JsonObject();
		jo.addProperty("grxxid", "fee78004-40e5-0f51-b62f-951574007281");
		jo.addProperty("qyids", "6f536cce-f3b8-4226-ebea-d7d2d3e65acc");
		jo.addProperty("gwids", "fee78004-40e5-0f51-b62f-951574007281");
		jo.addProperty("xxly", "3");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
	
//	public static void main(String[] args) {
//		SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-DD");
//		String format = df.format(String.valueOf("2016-12-21"));
//		System.out.println(format);
//	}
	
	
	/**
	 * 刷身份证添加身份证信息
	 * 
	 * 没有身份证照片？？？？？
	 * 
	 * TODO .....
	 */
	@Test
	public void addIdCardInfo(){
		String test_url = basic_url+""+"/addIdCardInfo";
		JsonObject jo = new JsonObject();
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}

	/**
	 * 查看政策文件
	 */
	@Test
	public void getPolicyDocuments(){
		String test_url = basic_url+""+"/getPolicyDocuments";
		JsonObject jo = new JsonObject();
		jo.addProperty("id", "");
		jo.addProperty("ptlx", "");
		String jsonStr = (String) WebClient.getWebClient(test_url, jo, String.class);
		System.out.println(jsonStr);
	}
}
