package top.hellooooo.blog.vo;

import lombok.Data;

import java.util.Date;

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

    private String detailHref;

    private String content;

    // TODO: 7/1/2022 User

    // TODO: 7/1/2022 tags

    // TODO: 7/1/2022 comments
}
