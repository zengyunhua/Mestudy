package com.tecsun.sisp.npm.util;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class LogUtil {

    public static boolean addLog(long userId, long appId, String description) {
        try {
            Client client = new Client();
            WebResource webResource = client
                    .resource("http://" + AppConfig.required("inner_proxy_ip") + ":" +
                            AppConfig.required("inner_proxy_port") +
                            AppConfig.get("platform_context_path", "/sisp") +
                            "/core/operatelog/addOperateLog");
            Form form = new Form();
            form.add("userId", userId);
            form.add("appId", appId);
            form.add("description", description);

            ClientResponse response = webResource.type(
                    MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, form);
            client.destroy();
            return null != response;
        } catch (Exception e) {
            return false;
        }
    }

}
