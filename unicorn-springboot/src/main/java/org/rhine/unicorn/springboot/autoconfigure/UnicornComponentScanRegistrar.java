package org.rhine.unicorn.springboot.autoconfigure;

import org.rhine.unicorn.spring.proxy.IdempotentAdvisor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public class UnicornComponentScanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(UnicornComponentScan.class.getName()));

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(IdempotentAdvisor.class);
        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        if (annotationAttributes != null) {
            String[] basePackages = annotationAttributes.getStringArray("basePackages");
            builder.addPropertyValue("packagesToScan", Arrays.stream(basePackages).collect(Collectors.toSet()));
        }
        BeanDefinitionReaderUtils.registerWithGeneratedName(builder.getBeanDefinition(), registry);
    }
}
