package top.hellooooo.blog.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Q
 * @date 7/2/2022 8:04 PM
 */
@Data
public class BaseCommentInfo {

    private Long id;

    private Date createTime;

    // TODO: 7/2/2022 检验登录状态并比较作者信息
    private Boolean isAuthor;

    private String content;

    private List<BaseCommentInfo> replyComments;

    private BaseCommentInfo parentComment;

    private BaseUserInfo user;
}
