package org.rhine.unicorn.samples.springxml;

import org.rhine.unicorn.samples.common.User;
import org.rhine.unicorn.samples.common.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationMain {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        UserService userService = applicationContext.getBean(UserService.class);
        User user = userService.newUser(1L);
        System.out.println(user.toString());
    }

}
