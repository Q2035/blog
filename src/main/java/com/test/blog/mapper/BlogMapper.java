package com.test.blog.mapper;

import com.test.blog.pojo.Blog;
import com.test.blog.dto.BlogQuery;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface BlogMapper {
    @Select("select * from t_blog where id=#{id}")
    Blog getBlog(Long id);

    @SelectProvider(type = BlogProvider.class, method = "listBlog")
    List<Blog> listBlog(BlogQuery blog);

    @Select("select * from t_blog where published = 1 order by update_time desc")
    List<Blog> listAllBlogs();

    @Insert("insert into t_blog values(default,#{blog.appreciation}, #{blog.commentabled}," +
            "#{blog.content},#{blog.createTime},#{blog.description},#{blog.firstPicture},#{blog.flag},#{blog.published}," +
            "#{blog.recommend},#{blog.shareStatement},#{blog.title},#{blog.updateTime}," +
            "#{blog.views},#{blog.type.id},#{blog.user.id})")
    void saveBlog(@Param("blog") Blog blog);

    @InsertProvider(type = BlogTagsProvider.class, method = "saveBlogTags")
    void saveBlogTags(@Param("blog") Blog blog);

    @Update("update t_blog set appreciation=#{blog.appreciation}, commentabled=#{blog.commentabled}," +
            "content=#{blog.content},description=#{blog.description},first_picture=#{blog.firstPicture},flag=#{blog.flag},published=#{blog.published}," +
            "recommend=#{blog.recommend},share_statement=#{blog.shareStatement},title=#{blog.title},update_time=#{blog.updateTime}," +
            "type_id=#{blog.type.id},user_id=#{blog.user.id} where id=#{id}")
    Integer updateBlog(@Param("id") Long id, @Param("blog") Blog blog);

    /**
     * 与tag存在外键关联，需要先去删除tag的关联才可以删除博客
     *
     * @param id
     */
    @Delete("delete from t_blog where id=#{id}")
    void deleteBlog(Long id);

    @Delete("delete from t_blog_tags where blogs_id=#{id}")
    void deleteTagsWithBlogId(Long id);

    @Select("select tags_id from t_blog_tags where blogs_id=#{id}")
    List<String> selectTagsWithBlogId(Long id);

    @Select("select * from t_blog where id=#{id}")
    Blog findById(Long id);

    @Select("select * from t_blog where title=#{title}")
    Blog findByTitle(String title);

    @Select("select * from t_blog where recommend=true and published =1 order by update_time DESC limit ${size}")
    List<Blog> listRecommmendBlogs(Integer size);

    @Select("select * from t_blog where content like #{query}" +
            " or title like #{query}")
    List<Blog> searchBlogWithString(String query);

    @Update("update t_blog b set b.views =b.views+1 where id=#{id}")
    void updateViews(Long id);

    @Select(" select * from t_blog WHERE id " +
            "in (select bt.blogs_id from t_blog_tags bt where bt.tags_id =#{id}) and published =1")
    List<Blog> listBlogByTagId(Long id);

    @Select("select DATE_FORMAT(b.update_time,'%Y') as year from t_blog b where published =1 GROUP BY YEAR ORDER BY" +
            " year DESC")
    List<String> findGroupYear();

    @Select("select * from t_blog b where date_format(b.update_time,'%Y')=#{year} and published = 1")
    List<Blog> findByYear(String year);

    @Select("select count(*) from t_blog where published = 1")
    Long countBlog();
}