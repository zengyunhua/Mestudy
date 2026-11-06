package com.tecsun.sisp.iface.server.controller.cssp;

import com.tecsun.sisp.iface.common.vo.Result;
import com.tecsun.sisp.iface.common.vo.SfzInfo;
import com.tecsun.sisp.iface.common.vo.picInfo;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

/**
 * The upload endpoints build file names from request fields. A hostile name must be
 * rejected before any file or database work; the services are not wired here, so
 * reaching them would fail the test with a NullPointerException.
 */
public class CsspBusControllerUploadTest {

    private static final String PHOTO = Base64.getEncoder().encodeToString(new byte[] {1, 2, 3});

    @Test
    public void uploadSfzRejectsPathInIdNumber() {
        SfzInfo bean = new SfzInfo();
        bean.setCerNum("../../../../tmp/escaped");
        bean.setName("Example");
        bean.setBase64String(PHOTO);

        Result result = (Result) new CsspBusController().uploadSfzByTSB(bean);

        assertEquals("201", result.getStatusCode());
    }

    @Test
    public void uploadPicRejectsPathInPicName() {
        picInfo bean = new picInfo();
        bean.setPicName("..\\..\\escaped");
        bean.setBase64String(PHOTO);
        bean.setCernum("110101199001010015");
        bean.setName("Example");

        Result result = (Result) new CsspBusController().uploadPicInfoByTSB(bean);

        assertEquals("201", result.getStatusCode());
    }
}
