package org.rhine.unicorn.core.utils;

import org.rhine.unicorn.core.imported.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    private static final Map<String, Class<?>> CLASSES_CACHE = new ConcurrentHashMap<>();

    private static final String DOT = ".";
    private static final String CLASS_FILE_SUFFIX = ".class";

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
        }
        return classLoader;
    }

    public static String getClassFileName(Class<?> clazz) {
        String name = clazz.getName();
        int lastDotIndex = name.lastIndexOf(DOT);
        return name.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
    }

    public static ClassReader getClassReader(Class<?> clazz) {
        try (InputStream is = clazz.getResourceAsStream(getClassFileName(clazz))) {
            return new ClassReader(is);
        } catch (IOException e) {
            logger.error("[" + clazz.getName() + "] getClassReader error", e);
        }
        return null;
    }

    public static Class<?> loadClass(String name) {
        try {
            if (CLASSES_CACHE.get(name) == null) {
                synchronized (CLASSES_CACHE) {
                    if (CLASSES_CACHE.get(name) == null) {
                        CLASSES_CACHE.put(name, getClassLoader().loadClass(name));
                    }
                }
            }
            return CLASSES_CACHE.get(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("class with name [" + name + "] can't been load", e);
        }
    }
}
