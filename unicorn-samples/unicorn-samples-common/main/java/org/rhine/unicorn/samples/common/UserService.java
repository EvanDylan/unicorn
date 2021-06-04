package org.rhine.unicorn.samples.common;

import org.rhine.unicorn.core.annotation.Idempotent;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Idempotent(key = "#id", duration = 10, timeUnit = TimeUnit.SECONDS)
    public User newUser(Long id) {
        System.out.println("create user with id " + id);
        return new User(id);
    }

}
