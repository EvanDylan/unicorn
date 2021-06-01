package org.rhine.unicorn.springboot.autoconfigure;

import org.rhine.unicorn.spring.context.IdempotentAnnotationBeanProcessor;
import org.rhine.unicorn.spring.utils.BeanDefinitionUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class UnicornComponentScanRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annotationAttributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(UnicornComponentScan.class.getName()));
        if (annotationAttributes != null) {
            String[] basePackages = annotationAttributes.getStringArray("basePackages");
            IdempotentAnnotationBeanProcessor.addPackagesToScan(basePackages);
        }
        BeanDefinitionUtils.registerBeanDefinition(IdempotentAnnotationBeanProcessor.class, registry);
    }
}
