package com.test.blog.service;

import com.test.blog.pojo.User;

public interface UserService {
    User checkUser(String username, String password);

    User getUserById(Long id);
}
