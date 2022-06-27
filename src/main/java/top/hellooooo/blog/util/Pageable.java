package top.hellooooo.blog.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * @author Q
 * @date 6/27/2022 4:02 PM
 */
@Data
public class Pageable<T> {

    /**
     * 当前页
     */
    private Integer curPage;

    /**
     * 是否还有数据
     */
    private Boolean hasMore;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 数据
     */
    private List<T> data;

    /**
     * 数据开始处
     * @return
     */
    @JsonIgnore
    public Integer getStart() {
        if (Objects.nonNull(curPage) && Objects.nonNull(pageSize)) {
            return curPage * pageSize;
        }
        return 0;
    }

    /**
     * 数据结束处
     * @return
     */
    @JsonIgnore
    public Integer getEnd() {
        if (Objects.nonNull(curPage) && Objects.nonNull(pageSize)) {
            return (curPage + 1) * pageSize;
        }
        return 1;
    }
}
