package org.rhine.unicorn.core.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.meta.ClassMetadata;
import org.rhine.unicorn.core.meta.IdempotentMetadata;
import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.expression.ExpressionParser;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.Store;

import java.lang.reflect.Method;

public class IdempotentAnnotationInterceptor implements MethodInterceptor {

    private final Object targetObject;

    private final String serviceName;

    private final Store store;

    private final ExpressionParser expressionParser;

    private final ClassMetadata classMetadata;

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        IdempotentMetadata idempotentMetadata = this.classMetadata.getIdempotentMetadata(method);
        if (idempotentMetadata == null) {
            return proxy.invoke(this.targetObject, args);
        }
        Object value = expressionParser.parse(new ExpressionContext(method, args, classMetadata));
        Message message = store.randomAccess(this.serviceName, idempotentMetadata.getName(), value.toString(), idempotentMetadata.getExpireMillis());
        if (message == null) {
            message = new Message();
            message.setServiceName(this.serviceName);
            message.setName(idempotentMetadata.getName());
            message.setKey(value.toString());
            message.setExpireMillis(idempotentMetadata.getExpireMillis());
            long offset = store.write(message);
        } else {
            return idempotentMetadata.getDuplicateBehaviorHandler().handler(obj, method, args, proxy);
        }
        return proxy.invoke(this.targetObject, args);
    }

    public IdempotentAnnotationInterceptor(Object targetObject, String serviceName, Store store, ExpressionParser expressionParser, ClassMetadata classMetadata) {
        this.targetObject = targetObject;
        this.serviceName = serviceName;
        this.store = store;
        this.expressionParser = expressionParser;
        this.classMetadata = classMetadata;
    }
}
