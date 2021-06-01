package org.rhine.unicorn.core.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SPI
public class DefaultBeanContainer implements BeanContainer {

    private static final Map<Class<?>, Object> BEAN_CACHE = new ConcurrentHashMap<>();

    @Override
    public Object getBean(Class<?> clazz) {
        return BEAN_CACHE.get(clazz);
    }

    @Override
    public void register(Class<?> clazz, Object object) {
        BEAN_CACHE.put(clazz, object);
    }
}
