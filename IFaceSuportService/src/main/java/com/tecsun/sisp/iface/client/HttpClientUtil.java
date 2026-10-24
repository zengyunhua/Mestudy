package com.tecsun.sisp.iface.client;

import com.tecsun.sisp.iface.common.util.JsonMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * ClassName: HttpClientUtil
 * Description:
 * @author Zhang Qingjie
 * @since 2015-06-09
 */
public class HttpClientUtil {

    private static final String APPLICATION_JSON = "application/json";

    public static final String getData(int port, String url, Object bean, boolean isPost) throws Exception {
        String result = null;
        HttpResponse response = null;
        String json = JsonMapper.buildNonEmptyMapper().toJson(bean);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        if (IFaceHttpsClient.isVerify) {
            httpClient = new SSLClient(port);
        }
        if (isPost) {
            response = httpClient.execute(doPost(url, json));
        } else {
            response = httpClient.execute(doGet(url));
        }
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, HTTP.UTF_8);
            }
        }
        return result;
    }

    public static final String getData(String url, Object bean, boolean isPost,boolean isVerify,int port) throws Exception {
        String result = null;
        HttpResponse response = null;
        String json = JsonMapper.buildNonEmptyMapper().toJson(bean);
        DefaultHttpClient httpClient = new DefaultHttpClient();
        if (isVerify) {
            httpClient = new SSLClient(port);
        }
        if (isPost) {
            response = httpClient.execute(doPost(url, json));
        } else {
            response = httpClient.execute(doGet(url));
        }
        if (response != null) {
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                result = EntityUtils.toString(resEntity, HTTP.UTF_8);
            }
        }
        return result;
    }

    static HttpPost doPost(String url, String encoderJson) throws Exception {
        StringEntity se = new StringEntity(encoderJson, "UTF-8");
        se.setContentType(APPLICATION_JSON);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON));
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        httpPost.setEntity(se);
        return httpPost;
    }

    private static HttpGet doGet(String url) {
        HttpGet httpPost = new HttpGet(url);
        httpPost.addHeader(HTTP.CONTENT_TYPE, APPLICATION_JSON);
        return httpPost;
    }
}
