package com.test.blog.mapper;

import com.test.blog.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from t_user where username=#{username} and password =#{password}")
    User checkUser(String username, String password);

    /**
     * 博客首页展示
     * @param id
     * @return
     */
    @Select("select id,avatar,nickname from t_user where id=#{id}")
    User getUserById(Long id);
}
