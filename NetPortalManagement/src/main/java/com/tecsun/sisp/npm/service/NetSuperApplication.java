package com.tecsun.sisp.npm.service;

import com.tecsun.sisp.npm.util.Database;
import com.tecsun.sisp.npm.util.JsonResponse;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public abstract class NetSuperApplication {
    protected final HttpServletRequest request;
    protected final int pageno;
    protected final int pagesize;

    protected NetSuperApplication(HttpServletRequest request, UriInfo uriInfo) {
        this.request = request;
        this.pageno = positiveInt(uriInfo, "pageno", 1);
        this.pagesize = positiveInt(uriInfo, "pagesize", 5);
    }

    protected Connection openConnection() throws SQLException {
        return Database.open();
    }

    protected String ok(Object data) {
        return JsonResponse.success(data);
    }

    protected String ok(Object data, Map<String, Object> extra) {
        return JsonResponse.success(data, extra);
    }

    protected String error(String message) {
        return JsonResponse.failure(message);
    }

    private int positiveInt(UriInfo uriInfo, String name, int defaultValue) {
        if (uriInfo == null || uriInfo.getQueryParameters().getFirst(name) == null) {
            return defaultValue;
        }
        try {
            int value = Integer.parseInt(uriInfo.getQueryParameters().getFirst(name));
            return value > 0 ? value : defaultValue;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
