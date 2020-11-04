package top.hellooooo.blog.service;

import top.hellooooo.blog.pojo.User;

public interface UserService {
    User checkUser(String username, String password);

    User getUserById(Long id);
}
