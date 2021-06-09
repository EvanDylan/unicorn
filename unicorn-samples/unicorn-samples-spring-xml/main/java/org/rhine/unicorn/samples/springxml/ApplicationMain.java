package org.rhine.unicorn.samples.springxml;

import org.rhine.unicorn.samples.springxml.entity.User;
import org.rhine.unicorn.samples.springxml.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationMain {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-context.xml");
        UserService userService = applicationContext.getBean(UserService.class);
        User user = userService.newUser();
        System.out.println(user.toString());
    }

}
