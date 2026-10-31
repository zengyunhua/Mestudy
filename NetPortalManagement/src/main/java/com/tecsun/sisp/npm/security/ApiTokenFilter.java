package com.tecsun.sisp.npm.security;

import com.tecsun.sisp.npm.util.AppConfig;
import com.tecsun.sisp.npm.util.JsonResponse;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ApiTokenFilter implements Filter {
    private String expectedToken;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        expectedToken = AppConfig.get("api.token", null);
        if (expectedToken == null || expectedToken.trim().isEmpty() || "CHANGE_ME".equals(expectedToken)) {
            throw new ServletException("api.token must be configured before starting NetPortalManagement");
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        String providedToken = httpRequest.getHeader("X-API-Token");
        if (providedToken == null) {
            String authorization = httpRequest.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                providedToken = authorization.substring("Bearer ".length()).trim();
            }
        }
        if (!constantTimeEquals(expectedToken, providedToken)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json; charset=UTF-8");
            httpResponse.getWriter().write(JsonResponse.failure("未授权"));
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        expectedToken = null;
    }

    private boolean constantTimeEquals(String expected, String actual) {
        if (expected == null || actual == null) {
            return false;
        }
        int difference = expected.length() ^ actual.length();
        int length = Math.min(expected.length(), actual.length());
        for (int i = 0; i < length; i++) {
            difference |= expected.charAt(i) ^ actual.charAt(i);
        }
        return difference == 0;
    }
}
