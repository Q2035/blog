package com.test.blog.mapper;

import com.test.blog.dto.BlogQuery;
import com.test.blog.vo.AdminBlogVO;
import com.test.blog.vo.BlogVO;

import java.util.List;

public interface BlogVOMapper {
    List<BlogVO> listAllBlogVOs();

    List<BlogVO> listAllBlogVOByTagId(Long id);

    List<BlogVO> listAllBlogVOByTypeId(String typeName);

    List<AdminBlogVO> listAllAdminBlogVO();

    List<AdminBlogVO> listSpecificAdminBlogs(BlogQuery blog);
}
