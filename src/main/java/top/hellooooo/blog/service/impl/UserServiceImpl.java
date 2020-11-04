package top.hellooooo.blog.service.impl;

import top.hellooooo.blog.mapper.UserMapper;
import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.service.UserService;
import top.hellooooo.blog.util.MD5Utils;
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
