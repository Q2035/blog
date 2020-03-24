package com.test.blog.mapper;

import com.test.blog.pojo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DetailedBlogMapper {

    List<Blog> selectBlogWithString(String query);

    Blog getBlogById(Long id);

    List<Blog> selectDetailsOfAllBlog();

    User queryUserById(Long id);

    Type queryTypeById(Long id);

    Blog getBlog(Long id);

    List<Comment> queryCommentsByBlogId(Long id);

    List<Tag> queryTagsByBlogId(Long id);

    List<Blog> listBlogsWithPages(int begin, int end);
}
