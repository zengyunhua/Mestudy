package com.tecsun.sisp.iface.server.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.tecsun.sisp.iface.common.util.Constants;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Dictionary lookups and inter-service POST calls.
 *
 * <p>Two responsibilities that share a single reason to exist: both need the internal
 * proxy address. {@link #getHost(String)} builds an absolute URL from
 * {@code inner_proxy_ip}, {@code inner_proxy_port} and {@code platform_context_path}, and
 * the POST helpers use it to reach peer modules.
 *
 * <p>The dictionary itself is served by the platform's core module and cached in Redis
 * under a key derived from the group id. A cache miss triggers an HTTP round trip; a
 * failure there returns an empty result rather than throwing, so a caller cannot tell
 * "no entries" from "core module down". Log at the call site if that distinction matters.
 *
 * <p>Uses Jersey 1.x. The client is created per call, which is wasteful — a shared
 * {@code Client} instance would be correct, since Jersey clients are thread-safe and
 * expensive to build.
 */
public class DictionaryUtil {

    private static Logger logger = LoggerFactory.getLogger(DictionaryUtil.class);

    public static String getHost(String uri) {
        com.tecsun.sisp.iface.server.util.Config config = com.tecsun.sisp.iface.server.util.Config.getInstance();
        return "http://" + config.get("inner_proxy_ip") + ":" +
                config.get("inner_proxy_port") + config.get("platform_context_path") + uri;
    }

    /**
     * post 请求
     * @param object 入参,json格式
     * @param resourceUrl  请求url
     * @return json格式数据
     */
    public static String postClientRequest(String object,String resourceUrl) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(resourceUrl);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, object);
        client.destroy();
        String result = response.getEntity(String.class);
        return result;
    }

    /**
     * post 请求
     * @param object 入参,json格式
     * @param resourceUrl  请求url
     * @return json格式数据
     */
    public static String postClientRequest(Form object,String resourceUrl) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(resourceUrl);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED)
                .post(ClientResponse.class, object);
        client.destroy();
        String result = response.getEntity(String.class);
        
        return result;
    }

    /**
     * get 请求
     * @param resourceUrl 请求URL
     * @return  json格式数据
     */
    public static String getClientRequest(String resourceUrl){
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        WebResource webResource = client.resource(resourceUrl);
        ClientResponse response = webResource.queryParams(queryParams).get(ClientResponse.class);
        client.destroy();
        String result = response.getEntity(String.class);
        return result;
    }

    /**
     * 根据大类和小类编码获取小类名称
     * @param groupId
     * @param code
     * @return
     * @throws Exception
     */
    public static String getDictName(String groupId,String code)throws Exception{
        try {
            if(StringUtils.isNotEmpty(groupId)&&StringUtils.isNotEmpty(code)) {
                String key= Constants.DICTIONGROUPKEY.replace("groupId", groupId);
                String result=JedisUtil.getValue(key);
                String[] dictionaryArray=result.split(",");
                for (String bean:dictionaryArray){
                    String[] dictbean=bean.split(":");
                    if(dictbean!=null&&dictbean.length==2) {
                        if (bean.split(":")[0].equals(code)) {
                            return bean.split(":")[1];
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("字典组转换出错,groupId:" + groupId + ",code:" + code);
        }
        return "";
    }

}
