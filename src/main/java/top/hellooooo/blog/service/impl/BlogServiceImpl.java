package top.hellooooo.blog.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import top.hellooooo.blog.exception.BlogNotFoundException;
import top.hellooooo.blog.mapper.BlogMapper;
import top.hellooooo.blog.mapper.BlogVOMapper;
import top.hellooooo.blog.mapper.ContactMapper;
import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.ContactMe;
import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.pojo.BlogQuery;
import top.hellooooo.blog.util.BlogConvertor;
import top.hellooooo.blog.util.Pageable;
import top.hellooooo.blog.util.UserConvertor;
import top.hellooooo.blog.vo.AdminBlogVO;
import top.hellooooo.blog.vo.BaseBlogInfo;
import top.hellooooo.blog.vo.BlogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.hellooooo.blog.service.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 注释的方法有误
 */
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;

    private final TypeService typeService;

    private final UserService userService;

    private final CommentService commentService;

    private final DetailedBlogService detailedBlogService;

    private final BlogVOMapper blogVOMapper;

    private final ContactMapper contactMapper;

    public BlogServiceImpl(BlogMapper blogMapper, TypeService typeService, UserService userService, CommentService commentService, DetailedBlogService detailedBlogService, BlogVOMapper blogVOMapper, ContactMapper contactMapper) {
        this.blogMapper = blogMapper;
        this.typeService = typeService;
        this.userService = userService;
        this.commentService = commentService;
        this.detailedBlogService = detailedBlogService;
        this.blogVOMapper = blogVOMapper;
        this.contactMapper = contactMapper;
    }

    @Override
    public List<BlogVO> listBlogByTagId(Long id) {
        return blogVOMapper.listAllBlogVOByTagId(id);
    }

    @Override
    public void updateViews(Long id) {
        blogMapper.updateViews(id);
    }

    @Override
    public Blog getBlog(Long id) {
        return blogMapper.findById(id);
    }

    @Override
    public List<Blog> listBlog(BlogQuery blog) {
        List<Blog> blogs = blogMapper.listBlog(blog);
        User user = userService.getUserById((long) 1);
        blogs.forEach(b -> {
            b.setUser(user);
            b.setType(typeService.getType(b.getTypeId()));
        });
        return blogs;
    }

    @Transactional
    @Override
    public void saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            blogMapper.saveBlog(blog);
        } else {
            deleteTagsWithBlogId(blog.getId());
            blog.setUpdateTime(new Date());
            blogMapper.updateBlog(blog.getId(), blog);
        }
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogMapper.findById(id);
        if (b == null) {
            throw new BlogNotFoundException("该博客不存在");
        }
        blogMapper.updateBlog(id, blog);
        return b;
    }

    /**
     * 删除博客之前应该先删除tags
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
//        删除标签
        List<String> tags = blogMapper.selectTagsWithBlogId(id);
        if (tags != null && !tags.isEmpty()) {
            blogMapper.deleteTagsWithBlogId(id);
        }
//         删除评论
        commentService.deleteCommentsByBlogId(id);
        blogMapper.deleteBlog(id);
    }

    @Override
    public void saveBlogTags(Blog blog) {
        blogMapper.saveBlogTags(blog);
    }

    @Override
    public Blog findByTitle(String title) {
        return blogMapper.findByTitle(title);
    }

    @Override
    public void deleteTagsWithBlogId(Long id) {
        blogMapper.deleteTagsWithBlogId(id);
    }

    @Override
    public List<String> selectTagsWithBlogId(Long id) {
        return blogMapper.selectTagsWithBlogId(id);
    }

    /**
     * 直接select * from t_blog 不会有User Type 以及Tags
     * 使用transient标记，暂时存储user_id,type_id
     *
     * @return
     */

    @Override
    public List<Blog> listAllBlogs() {
        return detailedBlogService.selectDetailsOfAllBlog();
    }

    @Override
    public List<Blog> listRecommmendBlogs(Integer size) {
        return blogMapper.listRecommmendBlogs(size);
    }

    @Override
    public Map<String, List<BlogVO>> archiveBlog() {
        List<String> years = blogMapper.findGroupYear();
        Map<String, List<BlogVO>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogMapper.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogMapper.countBlog();
    }

    /**
     * 只展示index页面需要的信息，不展示博客内容等多余的信息
     *
     * @return
     */
    @Override
    public List<BlogVO> listAllBlogVOs() {

        return blogVOMapper.listAllBlogVOs();
    }

    @Override
    public List<BlogVO> listBlogVOWithTypeId(String typeName) {
        return blogVOMapper.listAllBlogVOByTypeId(typeName);
    }

    @Override
    public List<AdminBlogVO> listAllAdminBlogs() {
        return blogVOMapper.listAllAdminBlogVO();
    }

    @Override
    public List<AdminBlogVO> listSpecificAdminBlogs(BlogQuery blog) {
        return blogVOMapper.listSpecificAdminBlogs(blog);
    }


    @Override
    public void insertContactIntoDB(ContactMe contactMe) {
        contactMapper.insertContactIntoDB(contactMe);
    }

    @Override
    public ContactMe searchContactInfoByEmail(String email) {
        return contactMapper.searchContactInfoByEmail(email);
    }

    @Override
    public Pageable<BaseBlogInfo> listBaseBlogInfo(Pageable<BaseBlogInfo> pageable) {
        List<Blog> blogs = blogMapper.listBlogsWithPagesNoContent(pageable.getStart(), pageable.getEnd());
        if (CollectionUtils.isEmpty(blogs)) {
            return pageable;
        }
        final List<BaseBlogInfo> collect = blogs.stream()
                .map(b -> {
                    BaseBlogInfo convert = BlogConvertor.convert(b);
                    // 提取作者信息
                    User userById = userService.getUserById(b.getUserId());
                    convert.setUser(UserConvertor.convert(userById));
                    return convert;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect) || collect.size() < pageable.getPageSize()) {
            pageable.setHasMore(false);
        } else {
            pageable.setHasMore(true);
        }
        pageable.setData(collect);
        return pageable;
    }
}
