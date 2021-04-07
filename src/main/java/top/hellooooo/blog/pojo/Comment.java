package top.hellooooo.blog.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Comment {

    private Long id;
    private String nickname;
    private String email;
    private String content;
    private String avatar;
    private Date createTime;

    private Blog blog;

    private List<Comment> replyComments = new ArrayList<>();

    private Comment parentComment;

    private transient Long parentId = Long.valueOf(-1);

    private boolean adminComment;
}

