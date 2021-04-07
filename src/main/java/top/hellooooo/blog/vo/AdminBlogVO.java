package top.hellooooo.blog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AdminBlogVO {
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
//    增加是否发布
    private boolean published;
    private String flag;
    private Date createTime;

}
