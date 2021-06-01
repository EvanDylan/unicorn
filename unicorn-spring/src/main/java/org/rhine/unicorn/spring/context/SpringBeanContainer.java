package org.rhine.unicorn.spring.context;

import org.rhine.unicorn.core.extension.BeanContainer;
import org.rhine.unicorn.core.extension.SPI;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SPI(name = "spring", order = 1)
public class SpringBeanContainer implements BeanContainer {

    private static final Map<Class<?>, Object> BEAN_CACHE = new ConcurrentHashMap<>();
    private static BeanFactory springBeanFactory;

    @Override
    public Object getBean(Class<?> clazz) {
        Object bean = BEAN_CACHE.get(clazz);
        if (bean == null) {
            try {
                bean = SpringBeanContainer.springBeanFactory.getBean(clazz);
            } catch (BeansException noSuchBean) {
                return null;
            }
        }
        return bean;
    }

    @Override
    public void register(Class<?> clazz, Object object) {
        BEAN_CACHE.put(clazz, object);
    }

    public static void initSpringBeanFactory(BeanFactory beanFactory) {
        SpringBeanContainer.springBeanFactory = beanFactory;
    }

}
