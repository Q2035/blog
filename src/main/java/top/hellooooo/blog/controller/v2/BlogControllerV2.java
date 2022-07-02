package top.hellooooo.blog.controller.v2;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.hellooooo.blog.common.Constants;
import top.hellooooo.blog.pojo.ArchiveResult;
import top.hellooooo.blog.service.BlogService;
import top.hellooooo.blog.util.CommonResult;
import top.hellooooo.blog.util.Pageable;
import top.hellooooo.blog.vo.BaseBlogInfo;
import top.hellooooo.blog.vo.DetailBlogInfo;

import java.util.List;

/**
 * 负责提供一些博客信息相关的内容
 *
 * @author Q
 * @date 6/27/2022 3:09 PM
 */
@RestController
@RequestMapping("/blog")
public class BlogControllerV2 {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlogService blogService;

    @GetMapping("/base/list")
    public CommonResult<Pageable<BaseBlogInfo>> baseBlogList(@RequestParam(value = "page", required = false) String page) {
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
        Pageable<BaseBlogInfo> baseBlogInfos = blogService.listBaseBlogInfo(pageable);
        return CommonResult.succ(baseBlogInfos);
    }

    @GetMapping("/archive")
    public CommonResult<ArchiveResult> blogArchive() {
        return CommonResult.succ(blogService.listArchive());
    }

    @GetMapping("/detail")
    public CommonResult<DetailBlogInfo> blogDetail(@RequestParam Long id) {
        DetailBlogInfo detailBlogInfo = blogService.getDetailBlog(id);
        return CommonResult.succ(detailBlogInfo);
    }
}
