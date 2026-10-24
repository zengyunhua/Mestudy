package com.tecsun.sisp.iface.common.util;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 解析xml
* @ClassName: Dom4JUtil 
*
 */
public class Dom4JUtil {
	public static final Logger logger = Logger.getLogger(Dom4JUtil.class);
	/**
	 * 调用ws接口返回XML节点元素
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<Element> getHISKSIterator(String str)
			throws DocumentException {
		Document document;
		document = DocumentHelper.parseText(str);
		Element element = document.getRootElement();
		Element RESULT = element.element("RESULT");
		Integer flag = Integer.parseInt(RESULT.getText());
		Iterator<Element> its = null;
		if (flag == 0) {
			Element MSG = element.element("MSG");
			Element XMLTable = MSG.element("XMLTable");
			its = XMLTable.elementIterator("XMLRec");
		}

		return its;
	}

	/**
	 * XML转换成JavaBean
	 * 
	 * @Title: readXMLToJavaBean
	 * @param t
	 * @param xml
	 * @return List<T> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> readXMLToJavaBean(T t, String xml)
			throws Exception {
		List<T> list = new ArrayList<T>();
		Field[] properties = t.getClass().getDeclaredFields();// 获得实例的属性
		// 实例的set方法
		Method setmeth;
		Iterator<Element> e = getHISKSIterator(xml);
		if(e == null){
			return list;
		}
		while (e.hasNext()) {
			Element foo = (Element) e.next();// 下一个二级节点
			t = (T) t.getClass().newInstance();// 获得对象的新的实例
			for (int j = 0; j < properties.length; j++) {// 遍历所有孙子节点
				// 实例的set方法
				setmeth = t.getClass().getMethod(
						"set"
								+ properties[j].getName().substring(0, 1)
										.toUpperCase()
								+ properties[j].getName().substring(1),
						properties[j].getType());
				// properties[j].getType()为set方法入口参数的参数类型(Class类型)
				setmeth.invoke(t, foo.elementText(properties[j].getName()));// 将对应节点的值存入
			}
			list.add(t);
		}
		return list;
	}

	/**
	 * DMO4J写入XML,获取Document对象
	 * 
	 * @param <T>
	 * @param obj
	 *            泛型对象
	 * @param entityPropertys
	 *            泛型对象的List集合
	 * @param o
	 *            节点名称数组
	 * @param lastRootStr
	 *            最后一个节点名称
	 */
	public static <T> Document writeXmlDocument(T obj, List<T> entityPropertys,
			String[] o, String lastRootStr) {
		try {
			// 新建student.xml文件并新增内容
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("ROOT");
			if(o != null && o.length > 0){
				for(int i = 0 ; i < o.length ; i++){
					root = root.addElement(o[i]);// 添加根节点
				}
			}
			Field[] properties = obj.getClass().getDeclaredFields();// 获得实体类的所有属性
			for (T t : entityPropertys) { // 递归实体
				Element secondRoot = root.addElement(lastRootStr); // 
				for (int i = 0; i < properties.length; i++) {
					// 反射get方法
					Method meth = t.getClass().getMethod(
							"get"
									+ properties[i].getName().substring(0, 1)
											.toUpperCase()
									+ properties[i].getName().substring(1));
					// 为二级节点添加属性，属性值为对应属性的值
					secondRoot.addElement(properties[i].getName()).setText(
							meth.invoke(t).toString());

				}
			}
			return document;
		} catch (Exception e) {
			logger.error("获取Document对象失败",e);
			return null;
		}
	}

	/**
	 * DMO4J写入XML
	 * 
	 * @param <T>
	 * @param obj
	 *            泛型对象
	 * @param entityPropertys
	 *            泛型对象的List集合
	 * @param Encode
	 *            XML自定义编码类型(推荐使用GBK)
	 * @param XMLPathAndName
	 *            XML文件的路径及文件名
	 */
	public static <T> void writeXmlDocument(T obj, List<T> entityPropertys,
			String Encode, String XMLPathAndName) {
		long lasting = System.currentTimeMillis();// 效率检测

		try {
			XMLWriter writer = null;// 声明写XML的对象
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(Encode);// 设置XML文件的编码格式

			String filePath = XMLPathAndName;// 获得文件地址
			File file = new File(filePath);// 获得文件

			if (file.exists()) {
				file.delete();
			}
			// 新建student.xml文件并新增内容
			Document document = DocumentHelper.createDocument();
			String rootname = obj.getClass().getSimpleName();// 获得类名
			Element root = document.addElement(rootname + "s");// 添加根节点
			Field[] properties = obj.getClass().getDeclaredFields();// 获得实体类的所有属性

			for (T t : entityPropertys) { // 递归实体
				Element secondRoot = root.addElement(rootname); // 二级节点

				for (int i = 0; i < properties.length; i++) {
					// 反射get方法
					Method meth = t.getClass().getMethod(
							"get"
									+ properties[i].getName().substring(0, 1)
											.toUpperCase()
									+ properties[i].getName().substring(1));
					// 为二级节点添加属性，属性值为对应属性的值
					secondRoot.addElement(properties[i].getName()).setText(
							meth.invoke(t).toString());

				}
			}
			// 生成XML文件
			writer = new XMLWriter(new FileWriter(file), format);
			writer.write(document);
			writer.close();
			long lasting2 = System.currentTimeMillis();
			System.out.println("写入XML文件结束,用时" + (lasting2 - lasting) + "ms");
		} catch (Exception e) {
			logger.error("写入XML文件失败",e);
		}

	}

	/**
	 *  在document对象中获取指定的str的xml数据
	* @Title: getStringByDocument 
	* @param  document
	* @param  o  
	* @param  str
	* @return String    返回类型 
	* @throws
	 */
	public static String getStringByDocument(Document document ,String[] o  , String str) {
		Element element = document.getRootElement();
		if( o != null && o.length > 0){
			for(int i = 0 ; i < o.length ; i ++){
				element = element.element(o[i]);
			}
		}
		Element xmlRec = element.element(str);
		System.out.println(xmlRec.asXML());
		return xmlRec.asXML();
	}

	/**
	 * 调用ws接口返回XML节点元素
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,String> readXmlToMap(String str)
			throws DocumentException {
		Map<String,String> result = new HashMap<String, String>();
		Document doc = DocumentHelper.parseText(str);
		List<Element> list = doc.getRootElement().elements();
		for(Iterator<Element> it= list.iterator();it.hasNext();){
			Element e = it.next();
			result.put(e.getName(),e.getText());
		}
		return result;
	}

}
