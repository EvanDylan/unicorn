package org.rhine.unicorn.samples.springboot;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.samples.springboot.entity.User;
import org.rhine.unicorn.samples.springboot.service.UserService;
import org.rhine.unicorn.storage.db.tx.DataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Random;

@SpringBootApplication(scanBasePackages = "org.rhine.unicorn.samples.springboot")
@MapperScan(basePackages = "org.rhine.unicorn.samples.springboot.mapper")
@RestController("/")
@Configuration
public class ApplicationMain {

    @Resource
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }

    @GetMapping("/users/{name}")
    public String createUser(@PathVariable String name) {
        User user = userService.newUser(name == null ? randomName() : name);
        return user.toString();
    }

    @GetMapping("/users/")
    public String createUser() {
        User user = userService.newUser(randomName());
        return user.toString();
    }

    String[] strings = {"A","B","C","D","E","F","J","H","I","J","K","L"};

    private String randomName() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            stringBuilder.append(strings[random.nextInt(strings.length)]);
        }
        return stringBuilder.toString();
    }

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/unicorn?characterEncoding=utf8&useSSL=false&allowMultiQueries=true");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setAutoCommit(false);
        return new DataSourceProxy(dataSource);
    }

}
