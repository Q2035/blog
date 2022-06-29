package top.hellooooo.blog.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import top.hellooooo.blog.mapper.BlogMapper;
import top.hellooooo.blog.mapper.TagMapper;
import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.Tag;
import top.hellooooo.blog.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.hellooooo.blog.util.Pageable;
import top.hellooooo.blog.vo.BaseBlogInfo;
import top.hellooooo.blog.vo.BaseTagInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private Logger logger = LoggerFactory.getLogger(TagServiceImpl.class);

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public void saveTag(Tag type) {
        tagMapper.saveTag(type);
    }

    @Override
    public Tag getTag(Long id) {
        return tagMapper.getTag(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagMapper.getTagByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagMapper.listTag();
    }

    @Override
    public List<Tag> listTags(String ids) {
        if (ids==null || ids.equals("")){
            return null;
        }
        return tagMapper.listTags(ids);
    }

    /**
     * 罪恶源头:导致我index页面加载差不多要10s
     * @param size
     * @return
     */
    @Override
    public List<Tag> listTagTop(Integer size) {
        return tagMapper.listTagTop(size);
    }

    @Override
    public List<Tag> getBlogTagsWithBlogId(Long id) {
        List<String > tagIds = tagMapper.getBlogTagsWithBlogId(id);
        if (tagIds ==null || tagIds.isEmpty()){
            logger.error("ERROR, the blog doesn't exist tags");
        }
        ArrayList<Tag> tags = new ArrayList<>();
        for (String tagId : tagIds) {
            tags.add(tagMapper.getTag(Long.valueOf(tagId)));
        }
        return tags;
    }

    @Override
    public List<BaseTagInfo> listAllTags() {
        final List<Tag> tags = listTag();
        if (CollectionUtils.isEmpty(tags)) {
            return new ArrayList<>();
        }
        return tags.stream()
                .map(t -> {
                    final BaseTagInfo r = new BaseTagInfo();
                    r.setName(t.getName());
                    r.setId(t.getId());
                    r.setCount(-1);
                    return r;
                }).collect(Collectors.toList());
    }

    @Override
    public Integer updateTag(Long id, Tag type) {
        return tagMapper.updateTag(id,type);
    }

    @Override
    public void deleteTag(Long id) {
        List<Blog> blogs = blogMapper.listBlogByTagId(id);
        if (blogs !=null && !blogs.isEmpty()){
            logger.warn("尝试删除tags，但是存在关联的博客，删除失败");
            return;
        }
        tagMapper.deleteTag(id);
    }

    @Override
    public void deleteTagsWithBlogId(Long id) {
        tagMapper.deleteTagsWithBlogId(id);
    }
}
