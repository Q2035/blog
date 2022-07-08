package top.hellooooo.blog.util;

import org.apache.commons.collections4.CollectionUtils;
import top.hellooooo.blog.pojo.Comment;
import top.hellooooo.blog.vo.BaseCommentInfo;
import top.hellooooo.blog.vo.BaseUserInfo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Q
 * @date 7/2/2022 9:21 PM
 */
public class CommentConvertor {
    /**
     * convert
     * @param source
     * @return
     */
    public static BaseCommentInfo convert(Comment source){
        if (Objects.isNull(source)) {
            return null;
        }
        final BaseCommentInfo target = new BaseCommentInfo();
        target.setId(source.getId());
        target.setContent(source.getContent());
        target.setCreateTime(source.getCreateTime());
        // TODO: 7/2/2022 评论存在问题
        // 子评论
        if (CollectionUtils.isNotEmpty(source.getReplyComments())) {
            target.setReplyComments(
                    source.getReplyComments()
                            .stream()
                            .map(CommentConvertor::convert)
                            .collect(Collectors.toList()));
        }
        final BaseUserInfo user = new BaseUserInfo();
        user.setAvatar(source.getAvatar());
        user.setNickname(source.getNickname());
        user.setId(source.getId());
        target.setUser(user);
        return target;
    }

    /**
     * comments convertor
     * @param comments 所有层次的评论平铺展开
     * @return
     */
    public static List<BaseCommentInfo> convertStraightforwardList(List<Comment> comments) {
        // TODO: 7/8/2022  
        return null;
    }
}
