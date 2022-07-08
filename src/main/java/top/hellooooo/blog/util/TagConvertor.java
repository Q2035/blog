package top.hellooooo.blog.util;

import top.hellooooo.blog.common.Constants;
import top.hellooooo.blog.pojo.Tag;
import top.hellooooo.blog.vo.BaseTagInfo;

import java.util.Objects;

/**
 * @author Q
 * @date 6/28/2022 9:47 PM
 */
public class TagConvertor {
    public static BaseTagInfo convert(Tag source) {
        if (Objects.isNull(source)) {
            return null;
        }
        final BaseTagInfo baseTagInfo = new BaseTagInfo();
        baseTagInfo.setCount(Constants.DEFAULT_TAG_COUNT);
        if (Objects.nonNull(source.getCount())) {
            baseTagInfo.setCount(source.getCount());
        }
        baseTagInfo.setId(source.getId());
        baseTagInfo.setName(source.getName());
        return baseTagInfo;
    }
}
