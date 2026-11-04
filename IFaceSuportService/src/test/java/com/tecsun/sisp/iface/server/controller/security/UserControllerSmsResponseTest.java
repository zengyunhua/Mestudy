package com.tecsun.sisp.iface.server.controller.security;

import com.tecsun.sisp.iface.common.vo.SmsRequstBean;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserControllerSmsResponseTest {

    @Test
    public void responseAllowlistDoesNotExposeVerificationCode() {
        SmsRequstBean request = new SmsRequstBean();
        request.setRandom("4827");
        request.setName("Example User");
        request.setNewPhoneNo("13800000000");
        request.setCernum("example-id");

        Map<String, String> response = UserController.buildSmsResponse(request);

        assertFalse(response.containsKey("random"));
        assertEquals(3, response.size());
        assertEquals("Example User", response.get("name"));
        assertEquals("13800000000", response.get("newPhoneNo"));
        assertEquals("example-id", response.get("cernum"));
    }
}
