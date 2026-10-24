package com.tecsun.sisp.iface.client;

import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class Test {

	public static void main(String[] args) throws Exception {
		  JsonObject json1 = new JsonObject();
	      json1.addProperty("aac002","44012719600110021X");
		  json1.addProperty("pageno",1);
		  json1.addProperty("pagesize",1);
		  
		  
//		String wsResult2 = conn("https://61.28.113.182:9443/sisp/iface/sec/FuncXa021",json1.toString());
//	    System.out.println("Contribution record 2 = "+wsResult2);
	    
	    
	    String wsResult3 = (String) getWebClient("http://61.28.113.182:8080/sisp/iface/sec/FuncXa021",json1 ,String.class);
	    System.out.println("Contribution record 3 = "+wsResult3);
	    
	}
	
	static String conn(String url , String json) throws Exception{
		 String result = "";
		 HttpResponse response = null;
		 DefaultHttpClient httpClient = new DefaultHttpClient();
		 httpClient = new SSLClient(9443);
		 response = httpClient.execute(HttpClientUtil.doPost(url, json));
		 if (response != null) {
	            HttpEntity resEntity = response.getEntity();
	            if (resEntity != null) {
	                result = EntityUtils.toString(resEntity, HTTP.UTF_8);
	            }
	        }
		 return result;
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
