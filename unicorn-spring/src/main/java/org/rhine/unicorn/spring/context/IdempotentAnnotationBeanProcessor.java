package org.rhine.unicorn.spring.context;

import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.interceptor.ProxyFactory;
import org.rhine.unicorn.core.utils.AnnotationUtils;
import org.rhine.unicorn.core.utils.StringUtils;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class IdempotentAnnotationBeanProcessor implements BeanPostProcessor, PriorityOrdered {

    private final static Set<String> packagesToScan = new LinkedHashSet<>();

    public IdempotentAnnotationBeanProcessor() {

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        if (declaredMethods.length == 0) {
            return bean;
        }
        if (packagesToScan.size() > 0) {
            String packageName = clazz.getPackage().getName();
            if (packagesToScan.stream().noneMatch(path -> StringUtils.contains(packageName, path))) {
                return bean;
            }
        }
        if (AnnotationUtils.isAnnotationPresent(Idempotent.class, declaredMethods)) {
            Config config = ExtensionFactory.INSTANCE.getInstance(Config.class);
            if (config == null) {
                throw new BeanInitializationException("bean of [" + Config.class.getName() + "] is null");
            }
           return ExtensionFactory.INSTANCE.getInstance(ProxyFactory.class).createProxy(bean, new Configuration(config));
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    public static void addPackagesToScan(String... packages) {
        if (packages != null) {
            packagesToScan.addAll(Arrays.asList(packages));
        }
    }

    public static void addPackagesToScan(Set<String> packages) {
        if (packages != null) {
            packagesToScan.addAll(packages);
        }
    }
}