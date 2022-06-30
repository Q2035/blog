package top.hellooooo.blog.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Q
 * @date 6/30/2022 7:13 PM
 */
@Data
public class ArchiveResult {
    /**
     * 博客计数
     */
    private Integer blogCount;

    /**
     * 博客年份信息
     */
    private List<BlogYearArchive> archives;
}
