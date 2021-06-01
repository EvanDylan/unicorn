package org.rhine.unicorn.spring.utils;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class BeanDefinitionUtils {

    public static void registerBeanDefinition(Class<?> clazz, BeanDefinitionRegistry registry) {
        registerBeanDefinition(BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition(), registry);
    }

    public static void registerBeanDefinition(AbstractBeanDefinition definition, BeanDefinitionRegistry registry) {
        BeanDefinitionReaderUtils.registerWithGeneratedName(definition, registry);
    }

}
