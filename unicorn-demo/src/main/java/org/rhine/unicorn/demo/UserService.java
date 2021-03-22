package org.rhine.unicorn.demo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.core.bootstrap.Configuration;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

public class UserService {

    @Idempotent(key = "#id", duration = 10, timeUnit = TimeUnit.DAYS)
    public void newUser(Long id) {
        System.out.println("create user with id " + id);
    }

    public static void main(String[] args) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/unicorn");
        config.setUsername("root");
        config.setPassword("JayEvan888266");
        DataSource dataSource = new HikariDataSource(config);

        Configuration configuration = new Configuration();
        UserService userService = (UserService) configuration.getProxyObject(UserService.class);
        userService.newUser(1L);
        System.out.println();
    }
}
