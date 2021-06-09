package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.imported.cglib.proxy.Callback;
import org.rhine.unicorn.core.imported.cglib.proxy.Enhancer;
import org.rhine.unicorn.core.imported.cglib.proxy.NoOp;
import org.rhine.unicorn.core.utils.AnnotationUtils;
import org.rhine.unicorn.core.utils.ClassUtils;

@SPI
public class DefaultProxyFactory implements ProxyFactory {

    private static final int IDEMPOTENT_PROXY = 0;
    private static final int NOOP = 1;

    @Override
    public Object createProxy(Object targetObject, Config config) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetObject.getClass());
        enhancer.setClassLoader(ClassUtils.getClassLoader());
        Callback[] callbacks = new Callback[] {
                new IdempotentAnnotationInterceptor(config),
                NoOp.INSTANCE
        };
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackFilter(
                method -> {
                    if (AnnotationUtils.isAnnotationPresent(method, Idempotent.class)) {
                        return IDEMPOTENT_PROXY;
                    }
                    return NOOP;
                }
        );
        return enhancer.create();
    }

}
