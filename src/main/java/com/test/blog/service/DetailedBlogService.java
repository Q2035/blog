package com.test.blog.service;

import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Type;
import com.test.blog.pojo.User;

import java.util.List;

public interface DetailedBlogService {

    /**
     * 缺少tags，不过问题不大
     * @return
     */
    List<Blog> selectDetailsOfAllBlog();

    User queryUserById(Long id);

    Type queryTypeById(Long id);

    Blog getBlogById(Long id);

    List<Blog> selectBlogWithString(String query);
}
