package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodInterceptor;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.store.RecordLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class IdempotentAnnotationInterceptor extends IdempotentAspectSupport implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(IdempotentAnnotationInterceptor.class);

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        RecordLog recordLog = this.readRecord(method, args);
        if (recordLog != null && !recordLog.hasExpired()) {
            DuplicateRequestHandler handler = this.duplicateRequestHandler(method);
            if (handler == null) {
                throw new IdempotentException("can't not obtain handler with annotation information [" + this.getMetadata(method).toString() + "]");
            }
            return handler.handler(method, args, recordLog);

        }
        Object object = proxy.invokeSuper(obj, args);
        this.writeRecord(method, args, recordLog, object);
        return object;
    }

    public IdempotentAnnotationInterceptor(Configuration configuration) {
        super(configuration);
    }
}
