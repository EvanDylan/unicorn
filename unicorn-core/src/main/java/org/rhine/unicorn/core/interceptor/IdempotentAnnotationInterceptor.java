package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.imported.cglib.proxy.MethodInterceptor;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class IdempotentAnnotationInterceptor extends IdempotentAspectSupport implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {


        return null;
    }
}
