package org.rhine.unicorn.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationUtils {

    public static <A extends Annotation> boolean isAnnotationPresent(Class<?> clazz, Class<A> annotationType) {
        return clazz.isAnnotationPresent(annotationType);
    }

    public static <A extends Annotation> boolean isAnnotationPresent(Method method, Class<A> annotationType) {
        return method.isAnnotationPresent(annotationType);
    }

    public static <A extends Annotation> A getAnnotation(Class<?> clazz, Class<A> annotationType) {
        if (clazz.isAnnotationPresent(annotationType)) {
            return clazz.getAnnotation(annotationType);
        }
        return null;
    }

    public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
        if (method.isAnnotationPresent(annotationType)) {
            return method.getAnnotation(annotationType);
        }
        return null;
    }

}
