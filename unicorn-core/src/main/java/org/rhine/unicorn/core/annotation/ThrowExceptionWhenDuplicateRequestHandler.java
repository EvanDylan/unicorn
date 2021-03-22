package org.rhine.unicorn.core.annotation;

import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.interceptor.IdempotentException;
import org.rhine.unicorn.core.store.Record;

import java.lang.reflect.Method;

@SPI(name = "exception")
public class ThrowExceptionWhenDuplicateRequestHandler implements DuplicateRequestHandler {
    @Override
    public Object handler(Object obj, Method method, Object[] args, MethodProxy proxy, Record record) {
        throw new IdempotentException("due to idempotent [" + method.getName() + "]" + " has been intercepted");
    }
}
