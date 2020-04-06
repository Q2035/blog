package com.test.blog.service;

import com.test.blog.pojo.Blog;
import com.test.blog.dto.BlogQuery;
import com.test.blog.vo.AdminBlogVO;
import com.test.blog.vo.BlogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogService {

    List<BlogVO> listBlogByTagId(Long id);

    void updateViews(Long id);

    Blog getBlog(Long id);

    List<Blog> listBlog(BlogQuery blog);

    void saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    void saveBlogTags(@Param("blog") Blog blog);

    Blog findByTitle(String title);

    void deleteTagsWithBlogId(Long id);

    List<String> selectTagsWithBlogId(Long id);

    /**
     * 返回所有的博客，不应该包含草稿的博客
     * @return
     */
    List<Blog> listAllBlogs();

    List<Blog> listRecommmendBlogs(Integer size);

    Map<String,List<BlogVO>> archiveBlog();

    Long countBlog();

    List<BlogVO> listAllBlogVOs();

    List<BlogVO> listBlogVOWithTypeId(String typeName);

    List<AdminBlogVO> listAllAdminBlogs();

    List<AdminBlogVO> listSpecificAdminBlogs(BlogQuery blog);
}
