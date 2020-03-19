package com.test.blog.controller.admin;

import com.test.blog.controller.IndexController;
import com.test.blog.pojo.Type;
import com.test.blog.service.TypeService;
import com.test.blog.util.PageUtils;
import com.test.blog.util.RedisUtil;
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

import javax.validation.Valid;
import java.util.List;

/**
 * updateType 无效
 */
@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IndexController indexController;

    @Autowired
    private TypeService typeService;

    @GetMapping("/types")
    public String types(@PageableDefault(size = 8,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        List<Type> tempTypes = typeService.listType();
        Page<Type> types = PageUtils.listConvertToPage(tempTypes, pageable);
        model.addAttribute("page",types);
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable("id") Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type,
                       BindingResult result,
                       RedirectAttributes attributes){
        Type type1 =typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","分类名称不可重复");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }
        try {
            typeService.saveType(type);
//            Redis Types 过期了
            redisUtil.expire(indexController.REDIS_TOP_TYPES,0);
        }catch (Exception e){
            attributes.addFlashAttribute("message","操作失败");
            e.printStackTrace();
        }
        attributes.addFlashAttribute("message","操作成功");
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type,
                       BindingResult result,
                       @PathVariable("id") Long id,
                       RedirectAttributes attributes){
        Type type1 =typeService.getTypeByName(type.getName());
        if (type1 != null){
            result.rejectValue("name","nameError","分类名称不可重复");
        }
        if (result.hasErrors()){
            return "admin/types-input";
        }
        int t =typeService.updateType(id,type);
        if (t ==0){
            attributes.addFlashAttribute("message","更新失败");
        }else{
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable("id") Long id,
                         RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}
