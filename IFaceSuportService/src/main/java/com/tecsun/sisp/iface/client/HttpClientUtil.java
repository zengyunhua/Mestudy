package com.tecsun.sisp.iface.client;

import com.tecsun.sisp.iface.common.util.JsonMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import java.io.IOException;
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
 * @since 2015-06-09
 */
public class HttpClientUtil {

    private static final String APPLICATION_JSON = "application/json";

    public static final String getData(int port, String url, Object bean, boolean isPost) throws Exception {
        return getData(url, bean, isPost, IFaceHttpsClient.isVerify, port);
    }

    public static final String getData(String url, Object bean, boolean isPost, boolean isVerify, int port) throws Exception {
        DefaultHttpClient httpClient = isVerify ? new SSLClient(port) : new DefaultHttpClient();
        httpClient.getParams().setIntParameter("http.connection.timeout", 10000);
        httpClient.getParams().setIntParameter("http.socket.timeout", 30000);
        try {
            org.apache.http.client.methods.HttpRequestBase req = isPost
                    ? doPost(url, JsonMapper.buildNonEmptyMapper().toJson(bean)) : doGet(url);
            String token = IFaceHttpsClient.currentToken();
            if (token != null && !token.isEmpty() && !url.endsWith("/checkLogin")) {
                req.setHeader("X-Token-Id", token);
            }
            HttpResponse response = httpClient.execute(req);
            HttpEntity entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            String result = entity == null ? null : EntityUtils.toString(entity, HTTP.UTF_8);
            if (status < HttpStatus.SC_OK || status >= HttpStatus.SC_MULTIPLE_CHOICES) {
                throw new IOException("Interface HTTP request failed with status " + status);
            }
            return result;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
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
