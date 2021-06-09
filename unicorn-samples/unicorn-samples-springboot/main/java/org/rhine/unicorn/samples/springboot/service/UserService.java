package org.rhine.unicorn.samples.springboot.service;

import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.samples.springboot.entity.User;
import org.rhine.unicorn.samples.springboot.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
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
    @Idempotent(key = "#name", duration = 10, timeUnit = TimeUnit.SECONDS)
    public User newUser(String name) {
        User user = new User(null, name);
        userMapper.insert(user);
        System.out.println(user);
        return user;
    }

}
