package com.test.blog.service.impl;

import com.test.blog.mapper.UserMapper;
import com.test.blog.pojo.User;
import com.test.blog.service.UserService;
import com.test.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User checkUser(String username, String password) {
        User user = userMapper.checkUser(username, MD5Utils.code(password));
        return user;
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.getUserById(id);
    }
}
