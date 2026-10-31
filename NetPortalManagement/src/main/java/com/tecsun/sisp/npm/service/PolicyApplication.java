package com.tecsun.sisp.npm.service;

import com.tecsun.sisp.npm.bean.PolicyVO;
import com.tecsun.sisp.npm.repository.NetPortalRepository;
import com.tecsun.sisp.npm.util.AppConfig;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

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
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Path("/npmanagement/anp")
public class PolicyApplication extends NetSuperApplication {
    private static final Logger LOGGER = Logger.getLogger(PolicyApplication.class);
    private static final NetPortalRepository REPOSITORY = new NetPortalRepository();
    private static final long MAX_UPLOAD_SIZE = 1000000L;

    public PolicyApplication(@Context HttpServletRequest request, @Context UriInfo ui) {
        super(request, ui);
    }

    @GET
    @Path("/getPolicyInfo")
    public String getPolicyInfo(@QueryParam("title") String title) {
        try (Connection connection = openConnection()) {
            List<PolicyVO> policies = REPOSITORY.findPolicies(connection, title, pageno, pagesize);
            Map<String, Object> extra = new HashMap<String, Object>();
            if (pageno == 1) {
                extra.put("total", REPOSITORY.countPolicies(connection, title));
            }
            return ok(policies, extra);
        } catch (SQLException e) {
            LOGGER.error("查询政策出错", e);
            return error("查询出错");
        }
    }

    @POST
    @Path("/savePolicy/{userId}/{appId}")
    public String savePolicy(@PathParam("userId") String userId,
                             @PathParam("appId") String appId,
                             @FormParam("id") int id,
                             @FormParam("district") String district,
                             @FormParam("title") String title,
                             @FormParam("context") String context,
                             @FormParam("fileNo") String fileNo,
                             @FormParam("type") String type,
                             @FormParam("publishDate") String publishDate) {
        try (Connection connection = openConnection()) {
            connection.setAutoCommit(false);
            REPOSITORY.savePolicy(connection, id, title,
                    context == null ? new byte[0] : context.getBytes(Charset.forName("UTF-8")),
                    type, fileNo, district, parseTimestamp(publishDate), new Timestamp(System.currentTimeMillis()));
            connection.commit();
            return ok("success");
        } catch (Exception e) {
            LOGGER.error("保存政策出错", e);
            return error("添加数据失败");
        }
    }

    @GET
    @Path("/uploadfile")
    public String uploadfile(@Context HttpServletResponse response) throws FileUploadException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            return error("请选择文件。");
        }
        Map<String, String> extensions = new HashMap<String, String>();
        extensions.put("image", "gif,jpg,jpeg,png,bmp");
        extensions.put("flash", "swf,flv");
        extensions.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        extensions.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

        String directory = request.getParameter("dir");
        if (directory == null) {
            directory = "image";
        }
        if (!extensions.containsKey(directory)) {
            return error("目录名不正确。");
        }

        File root = new File(AppConfig.get("upload.dir", "uploads"));
        File dateDirectory = new File(new File(root, directory), new SimpleDateFormat("yyyyMMdd").format(new Date()));
        if (!dateDirectory.exists() && !dateDirectory.mkdirs()) {
            return error("上传目录创建失败。");
        }
        if (!dateDirectory.canWrite()) {
            return error("上传目录没有写权限。");
        }

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        upload.setSizeMax(MAX_UPLOAD_SIZE);
        List<?> items = upload.parseRequest(request);
        for (Object value : items) {
            FileItem item = (FileItem) value;
            if (item.isFormField()) {
                continue;
            }
            String fileName = new File(item.getName()).getName();
            String extension = extension(fileName);
            if (extension.isEmpty() || !Arrays.asList(extensions.get(directory).split(",")).contains(extension)) {
                return error("上传文件扩展名是不允许的扩展名。");
            }
            String generatedName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
                    "_" + new Random().nextInt(1000) + "." + extension;
            try {
                item.write(new File(dateDirectory, generatedName));
            } catch (Exception e) {
                LOGGER.error("上传文件失败", e);
                return error("上传文件失败");
            }
            String url = request.getContextPath() + "/attached/" + directory + "/" +
                    new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + generatedName;
            response.setContentType("text/plain; charset=UTF-8");
            return ok(url);
        }
        return error("没有找到上传文件。");
    }

    @GET
    @Path("/getPolicyById/{id}/{userId}/{appId}")
    public String getPolicyById(@PathParam("id") long id) {
        try (Connection connection = openConnection()) {
            return ok(REPOSITORY.findPolicy(connection, id));
        } catch (SQLException e) {
            LOGGER.error("查询政策出错", e);
            return error("查询出错");
        }
    }

    @DELETE
    @Path("/delPolicyById/{id}")
    public String delPolicyById(@PathParam("id") long id) {
        try (Connection connection = openConnection()) {
            return ok(REPOSITORY.deletePolicy(connection, id) == 1 ? "success" : "not_found");
        } catch (SQLException e) {
            LOGGER.error("删除政策出错", e);
            return error("删除出错");
        }
    }

    private Timestamp parseTimestamp(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new Timestamp(System.currentTimeMillis());
        }
        String[] patterns = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd"};
        for (String pattern : patterns) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                format.setLenient(false);
                Date date = format.parse(value);
                return new Timestamp(date.getTime());
            } catch (ParseException ignored) {
                // Try the next compatible legacy format.
            }
        }
        throw new IllegalArgumentException("publishDate 格式不正确");
    }

    private String extension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase();
    }
}
