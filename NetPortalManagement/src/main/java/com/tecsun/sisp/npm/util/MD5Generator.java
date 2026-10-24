/**
 * 版权所有(C) 广东德生科技有限公司 2013-2020<br>
 * 创建日期 2011-5-14
 */
package com.tecsun.sisp.npm.util;

import java.security.MessageDigest;

/**
 * 功能说明：md5加密
 *
 */
public class MD5Generator {

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
