package top.hellooooo.blog.controller.admin;

import top.hellooooo.blog.pojo.Tag;
import top.hellooooo.blog.service.TagService;
import top.hellooooo.blog.util.PageUtils;
import top.hellooooo.blog.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.hellooooo.blog.util.RedisDataName;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by limi on 2017/10/16.
 */

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        List<Tag> t = tagService.listTag();
        Page<Tag> tags = PageUtils.listConvertToPage(t, pageable);
        model.addAttribute("page", tags);
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }


    @PostMapping("/tags")
    public String post(@Valid Tag tag,BindingResult result, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        try {
            tagService.saveTag(tag);
//            Redis tags过期了
            redisUtil.remove(RedisDataName.TOP_TAGS);
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("message", "新增失败");
        }
        attributes.addFlashAttribute("message", "新增成功");
        return "redirect:/admin/tags";
    }


    /**
     * 修改tag
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag, BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name","nameError","不能添加重复的分类");
        }
        if (result.hasErrors()) {
            return "admin/tags-input";
        }
        int t = tagService.updateTag(id,tag);
        if (t == 0 ) {
            attributes.addFlashAttribute("message", "更新失败");
            redisUtil.remove(RedisDataName.TOP_TAGS);
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }


}
