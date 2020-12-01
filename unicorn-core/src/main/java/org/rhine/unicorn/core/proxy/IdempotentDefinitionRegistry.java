package org.rhine.unicorn.core.proxy;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IdempotentDefinitionRegistry {

    private final Map<Method, IdempotentDefinition> idempotentDefinitionCache = new ConcurrentHashMap<>();

    public void register(Method method, IdempotentDefinition idempotentDefinition) {
        if (!idempotentDefinitionCache.containsKey(method)) {
            idempotentDefinitionCache.put(method, idempotentDefinition);
        }
    }

    public IdempotentDefinition getIdempotentDefinition(Method method) {
        return idempotentDefinitionCache.get(method);
    }

    public boolean containsIdempotentDefinition(Method method) {
        return idempotentDefinitionCache.containsKey(method);
    }
}
