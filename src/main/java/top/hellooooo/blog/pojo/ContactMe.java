package top.hellooooo.blog.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class ContactMe {
    private Long id;
    private String email;
    private String description;
    private Date createTime;
}
