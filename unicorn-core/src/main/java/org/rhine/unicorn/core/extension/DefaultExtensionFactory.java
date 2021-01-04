package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SPI(name = "default")
public class DefaultExtensionFactory implements ExtensionFactory {

    private final ObjectFactoryRegister objectFactoryRegister = new DefaultObjectFactoryRegister();
    private final Map<ExtensionMetadata, Object> extensionInstanceMap = new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInstance(final Class<?> clazz, final String name) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz parameter can't be null");
        }
        ExtensionMetadata extensionMetadata;
        if (clazz.isInterface()) {
            Class<?> matchedExtensionClass = ExtensionLoader.loadExtensionClass(clazz, name);
            extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(matchedExtensionClass);
        } else {
            extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(clazz);
        }

        Object o = null;
        if (!extensionMetadata.isSingleton()) {
            o = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
        } else {
            synchronized (extensionInstanceMap) {
                o = extensionInstanceMap.get(extensionMetadata);
                if (o == null) {
                    o = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
                    extensionInstanceMap.put(extensionMetadata, o);
                }
            }
        }

        // TODO 内置微型ioc容器用来初始化组件，管理依赖关系
        if (extensionMetadata.needInitializing()) {
            Initializing initializing = (Initializing) o;
            for (Method method : initializing.getClass().getDeclaredMethods()) {
                System.out.println(method.getName() + " " + method.isBridge());
            }
            initializing.inject(objectFactoryRegister);
        }
        return (T) o;
    }

    @Override
    public <T> T getInstance(Class<?> clazz) {
        return this.getInstance(clazz, "default");
    }
}
