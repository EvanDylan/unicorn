package org.rhine.unicorn.core.proxy;

import org.rhine.unicorn.core.imported.cglib.proxy.MethodInterceptor;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.expression.ExpressionParser;
import org.rhine.unicorn.core.meta.ClassMetadata;
import org.rhine.unicorn.core.meta.IdempotenAnnotationtMetadata;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.Storage;

import java.lang.reflect.Method;

public class IdempotentAnnotationInterceptor implements MethodInterceptor {

    private final Object targetObject;

    private final String serviceName;

    private final Storage storage;

    private final ExpressionParser expressionParser;

    private final ClassMetadata classMetadata;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        if (!this.classMetadata.hasIdempotenAnnotationtMetadata(method)) {
            return proxy.invoke(this.targetObject, args);
        }
        IdempotenAnnotationtMetadata idempotenAnnotationtMetadata = this.classMetadata.getIdempotentMetadata(method);
        Object value = expressionParser.parse(new ExpressionContext(method, args, idempotenAnnotationtMetadata));

        Message message = new Message();
        message.setServiceName(this.serviceName);
        message.setName(idempotenAnnotationtMetadata.getName());
        message.setKey(value.toString());
        long now = System.currentTimeMillis();
        message.setStoreTimestamp(now);
        message.setExpireMillis(now + idempotenAnnotationtMetadata.getRemainingExpireMillis());
        long offset = storage.writeIfAbsent(message);
        if (offset == 0) {
            return idempotenAnnotationtMetadata.getDuplicateBehaviorHandler().handler(obj, method, args, proxy);
        }
        return proxy.invoke(this.targetObject, args);
    }

    public IdempotentAnnotationInterceptor(Object targetObject, String serviceName, Storage storage, ExpressionParser expressionParser, ClassMetadata classMetadata) {
        this.targetObject = targetObject;
        this.serviceName = serviceName;
        this.storage = storage;
        this.expressionParser = expressionParser;
        this.classMetadata = classMetadata;
    }
}
