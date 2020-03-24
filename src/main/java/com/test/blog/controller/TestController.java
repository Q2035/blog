package com.test.blog.controller;

import com.test.blog.mapper.DetailedBlogMapper;
import com.test.blog.mapper.TestMapper;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Tag;
import com.test.blog.util.RedisUtil;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class TestController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private DetailedBlogMapper detailedBlogMapper;


//    @RequestMapping("/test")
//    @ResponseBody
//    public List<Blog> m1(){
//        long start = System.currentTimeMillis();
////        Blog blog = testMapper.getTagIdsBlog(Long.valueOf(12));
//        List<Blog> blogs = detailedBlogMapper.selectDetailsOfAllBlog();
//        System.out.println(System.currentTimeMillis()-start);
//        return blogs;
//    }
    @RequestMapping("/test")
    @ResponseBody
    public Blog m2(){
        Blog blog12 = detailedBlogMapper.getBlogById((long) 12);
        redisUtil.set("blog12",blog12);
        long start = System.currentTimeMillis();
        Blog blog = (Blog) redisUtil.get("blog12");
        System.out.println(System.currentTimeMillis()-start);
        return blog;
    }

    @RequestMapping("/test1")
    @ResponseBody
    public List<Tag> m3(){
        long start = System.currentTimeMillis();
        IndexController indexController = new IndexController();
        List<Tag> tags = (List<Tag>) redisUtil.get(indexController.REDIS_TOP_TAGS);
        System.out.println(System.currentTimeMillis()-start);
        return tags;
    }

}
