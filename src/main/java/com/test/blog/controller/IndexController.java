package com.test.blog.controller;

import com.test.blog.exception.BlogNotFoundException;
import com.test.blog.mapper.FriendLinksMapper;
import com.test.blog.mapper.UsefulToolsMapper;
import com.test.blog.pojo.*;
import com.test.blog.service.BlogService;
import com.test.blog.service.DetailedBlogService;
import com.test.blog.service.TagService;
import com.test.blog.service.TypeService;
import static com.test.blog.util.PageUtils.listConvertToPage;
import static com.test.blog.util.RedisDataName.*;

import com.test.blog.util.*;
import com.test.blog.vo.BlogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private MailUtil mailUtil;

    private Integer MAX_RECOMMEND_BLOG_NUM = 3;
    private Integer MAX_TYPE_INDEX = 6;
    private Integer MAX_TAG_INDEX = 10;

    @Value("${about.qq}")
    private String aboutQQ;

    @Value("${about.wechat}")
    private String aboutWeChat;

    @Value("${about.github}")
    private String aboutGithub;

    @Value("${mailRecipient}")
    private String MAIL_RECIPIENT;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


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
    @GetMapping({"/index","/"})
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

    @PostMapping("/search")
    public String search(@PageableDefault(size = 1000,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                        @RequestParam("query") String query,
                        Model model){
        List<Blog> o = detailedBlogService.selectBlogWithString("%" + query + "%");
        if (o==null || o.isEmpty()){
            logger.info("search |"+query+"| failed");
        }
        List<BlogVO> list = transferBlogToBlogVO(o);
        model.addAttribute("page", listConvertToPage(list,pageable));
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
    public String about(Model model){
        model.addAttribute("aboutQQ",aboutQQ);
        model.addAttribute("aboutWeChat",aboutWeChat);
        model.addAttribute("aboutGithub",aboutGithub);
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


    @ResponseBody
    @PostMapping("contact")
    public CommonResult contact(@RequestParam("email") String email,
                                @RequestParam("describe") String describe){
        CommonResult commonResult = new CommonResult();
        Date createTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
//            设置插入数据库的数据
        ContactMe contactMe = new ContactMe();
        contactMe.setCreateTime(createTime);
        contactMe.setDescription(describe);
        contactMe.setEmail(email);
//            插入前先查询
        ContactMe dbContact = blogService.searchContactInfoByEmail(email);
//        今日是否已插入过（默认是）
        boolean ifInserted = true;
        if (dbContact == null){
            ifInserted = false;
        }else {
            Date dbCreateTime = dbContact.getCreateTime();
            if (!format.format(createTime).equals(format.format(dbCreateTime))){
                ifInserted = false;
            }
        }
        if (!ifInserted){
            blogService.insertContactIntoDB(contactMe);
            //            邮件发送设置
            MailBean mailBean = new MailBean();
            mailBean.setRecipient(MAIL_RECIPIENT);
            mailBean.setSubject("MyBlog:Contact With Me-"+ format.format(createTime));
            mailBean.setContent("email:"+email+" description:"+describe+" on "+createTime);
            mailUtil.sendSimpleMain(mailBean);
//            消息返回设置
            commonResult.setSuccess(true);
            commonResult.setMessage("SUCCESS");
            commonResult.setCode(StatusCode.SUCCESS.getCode());
            return commonResult;

        }
        logger.warn("email:"+email+" 今天插入过数据，尝试再次插入，但是失败了.");
        commonResult.setSuccess(false);
        commonResult.setCode(StatusCode.INTERNALFAIL.getCode());
        commonResult.setMessage("FAILUR");
        return commonResult;
    }

    private List<BlogVO> transferBlogToBlogVO(List<Blog> blogs){
        List<BlogVO> list = new ArrayList<>();
        for (Blog blog : blogs) {
            BlogVO blogVO = new BlogVO();
            blogVO.setId(blog.getId());
            blogVO.setTitle(blog.getTitle());
            blogVO.setFirstPicture(blog.getFirstPicture());
            blogVO.setDescription(blog.getDescription());
            blogVO.setUpdateTime(blog.getUpdateTime());
            blogVO.setViews(blog.getViews());
            blogVO.setTypeName(blog.getType().getName());
            blogVO.setNickname(blog.getUser().getNickname());
            blogVO.setUserAvatar(blog.getUser().getAvatar());
            blogVO.setCreateTime(blog.getCreateTime());
            list.add(blogVO);
        }
        return list;
    }
}
