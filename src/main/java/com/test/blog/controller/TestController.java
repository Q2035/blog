package com.test.blog.controller;

import com.test.blog.dto.BlogQuery;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.MailBean;
import com.test.blog.service.BlogService;
import com.test.blog.service.DetailedBlogService;
import com.test.blog.util.CommonResult;
import com.test.blog.util.MailUtil;
import com.test.blog.util.StatusCode;
import com.test.blog.vo.AdminBlogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@Controller
@ResponseBody
@RequestMapping("test")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlogService blogService;

    @Autowired
    private DetailedBlogService detailedBlogService;

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

    @ResponseBody
    @PostMapping("contact")
    public CommonResult contact(@RequestParam("email")String email,
                                @RequestParam("describe")String discribe){
        logger.info("email:"+ email+" description:" + discribe);
        CommonResult commonResult = new CommonResult();
        commonResult.setSuccess(true);
        commonResult.setCode(StatusCode.SUCCESS.getCode());
        commonResult.setMessage("SUCCESS");
        return commonResult;
    }

    @Autowired
    private MailUtil mailUtil;

    @ResponseBody
    @GetMapping("mail")
    public String mail(){
        MailBean mailBean = new MailBean();
        mailBean.setRecipient("2383862181@qq.com");
        mailBean.setSubject("SpringBootMail-Text Mail");
        mailBean.setContent("SpringBootMail:send time:"+ new Date());
        mailUtil.sendSimpleMain(mailBean);
        return "OK";
    }

    /**
     * 把http连接升级成https连接
     * @return
     */
    @ResponseBody
    @RequestMapping("/t4")
    public List<Blog> t4(){
        String source = "http://114.55.147.153/";
        String target = "https://hellooooo.top/";
        List<Blog> blogs = blogService.listAllBlogs();
        for (Blog blog : blogs) {
            String firstPicture = blog.getFirstPicture();
            blog.setFirstPicture(firstPicture.replaceAll(source,target));
            blogService.updateBlog(blog.getId(),blog);
        }
        return blogs;
    }

    @ResponseBody
    @RequestMapping("/t5")
    public Blog t5(){
        return detailedBlogService.getBlog(Long.valueOf(13));
    }
}
