package top.hellooooo.blog.mapper;

import org.apache.ibatis.annotations.Select;
import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.Tag;
import top.hellooooo.blog.pojo.User;

import java.util.List;

/**
 * @author Q
 * @date 7/3/2022 9:38 PM
 */
public interface DetailedBlogMapperV2 {

    /**
     * getBlog，无tags等信息
     * @param blogId
     * @return
     */
    @Select("select * from t_blog where id = #{blogId} and published = 1")
    Blog getBlog(Long blogId);

    /**
     * getUser
     * @param userId
     * @return
     */
    @Select("select * from t_user where id = #{userId}")
    User getUser(Long userId);

    /**
     *
     * @param blogId
     * @return
     */
    List<Tag> listTags(Long blogId);
}
