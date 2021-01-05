package org.rhine.unicorn.core.extension;

import com.google.common.base.Objects;
import org.rhine.unicorn.core.utils.AnnotationUtils;
import org.rhine.unicorn.core.utils.CollectionUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ExtensionLoader {

    private static final String EXTENSION_DIRECTORY = "META-INF/unicorn/";

    private static final Map<Class<?>, TreeSet<ExtensionEntry>> interfaceWithExtensionEntries = new ConcurrentHashMap<>();

    public static Class<?> loadExtensionClass(final Class<?> clazz, final String spiName) {
        loadExtensionClass(clazz);
        TreeSet<ExtensionEntry> extensionEntries = interfaceWithExtensionEntries.get(clazz);
        if (extensionEntries == null) {
            throw new IllegalArgumentException("can't find implements class [" + clazz.getName() + "]");
        }
        return extensionEntries.stream()
                .filter(e -> spiName.equals(e.getExtensionName())).findFirst()
                .orElseThrow(() -> new RuntimeException("[" + clazz.getName() + "] with spiName [" + spiName + "] can't find matched implements extension class"))
                .getImplementationClass();
    }

    public static Class<?> loadExtensionClass(final Class<?> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("[" + clazz.getName() + "] is not interface");
        }
        TreeSet<ExtensionEntry> extensionEntries = interfaceWithExtensionEntries.get(clazz);
        if (CollectionUtils.isEmpty(extensionEntries)) {
            synchronized (interfaceWithExtensionEntries) {
                try {
                    ClassLoader classLoader = findClassLoader();
                    Enumeration<URL> urls = classLoader.getResources(EXTENSION_DIRECTORY + clazz.getName());
                    extensionEntries = new TreeSet<>();
                    while (urls.hasMoreElements()) {
                        Properties prop = new Properties();
                        URL url = urls.nextElement();
                        prop.load(url.openStream());
                        if (!prop.isEmpty()) {
                            for (Map.Entry<Object, Object> extensionClassName : prop.entrySet()) {
                                String extensionClassNameString = (String) extensionClassName.getKey();
                                try {
                                    Class<?> extensionClass = classLoader.loadClass(extensionClassNameString);
                                    extensionEntries.add(new ExtensionEntry(clazz, extensionClass));
                                    ExtensionDefinitionRegistry.register(extensionClass);
                                } catch (ClassNotFoundException e) {
                                    throw new IllegalArgumentException("Unable to load class [" + clazz.getName() + "]", e);
                                }
                            }
                        }
                    }
                    interfaceWithExtensionEntries.put(clazz, extensionEntries);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Unable to load extension with directory [" + EXTENSION_DIRECTORY + "]", e);
                }
            }
        }
        return extensionEntries.first().implementationClass;
    }

    private static ClassLoader findClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static class ExtensionEntry implements Comparable<ExtensionEntry> {

        private final String extensionName;
        private final Class<?> interfaceClass;
        private final Class<?> implementationClass;
        private final int order;

        public String getExtensionName() {
            return extensionName;
        }

        public Class<?> getImplementationClass() {
            return implementationClass;
        }

        @Override
        public int compareTo(ExtensionEntry o) {
            return Integer.compare(this.order, o.order) * -1;
        }

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

        public ExtensionEntry(Class<?> interfaceClass, Class<?> implementationClass) {
            SPI annotation = AnnotationUtils.getAnnotation(implementationClass, SPI.class);
            this.extensionName = annotation.name();
            this.interfaceClass = interfaceClass;
            this.implementationClass = implementationClass;
            this.order = annotation.order();
        }
    }
}
