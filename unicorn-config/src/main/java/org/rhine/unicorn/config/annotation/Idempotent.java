package org.rhine.unicorn.config.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * declare annotated method or all of methods of class must be idempotent.
 * see org.rhine.unicorn.config.annotation.Ignore
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Idempotent {

    int duration() default 1;

    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
