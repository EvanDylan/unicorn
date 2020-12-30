package org.rhine.unicorn.core.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class ProxyFactory {

    public static Object createProxy(Class<?> clazz, MethodInterceptor methodInterceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(methodInterceptor);
        return enhancer.create();
    }
}
