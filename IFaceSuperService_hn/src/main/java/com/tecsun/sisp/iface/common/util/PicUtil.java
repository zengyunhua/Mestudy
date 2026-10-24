package com.tecsun.sisp.iface.common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

/**
 * 
 * @title图片转换
 * @2019年1月8日-下午2:13:47
 * @version2019
 */
public class PicUtil {
    // 照片转化成base64字符串 path为照片路径
    public static String GetImageStr(String path) {
        String imgFile = path;// 待处理的voice
        InputStream in = null;
        byte[] data = null;
// 读取照片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
// 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

   /**
    * 
    * @title base64编码字符串转换为图片
    * @2019年1月8日-下午5:08:30
    *
    */
   public static boolean base64StrToImage(String imgStr, String path, String idCard) {
     if (imgStr == null)
     return false;
     BASE64Decoder decoder = new BASE64Decoder();
     try {
       // 解密
       byte[] b = decoder.decodeBuffer(imgStr);
       // 处理数据
       for (int i = 0; i < b.length; ++i) {
         if (b[i] < 0) {
           b[i] += 256;
         }
       }
      
      //getNowTime()+
       OutputStream out = new FileOutputStream(path+idCard+".jpg");
       out.write(b);
       out.flush();
       out.close();
       return true;
     } catch (Exception e) {
       return false;
     }
   }
   	
   /**
    * 获取一个随机数
    */
   	private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); // 时间格式化的格式
	public static  String getNowTime(){
		//String rannum = String.valueOf((int)((Math.random()*9000)+1000)); // 获取随机数
		String nowTimeStr = sDateFormat.format(new Date()).toString(); // 当前时间
		return nowTimeStr;
	}
//    public static void main(String[] args) {
//        System.out.println(GetImageStr("D:\\test1\\44.jpg"));
//        GenerateImage(GetImageStr("D:\\test1\\44.jpg") , "D://111.jpg");
//
//    }
}
