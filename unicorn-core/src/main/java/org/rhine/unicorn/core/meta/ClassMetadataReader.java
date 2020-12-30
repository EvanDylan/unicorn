package org.rhine.unicorn.core.meta;

import java.util.Collection;

public interface ClassMetadataReader {

    void addClass(Class<?> clazz);

    void addClass(Collection<Class<?>> classes);

    Collection<Class<?>> getInterface();

    ClassMetadata getClassMetadata(Class<?> clazz);

    Collection<ClassMetadata> getAllClassMetadata();
}
