package top.hellooooo.blog.controller.admin;

import org.springframework.beans.factory.BeanFactory;
import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.hellooooo.blog.util.LoginLog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller
@RequestMapping("/admin")
public class LoginController {

    private final UserService userService;

    private final BeanFactory beanFactory;

    public LoginController(UserService userService, BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.userService = userService;
    }

    private Lock lock = new ReentrantLock();

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
     *
     * @param username
     * @param password
     * @param request
     * @param attributes
     * @return
     */
    @PostMapping("authentication")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpServletRequest request,
                        RedirectAttributes attributes) {
        LoginLog loginLog;
        try {
            lock.lock();
            loginLog = beanFactory.getBean(LoginLog.class);
        }finally {
            lock.unlock();
        }
        // 上次尝试登录距现在超过30分钟则刷新LoginLog
        Date lastLoginTime = loginLog.getLastLoginTime();
        Date now = new Date();
        if (lastLoginTime == null || now.getTime() - lastLoginTime.getTime() > 30 * 60 * 1000) {
            loginLog.setFailLoginCounts(0);
        }
        // 登录失败错误次数小于3
        if (loginLog.getFailLoginCounts() <= 3) {
            HttpSession session = request.getSession();
            User user = userService.checkUser(username, password);
            if (user != null) {
                user.setPassword("");
                session.setAttribute("user", user);
                // 登录成功，刷新登录日志
                loginLog.setLastLoginTime(now);
                loginLog.setFailLoginCounts(0);
                return "admin/index";
            } else {
                // 如果使用Model，重定向之后页面无法取数据
                attributes.addFlashAttribute("message", "Password fail to authenticate");
                loginLog.increaseFailLoginCount();
                return "redirect:/admin";
            }
        }
        attributes.addFlashAttribute("message", "Too many attempts.");
        loginLog.increaseFailLoginCount();
        return "redirect:/admin";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
