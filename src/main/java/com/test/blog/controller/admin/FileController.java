package com.test.blog.controller.admin;

import com.test.blog.util.CommonResult;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

@Controller
@RequestMapping("/admin/files")
public class FileController {

    @Value("${file.basePath}")
    private String privateBasePath;

    @Value("${file.publicBasePath}")
    private String publicBasePath;

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    /**
     * 文件上传
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public CommonResult postFile(HttpServletRequest request,
                                 @RequestParam("file") MultipartFile file){
        CommonResult result = new CommonResult();
        if (file.isEmpty()){
            logger.warn("{} upload an empty file.",request.getRemoteAddr());
        }
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        logger.info("upload name:{} type:{}",fileName, contentType);
//        1:公开 0:私有
        String isPublished = request.getParameter("isPublished");
        String filePath = "";
        if (isPublished.equals("1")){
            filePath = publicBasePath + fileName;
        }else {
            filePath = privateBasePath + fileName;
        }
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
            result.setCode(500);
            result.setMessage("upload error!");
            result.setSuccess(false);
            return result;
        }
        result.setMessage("Success!");
        result.setCode(200);
        result.setSuccess(true);
        logger.info(result.toString());
        return result;
    }

    @ResponseBody
    @GetMapping("/download/{fileName}")
    public CommonResult download(@PathVariable("fileName")String fileName,
                                 HttpServletResponse response,
                                 HttpServletRequest request) throws UnsupportedEncodingException {
        File file = new File(privateBasePath + fileName);
        CommonResult commonResult = new CommonResult();
        if (!file.exists()){
            commonResult.setMessage("error,the file doesn't exist!");
            commonResult.setCode(400);
            commonResult.setSuccess(false);
            logger.warn("user:{}, the file:{} doesn't exist",request.getRemoteAddr(),fileName);
            return commonResult;
        }
        String agent = request.getHeader("User-Agent");
        String tempFileName;
        logger.info("browser agent:{}",agent);
        if(agent.contains("Edge")){	                                    //Edge   已测试
            logger.info("Edge");
            tempFileName = URLEncoder.encode(fileName,UTF_8).replaceAll("\\+","%20");//处理空格转为加号的问题
        }else if(agent.contains("Chrome")){                             //Chrome  已测试
            logger.info("Chrome");
            tempFileName = new String(fileName.getBytes(), ISO_8859_1);
        }else if(agent.contains("Firefox")){					        //Firefox  也是会变成加号
            logger.info("Firefox");
            tempFileName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+","%20");
        }else if(agent.contains("MSIE") || agent.contains("Trident")){  //IE11      已测试
            logger.info("IE");
            //在IE8以后，微软使用了Trident来作为IE浏览器的标志，兼容老的版本
            tempFileName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+","%20");//处理空格转为加号的问题
        }else{
            //其余浏览器  可测试并添加
            logger.info("Other");
            tempFileName = URLEncoder.encode(fileName,"UTF-8").replaceAll("\\+","%20");//待测试
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition","attachment;filename="+ tempFileName);
//        设置文件大小
        response.setContentLengthLong(file.length());
        byte[] buffer = new byte[1024];
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            OutputStream os = response.getOutputStream()) {
            while (bis.read(buffer) != -1){
                os.write(buffer);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commonResult;
    }
}