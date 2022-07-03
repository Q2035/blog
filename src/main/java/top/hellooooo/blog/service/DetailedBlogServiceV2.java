package top.hellooooo.blog.service;

import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.Comment;
import top.hellooooo.blog.pojo.Tag;

import java.util.List;

/**
 * @author Q
 * @date 7/3/2022 9:34 PM
 */
public interface DetailedBlogServiceV2 {

    /**
     * 仅获取博客信息
     * @param blogId
     * @return
     */
    Blog getBlog(Long blogId);

    /**
     * 列举所有博客的标签信息
     * @param blogId
     * @return
     */
    List<Tag> listAllTags(Long blogId);

    /**
     * 列举所有评论信息（层级关系不对）
     * @param blogId
     * @return
     */
    List<Comment> listAllComments(Long blogId);
}
