package top.hellooooo.blog.pojo;

import lombok.Data;
import top.hellooooo.blog.vo.BaseBlogInfo;

import java.util.List;

/**
 * @author Q
 * @date 6/30/2022 7:15 PM
 */
@Data
public class BlogYearArchive {
    /**
     * 年份
     */
    private String year;

    /**
     * 博客信息
     */
    private List<BaseBlogInfo> blogs;
}
