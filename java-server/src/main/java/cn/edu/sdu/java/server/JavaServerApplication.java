package cn.edu.sdu.java.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.edu.sdu.java.server.mapper")
public class JavaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavaServerApplication.class, args);
    }

}
