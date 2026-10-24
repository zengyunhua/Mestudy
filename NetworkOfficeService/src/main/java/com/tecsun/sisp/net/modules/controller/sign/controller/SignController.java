package com.tecsun.sisp.net.modules.controller.sign.controller;

import com.tecsun.sisp.net.common.Config;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tecsun.sisp.net.common.Constants;
import com.tecsun.sisp.net.common.vo.faceverify.Result;
import com.tecsun.sisp.net.modules.controller.sign.service.impl.PdfService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lorn on 2018/7/27.
 */
@RestController
@RequestMapping("/adapter/sign")
public class SignController {

    private static final Logger logger = LoggerFactory.getLogger(SignController.class);

    @Autowired
    private PdfService pdfService;

    @RequestMapping(value = "/signPdf", method = RequestMethod.POST, produces = "application/json")
    public Result SignPdf(@RequestBody Map<String,String> bean) {

        String templateName = bean.get("templateName");
        Map<String,String> dataFieldMap = new HashMap<>();
        dataFieldMap.put("id",bean.get("id"));
        dataFieldMap.put("name",bean.get("name"));
        dataFieldMap.put("channelcode",bean.get("channelcode"));
        Map<String,String> map = null;
        try {
            map = pdfService.getData(dataFieldMap);
        } catch (Exception e) {
            logger.error("生成签章证明失败.",e);
            return new Result("-100","生成签章证明失败." + e.getMessage(),"");
        }
        if(map == null || map.size() == 0)
            return new Result("-100","生成签章证明失败","");

        String pdfPath = null;
        try {
            pdfPath = pdfService.createSignFile(templateName, map);
        } catch (Exception e) {
            logger.error("生成签章证明失败.",e);
            return new Result("-100","生成签章证明失败。" + e.getMessage(),"");
        }

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("signfile",pdfPath);

        return new Result(Constants.RESULT_MESSAGE_SUCCESS,"生成签章证明成功",dataMap);
    }

    /**
     * Streams a generated PDF from the configured storage directory.
     *
     * <p>The canonical-path check prevents callers from escaping the storage
     * root with relative path segments or platform-specific separators.</p>
     */
    @RequestMapping(value = "/download/{fileName:.*}", method = RequestMethod.GET)
    public void downloadFile(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        File storageRoot = new File(Config.getInstance().get("signpdf.save.path")).getCanonicalFile();
        File file = new File(storageRoot, fileName).getCanonicalFile();
        String storagePrefix = storageRoot.getPath() + File.separator;

        if (!file.getPath().startsWith(storagePrefix) || !file.isFile()) {
            logger.warn("Rejected invalid signed PDF download request: {}", fileName);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Length", Long.toString(file.length()));
        String encodedName = URLEncoder.encode(file.getName(), "UTF-8").replace("+", "%20");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedName);

        try (InputStream input = new BufferedInputStream(new FileInputStream(file));
             OutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            IOUtils.copy(input, output);
            output.flush();
        } catch (IOException e) {
            logger.error("Failed to download signed PDF: {}", file.getName(), e);
            throw e;
        }
    }

}