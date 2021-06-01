package org.rhine.unicorn.spring.proxy;

import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.interceptor.ProxyFactory;

@SPI(name = "spring", order = 1)
public class SpringProxyFactory implements ProxyFactory {

    @Override
    public Object createProxy(Object targetObject, Configuration configuration) {
        org.springframework.aop.framework.ProxyFactory proxyFactory = new org.springframework.aop.framework.ProxyFactory();
        proxyFactory.setTarget(targetObject);
        proxyFactory.addAdvice(new IdempotentAnnotationInterceptor(configuration));
        return proxyFactory.getProxy();
    }
}
