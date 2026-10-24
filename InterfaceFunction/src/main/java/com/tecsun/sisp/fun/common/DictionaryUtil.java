package com.tecsun.sisp.fun.common;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.metadata.Database;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * ClassName: DictionaryUtil
 * Description:
 * CreateTime： 2015年06月05日 14时:49分
 */
public class DictionaryUtil {

    public static String getHost(String uri) {
        Config config = Config.getInstance();
        return "http://" + config.get("inner_proxy_ip") + ":" +
                config.get("inner_proxy_port") + config.get("platform_context_path") + uri;
    }

    /**
     * post 请求
     *
     * @param object      入参,json格式
     * @param resourceUrl 请求url
     * @return json格式数据
     */
    public static String postClientRequest(String object, String resourceUrl) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(getHost(resourceUrl));
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, object);
        client.destroy();
        String result = response.getEntity(String.class);
        return result;
    }

    /**
     * get 请求
     *
     * @param resourceUrl 请求URL
     * @return json格式数据
     */
    public static String getClientRequest(String resourceUrl) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        WebResource webResource = client.resource(resourceUrl);
        ClientResponse response = webResource.queryParams(queryParams).get(ClientResponse.class);
        client.destroy();
        String result = response.getEntity(String.class);
        return result;
    }

    public static String getValue(String key) throws Exception {
        JedisPool publicJedisPool = null;
        Jedis publicJedis = null;
        String value="";
        try {

            publicJedisPool = DataBase.getJedisPublicPool();
            publicJedis = publicJedisPool.getResource();
            value = publicJedis.get(key);
        } catch (Exception e) {
            throw new Exception("获取reids失败，key:" + key, e);
        } finally {
            if (publicJedis != null) {
                publicJedisPool.returnResource(publicJedis);
            }
        }
        return value;
    }
}
