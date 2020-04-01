package com.test.blog.controller;

import com.test.blog.exception.BlogNotFoundException;
import com.test.blog.mapper.DetailedBlogMapper;
import com.test.blog.mapper.FriendLinksMapper;
import com.test.blog.mapper.UsefulToolsMapper;
import com.test.blog.pojo.*;
import com.test.blog.service.BlogService;
import com.test.blog.service.DetailedBlogService;
import com.test.blog.service.TagService;
import com.test.blog.service.TypeService;
import static com.test.blog.util.PageUtils.listConvertToPage;
import static com.test.blog.util.RedisDataName.*;

import com.test.blog.util.MarkdownUtils;
import com.test.blog.util.RedisDataName;
import com.test.blog.util.RedisUtil;
import com.test.blog.vo.BlogVO;
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
    private DetailedBlogService detailedBlogService;

    @Autowired
    private FriendLinksMapper friendLinksMapper;

    @Autowired
    private UsefulToolsMapper usefulToolsMapper;

    @Autowired
    private RedisUtil redisUtil;

    private Integer MAX_RECOMMEND_BLOG_NUM = 3;
    private Integer MAX_TYPE_INDEX = 6;
    private Integer MAX_TAG_INDEX = 10;

    public String REDIS_BLOGS="blogs";
    public String REDIS_TOP_TYPES="topTypes";
    public String REDIS_TOP_TAGS="topTags";
//
    /**
     * 需要注意，不应该出现为草稿的博客
     * 2020年3月17日
     *      不知道为啥总是有很多重复查询所有博客，现在已经完了，明天再说。
     *      今天查询所有博客的xml配置文件已经可以正常运行了，Blog里可以包含Comment、User等，但是User内的Blog还未注入？（明天再说）
     * 2020.3.18
     *      很好，加了个addIntoBlogT，index访问时间从10s缩短到5s左右
     *      很可惜，还是有问题。redis缓存的数据无法更新。也就是缓存功能并不特别完整
     * 2020.3.19
     *      这里不写了，写不下了
     * 2020.3.28
     *      好极了，现在index页面启动可以在1s左右
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping({"/index","/"})
    public String index(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        List<BlogVO> blogs;
        List<Type> top;
        List<Tag> tags;
        if (redisUtil.exists(ALL_BLOGVOS)){
            blogs = (List<BlogVO>) redisUtil.get(ALL_BLOGVOS);
        }else{
            blogs = blogService.listAllBlogVOs();
            redisUtil.set(ALL_BLOGVOS,blogs);
        }
//        先留着，万一以后要做分页了
//        List<Blog> blogs1 = blogService.listAllBlogs();
//        int start =pageable.getPageNumber() * pageable.getPageSize();
//        int pageSize = pageable.getPageSize();
//        List<Blog> blogs = detailedBlogService.listBlogsWithPages( start, start + pageSize);
        if (redisUtil.exists(TOP_TYPES)){
            top = (List<Type>) redisUtil.get(TOP_TYPES);
        }else {
            top = typeService.findTypeTop(MAX_TYPE_INDEX);
            redisUtil.set(TOP_TYPES,top);
        }
        if (redisUtil.exists(TOP_TAGS)){
            tags = (List<Tag>) redisUtil.get(TOP_TAGS);
        }else{
            tags = tagService.listTagTop(MAX_TAG_INDEX);
            redisUtil.set(TOP_TAGS,tags);
        }
        List<BlogVO> recommmendBlogs = blogs.stream().filter(blog -> blog.isRecommend()).limit(MAX_RECOMMEND_BLOG_NUM).collect(Collectors.toList());
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

    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @RequestParam("query") String query,
                        Model model){
        List<Blog> o = detailedBlogService.selectBlogWithString("%" + query + "%");
        model.addAttribute("page", listConvertToPage(o,pageable));
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable("id")Long id,
                       Model model){
        Blog blog = detailedBlogService.getBlogById(id);
        List<Tag> tags = tagService.getBlogTagsWithBlogId(blog.getId());
        blog.setTags(tags);
        model.addAttribute("blog", blogConvert(blog));
        blogService.updateViews(id);
        redisUtil.remove(ALL_BLOGVOS);
        return "blog";
    }


    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
        List<BlogVO> blogs;
        if (redisUtil.exists(ALL_BLOGVOS)){
            blogs = (List<BlogVO>) redisUtil.get(ALL_BLOGVOS);
        }else{
            blogs = blogService.listAllBlogVOs();
            redisUtil.set(ALL_BLOGVOS,blogs);
        }
        List<BlogVO> recommendBlogVO = blogs.stream().filter(blog -> blog.isRecommend()).limit(3).collect(Collectors.toList());
        model.addAttribute("newblogs",recommendBlogVO);
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
