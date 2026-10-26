package com.tecsun.sisp.npm.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

import com.tecsun.sisp.npm.bean.PolicyVO;
import com.tecsun.sisp.npm.util.PublicMethod;
import com.tecsun.sisp.npm.util.StaticBean;

import bee.cloud.engine.db.DataType;
import bee.cloud.engine.db.cnd.CndFactory;
import bee.cloud.engine.db.core.Cnd;
import bee.cloud.engine.db.siud.Select;

@Path("/npmanagement/anp")
public class PolicyApplication extends NetSuperApplication {

    public PolicyApplication(@Context HttpServletRequest request, @Context UriInfo ui) throws SQLException {
        super(request, ui);
    }

    @GET
    @Path("/getPolicyInfo")
    public String getPolicyInfo(@QueryParam("title") String title) {
        PolicyVO vo = new PolicyVO(doe);
        try {
            Cnd cnd = CndFactory.getCnd(DataType.ORACLE, "1", Cnd.EQ, "1");
            if (!PublicMethod.isEmptyStr(title)) {
                //	title = new String(title.getBytes("iso8859-1"),"UTF-8");
                cnd.and(PolicyVO.TITLE, Cnd.LIKE, "%" + title + "%");
            }
            Select select = vo.getSelect().from(PolicyVO.class, PolicyVO.ID, PolicyVO.CONTEXT, PolicyVO.DISTRICT, PolicyVO.FILENO, PolicyVO.CREATE_TIME, PolicyVO.PUBLISH_DATE, PolicyVO.TITLE,PolicyVO.TYPE).where(cnd);
            Map<String, Object> map = new HashMap<String, Object>();
            if (this.pageno == 1)
                map.put(StaticBean.PARAMETER_CODE.total.toString(), select.count());
            select.limit(pagesize == 0 ? 5 : this.pagesize).offset(this.pageno);
            List list = select.go();
            return ok(list, map);

        } catch (SQLException e) {
            return error("查询出错");
        }
    }

    private static String requiredEnvironment(String key) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required environment variable: " + key);
        }
        return value;
    }

    @POST
    @Path("/savePolicy/{userId}/{appId}")
    public String savePolicy(@PathParam("userId") String userId, @PathParam("appId") String appId, @FormParam("id") int id,@FormParam("district") String district, @FormParam("title") String title, @FormParam("context") String context, @FormParam("fileNo") String fileNo,@FormParam("type") String type,@FormParam("publishDate") String publishDate) throws SQLException {
    	PreparedStatement ps = null;
    	Connection conn = null;
    	try{
            String url = requiredEnvironment("NPM_JDBC_URL");
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			String user = requiredEnvironment("NPM_JDBC_USERNAME");
			String pwd = requiredEnvironment("NPM_JDBC_PASSWORD");
			conn = DriverManager.getConnection(url,user, pwd); //建立连接
			Statement s = conn.createStatement(); 
			String saveUrl = request.getContextPath() + "/imgupload/";
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		     String dir=sdf.format(new Date());
		         File uploadDir = new File(saveUrl+dir);
		         uploadDir.mkdirs();
		         File file =new File(dir+".txt");
		         
		         file.createNewFile();
		         FileWriter fileWritter = new FileWriter(file.getName(),true);
	             BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	             bufferWritter.write(context);
	             bufferWritter.close();
		          
			int len = (int) file.length();
			InputStream fis = new java.io.FileInputStream(file);
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");   
		    Date date = df.parse(publishDate);
		    
		    String time = df.format(date);   
		    Timestamp publish_date = Timestamp.valueOf(time); 
		    Timestamp create_time = Timestamp.valueOf(df.format(new Date())); 
		    //修改
		    if(id>0){
		    	ps = conn.prepareStatement("UPDATE t_policy set title=?,context=?,type=?,fileno=?,district=?,publish_date=? where id=?") ;
		    	ps.setString(1, title);
				ps.setBinaryStream(2,fis,len);
				ps.setString(3, type);
				ps.setString(4, fileNo);
				ps.setString(5, district);
				ps.setTimestamp(6, publish_date);
				ps.setInt(7, id);
	
		    }else{
			ps = conn.prepareStatement("INSERT INTO t_policy (id,title,context,type,fileno,district,publish_date,create_time) VALUES (T_POLICY_SEQ.Nextval, ?,?,?,?,?,?,?)");
			ps.setString(1, title);
			ps.setBinaryStream(2,fis,len);
			ps.setString(3, type);
			ps.setString(4, fileNo);
			ps.setString(5, district);
			ps.setTimestamp(6, publish_date);
			ps.setTimestamp(7, create_time);
		    }
			ps.execute(); //执行操作
			conn.commit();
		         } catch (Exception e) {
		     		System.out.println("Error! "+e);
		     		return error("添加数据失败");
		 		}finally{
		 			if(ps != null){
						ps.close();
					}
					if(conn != null){
						conn.close();
					}
		 		}
       
       return ok("success");
		
    }

    @GET
    @Path("/uploadfile")
    public String uploadfile(@Context HttpServletResponse response) throws FileUploadException {
		String result = "";
		//文件保存目录路径
		//String savePath = pageContex.getServletContext().getRealPath("/") + "attached/";
		    	String savePath = request.getContextPath();
		//文件保存目录URL
		String saveUrl  = request.getContextPath() + "/attached/";
		
		//定义允许上传的文件扩展名
		HashMap<String, String> extMap = new HashMap<String, String>();
		extMap.put("image", "gif,jpg,jpeg,png,bmp");
		extMap.put("flash", "swf,flv");
		extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
		
		//最大文件大小
		long maxSize = 1000000;
		
		response.setContentType("text/html; charset=UTF-8");
		 ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
			if(!ServletFileUpload.isMultipartContent(request)){
				return error("请选择文件。");
			}
		//检查目录
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
		//	out.println(getError("上传目录不存在。"));
			return error("上传目录不存在。");
		}
		//检查目录写权限
		if(!uploadDir.canWrite()){
				return error("上传目录没有写权限。");
		}
		
		String dirName = request.getParameter("dir");
		if (dirName == null) {
			dirName = "image";
		}
		if(!extMap.containsKey(dirName)){
			return error("目录名不正确。");
		}
		//创建文件夹
		savePath += dirName + "/";
		saveUrl += dirName + "/";
		File saveDirFile = new File(savePath);
		if (!saveDirFile.exists()) {
			saveDirFile.mkdirs();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		saveUrl += ymd + "/";
		File dirFile = new File(savePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			long fileSize = item.getSize();
			if (!item.isFormField()) {
				//检查文件大小
				if(item.getSize() > maxSize){
					
					return error("上传文件大小超过限制。");
				}
				//检查扩展名
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
						return error("上传文件扩展名是不允许的扩展名。\n只允许" + extMap.get(dirName) + "格式。");
				}
		
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
				try{
					File uploadedFile = new File(savePath, newFileName);
					item.write(uploadedFile);
				}catch(Exception e){
				
							return error("上传文件失败。");
						
				}
		
				/*JSONObject obj = new JSONObject();
				obj.put("error", 0);
				obj.put("url", saveUrl + newFileName);*/
				return error(saveUrl + newFileName);
				
			}
		}
		return null;
    }
    

    @GET
    @Path("/getPolicyById/{id}/{userId}/{appId}")
    public String getPolicyById(@PathParam("id") long id) {
        try {
            PolicyVO vo = new PolicyVO(doe);
            vo.setId(id);
            vo.load();
            return ok(vo);
        } catch (SQLException e) {
            return error("查询出错");
        }
    }

    @DELETE
    @Path("/delPolicyById/{id}")
    public String delPolicyById(@PathParam("id") long id) {
        try {
            PolicyVO vo = new PolicyVO(doe);
            vo.setId(id);
            vo.delete();
            return ok("success");
        } catch (SQLException e) {
            return error("删除出错");
        }
    }
}
