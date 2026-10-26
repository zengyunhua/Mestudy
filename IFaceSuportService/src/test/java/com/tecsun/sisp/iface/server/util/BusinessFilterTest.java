package com.tecsun.sisp.iface.server.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class BusinessFilterTest {
    private static final String METHOD = "getSelfInfo";

    @Test public void deniesMissingAndMalformedPermissions() throws Exception {
        assertFalse(BusinessFilter.hasPermission(null, METHOD));
        assertFalse(BusinessFilter.hasPermission("{}", METHOD));
        assertFalse(BusinessFilter.hasPermission("{\"statusCode\":\"200\",\"result\":null}", METHOD));
        assertFalse(BusinessFilter.hasPermission("{\"statusCode\":\"200\",\"result\":false}", METHOD));
        assertFalse(BusinessFilter.hasPermission("{\"statusCode\":\"200\",\"result\":{}}", METHOD));
    }

    @Test public void rejectsWrongStatusOrMethod() throws Exception {
        assertFalse(BusinessFilter.hasPermission("{\"statusCode\":\"300\",\"result\":{\"funCode\":\"getSelfInfo\"}}", METHOD));
        assertFalse(BusinessFilter.hasPermission("{\"statusCode\":\"200\",\"result\":{\"funCode\":\"otherMethod\"}}", METHOD));
    }

    @Test public void acceptsMatchingPermission() throws Exception {
        assertTrue(BusinessFilter.hasPermission("{\"statusCode\":\"200\",\"result\":{\"funCode\":\"getSelfInfo\"}}", METHOD));
    }
}