package com.test.blog.mapper;

import com.test.blog.pojo.Tag;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TagMapper {
    @Insert("insert into t_tag values(#{id},#{name})")
    void saveTag(Tag type);

    @Select("select * from t_tag where id=#{id}")
    Tag getTag(Long id);

    @Select("select * from t_tag where name=#{name}")
    Tag getTagByName(String name);

    @Select("select * from t_tag")
    List<Tag> listTag();

    @Update("update t_tag set id=#{type.id},name=#{type.name} where id=#{id}")
    Integer updateTag(Long id, @Param("type") Tag type);

    @Delete("delete from t_tag where id=#{id}")
    void deleteTag(Long id);

    @Delete("delete from t_blog_tags where blogs_id =#{id}")
    void deleteTagsWithBlogId(Long id);

    @Select("select * from t_tag where id in (${ids})")
    List<Tag> listTags(String ids);

    @Select("select t.id,r.c, t.name from t_tag t," +
            "(select tags_id tid,count(blogs_id) c from t_blog_tags GROUP BY tags_id DESC) r" +
            " where r.tid = t.id limit ${size}")
    List<Tag> listTagTop(Integer size);

    @Select("select tags_id from t_blog_tags where blogs_id=#{id}")
    List<String> getBlogTagsWithBlogId(Long id);
}
