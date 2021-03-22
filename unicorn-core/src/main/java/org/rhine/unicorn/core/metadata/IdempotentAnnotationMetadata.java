package org.rhine.unicorn.core.metadata;

import org.rhine.unicorn.core.annotation.Idempotent;

import java.lang.reflect.Method;

public class IdempotentAnnotationMetadata extends ClassMetadata {

    private final Method method;

    private final Idempotent idempotent;

    private final long remainingExpireMillis;

    private final String name;

    private final String key;

    private final String duplicateBehavior;

    public Method getMethod() {
        return method;
    }

    public Idempotent getIdempotent() {
        return idempotent;
    }

    public long getRemainingExpireMillis() {
        return remainingExpireMillis;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public IdempotentAnnotationMetadata(Method method) {
        super(method.getClass());
        this.method = method;
        this.idempotent = method.getAnnotation(Idempotent.class);
        this.remainingExpireMillis = idempotent.timeUnit().toMillis(idempotent.duration());
        this.name = idempotent.name();
        this.key = idempotent.key();
        this.duplicateBehavior = idempotent.duplicateBehavior();
    }
}
