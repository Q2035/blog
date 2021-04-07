package top.hellooooo.blog.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Type {

    private Long id;
    private String name;

//    标记博客数
    private transient Integer count;

    private List<Blog> blogs = new ArrayList<>();
}

