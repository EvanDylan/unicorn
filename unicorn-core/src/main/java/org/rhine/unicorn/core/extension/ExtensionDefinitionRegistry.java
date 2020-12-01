package org.rhine.unicorn.core.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionDefinitionRegistry {

    private static final Map<Class<?>, ExtensionDefinition> extensionDefinitionCache = new ConcurrentHashMap<>();

    private static final Map<ExtensionDefinition, Object> extensionInstanceCache = new ConcurrentHashMap<>();

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

    public static Object getInstance(final Class<?> clazz) {
        synchronized (extensionInstanceCache) {
            ExtensionDefinition extensionDefinition = getExtensionDefinition(clazz);
            if (extensionDefinition == null) {
                throw new IllegalArgumentException("Unable get [" + clazz.getName() + "]");
            }
            Object instance = extensionInstanceCache.get(extensionDefinition);
            if (instance == null) {
                try {
                    instance = clazz.newInstance();
                    extensionInstanceCache.put(extensionDefinition, instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("can't create instance of [" + clazz.getName() + "]", e);
                }
            }
            return instance;
        }
    }
}
