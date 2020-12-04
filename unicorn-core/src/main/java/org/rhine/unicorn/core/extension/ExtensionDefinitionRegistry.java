package org.rhine.unicorn.core.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionDefinitionRegistry {

    private static final Map<Class<?>, ExtensionDefinition> extensionDefinitionCache = new ConcurrentHashMap<>();

    public static ExtensionDefinition register(final Class<?> clazz) {
        synchronized (extensionDefinitionCache) {
            ExtensionDefinition extensionDefinition = extensionDefinitionCache.get(clazz);
            if (extensionDefinition == null) {
                extensionDefinition = ExtensionDefinitionUtils.getExtensionDefinition(clazz);
                extensionDefinitionCache.put(clazz, extensionDefinition);
            }
            return extensionDefinition;
        }
    }

    public static ExtensionDefinition getExtensionDefinition(final Class<?> clazz) {
        return extensionDefinitionCache.get(clazz);
    }
}
