package org.rhine.unicorn.samples.common;

public class User {

    Long id;

    public User(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
