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
        resp.setContentType("application/json;charset=UTF-8");
        String ip = CommUtil.getIpAddr(req);
        String tokenId = req.getHeader("X-Token-Id");
        if (StringUtils.isBlank(tokenId)) {
            tokenId = req.getParameter("tokenId"); // Legacy clients; do not log this value.
        }
        boolean authorized = false;
        try {
            String redisIP = StringUtils.isNotBlank(tokenId) ? JedisUtil.getValue(tokenId) : null;
            if (StringUtils.isBlank(redisIP) || !redisIP.equals(ip)) {
                logger.warn("Rejected invalid or expired interface session");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().print(GlobalResult.error_301("登录已失效，请重新登录"));
                return;
            }
            // The channel is assigned at login. A caller-supplied method/deviceid cannot
            // select a less restrictive authorization path.
            String channel = JedisUtil.getValue(Constants.CHANNELKEY + tokenId);
            if (StringUtils.isBlank(channel)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().print(GlobalResult.error_301("登录已失效，请重新登录"));
                return;
            }
            if ("ThirdSystem".equals(channel)) {
                String uri = req.getRequestURI();
                String actualMethod = uri.substring(uri.lastIndexOf('/') + 1);
                if (StringUtils.isBlank(actualMethod) || !actualMethod.matches("[A-Za-z0-9_]+")) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().print(GlobalResult.error_303("无效接口方法"));
                    return;
                }
                String requestedMethod = req.getParameter("method");
                if (StringUtils.isNotBlank(requestedMethod) && !actualMethod.equals(requestedMethod)) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().print(GlobalResult.error_303("接口方法不匹配"));
                    return;
                }
                String userkey = Constants.USERKEY + tokenId;
                String userid = JedisUtil.getValue(userkey);
                if (StringUtils.isBlank(userid) || !checkUser(userid, actualMethod)) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().print(GlobalResult.error_303("没有此接口权限"));
                    return;
                }
                JedisUtil.setValue(userkey, userid);
                String userAreaid = Constants.USERAREAID + tokenId;
                String areaid = JedisUtil.getValue(userAreaid);
                if (StringUtils.isNotBlank(areaid)) {
                    JedisUtil.setValue(userAreaid, areaid);
                }
            } else if (!"TSB".equals(channel) && !"SelfService".equals(channel)
                    && !"NetPortal".equals(channel) && !"WeChat".equals(channel)) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().print(GlobalResult.error_303("未知登录渠道"));
                return;
            } else if (!checkDev(req.getParameter("deviceid"))) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().print(GlobalResult.error_303("设备校验失败"));
                return;
            }
            // Keep the server-assigned channel and token on the same sliding expiry.
            if ("SelfService".equals(channel)) {
                JedisUtil.setValue(tokenId, ip, Constants.SELFSERVICETIMEOUT);
                JedisUtil.setValue(Constants.CHANNELKEY + tokenId, channel, Constants.SELFSERVICETIMEOUT);
            } else {
                JedisUtil.setValue(tokenId, ip);
                JedisUtil.setValue(Constants.CHANNELKEY + tokenId, channel);
            }
            authorized = true;
        } catch (Exception e) {
            logger.error("BusinessFilter failed", e);
            if (!resp.isCommitted()) {
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                resp.getWriter().print(GlobalResult.error_302("认证服务暂时不可用"));
            }
        }
        if (authorized) {
            chain.doFilter(request, response);
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
                logger.error("Device validation failed");
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
    static boolean hasPermission(String response, String method) throws IOException {
        if (StringUtils.isBlank(response) || StringUtils.isBlank(method)) return false;
        Map map = JsonMapper.buildNormalMapper().fromJson(response, Map.class);
        if (map == null || !"200".equals(map.get("statusCode"))
                || !(map.get("result") instanceof Map)) return false;
        return method.equals(((Map) map.get("result")).get("funCode"));
    }
    private boolean checkUser(String userid , String method) throws IOException{
        try{
            String url = DictionaryUtil.getHost(Constants.INTERFACE_CHECKUSER)+"?userid="+java.net.URLEncoder.encode(userid, "UTF-8")+"&method="+java.net.URLEncoder.encode(method, "UTF-8");
            String result = DictionaryUtil.getClientRequest( url);
            return hasPermission(result, method);
        }catch(Exception e){
            logger.error("Permission validation failed for method {}", method);
            logger.error(e.getMessage());
            return false;
        }

    }
}
