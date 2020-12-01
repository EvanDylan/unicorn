package org.rhine.unicorn.core.proxy;

import com.google.common.collect.Lists;
import org.rhine.unicorn.core.annotation.Idempotent;
import org.rhine.unicorn.core.annotation.Ignore;
import org.rhine.unicorn.core.utils.AnnotationUtils;
import org.rhine.unicorn.core.utils.CollectionUtils;
import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Collection;

public class IdempotentDefinitionParser {

    public IdempotentDefinition parse(Method method) {
        if (!method.isAccessible() && AnnotationUtils.isAnnotationPresent(method, Ignore.class)) {
            return null;
        }
        Idempotent idempotent = AnnotationUtils.getAnnotation(method, Idempotent.class);
        if (idempotent == null) {
            return null;
        }
        return new IdempotentDefinition.Builder()
                .method(method)
                .name(idempotent.name())
                .key(idempotent.key())
                .duration(idempotent.duration())
                .timeUnit(idempotent.timeUnit()).build();
    }

    public Collection<IdempotentDefinition> parse(Class<?> targetClass) {
        Collection<IdempotentDefinition> idempotentDefinitions = Lists.newArrayList();
        Idempotent idempotent = AnnotationUtils.getAnnotation(targetClass, Idempotent.class);
        if (idempotent == null) {
            return idempotentDefinitions;
        }
        Collection<Method> methods = ReflectUtils.getAllDeclaredMethodsWithAnnotation(targetClass, Idempotent.class);
        if (CollectionUtils.isEmpty(methods)) {
            return idempotentDefinitions;
        }
        for (Method method : methods) {
            if (AnnotationUtils.isAnnotationPresent(method, Ignore.class)) {
                continue;
            }
            idempotentDefinitions.add(
                    new IdempotentDefinition.Builder()
                            .method(method)
                            .name(idempotent.name())
                            .key(idempotent.key())
                            .duration(idempotent.duration())
                            .timeUnit(idempotent.timeUnit()).build());
        }
        return idempotentDefinitions;
    }
}
