package org.rhine.unicorn.core.extension;

public interface BeanContainer {

    Object getBean(Class<?> clazz);

    void register(Class<?> clazz, Object object);

}
