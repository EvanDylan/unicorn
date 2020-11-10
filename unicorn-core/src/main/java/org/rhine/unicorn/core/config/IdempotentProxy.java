package org.rhine.unicorn.core.config;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IdempotentProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }

}
