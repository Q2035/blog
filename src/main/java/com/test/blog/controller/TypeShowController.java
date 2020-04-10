package com.test.blog.controller;

import com.test.blog.pojo.Type;
import com.test.blog.service.BlogService;
import com.test.blog.service.TypeService;
import com.test.blog.util.PageUtils;
import com.test.blog.vo.BlogVO;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 10000,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @PathVariable("id") Long id,
                        Model model){
        List<Type> types =typeService.findTypeTop(1000);
        String typeName ="";
        if (id ==-1){
            id = types.get(0).getId();
        }
        for (Type type : types) {
            if (id == type.getId()){
                typeName = type.getName();
            }
        }
        List<BlogVO> blogs = blogService.listBlogVOWithTypeId(typeName);
        Page<BlogVO> p = PageUtils.listConvertToPage(blogs, pageable);
        model.addAttribute("types",types);
        model.addAttribute("page",p);
        model.addAttribute("activeTypeId",id);
        return "types";
    }

}
