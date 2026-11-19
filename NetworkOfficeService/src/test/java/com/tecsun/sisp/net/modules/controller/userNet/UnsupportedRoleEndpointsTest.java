package com.tecsun.sisp.net.modules.controller.userNet;

import com.tecsun.sisp.net.modules.dao.area_sisppublic.AreaDAO;
import com.tecsun.sisp.net.modules.entity.request.UserRoleBean;
import com.tecsun.sisp.net.modules.entity.response.UserRolVo;
import com.tecsun.sisp.net.modules.service.impl.AreaServiceImpl;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import java.lang.reflect.Method;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/** No injected services: any attempted database call fails instead of silently passing. */
public class UnsupportedRoleEndpointsTest {
    @Test public void writesReturnHttpAndJson501WithoutDatabaseAccess() throws Exception {
        assertUnavailable("insertUserRol");
    }
    @Test public void queriesReturnHttpAndJson501WithoutDatabaseAccess() throws Exception {
        assertUnavailable("queryUserRloe");
    }
    private static void assertUnavailable(String operation) throws Exception {
        MockMvc mvc = standaloneSetup(new DraftsController()).build();
        MockHttpServletResponse response = mvc.perform(post("/userNet/drafts/" + operation)
                .contentType(MediaType.APPLICATION_JSON).content("{}")).andReturn().getResponse();
        assertEquals(501, response.getStatus());
        String body = response.getContentAsString();
        assertTrue(body, body.contains("\"statusCode\":\"501\""));
        assertTrue(body, body.contains("SQL"));
        assertFalse(body, body.contains("Invalid bound statement"));
        assertFalse(body, body.contains("\"statusCode\":\"200\""));
        assertEquals(405, mvc.perform(get("/userNet/drafts/" + operation))
                .andReturn().getResponse().getStatus());
    }
    @Test public void invalidJsonIsStillRejectedByMvc() throws Exception {
        MockMvc mvc = standaloneSetup(new DraftsController()).build();
        assertEquals(400, mvc.perform(post("/userNet/drafts/insertUserRol")
                .contentType(MediaType.APPLICATION_JSON).content("not-json")).andReturn().getResponse().getStatus());
    }
    @Test public void unimplementedMethodsAreNoLongerExposedByMyBatisDao() {
        for (Method method : AreaDAO.class.getMethods()) {
            assertFalse("Unbound DAO method remains: " + method.getName(),
                    "insertUserRol".equals(method.getName()) || "queryUserRloe".equals(method.getName()));
        }
    }
    @Test public void deprecatedInternalOperationsFailBeforeDaoAccess() {
        AreaServiceImpl service = new AreaServiceImpl();
        try {
            service.insertUserRol(new UserRolVo());
            fail("Must not report a successful write or call an unbound DAO");
        } catch (UnsupportedOperationException expected) {
            assertTrue(expected.getMessage().contains("SQL"));
        }
        try {
            service.queryUserRloe(new UserRoleBean());
            fail("Must not return a fabricated empty result");
        } catch (UnsupportedOperationException expected) {
            assertTrue(expected.getMessage().contains("SQL"));
        }
    }
}
