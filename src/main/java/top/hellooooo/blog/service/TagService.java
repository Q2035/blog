package top.hellooooo.blog.service;

import top.hellooooo.blog.pojo.Tag;
import top.hellooooo.blog.util.Pageable;
import top.hellooooo.blog.vo.BaseBlogInfo;
import top.hellooooo.blog.vo.BaseTagInfo;

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

    /**
     * 列举所有的标签
     * @return
     */
    List<BaseTagInfo> listAllTags();
}
