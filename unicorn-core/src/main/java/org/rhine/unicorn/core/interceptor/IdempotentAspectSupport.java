package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.metadata.IdempotentAnnotationMetadata;
import org.rhine.unicorn.core.serialize.Serialization;
import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.utils.RecordFlagUtils;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;

public class IdempotentAspectSupport {

    private String applicationName;
    private ExpressionEngine expressionEngine;
    private Serialization serialization;
    private Storage storage;

    protected Record readRecord(Method method, Object[] args) {
        IdempotentAnnotationMetadata metadata = getMetadata(method);
        Object expressionValue = evaluateExpressionValue(method, args, metadata.getKey());
        return this.storage.read(this.applicationName, metadata.getName(), String.valueOf(expressionValue));
    }

    protected void writeRecord(Method method, Object[] args, Record storedRecord, Object object) {
        IdempotentAnnotationMetadata metadata = getMetadata(method);
        Record record = new Record();
        long flag = 0;
        if (ReflectUtils.voidReturnType(method)) {
            flag = RecordFlagUtils.setReturnTypeFlag(flag);
        } else {
            flag = RecordFlagUtils.setSerializationFlag(flag, serialization.id());
            record.setClassName(method.getReturnType().getName());
            record.setResponse(serialization.serialize(object));
        }
        if (metadata.getKey() != null) {
            Object expressionValue = evaluateExpressionValue(method, args, metadata.getKey());
            record.setKey(String.valueOf(expressionValue));
        }
        record.setFlag(flag);
        record.setApplicationName(this.applicationName);
        record.setName(metadata.getName());

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

    protected Object evaluateExpressionValue(Method method, Object[] args, String expression) {
        return this.expressionEngine.evaluate(new ExpressionContext(method, args, expression));
    }

    protected IdempotentAnnotationMetadata getMetadata(Method method) {
        return new IdempotentAnnotationMetadata(method);
    }

    protected DuplicateRequestHandler duplicateRequestHandler(Method method) {
        String duplicateBehavior = this.getMetadata(method).getDuplicateBehavior();
        return ExtensionFactory.INSTANCE.getInstance(DuplicateRequestHandler.class, duplicateBehavior);
    }

    public IdempotentAspectSupport(Configuration configuration) {
        this.applicationName = configuration.getConfig().getApplicationName();
        this.expressionEngine = configuration.getExpressionEngine();
        this.storage = configuration.getStorage();
        this.serialization = configuration.getSerialization();
    }
}
