package org.rhine.unicorn.core.proxy;


import org.rhine.unicorn.core.imported.cglib.proxy.Enhancer;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodInterceptor;

public class ProxyFactory {

    public static Object createProxy(Class<?> clazz, MethodInterceptor methodInterceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(methodInterceptor);
        return enhancer.create();
    }
}
