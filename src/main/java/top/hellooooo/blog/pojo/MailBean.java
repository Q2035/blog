package top.hellooooo.blog.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MailBean implements Serializable {
    private String recipient;
    private String subject;
    private String content;
}
