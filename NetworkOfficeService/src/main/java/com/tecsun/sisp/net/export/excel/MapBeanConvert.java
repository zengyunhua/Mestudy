package com.tecsun.sisp.net.export.excel;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * map和javabean互相转换工具类
 *
 */
public class MapBeanConvert {

	  public static Map<String, Object> objectToMap(Object obj) throws Exception {  
		  Map<String, Object> map =new HashMap<>();
          // 获取person类的BeanInfo对象
          BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
          //获取属性描述器
          PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
          for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

              //获取属性名
              String key = propertyDescriptor.getName();
              if(key.equals("class"))
                  continue;
              //获取该属性的值
              Method readMethod = propertyDescriptor.getReadMethod();
              ////通过反射来调用Person的定义的getName()方法
              Object value = readMethod.invoke(obj);
              map.put(key, value);
          }
          return map;
	  }
	    
}
