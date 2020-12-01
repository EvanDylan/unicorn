package org.rhine.unicorn.core.extension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.rhine.unicorn.core.utils.CollectionUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    private static final String EXTENSION_DIRECTORY = "META-INF/unicorn/";

    private static final Map<String, Set<String>> LOADED_EXTENSIONS = new ConcurrentHashMap<>();

    private static final Map<String, Set<Class<?>>> LOADED_EXTENSION_IMPLEMENT_CLASSES = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getFirstInstance(final Class<T> clazz) {
        if (clazz == null) return null;
        Set<Class<?>> classes = loadingExtensionClass(clazz);
        if (CollectionUtils.isEmpty(classes)) {
            return null;
        }
        for (Class<?> implementsClass : classes) {
            Object o = ExtensionDefinitionRegistry.getInstance(implementsClass);
            if (o != null) {
                return (T) o;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> Collection<T> getInstance(final Class<T> clazz) {
        if (clazz == null) return null;
        Set<Class<?>> classes = loadingExtensionClass(clazz);
        if (CollectionUtils.isEmpty(classes)) {
            return null;
        }
        List<T> list = Lists.newArrayList();
        for (Class<?> implementsClass : classes) {
            Object o = ExtensionDefinitionRegistry.getInstance(implementsClass);
            if (o != null) {
                list.add((T) o);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(final Class<?> clazz, final String spiName) {
        if (clazz == null) return null;
        Set<Class<?>> classes = loadingExtensionClass(clazz);
        if (CollectionUtils.isEmpty(classes)) {
            return null;
        }
        for (Class<?> implementsClass : classes) {
            ExtensionDefinition extensionDefinition = ExtensionDefinitionRegistry.getExtensionDefinition(implementsClass);
            SPI spi = extensionDefinition.getSpi();
            if (spi != null && spi.name().equals(spiName)) {
                Object o = ExtensionDefinitionRegistry.getInstance(extensionDefinition.getClazz());
                if (o != null) {
                   return (T) o;
                }
            }
        }
        return null;
    }

    private static Set<Class<?>> loadingExtensionClass(final Class<?> clazz) {
        String classname = clazz.getName();
        Set<Class<?>> extensionImplementsClasses = LOADED_EXTENSION_IMPLEMENT_CLASSES.get(classname);
        if (CollectionUtils.isNotEmpty(extensionImplementsClasses)) {
            return extensionImplementsClasses;
        }
        synchronized (LOADED_EXTENSION_IMPLEMENT_CLASSES) {
            extensionImplementsClasses = Sets.newHashSet();
            for (String className : loadingExtensionConfig(clazz)) {
                try {
                    Class<?> implementClazz = getCurrentClassLoader().loadClass(className);
                    ExtensionDefinitionRegistry.register(implementClazz);
                    extensionImplementsClasses.add(implementClazz);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Unable to load class [" + className + "]", e);
                }
            }
            LOADED_EXTENSION_IMPLEMENT_CLASSES.put(classname, extensionImplementsClasses);
        }
        return extensionImplementsClasses;
    }

    private static Collection<String> loadingExtensionConfig(final Class<?> clazz) {
        ClassLoader classLoader = getCurrentClassLoader();
        String key = clazz.getName();
        if (LOADED_EXTENSIONS.get(key) != null) {
            return LOADED_EXTENSIONS.get(key);
        }
        try {
            Enumeration<URL> urls = classLoader.getResources(EXTENSION_DIRECTORY + clazz.getName());
            while (urls.hasMoreElements()) {
                Properties prop = new Properties();
                URL url = urls.nextElement();
                prop.load(url.openStream());
                if (prop.isEmpty()) {
                    continue;
                }
                for (Map.Entry<Object, Object> keyValueEntry : prop.entrySet()) {
                    synchronized (LOADED_EXTENSIONS) {
                        if (LOADED_EXTENSIONS.get(key) == null) {
                            LOADED_EXTENSIONS.put(key, Sets.newHashSet((String) keyValueEntry.getKey()));
                        } else {
                            LOADED_EXTENSIONS.get(key).add((String) keyValueEntry.getKey());
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load extension with directory [" + EXTENSION_DIRECTORY + "]", e);
        }
        return LOADED_EXTENSIONS.get(key);
    }

    private static ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
