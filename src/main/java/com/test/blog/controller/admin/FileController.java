package com.test.blog.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

@Controller
@RequestMapping("/files")
public class FileController {

    private String basePath = File.separator + "tmp" + File.separator;

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 文件上传
     * @return
     */
    @PostMapping("/upload")
    public String postFile(HttpServletRequest request,
                           @RequestParam("file") MultipartFile file){
        if (file.isEmpty()){
            logger.warn("{} upload an empty file.",request.getRemoteAddr());
        }
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        logger.info("{} upload:name{} type{}",fileName, contentType);
        String filePath = basePath + fileName;
        File dest = new File(filePath);
//        查看是否存在目录
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            logger.error("upload error!");
            e.printStackTrace();
        }
        return "redirect:/admin/files";
    }
}
