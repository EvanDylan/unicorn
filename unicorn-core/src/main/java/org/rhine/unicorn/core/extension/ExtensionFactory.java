package org.rhine.unicorn.core.extension;

import com.google.common.collect.Lists;
import org.rhine.unicorn.core.utils.ReflectUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionFactory {

    private static final Map<ExtensionMetadata, Object> extensionInstanceMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(final Class<?> clazz, final String name) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz parameter can't be null");
        }
        ExtensionMetadata extensionMetadata;
        if (clazz.isInterface()) {
            Class<?> matchedExtensionClass = ExtensionLoader.loadExtensionClass(clazz, name);
            extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(matchedExtensionClass);
        } else {
            extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(clazz);
        }

        if (!extensionMetadata.isSingleton()) {
            return (T) ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
        } else {
            synchronized (extensionInstanceMap) {
                Object o = extensionInstanceMap.get(extensionMetadata);
                if (o == null) {
                    o = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
                    extensionInstanceMap.put(extensionMetadata, o);
                }
                return (T) o;
            }
        }
    }

    public static <T> T newInstance(final Class<?> clazz) {
        if (clazz.isInterface()) {
            throw new IllegalArgumentException("[" + clazz.getName() + "] parameter can't be interface");
        }
        return getInstance(clazz, null);
    }

    public static <T> Collection<T> getAllInstance(final Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("[" + clazz.getName() + "] parameter must be interface");
        }
        Set<Class<?>> extensionClasses = ExtensionLoader.loadExtensionClass(clazz);
        Collection<T> collection = Lists.newArrayList();
        for (Class<?> extensionClass : extensionClasses) {
            collection.add(newInstance(extensionClass));
        }
        return collection;
    }
}
