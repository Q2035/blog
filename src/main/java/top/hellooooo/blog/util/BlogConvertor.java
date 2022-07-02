package top.hellooooo.blog.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.vo.BaseBlogInfo;
import top.hellooooo.blog.vo.DetailBlogInfo;

import java.util.Objects;
import java.util.stream.Collectors;

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
        target.setRecommend(blog.getRecommend());
        target.setFlag(blog.getFlag());
        target.setCreateTime(blog.getCreateTime());
        return target;
    }

    /**
     * detail
     * @param blog
     * @return
     */
    public static DetailBlogInfo detailConvert(Blog blog) {
        if (Objects.isNull(blog)) {
            return null;
        }
        DetailBlogInfo target = new DetailBlogInfo();
        target.setId(blog.getId());
        target.setTitle(blog.getTitle());
        target.setAppreciation(blog.getAppreciation());
        target.setCommentEnable(blog.getCommentabled());
        target.setFlag(blog.getFlag());
        target.setDescription(blog.getDescription());
        target.setFirstPicture(blog.getFirstPicture());
        target.setUpdateTime(blog.getUpdateTime());
        target.setViews(blog.getViews());
        target.setFlag(blog.getFlag());
        target.setCreateTime(blog.getCreateTime());
        // 作者信息转换
        target.setUser(UserConvertor.convert(blog.getUser()));
        // tag信息转换
        if (CollectionUtils.isNotEmpty(blog.getTags())) {
            target.setTags(
                    blog.getTags()
                            .stream()
                            .map(TagConvertor::convert)
                            .collect(Collectors.toList())
            );
        }
        // 评论信息转换
        if (CollectionUtils.isNotEmpty(blog.getComments())) {
            target.setComments(blog.getComments()
                    .stream()
                    .map(CommentConvertor::convert)
                    .collect(Collectors.toList()));
        }
        return target;
    }
}
