package top.hellooooo.blog.service.impl;

import top.hellooooo.blog.mapper.DetailedBlogMapper;
import top.hellooooo.blog.pojo.*;
import top.hellooooo.blog.service.DetailedBlogService;
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
    public Blog getBlog(Long id) {
        return detailedBlogMapper.getBlog(id);
    }

    @Override
    public List<Comment> queryCommentsByBlogId(Long id) {
        return detailedBlogMapper.queryCommentsByBlogId(id);
    }

    @Override
    public List<Tag> queryTagsByBlogId(Long id) {
        return detailedBlogMapper.queryTagsByBlogId(id);
    }

    @Override
    public List<Blog> listBlogsWithPages(int begin, int end) {
        return detailedBlogMapper.listBlogsWithPages(begin, end);
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
