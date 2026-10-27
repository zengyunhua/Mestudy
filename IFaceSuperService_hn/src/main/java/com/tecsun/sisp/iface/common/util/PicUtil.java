package com.tecsun.sisp.iface.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/** Validates and stores authentication images inside the configured directory. */
public class PicUtil {
    private static final int MAX_IMAGE_BYTES = 5 * 1024 * 1024;

    public static String GetImageStr(String path) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(new File(path).toPath()));
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean base64StrToImage(String imgStr, String path, String idCard) {
        if (imgStr == null || path == null || idCard == null
                || !idCard.matches("[A-Za-z0-9_-]{1,32}")
                || imgStr.length() > (MAX_IMAGE_BYTES * 4L / 3L + 4096)) {
            return false;
        }
        byte[] image;
        try {
            image = Base64.getMimeDecoder().decode(imgStr);
        } catch (IllegalArgumentException e) {
            return false;
        }
        if (image.length < 4 || image.length > MAX_IMAGE_BYTES
                || (image[0] & 0xff) != 0xff || (image[1] & 0xff) != 0xd8
                || (image[image.length - 2] & 0xff) != 0xff
                || (image[image.length - 1] & 0xff) != 0xd9) {
            return false;
        }
        Path temporary = null;
        try {
            Path directory = new File(path).getCanonicalFile().toPath();
            if (!Files.isDirectory(directory)) {
                return false;
            }
            Path destination = directory.resolve(idCard + ".jpg");
            temporary = Files.createTempFile(directory, "auth-", ".tmp");
            Files.write(temporary, image);
            Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (temporary != null) {
                try { Files.deleteIfExists(temporary); } catch (IOException ignored) { /* best effort */ }
            }
        }
    }
}
