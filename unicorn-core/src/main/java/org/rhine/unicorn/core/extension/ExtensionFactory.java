package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.ReflectUtils;

import java.util.List;

public interface ExtensionFactory {

    ExtensionFactory INSTANCE = (ExtensionFactory) ReflectUtils.newInstance(ExtensionLoader.loadFirstPriorityExtensionClass(ExtensionFactory.class));

    <T> T getInstance(final Class<T> clazz, final String name);

    <T> T getInstance(final Class<T> clazz);

    <T> List<T> getAllInstance(final Class<T> clazz);

    BeanContainer getContainer();

}
