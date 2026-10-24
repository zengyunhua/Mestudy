package com.tecsun.sisp.iface.server.controller;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import com.tecsun.sisp.iface.common.vo.Result;

/**
 * 控制器基类
 *
 *
 */
public abstract class BaseController {

    protected int pageno;
    protected int pagesize;
    protected HttpServletRequest request;

    public static String RESULT_MESSAGE_SUCCESS = "200"; // 成功
    public static String RESULT_MESSAGE_ERROR = "0"; // 失败

    public void set(HttpServletRequest request) {
        this.request = request;
        this.pagesize = getIntParameter("pagesize", 15);
        this.pageno = getIntParameter("pageno", 1);
    }

    public void set(HttpServletRequest request, Object obj) {
        this.set(request);
    }

    public Result result(String result, String message) {
        return result(result, message, null);
    }

    public Result result(String result, String message, Object obj) {
        return new Result(result, message, obj);
    }

    /*public Result ok(long total, String message, Object obj) {
        return result(RESULT_MESSAGE_SUCCESS, total, message, obj);
    }*/

   /* public Result ok(String message, Object obj) {
        return result(RESULT_MESSAGE_SUCCESS, 0, message, obj);
    }*/

    public Result error(String message, Object obj) {
        return result(RESULT_MESSAGE_ERROR, message, obj);
    }

   /* public Result result(String result, long total, String message, Object obj) {
        return new Result(result,  message, obj);
    }*/

    public int getIntParameter(String key, int defaultValue) {
        if (key != null && !"".equals(key)) {
            String str = request.getParameter(key);
            if (str != null && !"".equals(str)) {
                if (request.getParameter(key).matches("[0-9]+")) {
                    return Integer.parseInt(request.getParameter(key));
                }
            }
        }
        return defaultValue;
    }

    /**
     * iface接口统一方法
     *
     * @Title: getWebClient
     * @param url
     *            访问接口路径
     * @param json
     *            传值
     * @param resultClass
     *            返回的格式
     * @return Object 返回类型
     * @throws
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object getWebClient(String url, JsonObject json,
                                      Class resultClass) {

        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .post(ClientResponse.class, json.toString());
        client.destroy();

        return response.getEntity(resultClass);
    }

    /**
     * 统一form表单接口调用 1.传值form表单 2.接收JSON格式返回
     *
     * @param url
     * @param form
     * @param resultClass
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object getWebClient(String url, Form form, Class resultClass) {
        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_FORM_URLENCODED)
                .post(ClientResponse.class, form);
        client.destroy();

        return response.getEntity(resultClass);
    }

    /**
     * GET请求
     *
     * @param url
     * @param resultClass
     * @return
     */
    public static Object getWebClient(String url, Class resultClass) {

        ClientConfig cc = new DefaultClientConfig();
        Client client = Client.create(cc);
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .get(ClientResponse.class);
        client.destroy();

        return response.getEntity(resultClass);
    }

    /**
     * 生成32位编码
     *
     * @return string
     */
    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }

    /**
     * 对get请求头的url及参数进行url编码，编码格式为utf-8
     * @param source
     * @return
     */
    public static String urlEncode(String source) throws UnsupportedEncodingException{
        return URLEncoder.encode(source,"utf8");
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
			/*String p1 = param.substring(0,param.indexOf("="));
			String p2 = param.substring(param.indexOf("=") + 1);
			param = p1 + "=" + urlEncode(p2);*/
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(30);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            result = "";
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection
                    .setRequestProperty("user-agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }

        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
