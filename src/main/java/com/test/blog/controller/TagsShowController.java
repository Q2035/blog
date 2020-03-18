package com.test.blog.controller;

import com.test.blog.dto.BlogQuery;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Tag;
import com.test.blog.pojo.Type;
import com.test.blog.service.BlogService;
import com.test.blog.service.TagService;
import com.test.blog.util.PageUtils;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagsShowController {

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private IndexController indexController;

    @GetMapping("/tags/{id}")
    public String types(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @PathVariable("id") Long id,
                        Model model){
        List<Blog> allBlogs = blogService.listAllBlogs();
        List<Tag> tags = tagService.listTagTop(1000);
        indexController.addBlogsIntoT(tags,allBlogs);
        if (id ==-1){
            id = tags.get(0).getId();
        }
        List<Blog> blogs = blogService.listBlogByTagId(id);
        Page<Blog> p = PageUtils.listConvertToPage(blogs, pageable);
        model.addAttribute("tags",tags);
        model.addAttribute("page",p);
        model.addAttribute("activeTypeId",id);
        return "tags";
    }

}
