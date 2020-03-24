package com.test.blog.service.impl;

import com.test.blog.exception.BlogNotFoundException;
import com.test.blog.mapper.BlogMapper;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.User;
import com.test.blog.service.*;
import com.test.blog.dto.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 注释的方法有误
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private DetailedBlogService detailedBlogService;

    @Override
    public Blog testGetBlog(Long id) {
        return blogMapper.getBlog(id);
    }

    @Override
    public List<Blog> listBlogByTagId(Long id) {
        List<Blog> blogs = blogMapper.listBlogByTagId(id);
        if (blogs.isEmpty()){
            return new ArrayList<>();
        }
        User user = userService.getUserById(blogs.get(0).getUserId());
        blogs.forEach(b-> {
            b.setUser(user);
            b.setType(typeService.getType(b.getTypeId()));
        });
        return blogs;
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
        if (blog.getId() ==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            blogMapper.saveBlog(blog);
        }else{
            deleteTagsWithBlogId(blog.getId());
            blog.setUpdateTime(new Date());
            blogMapper.updateBlog(blog.getId(),blog);
        }
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogMapper.findById(id);
        if (b == null){
            throw new BlogNotFoundException("该博客不存在");
        }
        blogMapper.updateBlog(id,blog);
        return b;
    }

    /**
     * 删除博客之前应该先删除tags
     * @param id
     */
    @Transactional
    @Override
    public void deleteBlog(Long id) {
//        删除标签
        List<String> tags = blogMapper.selectTagsWithBlogId(id);
        if (tags!=null && !tags.isEmpty()){
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
     * @return
     */

    @Override
    public List<Blog> listAllBlogs(){
//        List<Blog> blogs = blogMapper.listAllBlogs();
//        for (Blog blog : blogs) {
//            blog.setUser(userService.getUserById(blog.getUserId()));
//            blog.setType(typeService.getType(blog.getTypeId()));
//            blog.setTags(tagService.getBlogTagsWithBlogId(blog.getId()));
//            blog.setTags(tagService.listTags(blog.getTagIds()));
//        }
        return detailedBlogService.selectDetailsOfAllBlog();
    }

    /**
     * 没有使用XML文件，这样处理毕竟方便
     * 有问题，需要改进
     * @param begin
     * @param end
     * @return
     */
    @Override
    public List<Blog> listBlogsWithPages(int begin, int end) {
        List<Blog> blogs = blogMapper.listBlogsWithPages(begin, end);
        User user = userService.getUserById((long) 1);
        blogs.forEach(b -> {
            b.setUser(user);
            b.setType(typeService.getType(b.getTypeId()));
        });
        return blogs;
    }

    @Override
    public List<Blog> listRecommmendBlogs(Integer size) {
        return blogMapper.listRecommmendBlogs(size);
    }

    @Override
    public List<Blog> searchBlogWithString(String query) {
        return blogMapper.searchBlogWithString(query);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years =blogMapper.findGroupYear();
        Map<String,List<Blog>> map =new HashMap<>();
        for (String year : years) {
            map.put(year,blogMapper.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogMapper.countBlog();
    }
}
