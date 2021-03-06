package org.rhine.unicorn.spring.proxy;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.interceptor.DuplicateRequestHandler;
import org.rhine.unicorn.core.interceptor.IdempotentAspectSupport;
import org.rhine.unicorn.core.interceptor.IdempotentException;
import org.rhine.unicorn.core.store.RecordLog;

import java.lang.reflect.Method;

public class IdempotentAnnotationInterceptor extends IdempotentAspectSupport implements MethodInterceptor {

    public IdempotentAnnotationInterceptor(Config config) {
        super(config);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();
        RecordLog recordLog = this.readRecord(method, args);
        if (recordLog != null && !recordLog.hasExpired()) {
            DuplicateRequestHandler handler = this.duplicateRequestHandler(invocation.getMethod());
            if (handler != null) {
                return handler.handler(method, args, recordLog);
            }
            throw new IdempotentException("can't not obtain handler with annotation information [" + this.getMetadata(method).toString() + "]");
        }
        Object object = invocation.proceed();
        this.writeRecord(method, args, recordLog, object);
        return object;
    }
}
