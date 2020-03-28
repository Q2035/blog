package com.test.blog.service;

import com.test.blog.pojo.Blog;
import com.test.blog.dto.BlogQuery;
import com.test.blog.vo.BlogVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog testGetBlog(Long id);

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

    /**
     * 从数据库中查找指定的分页数据[)
     * @param begin
     * @param end
     * @return
     */
    List<Blog> listBlogsWithPages(int begin,int end);

    List<Blog> listRecommmendBlogs(Integer size);

    List<Blog> searchBlogWithString(String query);

    Map<String,List<BlogVO>> archiveBlog();

    Long countBlog();

    List<BlogVO> listAllBlogVOs();

    List<BlogVO> listBlogVOWithTypeId(String typeName);
}
