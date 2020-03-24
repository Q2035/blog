package com.test.blog.controller.admin;

import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Tag;
import com.test.blog.pojo.User;
import com.test.blog.service.BlogService;
import com.test.blog.service.DetailedBlogService;
import com.test.blog.service.TagService;
import com.test.blog.service.TypeService;
import com.test.blog.util.PageUtils;
import com.test.blog.dto.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT ="admin/blogs-input";
    private static final String LIST ="admin/blogs";
    private static final String REDIRECT_LIST ="redirect:/admin/blogs";

    @Autowired
    private TypeService typeService;

    @Autowired
    private DetailedBlogService detailedBlogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;
    
    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog,
                       Model model){
        model.addAttribute("types",typeService.listType());
        addTypeForBlog(pageable, blog, model);
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog,
                       Model model){
        addTypeForBlog(pageable, blog, model);
        return "admin/blogs :: blogList";
    }

    private void addTypeForBlog(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                                BlogQuery blog,
                                Model model) {
        List<Blog> b = blogService.listBlog(blog);
        Page<Blog> blogs = PageUtils.listConvertToPage(b, pageable);
        model.addAttribute("page", blogs);
    }

    @GetMapping("/blogs/input")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable("id")Long id, Model model){
        setTypeAndTag(model);
        Blog blog = detailedBlogService.getBlogById(id);
        List<String> tags = blogService.selectTagsWithBlogId(id);
//        if (!tags.isEmpty()){
//            List<Tag> t =new ArrayList<>();
//            for (String tag : tags) {
//                t.add(new Tag(id,tag));
//            }
//            blog.setTags(t);
//        }
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }


    @PostMapping("/blogs")
    public String post(Blog blog,
                       HttpSession session,
                       RedirectAttributes attributes){
        if (blog.getFlag().equals("") || blog.getFlag() ==null){
            blog.setFlag("原创");
        }
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTags(blog.getTagIds()));
        try {
            blogService.saveBlog(blog);
            blog.setId(blogService.findByTitle(blog.getTitle()).getId());
            blogService.saveBlogTags(blog);
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message","发布失败");
        }
        attributes.addFlashAttribute("message","操作成功");

        return REDIRECT_LIST;
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable("id") Long id,
                         RedirectAttributes attributes){

        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }
}
