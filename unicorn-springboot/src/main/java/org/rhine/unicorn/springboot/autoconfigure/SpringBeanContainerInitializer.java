package org.rhine.unicorn.springboot.autoconfigure;

import org.rhine.unicorn.spring.context.SpringBeanContainer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringBeanContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        SpringBeanContainer.initSpringBeanFactory(applicationContext);
    }
}
