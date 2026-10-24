package com.tecsun.sisp.iface.server.util;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.DevBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * ClassName: BusinessFilter
 * Description:  拦截器：
 * 1.判断是否合法用户;
 * <p/>
 * CreateTime： 2015年06月07日 13时:44分
 */
public class BusinessFilter implements Filter {

    private static Logger logger = LoggerFactory.getLogger(BusinessFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 验证前端页面（TSB,selfterminal,weixin...）传入的tokenId是否合法
     * 如果有设备编码，则验证传入的设备编码是否合法（TSB）
     *
     * @param request
     * @param response
     * @param chain
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setCharacterEncoding("UTF-8");
        String ip = CommUtil.getIpAddr(req);
        String tokenId = req.getParameter("tokenId");
        String deviceid = req.getParameter("deviceid");
        String method = req.getParameter("method");
        System.out.println("========拦截器==========deviceid:"+deviceid);
        //String boy=getBody(req);
        try {
            String redisIP = StringUtils.isNotEmpty(tokenId) ? JedisUtil.getValue(tokenId) : "";
            if (StringUtils.isBlank(redisIP)) {
                String msg = "不存在此token，或超时被删除,tokenId:" + tokenId + ",IP：" + ip;
                logger.error("IFACE_ERRORCODE:IFAC-EERR-301,"+msg);
                resp.getWriter().print(GlobalResult.error_301(msg));
            } else {
                if (!redisIP.equals(ip)) {
                    logger.error("IFACE_ERRORCODE:IFAC-EERR-301,拦截非法访问,tokenId：" + tokenId + ",原IP：" + redisIP + ",拦截IP：" + ip);
                    resp.getWriter().print(GlobalResult.error_301("拦截非法访问，请重新登录"));
                }else{
                    if(!StringUtils.isBlank(method)){
                    //第三方公司拦截
                            //1.先验证url地址
                                String uri = req.getRequestURI();
                                if(!method.equals(uri.substring(uri.lastIndexOf("/")+1))){
                                    logger.error("IFACE_ERRORCODE:IFAC-EERR-301,拦截非法访问,第三方接口方法method非法,method:"+method+",接口uri:"+uri);
                                    resp.getWriter().print(GlobalResult.error_301("拦截非法访问，接口方法非法"));
                                }else{
                                    //2.验证用户接口权限
                                    String userkey = Constants.USERKEY + tokenId;
                                    String userid = JedisUtil.getValue(userkey);
                                    boolean flag = checkUser(userid, method);
                                    if(flag){
                                        JedisUtil.setValue(userkey, userid);
                                        JedisUtil.setValue(tokenId, ip);
                                        String userAreaid = Constants.USERAREAID + tokenId;
                                        String areaid = JedisUtil.getValue(userAreaid);
                                        JedisUtil.setValue(userAreaid, areaid);
                                        chain.doFilter(request, response);
                                    }else{
                                        String msg="拦截非法访问,该用户没有此接口权限";
                                        logger.error("IFACE_ERRORCODE:IFAC-EERR-303,"+msg);
                                        resp.getWriter().print(GlobalResult.error_303(msg));
                                    }
                                }
                        }else{
                            //如果校验ip通过，则校验设备编码
                            boolean flag=checkDev(deviceid);
                            if (flag) {
                                JedisUtil.setValue(tokenId, ip);
                                chain.doFilter(request, response);
                            }else{
                                String msg="拦截非法访问,deviceid:"+deviceid+",tokenId：" + tokenId + ",拦截IP：" + ip;
                                logger.error("IFACE_ERRORCODE:IFAC-EERR-303,"+msg);
                                resp.getWriter().print(GlobalResult.error_303(msg));
                            }
                        }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("IFACE_ERRORCODE:IFAC-EERR-302,BusinessFilter 拦截出错," + e.getMessage());
            resp.getWriter().print(GlobalResult.error_302("BusinessFilter 拦截出错," + e.getMessage()));
        }
    }

    private boolean checkDev(String deviceid) {
        if (StringUtils.isNotEmpty(deviceid)) {
            String result="";
            try {
                String dev_uri = DictionaryUtil.getHost(Constants.DEV_URI) + "/" + deviceid;
                result = DictionaryUtil.getClientRequest(dev_uri);
                if (!"".equals(result)) {
                    DevBean devBean = JsonMapper.buildNormalMapper().fromJson(result, DevBean.class);
                    if (devBean != null) {
                        if (StringUtils.equals(devBean.getStatus(), "0")) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("===checkDev:"+deviceid+",result:===" + result);
                logger.error(e.getMessage());
            }
        } else {
            return true;
        }
        return false;
    }

    public static String getBody(HttpServletRequest request) throws IOException {
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        body = stringBuilder.toString();
        return body;
    }

    public void destroy() {

    }
    private boolean checkUser(String userid , String method) throws IOException{
        try{
            String url = DictionaryUtil.getHost(Constants.INTERFACE_CHECKUSER)+"?userid="+userid+"&method="+method;
            String result = DictionaryUtil.getClientRequest( url);
            Map map = JsonMapper.buildNormalMapper().fromJson(result, Map.class);
            Map data = (Map) map.get("result");
            if(data == null) return false;else return true;
        }catch(Exception e){
            logger.error("===userid:"+userid+",method:===" + method);
            logger.error(e.getMessage());
            return false;
        }

    }
}
