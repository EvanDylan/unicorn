package org.rhine.unicorn.core.annotation;

import org.rhine.unicorn.core.interceptor.IdempotentReturnWhenDuplicateRequestHandler;
import org.rhine.unicorn.core.interceptor.ThrowExceptionWhenDuplicateRequestHandler;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * declare annotated method must be idempotent.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Idempotent {

    int duration() default 1;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * default qualified method name
     */
    String name() default "";

    /**
     * el expression
     */
    String key() default "";

    /**
     * default idempotent return
     * @see IdempotentReturnWhenDuplicateRequestHandler
     * @see ThrowExceptionWhenDuplicateRequestHandler
     */
    String duplicateBehavior() default "default";

}
