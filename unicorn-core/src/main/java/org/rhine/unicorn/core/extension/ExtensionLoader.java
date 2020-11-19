package org.rhine.unicorn.core.extension;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    public static final String EXTENSION_DIRECTORY = "META-INF/unicorn/";

    private static final Map<String, Set<String>> LOADED_EXTENSIONS = new ConcurrentHashMap<>();

    private static final Map<String, Set<ExtensionDefinition>> LOADED_CLASSES = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Object> BEEN_INITIALIZED = new ConcurrentHashMap<>();

    static {
        loadingExtensions(EXTENSION_DIRECTORY);
    }

    public static Class<?> findFirst(Class<?> clazz) {
        if (clazz == null) return null;
        String className = clazz.getName();
        Set<ExtensionDefinition> classes = LOADED_CLASSES.get(className);
        if (classes != null && !classes.isEmpty()) {
            return classes.iterator().next().getClazz();
        }
        synchronized (LOADED_EXTENSIONS) {
            Set<String> classNames = LOADED_EXTENSIONS.get(className);
            if (classNames != null && !classNames.isEmpty()) {

            }
        }
        return null;
    }

    public static Class<?> findClass(Class<?> clazz, String name) {

        return null;
    }

    private static void loadingExtensions(String path) {
        ClassLoader classLoader = getCurrentClassLoader();
        try {
            Enumeration<URL> urls = classLoader.getResources(path);
            while (urls.hasMoreElements()) {
                Properties prop = new Properties();
                URL url = urls.nextElement();
                prop.load(url.openStream());
                if (prop.isEmpty()) {
                    continue;
                }
                for (Map.Entry<Object, Object> keyValueEntry : prop.entrySet()) {
                    String key = (String) keyValueEntry.getKey();
                    String value = (String) keyValueEntry.getValue();
                    synchronized (LOADED_EXTENSIONS) {
                        if (LOADED_EXTENSIONS.get(key) == null) {
                            LOADED_EXTENSIONS.put(key, Sets.newHashSet(value));
                        } else {
                            LOADED_EXTENSIONS.get(key).add(value);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load extension with directory [" + EXTENSION_DIRECTORY + "]", e);
        }
    }

    private static List<ExtensionDefinition> loadExtensionClass() {
        return null;
    }

    private static ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
