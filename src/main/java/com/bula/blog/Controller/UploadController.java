package com.bula.blog.Controller;

import com.bula.blog.Util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link http://13blog.site
 */
@Controller
public class UploadController {

    private Logger logger = LoggerFactory.getLogger(UploadController.class);

    @PostMapping("/admin/blogs/md/uploadfile")
    public void uploadFileByEditormd(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(name = "editormd-image-file", required = true)
                                             MultipartFile file) throws IOException, URISyntaxException {
        try {
            String url = new QiNiuCloud().UpLoadFile(file);
            request.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type", "text/html");
            response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + url + "\"}");
        } catch (IOException e) {
            response.getWriter().write("{\"success\":0}");
        }


        //        String fileName = file.getOriginalFilename();
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));
//        //生成文件名称通用方法
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
//        Random r = new Random();
//        StringBuilder tempName = new StringBuilder();
//        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
//        String newFileName = tempName.toString();
//        //创建文件
//        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
//        String fileUrl = BlogUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
//        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
//        try {
//            if (!fileDirectory.exists()) {
//                if (!fileDirectory.mkdir()) {
//                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
//                }
//            }
//            file.transferTo(destFile);
//        request.setCharacterEncoding("utf-8");
//        response.setHeader("Content-Type", "text/html");
//        response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + url + "\"}");
//        } catch (UnsupportedEncodingException e) {
//            logger.debug("转码错误");
//            response.getWriter().write("{\"success\":0}");
//        } catch (IOException e) {
//            logger.debug("文件夹创建失败");
//            response.getWriter().write("{\"success\":0}");
//        }
    }

}
