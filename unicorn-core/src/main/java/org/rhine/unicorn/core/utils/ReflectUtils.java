package org.rhine.unicorn.core.utils;

import com.google.common.collect.Lists;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;

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

}
