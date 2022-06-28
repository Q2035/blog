package top.hellooooo.blog.controller.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hellooooo.blog.service.TagService;
import top.hellooooo.blog.util.CommonResult;
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

    @GetMapping("/list")
    public CommonResult<List<BaseTagInfo>> getTagList() {
        return CommonResult.succ(tagService.listAllTags());
    }
}
