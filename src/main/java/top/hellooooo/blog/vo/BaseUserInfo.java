package top.hellooooo.blog.vo;

import lombok.Data;

/**
 * @author Q
 * @date 6/27/2022 3:21 PM
 */
@Data
public class BaseUserInfo {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;
}
