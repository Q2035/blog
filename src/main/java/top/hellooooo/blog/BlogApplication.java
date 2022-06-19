package top.hellooooo.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.hellooooo.blog.util.QiniuUtil;


@MapperScan("top.hellooooo.blog.mapper")
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    // @Bean(initMethod = "init")
    // public QiniuUtil qiniuUtil(){
    //     return new QiniuUtil();
    // }
}
