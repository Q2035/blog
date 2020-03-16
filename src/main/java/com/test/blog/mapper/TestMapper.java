package com.test.blog.mapper;

import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Type;
import com.test.blog.pojo.User;

public interface TestMapper {

    Blog getBlog(Long id);

    User queryUserById(Long id);

    Type queryTypeById(Long id);
}
