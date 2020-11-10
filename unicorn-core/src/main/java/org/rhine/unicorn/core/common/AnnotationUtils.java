package org.rhine.unicorn.core.common;

import java.lang.annotation.Annotation;

public class AnnotationUtils {

    public static <A extends Annotation> A getAnnotation(Class clazz, Class<A> annotationType) {
        if (clazz.isAnnotation()) {
            return (A) clazz.getAnnotation(annotationType);
        }
        return null;
    }

}
