package com.test.blog.controller;

import com.test.blog.mapper.TestMapper;
import com.test.blog.pojo.Blog;
import com.test.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @RequestMapping("/test")
    @ResponseBody
    public Blog m1(){
        long start = System.currentTimeMillis();
        Blog blog = testMapper.getBlog(Long.valueOf(12));
        System.out.println(System.currentTimeMillis()-start);
        return blog;
    }
}
