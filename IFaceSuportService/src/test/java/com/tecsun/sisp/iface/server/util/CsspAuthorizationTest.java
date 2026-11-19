package com.tecsun.sisp.iface.server.util;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.server.controller.cssp.CsspBusController;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.w3c.dom.Document;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/** Real authorization branches, fake session/device backends; no private infrastructure. */
public class CsspAuthorizationTest {
    private static final String SESSION_ID = "example-session";
    private static final String IP = "127.0.0.1";
    private static final String UPLOAD = "/iface/cssp/uploadPicInfoByTSB";

    private static class Sessions extends BusinessFilter {
        final Map<String, String> values = new HashMap<String, String>();
        final Map<String, Integer> expiry = new HashMap<String, Integer>();
        boolean unavailable, renewalUnavailable, methodAllowed;
        boolean deviceAllowed = true;
        Sessions(String channel) {
            values.put(SESSION_ID, IP);
            if (channel != null) values.put(Constants.CHANNELKEY + SESSION_ID, channel);
        }
        @Override String sessionValue(String key) throws Exception {
            if (unavailable) throw new Exception("Simulated session service outage");
            return values.get(key);
        }
        @Override void renewSession(String key, String value) throws Exception {
            renewSession(key, value, -1);
        }
        @Override void renewSession(String key, String value, int minutes) throws Exception {
            if (renewalUnavailable) throw new Exception("Simulated session refresh outage");
            values.put(key, value);
            expiry.put(key, minutes);
        }
        @Override boolean checkDev(String deviceid) { return deviceAllowed; }
        @Override boolean checkUser(String userid, String method) { return methodAllowed; }
    }
    private static class Chain implements FilterChain {
        int calls;
        @Override public void doFilter(ServletRequest request, ServletResponse response) { calls++; }
    }
    private static MockHttpServletRequest request(String method, String path) {
        MockHttpServletRequest request = new MockHttpServletRequest(method, "/sisp" + path);
        request.setContextPath("/sisp");
        request.setServletPath(path);
        request.setRemoteAddr(IP);
        return request;
    }
    private static MockHttpServletResponse apply(Sessions filter, MockHttpServletRequest request, Chain chain)
            throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();
        filter.doFilter(request, response, chain);
        return response;
    }
    @Test public void descriptorProtectsWholeCsspPrefix() throws Exception {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new File("src/main/webapp/WEB-INF/web.xml"));
        assertEquals(1d, ((Number) javax.xml.xpath.XPathFactory.newInstance().newXPath().evaluate(
                "count(/web-app/filter-mapping[filter-name='BusinessFilter' and url-pattern='/iface/cssp/*'])",
                document, javax.xml.xpath.XPathConstants.NUMBER)).doubleValue(), 0d);
    }
    @Test public void missingSessionIsRejectedBeforeControllerOrBodyRead() throws Exception {
        MockHttpServletRequest request = request("POST", UPLOAD);
        request.setContent("example-body".getBytes("UTF-8"));
        Chain chain = new Chain();
        MockHttpServletResponse response = apply(new Sessions("TSB"), request, chain);
        assertEquals(401, response.getStatus());
        assertEquals(0, chain.calls);
        assertEquals("example-body", BusinessFilter.getBody(request));
        assertTrue(response.getContentType().startsWith("application/json"));
    }
    @Test public void allSensitiveRoutesRequireSession() throws Exception {
        String[] routes = {"applyCard", "uploadPicInfoByTSB", "auditByTSB", "uploadSfzByTSB",
                "uploadCardPicInfo", "getPicInfoByPC", "getNewPicInfoByTSB", "querybyPersonMessage",
                "register", "registerNew", "canApply", "test", "unknownFutureRoute"};
        for (String route : routes) {
            Chain chain = new Chain();
            assertEquals(route, 401, apply(new Sessions("TSB"), request("POST", "/iface/cssp/" + route), chain).getStatus());
            assertEquals(route, 0, chain.calls);
        }
    }
    @Test public void onlyExactCredentialLoginIsPublic() throws Exception {
        Sessions filter = new Sessions("TSB");
        filter.unavailable = true; // Public credential login does not look up an interface session.
        Chain chain = new Chain();
        assertEquals(200, apply(filter, request("POST", "/iface/cssp/login"), chain).getStatus());
        assertEquals(1, chain.calls);
        assertTrue(filter.expiry.isEmpty());
    }
    @Test public void loginAliasesAndOtherMethodsCannotBypassAuthentication() throws Exception {
        String[] aliases = {"/iface/cssp/login/", "/iface/cssp/login;jsessionid=example",
                "/iface/cssp/%6cogin", "/iface/cssp/login.json", "/iface/cssp//login"};
        for (String alias : aliases) {
            Chain chain = new Chain();
            assertEquals(alias, 401, apply(new Sessions("TSB"), request("POST", alias), chain).getStatus());
            assertEquals(0, chain.calls);
        }
        for (String method : new String[] {"GET", "PUT", "OPTIONS"}) {
            Chain chain = new Chain();
            assertEquals(401, apply(new Sessions("TSB"), request(method, "/iface/cssp/login"), chain).getStatus());
            assertEquals(0, chain.calls);
        }
    }
    @Test public void validHeaderSessionReachesApplicationAndRefreshesBothKeys() throws Exception {
        Sessions filter = new Sessions("TSB");
        MockHttpServletRequest request = request("POST", UPLOAD);
        request.addHeader("X-Token-Id", SESSION_ID);
        Chain chain = new Chain();
        assertEquals(200, apply(filter, request, chain).getStatus());
        assertEquals(1, chain.calls);
        assertTrue(filter.expiry.containsKey(SESSION_ID));
        assertTrue(filter.expiry.containsKey(Constants.CHANNELKEY + SESSION_ID));
    }
    @Test public void legacyParameterWorksButCannotOverrideHeader() throws Exception {
        MockHttpServletRequest request = request("POST", UPLOAD);
        request.setParameter("tokenId", SESSION_ID);
        Chain chain = new Chain();
        assertEquals(200, apply(new Sessions("TSB"), request, chain).getStatus());
        assertEquals(1, chain.calls);
        request.addHeader("X-Token-Id", "example-invalid-session");
        chain = new Chain();
        assertEquals(401, apply(new Sessions("TSB"), request, chain).getStatus());
        assertEquals(0, chain.calls);
    }
    @Test public void expiredSessionWrongIpAndMissingChannelAreRejected() throws Exception {
        Sessions[] filters = {new Sessions("TSB"), new Sessions("TSB"), new Sessions(null)};
        filters[0].values.remove(SESSION_ID);
        filters[1].values.put(SESSION_ID, "192.0.2.10"); // Documentation address, not infrastructure.
        for (Sessions filter : filters) {
            MockHttpServletRequest request = request("POST", UPLOAD);
            request.addHeader("X-Token-Id", SESSION_ID);
            Chain chain = new Chain();
            assertEquals(401, apply(filter, request, chain).getStatus());
            assertEquals(0, chain.calls);
        }
    }
    @Test public void unknownChannelAndInvalidDeviceAreForbidden() throws Exception {
        Sessions[] filters = {new Sessions("example-unknown-channel"), new Sessions("TSB")};
        filters[1].deviceAllowed = false;
        for (Sessions filter : filters) {
            MockHttpServletRequest request = request("POST", UPLOAD);
            request.addHeader("X-Token-Id", SESSION_ID);
            Chain chain = new Chain();
            assertEquals(403, apply(filter, request, chain).getStatus());
            assertEquals(0, chain.calls);
        }
    }
    @Test public void thirdSystemCannotSpoofMethodOrSkipPermission() throws Exception {
        Sessions filter = new Sessions("ThirdSystem");
        filter.values.put(Constants.USERKEY + SESSION_ID, "example-user");
        MockHttpServletRequest request = request("POST", UPLOAD);
        request.addHeader("X-Token-Id", SESSION_ID);
        request.setParameter("method", "example-other-method");
        Chain chain = new Chain();
        assertEquals(403, apply(filter, request, chain).getStatus());
        assertEquals(0, chain.calls);
        request.removeParameter("method");
        chain = new Chain();
        assertEquals(403, apply(filter, request, chain).getStatus());
        assertEquals(0, chain.calls);
        filter.methodAllowed = true;
        chain = new Chain();
        assertEquals(200, apply(filter, request, chain).getStatus());
        assertEquals(1, chain.calls);
    }
    @Test public void sessionOutagesFailClosed() throws Exception {
        Sessions[] filters = {new Sessions("TSB"), new Sessions("TSB")};
        filters[0].unavailable = true;
        filters[1].renewalUnavailable = true;
        for (Sessions filter : filters) {
            MockHttpServletRequest request = request("POST", UPLOAD);
            request.addHeader("X-Token-Id", SESSION_ID);
            Chain chain = new Chain();
            MockHttpServletResponse response = apply(filter, request, chain);
            assertEquals(503, response.getStatus());
            assertEquals(0, chain.calls);
            assertFalse(response.getContentAsString().contains(SESSION_ID));
        }
    }
    @Test public void selfServiceRefreshesSessionAndChannelWithSameExpiry() throws Exception {
        Sessions filter = new Sessions("SelfService");
        MockHttpServletRequest request = request("POST", UPLOAD);
        request.addHeader("X-Token-Id", SESSION_ID);
        Chain chain = new Chain();
        apply(filter, request, chain);
        assertEquals(1, chain.calls);
        assertEquals(Constants.SELFSERVICETIMEOUT, filter.expiry.get(SESSION_ID));
        assertEquals(Constants.SELFSERVICETIMEOUT, filter.expiry.get(Constants.CHANNELKEY + SESSION_ID));
    }
    @Test public void mvcRejectsUnauthenticatedWriteBeforeInvalidRequestBody() throws Exception {
        MockMvc mvc = standaloneSetup(new CsspBusController()).addFilters(new Sessions("TSB")).build();
        assertEquals(401, mvc.perform(post(UPLOAD).contentType(MediaType.APPLICATION_JSON)
                .content("not-json")).andReturn().getResponse().getStatus());
    }
    @Test public void mvcAllowsSessionThenPreservesPathValidation() throws Exception {
        MockMvc mvc = standaloneSetup(new CsspBusController()).addFilters(new Sessions("TSB")).build();
        MockHttpServletResponse response = mvc.perform(post(UPLOAD).header("X-Token-Id", SESSION_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"picName\":\"../escaped\",\"base64String\":\"AQID\",\"cernum\":\"110101199001010015\",\"name\":\"Example\"}"))
                .andReturn().getResponse();
        assertEquals(200, response.getStatus()); // Legacy validation uses application-level codes.
        assertTrue(response.getContentAsString().contains("\"statusCode\":\"201\""));
    }
}
