package top.hellooooo.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.hellooooo.blog.util.LoginLog;

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
    @Bean
    public LoginLog loginLog(){
        return new LoginLog();
    }

}
