package org.rhine.unicorn.core.proxy;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class IdempotentDefinition {

    private Method method;

    private int duration;

    private TimeUnit timeUnit;

    private String name;

    private String key;

    public Method getMethod() {
        return method;
    }

    public int getDuration() {
        return duration;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }



    public IdempotentDefinition(Builder builder) {
        this.method = builder.method;
        this.duration = builder.duration;
        this.timeUnit = builder.timeUnit;
        this.name = builder.name;
        this.key = builder.key;
    }

    public static class Builder {

        private Method method;

        private int duration;

        private TimeUnit timeUnit;

        private String name;

        private String key;

        public Builder method(Method method) {
            this.method = method;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder key(String duration) {
            this.key = key;
            return this;
        }

        public IdempotentDefinition build() {
            return new IdempotentDefinition(this);
        }

    }
}
