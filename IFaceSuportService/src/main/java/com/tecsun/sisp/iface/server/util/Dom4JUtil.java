package com.tecsun.sisp.iface.server.util;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.tecsun.sisp.iface.common.util.Constants;
import com.tecsun.sisp.iface.common.vo.faceverify.KeyValueVO;

/**
 * 解析xml
* @ClassName: Dom4JUtil 
* @date 2015年6月18日 下午9:46:56 
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
	 * 调用ws接口返回XML节点元素'
	 * 用于中心医院报文解析
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<Element> getHisIterator_zx(String str)
			throws DocumentException {
		Iterator<Element> its = null;
		try{
			Document document = DocumentHelper.parseText(str);
			Element element = document.getRootElement();
			Element returnresult = element.element("returnresult");
			Element returncode = returnresult.element("returncode");
			String message = returncode.getText();
			if(message.equals("1")){
				Element data = element.element("data");
				its = data.elementIterator("data_row");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return its ;
	}
    /**
     * 调用ws接口返回XML节点元素'
     * 用于档案查询报文解析
     */
    @SuppressWarnings("unchecked")
    public static Iterator<Element> getIterator_Archive(String str)
            throws DocumentException {
        Iterator<Element> its = null;
        try{
            Document document = DocumentHelper.parseText(str);
            Element element = document.getRootElement();
            its = element.elementIterator("row");
        }catch(Exception e){
            e.printStackTrace();
        }
        return its ;
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

	@SuppressWarnings("unchecked")
	public static Iterator<Element> getIterator(String xml){
		Iterator<Element> its = null;
		try{
			Document document = DocumentHelper.parseText(xml);
			Element element = document.getRootElement();
			Element returnresult = element.element("returnresult");
			Element returncode = returnresult.element("returncode");
			String message = returncode.getText();
			if(message.equals("1")){
				Element data = element.element("data");
				its = data.elementIterator("data_row");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return its ;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> readXMLToJavaBean_Zx(T t,String xml)
			throws Exception {
		List<T> list = new ArrayList<T>();
		Field[] properties = t.getClass().getDeclaredFields();// 获得实例的属性
		// 实例的set方法
		Method setmeth;
		Iterator<Element> e = getHisIterator_zx(xml);
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
    @SuppressWarnings("unchecked")
    public static <T> List<T> readXMLToJavaBean_Archive(T t,String xml)
            throws Exception {
        List<T> list = new ArrayList<T>();
        Field[] properties = t.getClass().getDeclaredFields();// 获得实例的属性
        // 实例的set方法
        Method setmeth;
        Iterator<Element> e = getIterator_Archive(xml);
        if(e == null){
            return list;
        }
        while (e.hasNext()) {
            Element foo = (Element) e.next();// 下一个二级节点
            t = (T) t.getClass().newInstance();// 获得对象的新的实例
            List<Element> columns = foo.elements("column");
            for (int j = 0; j < properties.length; j++) {// 遍历所有孙子节点
                // 实例的set方法
                setmeth = t.getClass().getMethod(
                        "set"
                                + properties[j].getName().substring(0, 1)
                                .toUpperCase()
                                + properties[j].getName().substring(1),
                        properties[j].getType());
                System.out.println(j+":"+columns.get(j).getName()+":"+columns.get(j).getText());
                // properties[j].getType()为set方法入口参数的参数类型(Class类型)
                setmeth.invoke(t,columns.get(j).getText());// 将对应节点的值存入
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
					String s = "";
					if(meth.invoke(t) != null && !"null".equals(meth.invoke(t))){
						s = meth.invoke(t).toString();
					}
					secondRoot.addElement(properties[i].getName()).setText(
							s);

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
		return xmlRec.asXML();
	}
	/**
	 * 调用ws接口返回XML节点元素
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<Element> getNewOrderIterator(String str)
			throws DocumentException {
		Iterator<Element> its = null;
		try{
			Document document = DocumentHelper.parseText(str);
			Element element = document.getRootElement();
			Element returnresult = element.element("returnresult");
			Element returncode = returnresult.element("returncode");
			String message = returncode.getText();
			if(message.equals("1")){
				its = element.elementIterator("outputvalues");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return its ;
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
	public static <T> List<T> readNewOrderXMLToJavaBean(T t, String xml)
			throws Exception {
		List<T> list = new ArrayList<T>();
		Field[] properties = t.getClass().getDeclaredFields();// 获得实例的属性
		// 实例的set方法
		Method setmeth;
		Iterator<Element> e = getNewOrderIterator(xml);
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
	 * 调用ws接口返回XML节点元素
	 */
	@SuppressWarnings("unchecked")
	public static int  checkWsReturnCode(String str)
			throws DocumentException {
		int returncodeValue = 0;
		try{
			Document document = DocumentHelper.parseText(str);
			Element element = document.getRootElement();
			Element returnresult = element.element("returnresult");
			Element returncode = returnresult.element("returncode");
			String message = returncode.getText();
			returncodeValue =Integer.valueOf(message);
		}catch(Exception e){
			e.printStackTrace();
		}
		return returncodeValue ;
	}
    /**
     * 调用档案查询的ws接口返回XML节点元素
     */
    @SuppressWarnings("unchecked")
    public static String  checkArchiveReturnCode(String str)
            throws DocumentException {
        String message="";
        try{
            Document document = DocumentHelper.parseText(str);
            Element element = document.getRootElement();
            String error=element.getName();
            if(error.equals("error"))
             message = element.getText();

        }catch(Exception e){
            e.printStackTrace();
        }
        return message ;
    }
    /**心医院接口，获取查询锁号结果
	 * 调用ws接口返回XML节点元素
	 */
	@SuppressWarnings("unchecked")
	public static int  checkLockflag(String str) throws DocumentException {
		int returnLockflag = 0;
		try{
			Document document = DocumentHelper.parseText(str);
			Element element = document.getRootElement();
			Element returnresult = element.element("outputvalues");
			Element lockflag = returnresult.element("lockflag");
			String message = lockflag.getText();
            returnLockflag =Integer.valueOf(message);
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnLockflag ;
	}
	
	
	/**
	 * 中心医院接口，获取科室总个数
	 */
	@SuppressWarnings("unchecked")
	public static int  checkDeptmentCont(String str)
			throws DocumentException {
		int returncodeValue = 0;
		try{
			Document document = DocumentHelper.parseText(str);
			Element element = document.getRootElement();
			Element returnresult = element.element("pageactionout");
			Element returncode = returnresult.element("rowcountall");
			String message = returncode.getText();
			returncodeValue =Integer.valueOf(message);
		}catch(Exception e){
			e.printStackTrace();
		}
		return returncodeValue ;
	}
	
	
	/**
	 * 中心医院接口，获取失败原因
	 */
	@SuppressWarnings("unchecked")
	public static String getMessage (String str)
			throws DocumentException {
		String returncodeValue = "";
		try{
			Document document = DocumentHelper.parseText(str);
			Element element = document.getRootElement();
			Element returnresult = element.element("returnresult");
			Element returncode = returnresult.element("errormsg");
			String message = returncode.getText();
			returncodeValue =message;
		}catch(Exception e){
			e.printStackTrace();
		}
		return returncodeValue ;
	}
	
	/**
	 * 解析省农保xml
	 * @param t
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> readXMLToJavaBean_SNB(T t,String xml)
			throws Exception {
		List<T> list = new ArrayList<T>();
		Field[] properties = t.getClass().getDeclaredFields();// 获得实例的属性
		// 实例的set方法
		Method setmeth;
			Document document = DocumentHelper.parseText(xml);
			Element element = document.getRootElement();
		//	Element data = element.element("rows");
			Iterator<Element> its = element.elementIterator("row");
		if(its == null){
			return list;
		}
		while (its.hasNext()) {
			Element foo = (Element) its.next();// 下一个二级节点
			t = (T) t.getClass().newInstance();// 获得对象的新的实例
			for (int j = 0; j < properties.length; j++) {// 遍历所有孙子节点
				// 实例的set方法
				setmeth = t.getClass().getMethod("set"
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
     * 将18位身份证号转化为15位返回,非18位身份证号原值返回 
     * @param identityCard 18位身份证号 
     * @return 15身份证 
     */  
    public static String get15Ic(String identityCard) {  
        String retId = "";  
        if(null == identityCard){  
            return retId;  
        }  
        if(identityCard.length() == 18){  
            retId = identityCard.substring(0, 6) + identityCard.substring(8, 17);  
        }else{  
            return identityCard;  
        }  
        return retId;  
    }  
      
    /** 
     * 将15位身份证号转化为18位返回，非15位身份证号原值返回 
     * @param identityCard 
     * @return 
     */  
    public static String get18Ic(String identityCard) {  
        String retId = "";  
        String id17 = "";  
        int sum = 0;  
        int y = 0;  
        // 定义数组存放加权因子（weight factor）  
        int[] wf = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };  
        // 定义数组存放校验码（check code）  
        String[] cc = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };  
        if (identityCard.length() != 15) {  
            return identityCard;  
        }  
        // 加上两位年19  
        id17 = identityCard.substring(0, 6) + "19" + identityCard.substring(6);  
        // 十七位数字本体码加权求和  
        for (int i = 0; i < 17; i++) {  
            sum = sum + Integer.valueOf(id17.substring(i, i + 1)) * wf[i];  
        }  
        // 计算模  
        y = sum % 11;  
        // 通过模得到对应的校验码 cc[y]  
        retId = id17 + cc[y];  
        return retId;  
    }  
    
    
    public static String getConfigUrl(String url ){
        Config config = Config. getInstance();
        String urlStr = config.get( url);
         return urlStr ;
  }
    
    
    //德生宝传汉字，后台需要根据汉字找出相对庆的代码
   public static List<KeyValueVO> getNationByKey(String keyValue) {
    	 List<KeyValueVO> list = new ArrayList();
    	 String key = Constants.DICTIONGROUPKEY.replace("groupId", keyValue);
         String result=Constants.RESULT_MESSAGE_SUCCESS;
		try {
			if(!key.isEmpty()){
				result = JedisUtil.getValue(key);
				String[] dictionaryArray=result.split(",");
				for (String bean:dictionaryArray){
					KeyValueVO bank = new KeyValueVO();
					bank.setKey(bean.split(":")[0]);
					bank.setValue(bean.split(":")[1]);
					list.add(bank);
				}
			}else{
				logger.error("调用:CollectIface:getNationByKey入参不能为空！");
			}
		} catch (Exception e) {
			logger.error("调用:CollectIface:getNationByKey" +e.getMessage());
			e.printStackTrace();
		}
    	return list;
    }

	// 从redis中找出民族对的代码
   // ethnicValue 代表民族 --判断德生宝入参-民族，根据德生宝传的民族中文汉字查询出相对应的民族代码\
   //sexVaule代表性别
   /**
    * 
    * @param type ethnicValue 代表民族,sexVaule代表性别
    * @return
    * @ 判断德生宝入参-民族,性别 ，根据德生宝传的民族中文汉字查询出相对应的民族代码、性别代码
    */
	public static String getKeyByValue(String type) {
		String nation = "01";
		String keyValue = Constants.PARAM_NATION_GROUP;
		if (type != null && type.length() > 0) {
			if("ethnicValue".equals(type)){
				type = Constants.PARAM_NATION_GROUP;
			}else if("sexValue".equals(type)){
				type = Constants.SEX_GROUP;
			}else {
				type = Constants.PARAM_NATION_GROUP;
			}
			
			List<KeyValueVO> nationList = getNationByKey(keyValue);
			if (!nationList.isEmpty() && nationList.size() > 0) {
				for (int i = 0; i < nationList.size(); i++) {
					if (type.trim().equals(nationList.get(i).getValue())) {
						nation =nationList.get(i).getKey();
						break;
					}
				}
			}
		}
		return nation;
	}
}
