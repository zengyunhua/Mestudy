package com.tecsun.sisp.iface.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/**
 * 
 * Jackson json 操作类
 * 
 */

public class JsonHelper {

	public static final Logger logger = Logger.getLogger(JsonHelper.class);
	
	private static final  ObjectMapper mapper = new ObjectMapper(); 
	
	public static ObjectMapper getInstance() {

	     return mapper;
	}

	/**
	 * json转javaBean
	 * @param jsonString
	 * @param t
	 * @return
	 */
	public static <T> Object jsonToJavaBean(String jsonString , Class<T> t) {
		Object result = null;
		try {
			result = mapper.readValue(jsonString, t);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * javaBean , 集合 , 数组 转json
	 * @param o
	 * @return
	 */
	public static String javaBeanToJson(Object o){
		String json = null;
		try {
			json = mapper.writeValueAsString(o);
		}  catch (Exception e) {
			logger.error(e.getMessage());
		}
		return json;
	}

	/**
	 * json转Map
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, Object> jsonToMap(String jsonStr) {
        try {
			return mapper.readValue(jsonStr, Map.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
    }
	
	/**
	 * Map转javaBean
	 * @param map
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> T mapToJavaBean(Map map, Class<T> clazz) {
	    return mapper.convertValue(map, clazz);
	}
	
	/**
	 * JSON字符串转换为Map与JavaBean 
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> Map<String, T> jsonToMap(String jsonStr, Class<T> clazz)
            throws Exception {
        Map<String, Map<String, Object>> map = mapper.readValue(jsonStr,
                new TypeReference<Map<String, T>>() {
                });
        Map<String, T> result = new HashMap<String, T>();
        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), mapToJavaBean(entry.getValue(), clazz));
        }
        return result;
    }
	
	/**
	 * JSON数组字符串转换为List与JavaBean
	 * @param jsonArrayStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
    public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<Map<String, Object>> list = mapper.readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(mapToJavaBean(map, clazz));
        }
        return result;
    }

}
