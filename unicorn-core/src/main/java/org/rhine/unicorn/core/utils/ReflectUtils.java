package org.rhine.unicorn.core.utils;

import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ReflectUtils {

    public static <A extends Annotation> Collection<Method> getAllDeclaredMethodsWithAnnotation(Class<?> targetClass, Class<A> annotationType) {
        Method[] methods = targetClass.getDeclaredMethods();
        Collection<Method> coll = Lists.newArrayList();
        if (methods.length > 0) {
            for (Method method : methods) {
                if (AnnotationUtils.isAnnotationPresent(method, annotationType)) {
                    coll.add(method);
                }
            }
        }
        return coll;
    }

    public static Class<?>[] getInterfaces(Class<?> clazz) {
        return clazz.getInterfaces();
    }

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("can't create instance of [" + clazz.getName() + "]", e);
        }
    }

    public static Method getDeclaredMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<Method> getDeclaredMethods(Class<?> clazz, boolean filterBridge) {
        List<Method> collection = new ArrayList(Arrays.asList(clazz.getDeclaredMethods()));
        for (int i = collection.size() - 1; i >= 0; i--) {
            if (filterBridge && collection.get(i).isBridge()) {
                collection.remove(i);
            }
        }
        return collection;
    }
}
