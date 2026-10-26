package com.tecsun.sisp.iface.client;

import org.junit.Test;
import static org.junit.Assert.*;

public class IFaceHttpsClientTest {
    @Test public void rejectsFailureRatherThanReturningEmptyPage() throws Exception {
        try {
            IFaceHttpsClient.requireSuccessfulPage("{\"statusCode\":\"0\",\"message\":\"denied\"}");
            fail("Must reject a failed response");
        } catch (IllegalStateException expected) {
            assertTrue(expected.getMessage().contains("0"));
        }
    }

    @Test public void acceptsSuccessEnvelope() throws Exception {
        assertNotNull(IFaceHttpsClient.requireSuccessfulPage("{\"statusCode\":\"200\",\"data\":{}}"));
    }
}
