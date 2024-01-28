package top.kingdon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application{
    public static void main(String[] args) {
        // 启动Spring Boot应用程序
        SpringApplication.run(Application.class, args);
    }

}
