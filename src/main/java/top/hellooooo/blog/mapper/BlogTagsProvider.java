package top.hellooooo.blog.mapper;

import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.Tag;

import java.util.List;

public class BlogTagsProvider {
    public String saveBlogTags(Blog blog){
        List<Tag> tags = blog.getTags();
        if (tags==null || tags.isEmpty()){
            return "";
        }
        StringBuffer sql =new StringBuffer("insert into t_blog_tags(blogs_id,tags_id) values");
        for (Tag tag : tags) {
            sql.append("("+blog.getId()+","+tag.getId()+"),");
        }
        sql.delete(sql.length()-1,sql.length());
        return sql.toString();
    }

}
