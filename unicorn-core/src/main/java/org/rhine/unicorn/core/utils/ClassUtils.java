package org.rhine.unicorn.core.utils;

import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class ClassUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);

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
}
