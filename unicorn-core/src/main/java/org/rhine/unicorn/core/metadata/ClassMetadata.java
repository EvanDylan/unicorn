package org.rhine.unicorn.core.metadata;

import com.google.common.collect.Sets;
import org.rhine.unicorn.core.annotation.Idempotent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClassMetadata {

    private final Class<?> clazz;

    private final Collection<Method> methods;

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
        Set<Method> matchedMethod = Sets.newHashSet();
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
