package com.test.blog.mapper;

import com.test.blog.pojo.*;

import java.util.List;

public interface TestMapper {

    Blog getBlog(Long id);

    User queryUserById(Long id);

    Type queryTypeById(Long id);

    List<Comment> queryCommentsByBlogId(Long id);

    List<Tag> queryTagsByBlogId(Long id);
}
