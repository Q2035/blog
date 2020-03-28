package com.test.blog.controller;

import com.test.blog.mapper.DetailedBlogMapper;
import com.test.blog.mapper.TagMapper;
import com.test.blog.mapper.TestMapper;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Tag;
import com.test.blog.service.BlogService;
import com.test.blog.util.RedisUtil;
import com.test.blog.vo.BlogVO;
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
    private BlogService blogService;

    @Autowired
    private TagMapper tagMapper;

    @RequestMapping("test")
    @ResponseBody
    public List<Tag> t1(){
        return tagMapper.listTagTop(10);
    }

}
