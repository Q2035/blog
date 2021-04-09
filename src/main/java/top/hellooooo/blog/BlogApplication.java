package top.hellooooo.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import top.hellooooo.blog.util.LoginLog;
import top.hellooooo.blog.util.QiniuUtil;


@MapperScan("top.hellooooo.blog.mapper")
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    /**
     * 将LoginLog加入容器
     * @return
     */
    @Scope("singleton")
    @Bean
    public LoginLog loginLog(){
        return new LoginLog();
    }

    @Bean(initMethod = "init")
    public QiniuUtil qiniuUtil(){
        return new QiniuUtil();
    }
}