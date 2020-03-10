package com.test.blog.mapper;

import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Type;
import com.test.blog.pojo.User;

import java.util.List;

public interface DetailedBlogMapper {

    List<Blog> selectBlogWithString(String query);

    Blog getBlogById(Long id);

    List<Blog> selectDetailsOfAllBlog();

    User queryUserById(Long id);

    Type queryTypeById(Long id);
}
