import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import sun.misc.BASE64Encoder;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class Test {

	public static void main(String[] args) {
		JsonObject loginJosn = new JsonObject();
		loginJosn.addProperty("registtype", "1");
		loginJosn.addProperty("idCard", "110101199001010015");
		loginJosn.addProperty("photo", GetImageStr("E://110101199001010015.jpg"));
		loginJosn.addProperty("name", "魏木清");
		String loginUrl = "http://localhost:5555/iface/iface/face/registOne";
//		String loginResult = (String) getWebClient(loginUrl, loginJosn, String.class);
//		System.out.println(loginResult);
		
		JsonObject isCheckJson = new JsonObject();
		isCheckJson.addProperty("type", 1);
		isCheckJson.addProperty("isCheckType", 1);
		isCheckJson.addProperty("idCard", "110101199001010015");
		String isCheckUrl = "http://localhost:5555/iface/iface/face/isCheck";
		String isCheckResult = (String) getWebClient(isCheckUrl, isCheckJson, String.class);
		System.out.println(isCheckResult);
	}
	
    public static String GetImageStr(String path) {// voice转化成base64字符串
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getWebClient(String url , JsonObject json , Class resultClass){

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
