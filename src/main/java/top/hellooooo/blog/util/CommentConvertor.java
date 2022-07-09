package top.hellooooo.blog.util;

import org.apache.commons.collections4.CollectionUtils;
import top.hellooooo.blog.pojo.Comment;
import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.vo.BaseCommentInfo;
import top.hellooooo.blog.vo.BaseUserInfo;

import java.util.*;
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
        target.setIsAuthor(source.isAdminComment());
        // 子评论
        if (CollectionUtils.isNotEmpty(source.getReplyComments())) {
            target.setReplyComments(
                    source.getReplyComments()
                            .stream()
                            .map(CommentConvertor::convert)
                            .collect(Collectors.toList()));
        }
        // 父评论
        if (Objects.nonNull(source.getParentComment())) {
            Comment pc = source.getParentComment();
            BaseCommentInfo parentComment = new BaseCommentInfo();
            target.setIsAuthor(pc.isAdminComment());
            BaseUserInfo user = new BaseUserInfo();
            user.setNickname(pc.getNickname());
            user.setAvatar(pc.getAvatar());
            target.setUser(user);
            target.setParentComment(parentComment);
        }
        final BaseUserInfo user = new BaseUserInfo();
        user.setAvatar(source.getAvatar());
        user.setNickname(source.getNickname());
        // user.setId(source.getId());
        target.setUser(user);
        return target;
    }

    /**
     * comments convertor
     * @param comments 所有层次的评论平铺展开
     * @return
     */
    public static List<BaseCommentInfo> convertStraightforwardList(List<Comment> comments) {
        Map<Long, Comment> commentsMapCache = new HashMap<>();
        List<Comment> parentComments = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(comments)) {
            comments.stream()
                    .forEach(c -> {
                        commentsMapCache.put(c.getId(), c);
                        if (Objects.isNull(c.getParentId()) || c.getParentId() == -1L) {
                            parentComments.add(c);
                            return;
                        }
                        Comment parentComment = commentsMapCache.get(c.getParentId());
                        c.setParentComment(parentComment);
                        List<Comment> replyComments = parentComment.getReplyComments();
                        if (CollectionUtils.isEmpty(replyComments)) {
                            replyComments = new ArrayList<>();
                        }
                        replyComments.add(c);
                        parentComment.setReplyComments(replyComments);
                    });
        }
        return parentComments.stream()
                .map(CommentConvertor::convert)
                .collect(Collectors.toList());
    }
}
