package com.test.blog.controller;

import com.test.blog.mapper.DetailedBlogMapper;
import com.test.blog.mapper.TestMapper;
import com.test.blog.pojo.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class TestController {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private DetailedBlogMapper detailedBlogMapper;

    @RequestMapping("/test")
    @ResponseBody
    public List<Blog> m1(){
        long start = System.currentTimeMillis();
//        Blog blog = testMapper.getTagIdsBlog(Long.valueOf(12));
        List<Blog> blogs = detailedBlogMapper.selectDetailsOfAllBlog();
        System.out.println(System.currentTimeMillis()-start);
        return blogs;
    }
}
