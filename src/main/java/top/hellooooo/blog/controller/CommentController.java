package top.hellooooo.blog.controller;

import top.hellooooo.blog.pojo.Comment;
import top.hellooooo.blog.pojo.User;
import top.hellooooo.blog.service.BlogService;
import top.hellooooo.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    private final BlogService blogService;

    private final CommentService commentService;

    @Value("${comment.avatar}")
    private String avatar;

    public CommentController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
    }

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable("blogId")Long blogId,
                           Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment,
                       HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+ blogId;
    }
}
