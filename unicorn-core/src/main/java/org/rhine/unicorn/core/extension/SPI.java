package org.rhine.unicorn.core.extension;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SPI {

    String name() default "default";

    boolean singleton() default true;

    int order() default 0;
}
