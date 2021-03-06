package org.rhine.unicorn.core.metadata;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class SimpleClassMetadataReader implements ClassMetadataReader {

    private final Set<Class<?>> classes = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final Map<Class<?>, ClassMetadata> classClassMetadataRegistry = new ConcurrentHashMap<>();

    @Override
    public void addClass(Class<?> clazz) {
        this.classes.add(clazz);
        this.classClassMetadataRegistry.put(clazz, new ClassMetadata(clazz));
    }

    @Override
    public void addClass(Collection<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            this.addClass(clazz);
        }
    }

    public Collection<Class<?>> getClasses() {
        return Collections.unmodifiableCollection(classes);
    }

    @Override
    public Collection<Class<?>> getInterface() {
        return classes.stream().filter(Class::isInterface).collect(Collectors.toList());
    }

    @Override
    public ClassMetadata getClassMetadata(Class<?> clazz) {
        return classClassMetadataRegistry.get(clazz);
    }

    @Override
    public Collection<ClassMetadata> getAllClassMetadata() {
        return Collections.unmodifiableCollection(classClassMetadataRegistry.values());
    }
}
