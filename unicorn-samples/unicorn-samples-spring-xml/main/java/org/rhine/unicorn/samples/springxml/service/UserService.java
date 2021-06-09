package org.rhine.unicorn.samples.springxml.service;

import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.samples.springxml.entity.User;
import org.rhine.unicorn.samples.springxml.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Transactional(rollbackFor = Exception.class)
    @Idempotent(key = "#id", duration = 10, timeUnit = TimeUnit.SECONDS)
    public User newUser() {
        String name = randomName();
        long id = userMapper.insert(name);
        return new User(id, name);
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

}
