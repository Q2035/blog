package com.test.blog.controller;

import com.test.blog.dto.BlogQuery;
import com.test.blog.service.BlogService;
import com.test.blog.vo.AdminBlogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("test")
public class TestController {

    @Autowired
    private BlogService blogService;

    @RequestMapping("t1")
    public List<AdminBlogVO> t1(){
        return blogService.listAllAdminBlogs();
    }
    @RequestMapping("t2")
    public List<AdminBlogVO> t2(){
        BlogQuery blog = new BlogQuery();
        blog.setRecommend(false);
        blog.setTypeId((long) 4);
        return blogService.listSpecificAdminBlogs(blog);
    }
}
