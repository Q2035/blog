package top.hellooooo.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.blog.mapper.DetailedBlogMapperV2;
import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.Comment;
import top.hellooooo.blog.pojo.Tag;
import top.hellooooo.blog.service.DetailedBlogServiceV2;

import java.util.List;

/**
 * @author Q
 * @date 7/3/2022 9:36 PM
 */
@Service
public class DetailedBlogServiceV2Impl implements DetailedBlogServiceV2 {

    @Autowired
    private DetailedBlogMapperV2 detailedBlogMapper;

    @Override
    public Blog getBlog(Long blogId) {
        Blog blog = detailedBlogMapper.getBlog(blogId);
        blog.setTags(detailedBlogMapper.listTags(blogId));
        blog.setUser(detailedBlogMapper.getUser(blogId));
        blog.setComments(detailedBlogMapper.listComments(blogId));
        return blog;
    }

    @Override
    public List<Tag> listAllTags(Long blogId) {
        return null;
    }

    @Override
    public List<Comment> listAllComments(Long blogId) {
        return null;
    }
}
