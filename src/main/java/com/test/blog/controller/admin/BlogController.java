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
import com.test.blog.util.RedisUtil;
import com.test.blog.vo.AdminBlogVO;
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

import static com.test.blog.util.PageUtils.listConvertToPage;
import static com.test.blog.util.RedisDataName.*;

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

    @Autowired
    private RedisUtil redisUtil;
    
    @GetMapping("/blogs")
    public String list(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model){
        model.addAttribute("types",typeService.listType());
        List<AdminBlogVO> b = blogService.listAllAdminBlogs();
        Page<AdminBlogVO> blogs = listConvertToPage(b, pageable);
        model.addAttribute("page", blogs);
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog,
                       Model model){
        List<AdminBlogVO> blogs = blogService.listSpecificAdminBlogs(blog);
        Page<AdminBlogVO> page = listConvertToPage(blogs, pageable);
        model.addAttribute("page",page);
        return "admin/blogs :: blogList";
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
        blog.init();
        model.addAttribute("blog", blog);
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }


    /**
     * 新增或者修改博客内容
     *  此处需要更新Redis内容
     * @param blog
     * @param session
     * @param attributes
     * @return
     */
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
            redisUtil.remove(ALL_BLOGVOS);
            redisUtil.remove(TOP_TAGS);
            redisUtil.remove(TOP_TYPES);
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message","发布失败");
            return "error/500";
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
