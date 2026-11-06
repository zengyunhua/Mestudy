package com.tecsun.sisp.iface.common.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PicUtilTest {

    private static final byte[] IMAGE = {(byte) 0xff, (byte) 0xd8, 1, 2, 3, (byte) 0xff, (byte) 0xd9};
    private static final String IMAGE_BASE64 = Base64.getEncoder().encodeToString(IMAGE);

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void writesImageInsideDirectory() throws Exception {
        File dir = folder.newFolder("photos");

        assertTrue(PicUtil.writeImage(IMAGE_BASE64, dir.getPath(), "110101199001010015.jpg"));

        assertArrayEquals(IMAGE, Files.readAllBytes(new File(dir, "110101199001010015.jpg").toPath()));
    }

    @Test
    public void createsMissingDirectory() {
        File dir = new File(folder.getRoot(), "a/b");

        assertTrue(PicUtil.writeImage(IMAGE_BASE64, dir.getPath(), "x.jpg"));
        assertTrue(new File(dir, "x.jpg").isFile());
    }

    @Test
    public void rejectsTraversalAndLeavesNoFile() throws Exception {
        File dir = folder.newFolder("photos");
        String[] hostile = {"../escaped.jpg", "..\\escaped.jpg", "sub/escaped.jpg", "C:escaped.jpg", "..", ".", ""};

        for (String name : hostile) {
            assertFalse(name, PicUtil.writeImage(IMAGE_BASE64, dir.getPath(), name));
        }
        assertFalse(new File(folder.getRoot(), "escaped.jpg").exists());
        assertEquals(0, dir.list().length);
    }

    @Test
    public void rejectsInvalidAndOversizedData() throws Exception {
        File dir = folder.newFolder("photos");
        byte[] tooLarge = new byte[PicUtil.MAX_IMAGE_BYTES + 1];
        Arrays.fill(tooLarge, (byte) 1);

        assertFalse(PicUtil.writeImage("abcde", dir.getPath(), "bad.jpg"));
        assertFalse(PicUtil.writeImage("", dir.getPath(), "empty.jpg"));
        assertFalse(PicUtil.writeImage(Base64.getEncoder().encodeToString(tooLarge), dir.getPath(), "big.jpg"));
        assertFalse(PicUtil.writeImage(null, dir.getPath(), "null.jpg"));
        assertFalse(PicUtil.GenerateImage(Base64.getEncoder().encodeToString(tooLarge), new File(dir, "big2.jpg").getPath()));
        assertEquals(0, dir.list().length);
    }

    @Test
    public void acceptsRealisticNames() {
        assertTrue(PicUtil.isSafeFileName("person_11010119900101001X20260101120000.jpg"));
        assertTrue(PicUtil.isSafeFileName("QM_张三_20260101120000.jpg"));
        assertFalse(PicUtil.isSafeFileName("a\u0000b.jpg"));
        assertFalse(PicUtil.isSafeFileName(null));
    }
}
