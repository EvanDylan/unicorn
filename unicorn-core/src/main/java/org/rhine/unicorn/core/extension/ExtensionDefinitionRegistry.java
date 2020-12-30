package org.rhine.unicorn.core.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionDefinitionRegistry {

    private static final Map<Class<?>, ExtensionMetadata> extensionDefinitionCache = new ConcurrentHashMap<>();

    public static ExtensionMetadata register(final Class<?> clazz) {
        synchronized (extensionDefinitionCache) {
            ExtensionMetadata extensionMetadata = extensionDefinitionCache.get(clazz);
            if (extensionMetadata == null) {
                extensionMetadata = ExtensionDefinitionUtils.getExtensionDefinition(clazz);
                extensionDefinitionCache.put(clazz, extensionMetadata);
            }
            return extensionMetadata;
        }
    }

    public static ExtensionMetadata getExtensionDefinition(final Class<?> clazz) {
        return extensionDefinitionCache.get(clazz);
    }
}
