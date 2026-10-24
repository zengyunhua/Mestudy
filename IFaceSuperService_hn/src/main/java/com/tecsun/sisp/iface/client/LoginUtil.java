package com.tecsun.sisp.iface.client;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.util.JsonMapper;
import com.tecsun.sisp.iface.common.vo.Result;

import java.security.MessageDigest;

/**
 * ClassName: LoginUtil
 * Description:  接口鉴权工具类
 * CreateTime： 2015年06月09日 20时:30分
 */
public class LoginUtil {/*

    public static String checkLogin(int port,String url, String type) throws Exception {
        LoginVO vo = new LoginVO();
        vo.setUserName(generateValue(type));
        vo.setNet_password(generateValue(type));
        vo.setType(type);
        Result result = null;
        String msg = HttpClientUtil.getData(port,url, vo, true);
        if (msg != null && msg.length() > 0) {
            result = JsonMapper.buildNormalMapper().fromJson(msg, Result.class);
        }
        if(result!=null){
        	 if (!result.getStatusCode().equals(Constants.RESULT_MESSAGE_SUCCESS)) {
                 throw new Exception("接口鉴权失败！渠道：" + type);
             }
        	 return result.getData().toString();
        }else{
        	return "";
        }
       
    }

    private static String generateValue(String param) {
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

    private static String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        final char[] hexCode = "0123456789abcdef".toCharArray();
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }*/
}
