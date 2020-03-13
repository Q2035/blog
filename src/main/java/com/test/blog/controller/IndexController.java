package com.test.blog.controller;

import com.test.blog.exception.BlogNotFoundException;
import com.test.blog.mapper.DetailedBlogMapper;
import com.test.blog.mapper.FriendLinksMapper;
import com.test.blog.mapper.UsefulToolsMapper;
import com.test.blog.pojo.*;
import com.test.blog.service.BlogService;
import com.test.blog.service.TagService;
import com.test.blog.service.TypeService;
import static com.test.blog.util.PageUtils.listConvertToPage;

import com.test.blog.util.MarkdownUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private DetailedBlogMapper detailedBlogMapper;

    @Autowired
    private FriendLinksMapper friendLinksMapper;

    @Autowired
    private UsefulToolsMapper usefulToolsMapper;

    /**
     * 需要注意，不应该出现为草稿的博客
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping({"/index","/"})
    public String index(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        List<Blog> blogs = blogService.listAllBlogs();
        List<Type> top = typeService.findTop(6);
        List<Tag> tags = tagService.listTagTop(10);
        List<Blog> recommmendBlogs = blogService.listRecommmendBlogs(8);
        List<FriendLinks> links = friendLinksMapper.listAllLinks();
        List<UsefulTool> usefulTools = usefulToolsMapper.listAllLinks();
        model.addAttribute("tools",usefulTools);
        model.addAttribute("links",links);
        model.addAttribute("types",top);
        model.addAttribute("tags", tags);
        model.addAttribute("recommendBlogs",recommmendBlogs);
        model.addAttribute("page", listConvertToPage(blogs,pageable));
        return "index";
    }

//    移除不被推荐的blog
    private List<Blog> removeUnRecommendBlog(List<Blog> blogs){
        List<Blog> resultBlog =new ArrayList<>();
        BeanUtils.copyProperties(blogs,resultBlog);
        for (Blog blog : blogs) {
            if (!blog.isRecommend()){
                resultBlog.remove(blog);
            }
        }
        return resultBlog;
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @RequestParam("query") String query,
                        Model model){
        List<Blog> o = detailedBlogMapper.selectBlogWithString("%" + query + "%");
        model.addAttribute("page", listConvertToPage(o,pageable));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable("id")Long id,
                       Model model){
        Blog blog = detailedBlogMapper.getBlogById(id);
        List<Tag> tags = tagService.getBlogTagsWithBlogId(blog.getId());
        blog.setTags(tags);
        model.addAttribute("blog", blogConvert(blog));
        blogService.updateViews(id);
        return "blog";
    }


    @GetMapping("/about")
    public String about(){

        return "about";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommmendBlogs(3));
        return "_fragments :: newblogList";
    }

    /**
     * 使用一个新的Blog的目的在于保证数据库数据的纯净
     * @param blog
     */
    private Blog blogConvert(Blog blog){
        if (blog ==null){
            throw new BlogNotFoundException("The blog doesn't exist");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content =b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }
}
