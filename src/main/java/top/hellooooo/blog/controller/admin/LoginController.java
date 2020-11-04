package top.hellooooo.blog.controller.admin;

import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping({"","login"})
    public String loginPage(HttpServletRequest request){
        HttpSession session = request.getSession();
//        不同浏览器服务器对应的session不一样，没事儿
        if (session.getAttribute("user")!=null){
            return "admin/index";
        }
        return "admin/login";
    }

    @PostMapping("authentication")
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        HttpServletRequest request,
                        RedirectAttributes attributes){
        HttpSession session = request.getSession();
        User user = userService.checkUser(username, password);
        if (user !=null){
            user.setPassword("");
            session.setAttribute("user",user);
            return "admin/index";
        }else {
//            如果使用Model，重定向之后页面无法取数据
            attributes.addFlashAttribute("message","password fail to authenticate");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
