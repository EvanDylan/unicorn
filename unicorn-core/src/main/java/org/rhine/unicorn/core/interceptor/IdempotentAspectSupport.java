package org.rhine.unicorn.core.interceptor;

import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.expression.ExpressionContext;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.metadata.IdempotentAnnotationMetadata;
import org.rhine.unicorn.core.serialize.Serialization;
import org.rhine.unicorn.core.store.RecordLog;
import org.rhine.unicorn.core.utils.RecordFlagUtils;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.utils.ReflectUtils;
import org.rhine.unicorn.core.utils.TimeUtils;

import java.lang.reflect.Method;

public class IdempotentAspectSupport {

    private final String applicationName;
    private final ExpressionEngine expressionEngine;
    private final Serialization serialization;
    private final Storage storage;

    protected RecordLog readRecord(Method method, Object[] args) {
        IdempotentAnnotationMetadata metadata = getMetadata(method);
        Object expressionValue = evaluateExpressionValue(method, args, metadata.getKey());
        return this.storage.read(this.applicationName, metadata.getName(), String.valueOf(expressionValue));
    }

    protected void writeRecord(Method method, Object[] args, RecordLog recordLog, Object object) {
        IdempotentAnnotationMetadata metadata = getMetadata(method);
        if (recordLog == null) {
            recordLog = new RecordLog();
        }
        long flag = recordLog.getFlag();
        if (ReflectUtils.voidReturnType(method)) {
            flag = RecordFlagUtils.setReturnTypeFlag(flag);
        } else {
            flag = RecordFlagUtils.setSerializationFlag(flag, serialization.id());
            recordLog.setClassName(method.getReturnType().getName());
            recordLog.setResponse(serialization.serialize(object));
        }
        if (metadata.getKey() != null) {
            Object expressionValue = evaluateExpressionValue(method, args, metadata.getKey());
            recordLog.setKey(String.valueOf(expressionValue));
        }
        recordLog.setFlag(flag);
        recordLog.setApplicationName(this.applicationName);
        recordLog.setName(metadata.getName());

        long currentTime = TimeUtils.getNow();
        recordLog.setStoreTimestamp(currentTime);
        recordLog.setExpireMillis(TimeUtils.plusMillis(currentTime, metadata.getRemainingExpireMillis()));
        this.storage.write(recordLog);
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
