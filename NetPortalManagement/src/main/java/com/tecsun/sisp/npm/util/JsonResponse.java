package com.tecsun.sisp.npm.util;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

public final class JsonResponse {
    private static final Gson GSON = new Gson();

    private JsonResponse() {
    }

    public static String success(Object data) {
        return success(data, null);
    }

    public static String success(Object data, Map<String, Object> extra) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("statusCode", "200");
        response.put("message", "success");
        response.put("data", data);
        if (extra != null) {
            response.putAll(extra);
        }
        return GSON.toJson(response);
    }

    public static String failure(String message) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        response.put("statusCode", "0");
        response.put("message", message);
        response.put("data", null);
        return GSON.toJson(response);
    }
}
