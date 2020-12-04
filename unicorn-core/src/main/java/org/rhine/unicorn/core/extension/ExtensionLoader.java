package org.rhine.unicorn.core.extension;

import com.google.common.base.Objects;
import org.rhine.unicorn.core.utils.AnnotationUtils;
import org.rhine.unicorn.core.utils.CollectionUtils;
import org.rhine.unicorn.core.utils.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ExtensionLoader {

    private static final String EXTENSION_DIRECTORY = "META-INF/unicorn/";

    private static final Map<Class<?>, Set<Class<?>>> interfaceWithExtensionClassMap = new ConcurrentHashMap<>();

    private static final Map<ExtensionEntry, Class<?>> extensionEntryWithExtensionClassMap = new ConcurrentHashMap<>();

    public static Class<?> loadExtensionClass(final Class<?> clazz, final String name) {
        loadExtensionClass(clazz);
        Class<?> extensionClass = extensionEntryWithExtensionClassMap.get(new ExtensionEntry(clazz, name));
        if (extensionClass == null) {
            throw new IllegalArgumentException("can't find implements class [" + clazz.getName() + "]");
        }
        return extensionClass;
    }

    public static Set<Class<?>> loadExtensionClass(final Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("[" + clazz.getName() + "] is not interface");
        }
        Set<Class<?>> matchedClasses = interfaceWithExtensionClassMap.get(clazz);
        if (CollectionUtils.isNotEmpty(matchedClasses)) {
            return matchedClasses;
        }
        synchronized (interfaceWithExtensionClassMap) {
            try {
                ClassLoader classLoader = findClassLoader();
                Enumeration<URL> urls = classLoader.getResources(EXTENSION_DIRECTORY + clazz.getName());
                matchedClasses = new HashSet<>();
                while (urls.hasMoreElements()) {
                    Properties prop = new Properties();
                    URL url = urls.nextElement();
                    prop.load(url.openStream());
                    if (!prop.isEmpty()) {
                        for (Map.Entry<Object, Object> extensionClassName : prop.entrySet()) {
                            String extensionClassNameString = (String) extensionClassName.getKey();
                            try {
                                Class<?> extensionClass = classLoader.loadClass(extensionClassNameString);
                                ExtensionEntry extensionEntry = new ExtensionEntry(clazz, getExtensionName(extensionClass));
                                extensionEntryWithExtensionClassMap.put(extensionEntry, extensionClass);
                                matchedClasses.add(extensionClass);
                                ExtensionDefinitionRegistry.register(extensionClass);
                            } catch (ClassNotFoundException e) {
                                throw new IllegalArgumentException("Unable to load class [" + clazz.getName() + "]", e);
                            }
                        }
                    }
                }
                interfaceWithExtensionClassMap.put(clazz, matchedClasses);
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to load extension with directory [" + EXTENSION_DIRECTORY + "]", e);
            }
        }
        return matchedClasses;
    }

    private static ClassLoader findClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static String getExtensionName(Class<?> clazz) {
        SPI spi = AnnotationUtils.getAnnotation(clazz, SPI.class);
        if (spi == null) {
            return StringUtils.EMPTY_STRING;
        } else {
            return spi.name();
        }
    }

    private static class ExtensionEntry {

        private Class<?> interfaceClass;

        private String extensionName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExtensionEntry that = (ExtensionEntry) o;
            return Objects.equal(interfaceClass, that.interfaceClass) &&
                    Objects.equal(extensionName, that.extensionName);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(interfaceClass, extensionName);
        }

        public ExtensionEntry(Class<?> interfaceClass, String extensionName) {
            this.interfaceClass = interfaceClass;
            this.extensionName = extensionName;
        }
    }
}
