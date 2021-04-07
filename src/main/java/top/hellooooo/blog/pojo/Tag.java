package top.hellooooo.blog.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Tag {

    private Long id;

    private transient Integer count;

    private String name;

    private List<Blog> blogs = new ArrayList<>();

}

