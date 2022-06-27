package top.hellooooo.blog.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author Q
 * @date 6/27/2022 3:19 PM
 */
@Data
public class BaseBlogInfo {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String description;

    /**
     * 首图
     */
    private String firstPicture;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 查看次数
     */
    private Integer views;

    /**
     * 用户信息
     */
    private BaseUserInfo user;

    /**
     * 是否推荐
     */
    private boolean recommend;

    /**
     * 标记
     */
    private String flag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 查看链接，跳转到博客详情页
     */
    private String detailHref;
}
