package org.rhine.unicorn.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * declare annotated method or all of methods of class must be idempotent.
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
     * @see org.rhine.unicorn.core.annotation.IdempotentReturnWhenDuplicateRequestHandler
     * @see org.rhine.unicorn.core.annotation.ThrowExceptionWhenDuplicateRequestHandler
     */
    String duplicateBehavior() default "default";

}
