package top.hellooooo.blog.controller.admin;

import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.FileVO;
import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.service.BlogService;
import top.hellooooo.blog.service.DetailedBlogService;
import top.hellooooo.blog.service.TagService;
import top.hellooooo.blog.service.TypeService;
import top.hellooooo.blog.pojo.BlogQuery;
import top.hellooooo.blog.util.RedisUtil;
import top.hellooooo.blog.vo.AdminBlogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static top.hellooooo.blog.util.PageUtils.listConvertToPage;
import static top.hellooooo.blog.util.RedisDataName.*;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Value("${file.basePath}")
    private String basePath;

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

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
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

    /**
     * 文件下载列表
     * @param model
     * @return
     */
    @GetMapping("/files")
    public String files(Model model){
        List<FileVO> files = new ArrayList<>();
        File file = new File(basePath);
        if (!file.exists()){
            logger.warn("the file {} doesn't exist",basePath);
        }
        for (File listFile : file.listFiles()) {
//            直接跳过文件夹
            if (listFile.isDirectory()){
                continue;
            }
            FileVO fileVO = new FileVO();
            fileVO.setFileName(listFile.getName());
            fileVO.setUpdateDate(new Date(listFile.lastModified()));
            long totalSpace = listFile.length();
            fileVO.setFileSize(formatConversion(totalSpace));
            files.add(fileVO);
        }
        model.addAttribute("files",files);
        return "admin/files";
    }

    public String formatConversion(long size){
        String result;
        if (size<1024){
            result = size  + " B";
        }else if (size < 1024 * 1024){
            result  = size / 1024  + " KB";
        }else if (size < 1024 * 1024 * 1024){
            result = size / 1024 / 1024 + " MB";
        }else {
            result  = size / 1024 /1024 /1024 + " GB";
        }
        return result;
    }
}