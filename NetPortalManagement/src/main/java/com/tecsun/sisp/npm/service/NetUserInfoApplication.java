package com.tecsun.sisp.npm.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.xx.util.property.Config;
import org.apache.log4j.Logger;

import bee.cloud.engine.db.DataType;
import bee.cloud.engine.db.cnd.CndFactory;
import bee.cloud.engine.db.core.Cnd;
import bee.cloud.engine.db.siud.Select;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.tecsun.sisp.npm.bean.NetUserInfoVO;
import com.tecsun.sisp.npm.util.MD5Generator;
import com.tecsun.sisp.npm.util.PublicMethod;
import com.tecsun.sisp.npm.util.StaticBean;

/**
 * created by fuweifeng on 2015-6-1.
 */
@Path("/npmanagement/netUserInfo")
public class NetUserInfoApplication extends NetSuperApplication {
    static Logger logger = Logger.getLogger(NetUserInfoApplication.class);

    public NetUserInfoApplication(@Context HttpServletRequest request, @Context UriInfo ui) throws SQLException {
        super(request, ui);
    }

    /*
       @version
       @parameter
       @return
       @throws
       @ 在网办系统中查询出用户信息；
     */
    @GET
    @Path("/queryNetUserInfoList")
    public String queryNetUserInfoList(@QueryParam("name") String name, @QueryParam("cernum") String cernum, @QueryParam("cardno") String cardno) {
        NetUserInfoVO vo = new NetUserInfoVO(doe);
        try {
            Cnd cnd = CndFactory.getCnd(DataType.ORACLE, "1", Cnd.EQ, "1");
            if (!PublicMethod.isEmptyStr(name)) {
                cnd.and(NetUserInfoVO.NAME, Cnd.LIKE, "%" + name.trim() + "%");
            }

            if (!PublicMethod.isEmptyStr(cernum)) {
                cnd.and(NetUserInfoVO.CERNUM, Cnd.LIKE, "%" + cernum.trim() + "%");
            }

            if (!PublicMethod.isEmptyStr(cardno)) {
                cnd.and(NetUserInfoVO.CARDNO, Cnd.LIKE, "%" + cardno.trim() + "%");
            }
            Select select = vo.getSelect().from(NetUserInfoVO.class).where(cnd);
            Map<String, Object> map = new HashMap<String, Object>();
            if (this.pageno == 1)
                map.put(StaticBean.PARAMETER_CODE.total.toString(), select.count());
            select.limit(pagesize == 0 ? 5 : this.pagesize).offset(this.pageno);
            List list = select.go();
            if(list != null && list.size()>0){
            	vo = (NetUserInfoVO)list.get(0);
            }
            return ok(vo);
        } catch (SQLException e) {
            logger.error("在网办系统中查询用户信息出错" + e);
            return error("在网办系统中查询用户信息出错");
        }
    }

    /*
       @version
       @parameter
       @return
       @throws
       @ 在网办系统中修改用户信息的登陆密码；
     */
    @GET
    @Path("/updateNetUserInfo")
    public String updateNewsInfo(@QueryParam("cernum") String cernum,@QueryParam("name") String name) {
        NetUserInfoVO vo = new NetUserInfoVO(doe);
        String result = "";
        try {
        	Cnd cnd = CndFactory.getCnd(DataType.ORACLE, "1", Cnd.EQ, "1");
            if (!PublicMethod.isEmptyStr(name)) {
                cnd.and(NetUserInfoVO.NAME, Cnd.LIKE, "%" + name.trim() + "%");
            }

            if (!PublicMethod.isEmptyStr(cernum)) {
                cnd.and(NetUserInfoVO.CERNUM, Cnd.LIKE, "%" + cernum.trim() + "%");
            }
            Select select = vo.getSelect().from(NetUserInfoVO.class).where(cnd);
            List list = select.go();
            if (list.size() > 0) {
                vo = (NetUserInfoVO) list.get(0);
                Random random = new Random();
                int pwd = random.nextInt(999999);
                vo.setNet_password( MD5Generator.generateValue(String.valueOf(pwd)));
                vo.setIs_update("1");
                vo.update();
                result ="sucess";
            }else{
            	result ="error"; //已开通帐号
            }
            return ok(result);
        } catch (SQLException e) {
            logger.error("开通帐号出错：" + e);
            return error("开通帐号出错");
        }
    }

    /*
       @version
       @parameter
       @return
       @throws
       @ 在网办系统中根据身份号查询看用户的详细信息；
     */
    @GET
    @Path("/queryNetUserInfoById/{cernum}")
    public String queryNetUserInfoById(@PathParam("cernum") String cernum) {
        NetUserInfoVO vo = new NetUserInfoVO(doe);
        try {
            /*Select select = vo.getSelect().from(NetUserInfoVO.class).where(doe.cnd(NetUserInfoVO.CERNUM, Cnd.EQ, cernum));
            List list  = select.go();*/
            vo.setCernum(cernum);
            vo.load();
            return ok(vo);
        } catch (SQLException e) {
            logger.error("在网办系统中查询看用户的详细信息出错" + e);
            return error("在网办系统中查询看用户的详细信息出错");
        }
    }

    /*
       @version
       @parameter
       @return
       @throws
       @ 在网办系统中根据身份号、社保卡号查询看用户的照片信息；
     */
    @GET
    @Path("/queryUserPhoto/{cernum}/{cardno}")
    public String queryUserPhoto(@PathParam("cernum") String cernum, @PathParam("cardno") String cardno) throws SQLException {
        String carInterfacePath = "http://" + Config.getInstance().get("inner_proxy_ip") + ":" +
                Config.getInstance().get("inner_proxy_port") +
                Config.getInstance().get("platform_context_path") +
                "/ifaceInner/cardInfo/getCardAllInfo";
        JsonObject json = new JsonObject();
        json.addProperty("id", cernum);
        json.addProperty("cardNo", cardno);
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(carInterfacePath);
        ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, json.toString());
        client.destroy();

        String result = response.getEntity(String.class);
        return ok(result);
    }

    /*
       @version
       @parameter
       @return
       @throws
       @ 重置用户密码
     */
    @POST
    @Path("/updateNetUserInfoPwd/{cernum}/{net_password}")
    public String updateNetUserInfoPwd(@PathParam("cernum") String cernum,@PathParam("net_password") String net_password) {
        NetUserInfoVO vo = new NetUserInfoVO(doe);
        try {
        	Cnd cnd = CndFactory.getCnd(DataType.ORACLE, "1", Cnd.EQ, "1");
            if (!PublicMethod.isEmptyStr(cernum)) {
                cnd.and(NetUserInfoVO.CERNUM, Cnd.LIKE, "%" + cernum.trim() + "%");
            }
            Select select = vo.getSelect().from(NetUserInfoVO.class).where(cnd);
            List list = select.go();
            if (list.size() > 0) {
                vo = (NetUserInfoVO) list.get(0);
            }
            int i =0;
            if(vo.getIs_update() != null && !vo.getIs_update().equals("")){
              i = Integer.parseInt(vo.getIs_update())+1;
            }
            if (!PublicMethod.isEmptyStr(net_password)) {
            	net_password = MD5Generator.generateValue(net_password); 
            }
            vo.setIs_update(String.valueOf(i));
            vo.setNet_password(net_password);
            vo.update();
            return ok("sucess");
        } catch (SQLException e) {
        	e.printStackTrace();
            logger.error("重置密码出错：" + e);
            return error("重置密码出错");
        }
    }
}
