package top.hellooooo.blog.mapper;

import top.hellooooo.blog.dto.BlogQuery;
import top.hellooooo.blog.vo.AdminBlogVO;
import top.hellooooo.blog.vo.BlogVO;

import java.util.List;

public interface BlogVOMapper {
    List<BlogVO> listAllBlogVOs();

    List<BlogVO> listAllBlogVOByTagId(Long id);

    List<BlogVO> listAllBlogVOByTypeId(String typeName);

    List<AdminBlogVO> listAllAdminBlogVO();

    List<AdminBlogVO> listSpecificAdminBlogs(BlogQuery blog);
}
