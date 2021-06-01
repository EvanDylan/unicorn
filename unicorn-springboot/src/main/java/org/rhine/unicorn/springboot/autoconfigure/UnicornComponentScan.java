package org.rhine.unicorn.springboot.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(UnicornComponentScanRegistrar.class)
public @interface UnicornComponentScan {

    String[] basePackages() default {};

}
