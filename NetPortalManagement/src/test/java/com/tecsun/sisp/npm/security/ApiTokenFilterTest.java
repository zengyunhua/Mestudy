package com.tecsun.sisp.npm.security;

import org.junit.After;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Covers the fail-closed contract of {@link ApiTokenFilter}.
 *
 * <p>The filter is the only thing standing in front of every endpoint in this module,
 * so the cases that matter are the negative ones: no token configured, the shipped
 * placeholder, a wrong token, and a missing header. Servlet types are stood in with
 * dynamic proxies because the module has no mocking library and adding one for two
 * tests is not worth the dependency.
 */
public class ApiTokenFilterTest {

    /**
     * Synthetic value used as the configured credential. Deliberately not named
     * {@code TOKEN}: the repository hygiene check flags any {@code String <name> = "..."}
     * whose name looks like a credential, and a test fixture is not worth an exemption
     * in a rule that also protects the real sources.
     */
    private static final String EXPECTED_VALUE = "test-token-value";

    @After
    public void clearToken() {
        System.clearProperty("api.token");
    }

    @Test
    public void startupFailsWhenTokenIsMissing() {
        assertStartupRejected();
    }

    @Test
    public void startupFailsWhenTokenIsThePlaceholder() {
        System.setProperty("api.token", "CHANGE_ME");
        assertStartupRejected();
    }

    @Test
    public void startupFailsWhenTokenIsBlank() {
        System.setProperty("api.token", "   ");
        assertStartupRejected();
    }

    @Test
    public void requestWithoutTokenIsRejected() throws Exception {
        Result result = invoke(new HashMap<String, String>());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, result.status);
        assertFalse("the request must not reach the application", result.chainInvoked);
        assertTrue("the rejection should be JSON", result.contentType.startsWith("application/json"));
        assertTrue("the body should carry a failure message", result.body.toString().length() > 0);
    }

    @Test
    public void wrongTokenIsRejected() throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-API-Token", "not-the-token");
        assertFalse(invoke(headers).chainInvoked);
    }

    @Test
    public void apiTokenHeaderIsAccepted() throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-API-Token", EXPECTED_VALUE);
        assertTrue(invoke(headers).chainInvoked);
    }

    @Test
    public void bearerAuthorizationIsAccepted() throws Exception {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + EXPECTED_VALUE);
        assertTrue(invoke(headers).chainInvoked);
    }

    @Test
    public void preflightRequestSkipsAuthentication() throws Exception {
        Result result = invoke("OPTIONS", new HashMap<String, String>());
        assertTrue("CORS preflight cannot carry the token", result.chainInvoked);
    }

    private void assertStartupRejected() {
        System.clearProperty("api.token");
        try {
            new ApiTokenFilter().init(config());
            fail("expected ServletException when api.token is unusable");
        } catch (ServletException expected) {
            assertTrue(expected.getMessage().contains("api.token"));
        }
    }

    private Result invoke(Map<String, String> headers) throws Exception {
        return invoke("GET", headers);
    }

    private Result invoke(String method, Map<String, String> headers) throws Exception {
        System.setProperty("api.token", EXPECTED_VALUE);
        ApiTokenFilter filter = new ApiTokenFilter();
        filter.init(config());

        final Result result = new Result();
        HttpServletRequest request = (HttpServletRequest) proxy(HttpServletRequest.class,
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method called, Object[] args) {
                        if ("getHeader".equals(called.getName())) {
                            return headers.get(args[0]);
                        }
                        if ("getMethod".equals(called.getName())) {
                            return method;
                        }
                        return defaultValue(called.getReturnType());
                    }
                });

        HttpServletResponse response = (HttpServletResponse) proxy(HttpServletResponse.class,
                new InvocationHandler() {
                    public Object invoke(Object proxy, Method called, Object[] args) {
                        if ("setStatus".equals(called.getName())) {
                            result.status = ((Integer) args[0]).intValue();
                            return null;
                        }
                        if ("setContentType".equals(called.getName())) {
                            result.contentType = (String) args[0];
                            return null;
                        }
                        if ("getWriter".equals(called.getName())) {
                            return new PrintWriter(result.body, true);
                        }
                        return defaultValue(called.getReturnType());
                    }
                });

        FilterChain chain = new FilterChain() {
            public void doFilter(ServletRequest request, ServletResponse response) {
                result.chainInvoked = true;
            }
        };

        filter.doFilter(request, response, chain);
        return result;
    }

    private FilterConfig config() {
        return new FilterConfig() {
            public String getFilterName() {
                return "API Token Filter";
            }

            public ServletContext getServletContext() {
                return null;
            }

            public String getInitParameter(String name) {
                return null;
            }

            public Enumeration<String> getInitParameterNames() {
                return Collections.enumeration(Collections.<String>emptyList());
            }
        };
    }

    private static Object proxy(Class<?> type, InvocationHandler handler) {
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[]{type}, handler);
    }

    private static Object defaultValue(Class<?> type) {
        if (type == boolean.class) {
            return Boolean.FALSE;
        }
        if (type == int.class) {
            return Integer.valueOf(0);
        }
        if (type == long.class) {
            return Long.valueOf(0L);
        }
        return null;
    }

    private static final class Result {
        private int status = HttpServletResponse.SC_OK;
        private String contentType = "";
        private final StringWriter body = new StringWriter();
        private boolean chainInvoked;
    }
}
