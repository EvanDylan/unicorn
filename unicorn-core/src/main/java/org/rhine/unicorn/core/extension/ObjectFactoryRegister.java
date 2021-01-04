package org.rhine.unicorn.core.extension;

public interface ObjectFactoryRegister {

    <T> T get(String name);

    void register(String beanName, Object o);

}
