package top.hellooooo.blog.service;

import top.hellooooo.blog.pojo.Tag;

import java.util.List;

public interface TagService {
    void saveTag(Tag type);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    List<Tag> listTag();

    Integer updateTag(Long id, Tag type);

    void deleteTag(Long id);

    void deleteTagsWithBlogId(Long id);

    List<Tag> listTags(String ids);

    List<Tag> listTagTop(Integer size);

    List<Tag> getBlogTagsWithBlogId(Long id);
}
