package org.rhine.unicorn.core.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ClassMetadata {

    private final Class<?> clazz;

    private final Collection<Method> methods;

    public Class<?> getClazz() {
        return clazz;
    }

    public boolean isInterface() {
        return clazz.isInterface();
    }

    public String getName() {
        return clazz.getName();
    }

    public Annotation[] getAnnotations() {
        return clazz.getAnnotations();
    }

    public Collection<Method> getMethodsWithAnnotation(Class<? extends Annotation> annotationClass) {
        Set<Method> matchedMethod = new HashSet<>();
        for (Method method : this.methods) {
            if (method.isAnnotationPresent(annotationClass)) {
                matchedMethod.add(method);
            }
        }
        return matchedMethod;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    public ClassMetadata(Class<?> clazz) {
        this.clazz = clazz;
        this.methods = Arrays.asList(clazz.getMethods());
    }
}
