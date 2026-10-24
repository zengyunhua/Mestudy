/**
 * 版权所有(C) 广东德生科技有限公司 2013-2020<br>
 * 创建日期 2011-5-14
 */
package com.tecsun.sisp.npm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import redis.clients.jedis.Jedis;

/**
 * 功能说明：公共类
 *
 */
public class PublicMethod {
    protected PublicMethod() {
    }

    /**
     * 获取IP地址
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 模糊查询不同datasource的key
     *
     * @param jedis
     * @param tableName
     * @param unique
     * @param id
     * @return
     */
    public static String getKey(Jedis jedis, String source, String tableName, String unique, String id) {
        String key = "";
        String uniqueOrId = isEmptyStr(unique) ? ":id:" + id : unique;
        if (!PublicMethod.isEmptyStr(source)) {
            key = source + ":" + tableName + uniqueOrId;
            System.out.println("key:" + key);
            return key;
        }
        String pkey = "*" + tableName + uniqueOrId;
        System.out.println("pkey:" + pkey);
        Set<String> set = jedis.keys(pkey);
        Iterator<String> iterator = set.iterator();
        if (iterator.hasNext())
            key = iterator.next();
        System.out.println("key:" + key);
        return key;
    }

    /**
     * 判断一个字符串是否为空 null 和 空字符
     *
     * @param str String
     * @return Boolean 为空返回True 不为空返回 False
     */
    public static boolean isEmptyStr(Object str) {
        return str == null || str.toString().trim().length() < 1;
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
}
