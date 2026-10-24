package com.tecsun.sisp.fun.common;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * Created by zhangqingjie on 15-5-18.
 */
public class CommUtil {

    /**
     * 测试主方法
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
    	JsonObject json = new JsonObject();
    	json.addProperty("userName", "1");
    	json.addProperty("userPwd", generateValue("1").toUpperCase());
    	String url = "http://127.0.0.1/sisp/interfacefunction/user/userLogin?aaa=11?";
    	ClientConfig cc = new DefaultClientConfig();
    	Client client = Client.create(cc);
		WebResource webResource = client.resource(url);
    	ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON_TYPE)
    			.type(MediaType.APPLICATION_JSON_TYPE)
    			.post(ClientResponse.class, json.toString());
		client.destroy();
		String re = response.getEntity(String.class);
    	System.out.println(re);
    	Map map = JsonMapper.buildNormalMapper().fromJson(re, Map.class);
    	System.out.println(map);
    	Map resultMap = (Map) map.get("result");
    	if(resultMap == null) System.out.println(123);
    	else {
    		System.out.println(resultMap.get("areaId"));
    	}
    	String s = url.substring(0 , url.indexOf("?"));
    	System.out.println(s.substring(s.lastIndexOf("/")+1));
    }

    /**
     * 格式化日期
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDate(Date date,String parrten){
        SimpleDateFormat f = new SimpleDateFormat(parrten);
        String sDate = f.format(date);
        return sDate;
    }

    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal= Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
        return currYearLast;
    }

    /**
     * 获取某年某月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return cal.getTime();
    }

    public static Date parseDate(String date) throws Exception {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            throw new Exception("时间转换失败");
        }
    }
    
    private static final char[] hexCode = "0123456789abcdef".toCharArray();

    /**
     * 用md5加密字符串
     *
     * @return
     */
    public static String generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return toHexString(messageDigest);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

}
