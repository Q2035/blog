package top.hellooooo.blog.vo;


import lombok.Data;

import java.util.Date;

/**
 * 此类用于index页面展示，实际上，Index页面的博客不需要博客内容
 */
@Data
public class BlogVO {
    private Long id;
    private String title;
    private String description;
    private String firstPicture;
    private Date updateTime;
    private Integer views;
    private String typeName;
    private String nickname;
    private String userAvatar;
    private boolean recommend;
    private String flag;
    private Date createTime;
}
