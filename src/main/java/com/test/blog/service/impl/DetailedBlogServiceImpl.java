package com.test.blog.service.impl;

import com.test.blog.mapper.DetailedBlogMapper;
import com.test.blog.pojo.Blog;
import com.test.blog.pojo.Type;
import com.test.blog.pojo.User;
import com.test.blog.service.DetailedBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailedBlogServiceImpl implements DetailedBlogService {

    @Autowired
    private DetailedBlogMapper detailedBlogMapper;

    @Override
    public List<Blog> selectDetailsOfAllBlog() {
        return detailedBlogMapper.selectDetailsOfAllBlog();
    }

    @Override
    public User queryUserById(Long id) {
        return detailedBlogMapper.queryUserById(id);
    }

    @Override
    public Type queryTypeById(Long id) {
        return detailedBlogMapper.queryTypeById(id);
    }

    @Override
    public Blog getBlogById(Long id) {
        return detailedBlogMapper.getBlogById(id);
    }

    @Override
    public List<Blog> selectBlogWithString(String query) {
        return detailedBlogMapper.selectBlogWithString("%"+query+"%");
    }
}
