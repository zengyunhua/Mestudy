package com.tecsun.sisp.iface.common.util;

import java.io.*;
import java.util.Base64;

/**
 * Created by Sandwich on 2015/11/12.
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
        return Base64.getEncoder().encodeToString(data);// 返回Base64编码过的字节数组字符串
    }

    // base64字符串转化成照片
    public static boolean GenerateImage(String imgStr,String path) { // 对字节数组字符串进行Base64解码并生成照片
        if (imgStr == null) //照片数据为空
            return false;
        try {
// Base64解码
            byte[] b = Base64.getMimeDecoder().decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
// 生成照片
            // C:\Users\Sandwich\Desktop\base64\1.jpg
            String imgFilePath = path;// 新生成的照片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//    public static void main(String[] args) {
//        System.out.println(GetImageStr("D:\\test1\\44.jpg"));
//        GenerateImage(GetImageStr("D:\\test1\\44.jpg") , "D://111.jpg");
//
//    }
}
