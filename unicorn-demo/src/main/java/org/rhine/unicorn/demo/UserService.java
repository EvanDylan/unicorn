package org.rhine.unicorn.demo;

import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.core.bootstrap.Configuration;

import java.util.concurrent.TimeUnit;

public class UserService {

    @Idempotent(key = "#id", duration = 10, timeUnit = TimeUnit.DAYS)
    public void newUser(Long id) {
        System.out.println("create user with id " + id);
    }

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        UserService userService = (UserService) configuration.getProxyObject(UserService.class);
        userService.newUser(1L);
        System.out.println();
    }
}