package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.annotation.DuplicateRequestHandler;
import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodInterceptor;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.store.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class IdempotentAnnotationInterceptor extends IdempotentAspectSupport implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(IdempotentAnnotationInterceptor.class);

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Record storedRecord = this.readRecord(method, args);
        if (storedRecord != null && !storedRecord.hasExpired()) {
            DuplicateRequestHandler handler = this.duplicateRequestHandler(method);
            if (handler != null) {
                return handler.handler(obj, method, args, proxy, storedRecord);
            }
            logger.warn("can't not obtain handler with annotation information [{}]", this.getMetadata(method).toString());
        }
        Object object = proxy.invokeSuper(obj, args);
        this.writeRecord(method, args, storedRecord, object);
        return object;
    }

    public IdempotentAnnotationInterceptor(Configuration configuration) {
        super(configuration);
    }
}
