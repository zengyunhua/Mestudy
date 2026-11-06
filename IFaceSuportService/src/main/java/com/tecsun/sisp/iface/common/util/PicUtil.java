
package com.tecsun.sisp.iface.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * Base64 image helpers used by the photo-upload endpoints.
 *
 * <p>Callers that build a file name from request data must use
 * {@link #writeImage(String, String, String)}: it rejects names that could leave the target
 * directory and caps the decoded size. {@link #GenerateImage(String, String)} is kept for
 * source compatibility of the client jar and only applies the size cap.
 *
 * Created by Sandwich on 2015/11/12.
 */
public class PicUtil {

    /** Upper bound for one decoded image. ID photos and signatures are far below this. */
    public static final int MAX_IMAGE_BYTES = 5 * 1024 * 1024;

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

    /**
     * Returns {@code true} when {@code fileName} is a single path segment: not blank, at most
     * 128 characters, not {@code .} or {@code ..}, and free of {@code /}, {@code \}, {@code :}
     * and control characters. Chinese names and ID numbers are accepted.
     */
    public static boolean isSafeFileName(String fileName) {
        if (fileName == null || fileName.isEmpty() || fileName.length() > 128
                || ".".equals(fileName) || "..".equals(fileName)) {
            return false;
        }
        for (int i = 0; i < fileName.length(); i++) {
            char c = fileName.charAt(i);
            if (c == '/' || c == '\\' || c == ':' || c < 0x20 || c == 0x7f) {
                return false;
            }
        }
        return true;
    }

    /**
     * Decodes {@code imgStr} and writes it to {@code directory/fileName}.
     *
     * <p>Returns {@code false} without touching the file system when the name is unsafe
     * (see {@link #isSafeFileName(String)}), the data is not valid base64, or it decodes to
     * more than {@link #MAX_IMAGE_BYTES}. The directory is created if it does not exist, and
     * the resolved file must still be inside it.
     *
     * @param imgStr base64 image data (MIME line breaks allowed)
     * @param directory target directory, trusted (comes from code or configuration)
     * @param fileName file name, untrusted (usually built from request fields)
     */
    public static boolean writeImage(String imgStr, String directory, String fileName) {
        if (imgStr == null || directory == null || directory.isEmpty() || !isSafeFileName(fileName)) {
            return false;
        }
        byte[] image = decode(imgStr);
        if (image == null) {
            return false;
        }
        try {
            File dir = new File(directory).getCanonicalFile();
            if (!dir.isDirectory() && !dir.mkdirs()) {
                return false;
            }
            File target = new File(dir, fileName).getCanonicalFile();
            if (!dir.equals(target.getParentFile())) {
                return false;
            }
            write(image, target);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Writes base64 data to a caller-built path. Applies the size cap but cannot check the
     * path; use {@link #writeImage(String, String, String)} when any part of the path comes
     * from a request.
     */
    @Deprecated
    public static boolean GenerateImage(String imgStr, String path) { // 对字节数组字符串进行Base64解码并生成照片
        if (imgStr == null || path == null) //照片数据为空
            return false;
        byte[] image = decode(imgStr);
        if (image == null) {
            return false;
        }
        try {
            write(image, new File(path));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Returns the decoded bytes, or {@code null} for invalid or oversized input. */
    private static byte[] decode(String imgStr) {
        // Base64 needs 4 characters per 3 bytes plus line breaks; 2x is a loose pre-decode
        // bound that keeps an oversized request from being decoded at all.
        if (imgStr.length() > MAX_IMAGE_BYTES * 2L) {
            return null;
        }
        try {
            byte[] image = Base64.getMimeDecoder().decode(imgStr);
            return image.length == 0 || image.length > MAX_IMAGE_BYTES ? null : image;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static void write(byte[] image, File target) throws IOException {
        OutputStream out = new FileOutputStream(target);
        try {
            out.write(image);
            out.flush();
        } finally {
            out.close();
        }
    }
}
