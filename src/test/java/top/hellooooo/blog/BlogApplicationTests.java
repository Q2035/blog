package top.hellooooo.blog;

import top.hellooooo.blog.mapper.BlogMapper;
import top.hellooooo.blog.mapper.CommentMapper;
import top.hellooooo.blog.mapper.DetailedBlogMapper;
import top.hellooooo.blog.mapper.DetailedBlogMapperV2;
import top.hellooooo.blog.pojo.Blog;
import top.hellooooo.blog.pojo.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hellooooo.blog.pojo.User;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private DetailedBlogMapper detailedBlogMapper;

    @Autowired
    private DetailedBlogMapperV2 detailedBlogMapperV2;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Test
    void contextLoads() {
        String str = "Spring,SpringBoot,Maven";
//        String[] split = str.split(",");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        for (String a : list) {
            a = a + "SS";
        }
        System.out.println(list);
    }

    @Test
    void m1() {
        List<Blog> blogs = detailedBlogMapper.selectDetailsOfAllBlog();
        System.out.println(blogs);
    }

    @Test
    void m2() {
        List<Comment> comments = commentMapper.listCommentByBlogId((long) 1);
        Comment comment = new Comment();
        comment.setAvatar("s");
        Blog blog = new Blog();
        blog.setId((long) 1);
        comment.setBlog(blog);
        comment.setParentComment(null);
        comment.setReplyComments(null);
        commentMapper.saveComment(comment);
    }


    @Test
    void m3() {
        List<Integer> list =new ArrayList<>();
        list.add(32);
        list.add(3);
        list.add(2);
        list.add(5);
        list.add(7);
        list.sort((x,y)->{
            if ( x > y ){
                return 1;
            }
            return -1;
        });
        System.out.println(list);

    }

    @Test
    void m4(){
        String reffix ="D:\\temp";
        System.out.println(reffix);
    }

    @Test
    void testGetBlog() {
        // 简单blog
        // final Blog blog = blogMapper.getBlog(71L);
        // 复杂blog
        // final Blog b = detailedBlogMapper.getBlog(71L);
        final Blog blog = detailedBlogMapperV2.getBlog(71L);
        final User user = detailedBlogMapperV2.getUser(blog.getUserId());
        blog.setUser(user);
        System.out.println(user);
    }
}