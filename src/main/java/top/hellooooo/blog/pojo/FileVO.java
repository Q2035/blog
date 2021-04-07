package top.hellooooo.blog.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @Create: 13/04/2020 21:35
 * @Author: Q
 */
@Data
public class FileVO {
    private String fileName;
    private String fileSize;
    private Date updateDate;
}
