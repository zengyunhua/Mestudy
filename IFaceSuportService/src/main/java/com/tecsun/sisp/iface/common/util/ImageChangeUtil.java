package com.tecsun.sisp.iface.common.util;

import org.apache.commons.lang.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.util.Iterator;

/**
 * 图片转换类
 *
 */
public class ImageChangeUtil {
	
	/**
	 * 图片转换成base64位字符串
	 * @param path 图片路径
	 * @return base64位字符串 
	 */
	public static String getImageStr(String path) {
        if(StringUtils.isBlank(path)) return null;
		String imgFile = path;// 待处理的voice
		
		//判断文件是否存在
		File file = new File(path);
		if(file == null || !file.exists())
			return null;
			
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
	 * base64字符串转化成图片
	 * @param imgStr base64字符串
	 * @param path 图片路径
	 * @return boolean true 成功  false 失败
	 */
	public static boolean generateImage(String imgStr, String path) { // 对字节数组字符串进行Base64解码并生成照片
		if (StringUtils.isBlank(imgStr)||StringUtils.isBlank(path)) // 照片数据为空
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		OutputStream out = null;
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成照片
			// C:\Users\Sandwich\Desktop\base64\1.jpg
			String imgFilePath = path;// 新生成的照片
			out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally{
			if(out !=null){
				try {
					out.flush();
					out.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * 判断一个文件是否为完整的图片
	 * @param path
	 * @return
	 */
	public static boolean isImage(String path){
		try{
			if (StringUtils.isBlank(path)) // 照片路径为空
				return false;
			File file = new File(path);
			ImageInputStream iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> its = ImageIO.getImageReaders(iis);
			if (!its.hasNext()){
				return false;
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 删除文件
	 * @param path
	 */
	public static void deletePic(String path){
        if(StringUtils.isNotBlank(path)) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
	}
	
	/**
	 * 判断一个字符串是否是整数
	 * @param str
	 * @return
	 */
	public static boolean isIntegerByString(String str){
		try {   
		    //把字符串强制转换为数字   
		    Integer.valueOf(str);  
		    //如果是数字，返回True   
		    return true;  
		} catch (Exception e) {  
		    //如果抛出异常，返回False   
		    return false;  
		}  
	}
}
