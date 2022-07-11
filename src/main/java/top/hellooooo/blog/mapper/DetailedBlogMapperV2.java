package top.hellooooo.blog.mapper;

import org.apache.ibatis.annotations.Select;
import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.Comment;
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
    @Select("select bt.blogs_id,t.id,t.`name`from t_tag t join t_blog_tags bt on t.id = bt.tags_id and bt.blogs_id = #{blogId}")
    List<Tag> listTags(Long blogId);

    /**
     * list comments
     * @param blogId
     * @return
     */
    @Select("select `id`, `avatar`, `content` , `create_time`, `email`, `nickname`, `blog_id` , `parent_comment_id` as parent_id , `admin_comment` from t_comment where blog_id = #{blogId} order by `create_time`")
    List<Comment> listComments(Long blogId);
}
