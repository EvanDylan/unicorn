package org.rhine.unicorn.core.proxy;

import org.rhine.unicorn.core.utils.ProxyUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class IdempotentProxyFactory {

    public Object createProxy(Class<?> targetClass, InvocationHandler h) {
        if (!ProxyUtils.supportJdkProxy(targetClass)) {
            throw new IllegalArgumentException(targetClass.getName() + " is not an interface");
        }
        return Proxy.newProxyInstance(IdempotentProxyFactory.class.getClassLoader(), new Class[]{targetClass}, h);
    }

}
