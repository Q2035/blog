package top.hellooooo.blog.controller.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

/**
 * @author Q
 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    private final UserService userService;

    private final String BLACK_LIST_NAME = "blacklist";

    private final String LOGIN_COUNT = "login_count";

    private final Integer LOGIN_MAX_COUNT = 3;

    private final Long ONE_DAY = 24 * 60 * 60 * 1000L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public LoginController(UserService userService, BeanFactory beanFactory) {
        this.userService = userService;
    }

    @GetMapping({"", "login"})
    public String loginPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
//        不同浏览器服务器对应的session不一样，没事儿
        if (session.getAttribute("user") != null) {
            return "admin/index";
        }
        return "admin/login";
    }

    /**
     * 认证
     * @param username
     * @param password
     * @param request
     * @param attributes
     * @return
     */
    @PostMapping("authentication")
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        HttpServletRequest request,
                        RedirectAttributes attributes){
        HttpSession session = request.getSession();
        String userIP = request.getHeader("x-forwarded-for");
        // 先检查用户是否尝试登录多次
        // 使用用户IP作为唯一凭证
        Object blackList = session.getAttribute(BLACK_LIST_NAME);
        if (blackList != null) {
            HashMap<String, Date> bl = (HashMap<String, Date>) blackList;
            // 加入黑名单时间
            Date date = bl.get(userIP);
            Date now = new Date();
            if (date != null) {
                if (now.getTime() - date.getTime() < ONE_DAY) {
                    logger.info("now:{} date:{} oneday:{}", now.getTime(), date.getTime(), ONE_DAY);
                    attributes.addFlashAttribute("message", "this ip is blocked");
                    return "redirect:/admin";
                } else {
                    session.removeAttribute(BLACK_LIST_NAME);
                    session.removeAttribute(LOGIN_COUNT);
                }
            }
        } else {
            // 判断当前用户是否尝试多次登录
            Object attribute = session.getAttribute(LOGIN_COUNT);
            if (attribute != null) {
                Integer count = (Integer) attribute;
                // 超过登录尝试上限
                if (count >= LOGIN_MAX_COUNT) {
                    HashMap<String, Date> hashMap = new HashMap<>();
                    hashMap.put(userIP, new Date());
                    session.setAttribute(BLACK_LIST_NAME, hashMap);
                    attributes.addFlashAttribute("message", "Too many login attempts");
                    return "redirect:/admin";
                }
            }
        }
        User user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword("");
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            Object attribute = session.getAttribute(LOGIN_COUNT);
            if (attribute == null) {
                session.setAttribute(LOGIN_COUNT, 1);
            } else {
                session.setAttribute(LOGIN_COUNT, (Integer)attribute + 1);
            }
            // 如果使用Model，重定向之后页面无法取数据
            attributes.addFlashAttribute("message", "password fail to authenticate");
            return "redirect:/admin";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
