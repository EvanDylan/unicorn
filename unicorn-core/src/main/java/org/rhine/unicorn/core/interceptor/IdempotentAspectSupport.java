package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.imported.cglib.proxy.MethodProxy;
import org.rhine.unicorn.core.metadata.ClassMetadata;
import org.rhine.unicorn.core.metadata.IdempotentAnnotationMetadata;
import org.rhine.unicorn.core.serialize.Serialization;
import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.store.RecordFlag;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;

public class IdempotentAspectSupport {

    private String serviceName;

    private Class<?> targetClass;

    private Object targetObject;

    private ClassMetadata matchedClassMetadata;

    private ExpressionEngine expressionEngine;

    private Serialization serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class);
    private Storage storage = ExtensionFactory.INSTANCE.getInstance(Storage.class);

    private void record(Method method, Object[] args, MethodProxy proxy) throws Throwable {
        IdempotentAnnotationMetadata metadata = getMetadata(method);
        Record record = new Record();
        long flag = 0;
        if (ReflectUtils.voidReturnType(method)) {
            flag = RecordFlag.settingFlag(flag, RecordFlag.VOID_RETURN_TYPE_FLAG);
        } else {
            flag = RecordFlag.settingFlag(flag, RecordFlag.SERIALIZE__PROTOBUF_FLAG);
            Object expressionValue = evaluateExpressionValue(method, args, metadata.getKey());
            record.setKey(String.valueOf(expressionValue));
            record.setResponse(serialization.serialize(expressionValue));
        }
        record.setFlag(flag);
        record.setServiceName(this.serviceName);
        record.setClassName(method.getDeclaringClass().getName());
        record.setName(metadata.getName());

        Record storedRecord = this.storage.read(this.serviceName, metadata.getName(), record.getKey());

        long currentTime = System.currentTimeMillis();
        if (storedRecord == null) {
            record.setStoreTimestamp(currentTime);
            record.setExpireMillis(currentTime + metadata.getRemainingExpireMillis());
            this.storage.write(record);
        } else {
            storedRecord.setStoreTimestamp(currentTime);
            storedRecord.setExpireMillis(currentTime + metadata.getRemainingExpireMillis());
            this.storage.update(storedRecord);
        }
    }

    private Object evaluateExpressionValue(Method method, Object[] args, String expression) {
        return this.expressionEngine.evaluate(new ExpressionContext(method, args, expression));
    }

    private IdempotentAnnotationMetadata getMetadata(Method method) {
        return new IdempotentAnnotationMetadata(method);
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }
}
