

import org.junit.Test;

import com.google.gson.JsonObject;
import com.tecsun.sisp.iface.client.WebClient;
import com.tecsun.sisp.iface.common.util.PicUtil;

/**
 * @date 2016年11月17日 下午3:40:07 
 */
public class CsspControllerTest {

	private static String TEST_URL = "http://127.0.0.1:8081/IFaceSuportService/iface/cssp/";
//	private static String TEST_URL = "http://10.132.1.73:8080/sisp/iface/cssp/";
	
	/**
	 * 申领条件测试（是否可以申领）
	 */
	@Test
	public void canApply(){
		String url = TEST_URL + "register?deviceid=864881020737852";
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("certNum", "42100319810420154X22222333");
		jsonObject.addProperty("name", "xx");
		jsonObject.addProperty("soCardNum", "xx");
		String result = (String) WebClient.getWebClient(url, jsonObject,String.class);
		System.out.println(result);
	}
	
	/**
	 * 精准发卡测试
	 */
	@Test
	public void uploadCardPicInfo(){
		String url = TEST_URL + "uploadCardPicInfo?deviceid=864881020745178";
		JsonObject jsonObject = new JsonObject();
		
		jsonObject.addProperty("certNum", "420902199108072155");
		jsonObject.addProperty("name", "张志威");
		jsonObject.addProperty("soCardNum", "K41607920");
		
		//jsonObject.addProperty("agentName", "汤唯");
		//jsonObject.addProperty("agentcertNum", "666681199109248888");
		jsonObject.addProperty("no", "0");//0_本人领卡
		jsonObject.addProperty("base64String",PicUtil.GetImageStr("d:/tangwei.jpg"));
		String result = (String) WebClient.getWebClient(url, jsonObject,String.class);
		System.out.println(result);
	}
	
	/**
	 * 滞留卡登记测试
	 */
	@Test
	public void registerTest(){
		String url = TEST_URL + "register?deviceid=864881020745178";
		//for(int i = 0;i < 15; i++){
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("certNum", "420116201309121720");
			jsonObject.addProperty("name", "吴诗诗");
			jsonObject.addProperty("soCardNum", "K41798165");
			jsonObject.addProperty("reason", "联系不上参保人");
			
			String result = (String) WebClient.getWebClient(url, jsonObject,String.class);
			System.out.println(result);
		//}
	}
	
}
