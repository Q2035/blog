package com.test.blog.controller;

import com.test.blog.dto.BlogQuery;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Type;
import com.test.blog.service.BlogService;
import com.test.blog.service.TypeService;
import com.test.blog.util.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Iterator;
import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @PathVariable("id") Long id,
                        Model model){

        List<Type> types =typeService.findTop(1000);
        if (id ==-1){
            id = types.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        List<Blog> blogs = blogService.listBlog(blogQuery);
        Iterator<Blog> iterator = blogs.iterator();
        Blog temp;
        while (iterator.hasNext()){
            temp = iterator.next();
            if (!temp.isPublished()){
                iterator.remove();
            }
        }
        Page<Blog> p = PageUtils.listConvertToPage(blogs, pageable);
        model.addAttribute("types",types);
        model.addAttribute("page",p);
        model.addAttribute("activeTypeId",id);
        return "types";
    }

}
