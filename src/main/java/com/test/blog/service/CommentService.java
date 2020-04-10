package com.test.blog.service;

import com.test.blog.pojo.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> selectCommentsByParentId(Long pid);

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId);

    List<Comment> listCommentByBlogId(Long blogId);

    void saveComment(Comment comment);

    Comment selectCommentById(Long id);

    void deleteCommentsByBlogId(Long id);

}
