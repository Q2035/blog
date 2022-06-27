package top.hellooooo.blog.util;

import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.vo.BaseUserInfo;

import java.util.Objects;

/**
 * @author Q
 * @date 6/27/2022 3:44 PM
 */
public class UserConvertor {
    public static BaseUserInfo convert(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        BaseUserInfo baseUserInfo = new BaseUserInfo();
        baseUserInfo.setId(user.getId());
        baseUserInfo.setNickname(user.getNickname());
        baseUserInfo.setAvatar(user.getAvatar());
        return baseUserInfo;
    }
}
