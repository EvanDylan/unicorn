package org.rhine.unicorn.core.interceptor;


public interface ProxyFactory {

    Object createProxy(Class<?> clazz);

}
