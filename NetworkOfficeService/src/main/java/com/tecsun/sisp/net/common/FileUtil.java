package com.tecsun.sisp.net.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FileUtil {
    /**
     * log4j实例对象.
     */
    private static Logger logger = LogManager.getLogger(FileUtil.class.getName());

    /**
     * 验证字符串是否为正确路径名的正则表达式
     * 语法: 通过 sPath.matches(matches) 方法的返回值判断是否正确  sPath 为路径字符串
     */
    public static String matches = "[A-Za-z]:\\\\[^:?\"><*]*";

    /**
     * 缓冲区容量
     */
    public static final int BUFFER_SIZE = 16 * 1024;

    /**
     * 文件存放的根目录
     */
    public static final String rootPath = File.separator + "resource";

    /**
     * 文件存放的根目录
     */
    public static final String rootURL = "./WEB-INF/resource/";

    /**
     * 获取项目的根路径URI
     * <p>
     * 注:MAVEN工程在junit测试中获取到是项目的根目录,而不是webContent目录
     *
     * @return
     * @throws URISyntaxException
     */
    public static URI getRootURI() throws URISyntaxException {
        // WEB-INF/classes
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        return url.toURI().resolve("../../");
    }

    /**
     * 功能: 通过相对路径生成新的绝对路径
     *
     * @param relavtiveURL 路径 前一定要有"/"
     * @return
     */
    public static String getResourcePath(final String relavtiveURL) {
        try {
            return getRootURI().resolve(rootURL + relavtiveURL).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error("生成路径失败", e);
            return null;
        }
    }

    /**
     * 根据相对路径获取文件.
     *
     * @param relavtiveURL
     * @return
     */
    public static File getResourceFile(String relavtiveURL) {
        String filePath = getResourcePath(relavtiveURL);
        // 获取文件引用
        File file = new File(filePath);
        // 如果文件存在,返回文件;否则返回null
        return file.exists() ? file : null;
    }

    /**
     * 根据相对于WEB-INF/classes的路径，获取properties文件
     *
     * @param relavtiveURL 例如"./user-config/mail.properties"
     * @return
     */
    public static Properties getConfigProperties(String relavtiveURL) {
        Properties properties = null;
        // WEB-INF/classes
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        try {
            // WEB-INF/classes/relavtiveURL
            String path = url.toURI().resolve(relavtiveURL).getPath();
            logger.debug(path);
            File configFile = new File(path);
            if (configFile.exists()) {
                properties = new Properties();
                properties.load(new FileInputStream(configFile));

            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            logger.error("", e);
        }
        if (properties == null) logger.error("未能获取配置信息:"+relavtiveURL);
        return properties;
    }

    /**
     * 根据相对路径获取文件.
     *
     * @param relavtiveURL
     * @return
     */
    public static File getConfigFile(String relavtiveURL) {
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        File file = null;
        try {
            // WEB-INF/classes/relavtiveURL
            String path = url.toURI().resolve(relavtiveURL).getPath();
            logger.debug(path);
            file = new File(path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error("", e);
        }
        // 如果文件存在,返回文件;否则返回null
        return file != null && file.exists() ? file : null;
    }

    /**
     * 将源文件的内容复制到目录绝对路径上
     *
     * @param src      源文件
     * @param filePath 目标绝对路径
     * @throws IOException
     */
    public static void copy(final File src, final String filePath) throws IOException {
        if (createFile(filePath))
            copy(src, new File(filePath));
    }

    /**
     * 功能: 将文件src复制到文件dst
     *
     * @param src 源文件
     * @param dst 目的文件
     * @throws IOException
     */
    public static void copy(final File src, final File dst) throws IOException {
        inToout(new FileInputStream(src), new FileOutputStream(dst));
    }

    /**
     * 将文件流保存到本地文件中
     *
     * @param src      源文件流
     * @param filePath 目标绝对路径
     * @throws IOException
     */
    public static void create(final InputStream src, final String filePath) throws IOException {
        if (createFile(filePath))
            inToout(src, new FileOutputStream(filePath));
    }

    /**
     * 功能:
     *
     * @param file
     * @return
     */
    public static byte[] getByteArray(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(BUFFER_SIZE);
            byte[] b = new byte[BUFFER_SIZE];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 功能: 将文件src复制到文件dst
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void inToout(final InputStream src, final OutputStream dst) throws IOException {
        try {

            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(src,
                        BUFFER_SIZE);
                out = new BufferedOutputStream(dst,
                        BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                //防止写的时候设置长度每次都写1024个字节，导致最后不足1024个字节也会写入大量的空字符
                // 增加起始长度 每次都写入实际读到的长度，所以不会出现空字符。
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.flush();
            } finally {
                if (null != in) {
                    in.close();
                }
                if (null != out) {
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * 功能: web限速下载文件
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    public static void downloadLimit(final File file, final OutputStream os) throws IOException {
    	byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
    	long speed =3*1024*1024L;//限制下载速度为3mb/s,TODO 此处可以动态设置,
    	long current=0;
    	long startTime=System.currentTimeMillis();
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            while((i=bis.read(buffer))!=-1){
				current=current+i;
				os.write(buffer);
				if(current>speed){
					while(true){
			            if(System.currentTimeMillis()>startTime+1000){
			                break;
			            }
			        }
					current=0;
					startTime=System.currentTimeMillis();
				}
			}
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据绝对路径生成空文件
     *
     * @param destFileName
     * @return
     */
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            logger.error("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            logger.error("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在  
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录  
            logger.debug("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {
                logger.error("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件  
        try {
            if (file.createNewFile()) {
                logger.debug("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                logger.error("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        if (sPath == null) {
            return flag;
        }
        File file = new File(sPath);
        // 判断目录或文件是否存在  
        if (!file.exists()) {  // 不存在返回 false  
            return flag;
        } else {
            // 判断是否为文件  
            if (file.isFile()) {  // 为文件时调用删除文件方法  
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法  
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    private static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            //删除子文件  
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录  
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录  
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }
    
}
