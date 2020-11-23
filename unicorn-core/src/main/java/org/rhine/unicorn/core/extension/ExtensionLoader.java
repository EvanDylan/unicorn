package org.rhine.unicorn.core.extension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.rhine.unicorn.core.common.CollectionUtils;
import org.rhine.unicorn.core.common.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    private static final String EXTENSION_DIRECTORY = "META-INF/unicorn/";

    private static final Map<String, Set<String>> LOADED_EXTENSIONS = new ConcurrentHashMap<>();

    private static final Map<String, Set<Class<?>>> LOADED_EXTENSION_IMPLEMENT_CLASSES = new ConcurrentHashMap<>();

    static {
        loadingExtensions(EXTENSION_DIRECTORY);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFirstInstance(Class<T> clazz) {
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
    public static <T> List<T> getInstance(Class<T> clazz) {
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
    public static <T> T getInstance(Class<?> clazz, String spiName) {
        if (clazz == null) return null;
        Set<Class<?>> classes = loadingExtensionClass(clazz);
        if (CollectionUtils.isEmpty(classes)) {
            return null;
        }
        for (Class<?> implementsClass : classes) {
            ExtensionDefinition extensionDefinition = ExtensionDefinitionRegistry.getExtensionDefinition(implementsClass);
            if (extensionDefinition == null) continue;
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

    private static Set<Class<?>> loadingExtensionClass(Class<?> clazz) {
        String classname = clazz.getName();
        ClassLoader classLoader = getCurrentClassLoader();
        Set<String> classNames = LOADED_EXTENSIONS.get(classname);
        if (classNames == null || classNames.isEmpty()) {
            return Sets.newHashSet();
        }
        synchronized (LOADED_EXTENSION_IMPLEMENT_CLASSES) {
            Set<Class<?>> extensionImplementsClasses = Sets.newHashSet();
            for (String className : classNames) {
                try {
                    Class<?> implementClazz = classLoader.loadClass(className);
                    extensionImplementsClasses.add(implementClazz);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException("Unable to load class [" + className + "]", e);
                }
            }
            LOADED_EXTENSION_IMPLEMENT_CLASSES.put(classname, extensionImplementsClasses);
            return extensionImplementsClasses;
        }
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
                    String[] value = StringUtils.splitWithCommaSeparator((String) keyValueEntry.getValue()) ;
                    synchronized (LOADED_EXTENSIONS) {
                        if (LOADED_EXTENSIONS.get(key) == null) {
                            LOADED_EXTENSIONS.put(key, Sets.newHashSet(value));
                        } else {
                            LOADED_EXTENSIONS.get(key).addAll(Arrays.asList(value));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to load extension with directory [" + EXTENSION_DIRECTORY + "]", e);
        }
    }

    private static ClassLoader getCurrentClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
