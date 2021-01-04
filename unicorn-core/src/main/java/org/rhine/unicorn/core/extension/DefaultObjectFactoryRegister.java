package org.rhine.unicorn.core.extension;

import java.util.concurrent.ConcurrentHashMap;

@SPI(name = "default")
public class DefaultObjectFactoryRegister implements ObjectFactoryRegister {

    private final ConcurrentHashMap<String, Object> objectRegister = new ConcurrentHashMap<>();

    @Override
    public <T> T get(String beanName) {
        return (T) objectRegister.get(beanName);
    }

    @Override
    public void register(String beanName, Object o) {
        objectRegister.put(beanName, o);
    }
}
