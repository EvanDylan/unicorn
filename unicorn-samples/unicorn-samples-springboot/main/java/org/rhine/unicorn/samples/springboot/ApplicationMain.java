package org.rhine.unicorn.samples.springboot;

import com.zaxxer.hikari.HikariDataSource;
import org.rhine.unicorn.samples.common.User;
import org.rhine.unicorn.samples.common.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = "org.rhine.unicorn.samples")
@RestController("/")
@Configuration
public class ApplicationMain {

    @Resource
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    @GetMapping("/")
    public String createUser() {
        long start = System.currentTimeMillis();
        User user = userService.newUser(1L);
        long end = System.currentTimeMillis();
        System.out.println((end - start) + user.toString());
        return user.toString();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/unicorn?characterEncoding=utf8&useSSL=false&allowMultiQueries=true");
        dataSource.setUsername("root");
        dataSource.setPassword("JayEvan888266");
        return dataSource;
    }
}
