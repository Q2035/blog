package top.hellooooo.blog.pojo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * CREATE TABLE `t_blog` (
 *   `id` bigint(20) NOT NULL,
 *   `appreciation` bit(1) NOT NULL,
 *   `commentabled` bit(1) NOT NULL,
 *   `content` varchar(255) DEFAULT NULL,
 *   `create_time` datetime DEFAULT NULL,
 *   `first_picture` varchar(255) DEFAULT NULL,
 *   `flag` varchar(255) DEFAULT NULL,
 *   `published` bit(1) NOT NULL,
 *   `recommend` bit(1) NOT NULL,
 *   `share_statement` bit(1) NOT NULL,
 *   `title` varchar(255) DEFAULT NULL,
 *   `update_time` datetime DEFAULT NULL,
 *   `views` int(11) DEFAULT NULL,
 *   `type_id` bigint(20) DEFAULT NULL,
 *   `user_id` bigint(20) DEFAULT NULL,
 *   PRIMARY KEY (`id`),
 *   KEY `FK292449gwg5yf7ocdlmswv9w4j` (`type_id`),
 *   KEY `FK8ky5rrsxh01nkhctmo7d48p82` (`user_id`),
 *   CONSTRAINT `FK292449gwg5yf7ocdlmswv9w4j` FOREIGN KEY (`type_id`) REFERENCES `t_type` (`id`),
 *   CONSTRAINT `FK8ky5rrsxh01nkhctmo7d48p82` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8
 */
@Data
public class Blog {

    private Long id;

    private String title;
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private String description;
    private boolean appreciation;
    private boolean shareStatement;
    private boolean commentabled;
    private boolean published;
    private boolean recommend;
    private Date createTime;
    private Date updateTime;

    private Type type;

    private List<Tag> tags = new ArrayList<>();

    private User user;

    private List<Comment> comments = new ArrayList<>();

    private transient String tagIds;
    private transient Long typeId;
    private transient Long userId;


    public void init(){
        this.tagIds =tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags){
        if (!tags.isEmpty()){
            StringBuffer ids =new StringBuffer();
            boolean flag =false;
            for (Tag tag : tags) {
                if (flag){
                    ids.append(",");
                }else {
                    flag =true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        }else {
            return tagIds;
        }
    }
}

