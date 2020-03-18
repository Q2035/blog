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
import com.test.blog.util.RedisUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private RedisUtil redisUtil;

    private Integer MAX_RECOMMEND_BLOG_NUM = 3;
    private Integer MAX_TYPE_INDEX = 6;
    private Integer MAX_TAG_INDEX = 10;
//
    /**
     * 需要注意，不应该出现为草稿的博客
     * 2020年3月17日
     *      不知道为啥总是有很多重复查询所有博客，现在已经完了，明天再说。
     *      今天查询所有博客的xml配置文件已经可以正常运行了，Blog里可以包含Comment、User等，但是User内的Blog还未注入？（明天再说）
     * 2020.3.18
     *      很好，加了个addIntoBlogT，index访问时间从10s缩短到5s左右
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping({"/index","/"})
    public String index(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        List<Blog> blogs = (List<Blog>) redisUtil.get("blogs");
        if (blogs==null){
            List<Blog> blogs1 = blogService.listAllBlogs();
            redisUtil.set("blogs",blogs1);
            blogs = blogs1;
        }
        List<Type> top = typeService.findTop(MAX_TYPE_INDEX);
        addBlogsIntoT(top,blogs);
        List<Tag> tags = tagService.listTagTop(MAX_TAG_INDEX);
        addBlogsIntoT(tags,blogs);
//        List<Blog> recommmendBlogs = blogService.listRecommmendBlogs(8);
        List<Blog> recommmendBlogs = blogs.stream().filter(blog -> blog.isRecommend()).limit(MAX_RECOMMEND_BLOG_NUM).collect(Collectors.toList());
//        这两个查询肯定不会很耗时间
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

    protected  <T> void addBlogsIntoT(List<T> t,List<Blog> blogs){
        List<Blog> temp = null;

        for (T t1 : t) {
            if (t1 instanceof Type){
                temp = new ArrayList<>();
                for (Blog blog : blogs) {
                    if (blog.getType().getId() == ((Type) t1).getId()){
                        temp.add(blog);
                    }
                }
                ((Type) t1).setBlogs(temp);
            }else if (t1 instanceof Tag){
//                也就是把blogs插入tag中
//                存储位置是两张表，那么就需要再查一次数据库？join子句？先来试试
                temp = new ArrayList<>();
                for (Blog blog : blogs) {
                    List<Tag> tags = blog.getTags();
                    for (Tag tag : tags) {
                        if (tag.getId() == ((Tag) t1).getId()){
                            temp.add(blog);
                            break;
                        }
                    }
                }
                ((Tag) t1).setBlogs(temp);
            }
        }
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
