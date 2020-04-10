package com.test.blog.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/files")
public class FileController {

    /**
     * 文件上传
     * @return
     */
    @PostMapping("/upload")
    public String postFile(){
        return "redirect:/admin/files";
    }
}
