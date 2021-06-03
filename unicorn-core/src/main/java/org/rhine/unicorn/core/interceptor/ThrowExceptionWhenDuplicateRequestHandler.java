package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.RecordLog;

import java.lang.reflect.Method;

@SPI(name = "exception")
public class ThrowExceptionWhenDuplicateRequestHandler implements DuplicateRequestHandler {
    @Override
    public Object handler(Method method, Object[] args, RecordLog recordLog) {
        throw new IdempotentException("due to idempotent [" + method.getName() + "]" + " has been intercepted");
    }
}
