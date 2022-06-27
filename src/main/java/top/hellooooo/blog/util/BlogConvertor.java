package top.hellooooo.blog.util;

import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.vo.BaseBlogInfo;

import java.util.Objects;

/**
 * @author Q
 * @date 6/27/2022 3:39 PM
 */
public class BlogConvertor {
    /**
     * 提取博客基本信息（未提取用户信息及具体跳转链接信息）
     * @param blog
     * @return
     */
    public static BaseBlogInfo convert(Blog blog) {
        if (Objects.isNull(blog)) {
            return null;
        }
        BaseBlogInfo target = new BaseBlogInfo();
        target.setId(blog.getId());
        target.setTitle(blog.getTitle());
        target.setDescription(blog.getDescription());
        target.setFirstPicture(blog.getFirstPicture());
        target.setUpdateTime(blog.getUpdateTime());
        target.setViews(blog.getViews());
        target.setRecommend(blog.isRecommend());
        target.setFlag(blog.getFlag());
        target.setCreateTime(blog.getCreateTime());
        return target;
    }
}
