package com.tecsun.sisp.iface.common.util;

import org.junit.Test;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.Assert.*;

public class BusinessFilterTest {
    @Test public void missingTokenIsRejectedBeforeRedisAndController() throws Exception {
        StringWriter output = new StringWriter();
        HttpServletRequest request = (HttpServletRequest) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{HttpServletRequest.class}, (proxy, method, args) -> {
                    if ("getRemoteAddr".equals(method.getName())) return "127.0.0.1";
                    return null;
                });
        HttpServletResponse response = (HttpServletResponse) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{HttpServletResponse.class}, (proxy, method, args) -> {
                    if ("getWriter".equals(method.getName())) return new PrintWriter(output, true);
                    return null;
                });
        AtomicBoolean invoked = new AtomicBoolean();
        FilterChain chain = new FilterChain() {
            public void doFilter(ServletRequest req, ServletResponse resp) { invoked.set(true); }
        };
        new BusinessFilter().doFilter(request, response, chain);
        assertFalse(invoked.get());
        assertTrue(output.toString().contains("IFAC-EERR-301"));
    }
}
