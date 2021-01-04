package org.rhine.unicorn.core.annotation;

import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.proxy.IdempotentException;

import java.lang.reflect.Method;

@SPI(name = "exception")
public class ExceptionDuplicateBehaviorHandler implements DuplicateBehaviorHandler {
    @Override
    public Object handler(Object obj, Method method, Object[] args, MethodProxy proxy) {
        throw new IdempotentException("due to idempotent [" + method.getName() + "]" + " has been intercepted");
    }
}
