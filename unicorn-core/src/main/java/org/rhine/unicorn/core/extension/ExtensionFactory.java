package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.ReflectUtils;

public interface ExtensionFactory {

    ExtensionFactory INSTANCE = (ExtensionFactory) ReflectUtils.newInstance(ExtensionLoader.loadExtensionClass(ExtensionFactory.class).iterator().next());

    <T> T getInstance(final Class<?> clazz, final String name);

    <T> T getInstance(final Class<?> clazz);

}
