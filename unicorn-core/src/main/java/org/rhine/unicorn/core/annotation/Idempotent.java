package org.rhine.unicorn.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * declare annotated method or all of methods of class must be idempotent.
 * @see Ignore
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Idempotent {

    int duration() default 1;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * default is qualified method name
     */
    String name() default "";

    String key() default "";

}
