package org.rhine.unicorn.core.extension;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionDefinitionRegistry {

    private static final Map<Class<?>, ExtensionDefinition> CACHED_EXTENSION_IMPLEMENT_DEFINITION = new ConcurrentHashMap<>();

    private static final Map<ExtensionDefinition, Object> CACHED_EXTENSION_IMPLEMENT_CLASS_INSTANCE = new ConcurrentHashMap<>();

    public static ExtensionDefinition register(Class<?> clazz) {
        synchronized (CACHED_EXTENSION_IMPLEMENT_DEFINITION) {
            ExtensionDefinition extensionDefinition = CACHED_EXTENSION_IMPLEMENT_DEFINITION.get(clazz);
            if (extensionDefinition == null) {
                extensionDefinition = ExtensionDefinitionUtils.getExtensionDefinition(clazz);
                CACHED_EXTENSION_IMPLEMENT_DEFINITION.put(clazz, extensionDefinition);
            }
            return extensionDefinition;
        }
    }

    public static ExtensionDefinition getExtensionDefinition(Class<?> clazz) {
        return CACHED_EXTENSION_IMPLEMENT_DEFINITION.get(clazz);
    }

    public static Object getInstance(Class<?> clazz) {
        ExtensionDefinition extensionDefinition;
        synchronized (CACHED_EXTENSION_IMPLEMENT_DEFINITION) {
            extensionDefinition = getExtensionDefinition(clazz);
            if (extensionDefinition == null) {
                extensionDefinition = register(clazz);
            }
        }
        synchronized (CACHED_EXTENSION_IMPLEMENT_CLASS_INSTANCE) {
            Object instance = CACHED_EXTENSION_IMPLEMENT_CLASS_INSTANCE.get(extensionDefinition);
            if (instance == null) {
                try {
                    instance = clazz.newInstance();
                    CACHED_EXTENSION_IMPLEMENT_CLASS_INSTANCE.put(extensionDefinition, instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException("can't create instance of [" + clazz.getName() + "]", e);
                }
            }
            return instance;
        }
    }
}
