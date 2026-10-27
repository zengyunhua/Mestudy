package com.tecsun.sisp.iface.common.util;

import org.junit.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import static org.junit.Assert.*;

public class PicUtilTest {
    @Test public void writesJpegToConfiguredDirectory() throws Exception {
        Path dir = Files.createTempDirectory("auth-test-");
        try {
            byte[] jpeg = {(byte)0xff, (byte)0xd8, 1, 2, (byte)0xff, (byte)0xd9};
            assertTrue(PicUtil.base64StrToImage(Base64.getEncoder().encodeToString(jpeg), dir.toString(), "123456X"));
            assertArrayEquals(jpeg, Files.readAllBytes(dir.resolve("123456X.jpg")));
        } finally {
            Files.deleteIfExists(dir.resolve("123456X.jpg")); Files.deleteIfExists(dir);
        }
    }

    @Test public void rejectsTraversalAndInvalidImages() throws Exception {
        Path dir = Files.createTempDirectory("auth-test-");
        try {
            String jpeg = Base64.getEncoder().encodeToString(new byte[]{(byte)0xff, (byte)0xd8, 1, 2, (byte)0xff, (byte)0xd9});
            assertFalse(PicUtil.base64StrToImage(jpeg, dir.toString(), "../outside"));
            assertFalse(PicUtil.base64StrToImage("not base64!", dir.toString(), "123"));
            assertFalse(PicUtil.base64StrToImage(Base64.getEncoder().encodeToString(new byte[]{1, 2, 3}), dir.toString(), "123"));
            assertFalse(Files.exists(dir.resolve("123.jpg")));
        } finally { Files.deleteIfExists(dir); }
    }
}
