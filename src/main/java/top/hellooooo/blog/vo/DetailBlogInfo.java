package top.hellooooo.blog.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Q
 * @date 7/1/2022 8:44 PM
 */
@Data
public class DetailBlogInfo {
    private Long id;

    private String title;

    private String description;

    private Date createTime;

    private Date updateTime;

    private Integer views;

    private Boolean appreciation;

    private Boolean commentEnable;

    private String flag;

    private String firstPicture;

    /**
     * 具体链接，此处可省略
     */
    private String detailHref;

    private String content;

    private BaseUserInfo user;

    private List<BaseTagInfo> tags;

    private List<BaseCommentInfo> comments;
}
