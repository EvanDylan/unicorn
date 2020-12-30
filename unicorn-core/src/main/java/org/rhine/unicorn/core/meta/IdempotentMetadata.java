package org.rhine.unicorn.core.meta;

import org.rhine.unicorn.core.annotation.DuplicateBehaviorHandler;
import org.rhine.unicorn.core.annotation.ExceptionDuplicateBehaviorHandler;
import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class IdempotentMetadata {

    private final Method method;

    private final Idempotent idempotent;

    private final long expireMillis;

    private final String name;

    private final String key;

    private final String duplicateBehavior;

    private DuplicateBehaviorHandler duplicateBehaviorHandler;

    public Method getMethod() {
        return method;
    }

    public Idempotent getIdempotent() {
        return idempotent;
    }

    public long getExpireMillis() {
        return expireMillis;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public DuplicateBehaviorHandler getDuplicateBehaviorHandler() {
        return duplicateBehaviorHandler;
    }

    public IdempotentMetadata(Method method) {
        this.method = method;
        this.idempotent = method.getAnnotation(Idempotent.class);
        this.expireMillis = idempotent.timeUnit().toMillis(idempotent.duration());
        this.name = StringUtils.isEmpty(idempotent.name()) ? method.getName() : idempotent.name();
        this.key = idempotent.key();
        this.duplicateBehavior = idempotent.duplicateBehavior();
        this.duplicateBehaviorHandler = ExtensionFactory.getInstance(DuplicateBehaviorHandler.class, duplicateBehavior);
    }
}
