package com.tecsun.sisp.npm.service;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.tecsun.sisp.npm.bean.NetUserInfoVO;
import com.tecsun.sisp.npm.repository.NetPortalRepository;
import com.tecsun.sisp.npm.util.AppConfig;
import com.tecsun.sisp.npm.util.MD5Generator;
import com.tecsun.sisp.npm.util.PublicMethod;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

@Path("/npmanagement/netUserInfo")
public class NetUserInfoApplication extends NetSuperApplication {
    private static final Logger LOGGER = Logger.getLogger(NetUserInfoApplication.class);
    private static final NetPortalRepository REPOSITORY = new NetPortalRepository();

    public NetUserInfoApplication(@Context HttpServletRequest request, @Context UriInfo ui) {
        super(request, ui);
    }

    @GET
    @Path("/queryNetUserInfoList")
    public String queryNetUserInfoList(@QueryParam("name") String name,
                                       @QueryParam("cernum") String cernum,
                                       @QueryParam("cardno") String cardno) {
        try (Connection connection = openConnection()) {
            List<NetUserInfoVO> users = REPOSITORY.findUsers(connection, name, cernum, cardno, pageno, pagesize);
            return ok(users.isEmpty() ? null : users.get(0));
        } catch (SQLException e) {
            LOGGER.error("在网办系统中查询用户信息出错", e);
            return error("在网办系统中查询用户信息出错");
        }
    }

    @GET
    @Path("/updateNetUserInfo")
    public String updateNewsInfo(@QueryParam("cernum") String cernum, @QueryParam("name") String name) {
        try (Connection connection = openConnection()) {
            List<NetUserInfoVO> users = REPOSITORY.findUsers(connection, name, cernum, null, 1, 1);
            if (users.isEmpty()) {
                return ok("error");
            }
            NetUserInfoVO user = users.get(0);
            int updateCount = parseCount(user.getIs_update()) + 1;
            String password = String.format("%06d", new Random().nextInt(1000000));
            int updated = REPOSITORY.updatePassword(connection, user.getCernum(),
                    MD5Generator.generateValue(password), String.valueOf(updateCount));
            return ok(updated == 1 ? "sucess" : "error");
        } catch (SQLException e) {
            LOGGER.error("开通帐号出错", e);
            return error("开通帐号出错");
        }
    }

    @GET
    @Path("/queryNetUserInfoById/{cernum}")
    public String queryNetUserInfoById(@PathParam("cernum") String cernum) {
        try (Connection connection = openConnection()) {
            return ok(REPOSITORY.findUser(connection, cernum));
        } catch (SQLException e) {
            LOGGER.error("在网办系统中查询用户详细信息出错", e);
            return error("在网办系统中查询用户详细信息出错");
        }
    }

    @GET
    @Path("/queryUserPhoto/{cernum}/{cardno}")
    public String queryUserPhoto(@PathParam("cernum") String cernum,
                                 @PathParam("cardno") String cardno) {
        String interfacePath = "http://" + AppConfig.required("inner_proxy_ip") + ":" +
                AppConfig.required("inner_proxy_port") + AppConfig.get("platform_context_path", "/sisp") +
                "/ifaceInner/cardInfo/getCardAllInfo";
        JsonObject json = new JsonObject();
        json.addProperty("id", cernum);
        json.addProperty("cardNo", cardno);
        ClientConfig clientConfig = new DefaultClientConfig();
        Client client = Client.create(clientConfig);
        try {
            WebResource resource = client.resource(interfacePath);
            ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
                    .type(MediaType.APPLICATION_JSON)
                    .post(ClientResponse.class, json.toString());
            return ok(response.getEntity(String.class));
        } finally {
            client.destroy();
        }
    }

    @POST
    @Path("/updateNetUserInfoPwd/{cernum}/{net_password}")
    public String updateNetUserInfoPwd(@PathParam("cernum") String cernum,
                                       @PathParam("net_password") String netPassword) {
        try (Connection connection = openConnection()) {
            NetUserInfoVO user = REPOSITORY.findUser(connection, cernum);
            if (user == null) {
                return error("用户不存在");
            }
            int updateCount = parseCount(user.getIs_update()) + 1;
            String hashedPassword = PublicMethod.isEmptyStr(netPassword)
                    ? netPassword : MD5Generator.generateValue(netPassword);
            int updated = REPOSITORY.updatePassword(connection, cernum, hashedPassword,
                    String.valueOf(updateCount));
            return ok(updated == 1 ? "sucess" : "error");
        } catch (SQLException e) {
            LOGGER.error("重置密码出错", e);
            return error("重置密码出错");
        }
    }

    private int parseCount(String value) {
        if (PublicMethod.isEmptyStr(value)) {
            return 0;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
