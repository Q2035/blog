package top.hellooooo.blog.controller.v2;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.hellooooo.blog.common.Constants;
import top.hellooooo.blog.service.BlogService;
import top.hellooooo.blog.service.TagService;
import top.hellooooo.blog.util.CommonResult;
import top.hellooooo.blog.util.Pageable;
import top.hellooooo.blog.util.StatusCode;
import top.hellooooo.blog.vo.BaseBlogInfo;
import top.hellooooo.blog.vo.BaseTagInfo;

import java.util.List;

/**
 * @author Q
 * @date 6/28/2022 9:44 PM
 */
@RestController
@RequestMapping("/tag")
public class TagControllerV2 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    /**
     * 列出所有Tag
     * @return
     */
    @GetMapping("/list")
    public CommonResult<List<BaseTagInfo>> getTagList() {
        return CommonResult.succ(tagService.listAllTags());
    }

    /**
     * 分页列举博客信息
     *
     * @return
     */
    @GetMapping("/list/{tagId}")
    public CommonResult<Pageable<BaseBlogInfo>> getTagBlogList(@PathVariable Long tagId,
                                                               @RequestParam(value = "page", required = false) String page) {
        if (tagId < 0) {
            return CommonResult.fail(StatusCode.NORESOURCE, "非法ID");
        }
        int page_ = 0;
        if (StringUtils.isNotBlank(page)) {
            try {
                page_ = Integer.parseInt(page);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
        final Pageable<BaseBlogInfo> pageable = new Pageable<>();
        pageable.setPageSize(Constants.DEFAULT_SIZE_PER_PAGE);
        pageable.setCurPage(page_);
        final Pageable<BaseBlogInfo> baseBlogInfoPageable = blogService.listBaseBlogInfoByTagId(pageable, tagId);
        return CommonResult.succ(baseBlogInfoPageable);
    }
}
