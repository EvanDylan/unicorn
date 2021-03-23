package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SPI
public class DefaultExtensionFactory implements ExtensionFactory {

    public static final DefaultExtensionFactory INSTANCE = new DefaultExtensionFactory();

    private final Map<ExtensionMetadata, Object> extensionInstanceMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> singletonInCreation = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    private static final String INTERNAL_CLASS_PATH = "org.rhine.unicorn";

    @Override
    public <T> T getInstance(final Class<T> clazz, final String spiName) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz parameter can't be null");
        }
        ExtensionMetadata extensionMetadata;
        if (clazz.isInterface()) {
            Class<?> matchedExtensionClass = ExtensionLoader.loadExtensionClass(clazz, spiName);
            extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(matchedExtensionClass);
        } else {
            extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(clazz);
        }
        return (T) doGetInstance(extensionMetadata, extensionMetadata.isSingleton());
    }

    @Override
    public <T> T getInstance(final Class<T> clazz) {
        if (!internalClass(clazz)) {
            return resolveDependencyBean(clazz);
        }
        return this.getInstance(clazz, "default");
    }

    @Override
    public void register(final Object o) {
        singletonObjects.put(o.getClass().getName(), o);
    }

    private Object doGetInstance(final ExtensionMetadata extensionMetadata, final boolean singleton) {
        Object object;
        String beanName = extensionMetadata.getExtensionClass().getName();
        if (singleton) {
            if (!extensionMetadata.shouldInitializing()) {
                if (singletonObjects.get(beanName) == null) {
                    object = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
                    singletonObjects.put(beanName, object);
                }
                return singletonObjects.get(beanName);
            } else {
                // current bean already been initializing
                if (singletonInCreation.containsKey(beanName)) {
                    object = singletonInCreation.remove(beanName);
                    earlySingletonObjects.put(beanName, object);
                    return object;
                }
                Object initObject = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
                singletonInCreation.put(beanName, initObject);
                Method injectMethod = ReflectUtils.getFirstMatchedMethod(extensionMetadata.getExtensionClassDeclaredMethods(), "inject");
                if (injectMethod == null) {
                    throw new RuntimeException("[" + "beanName" + "] implements Initializing interface, but without inject method");
                }

                // inject object
                Class<?> injectClassType = injectMethod.getParameterTypes()[0];
                Object injectObject = singletonObjects.get(injectClassType.getName());
                if (injectObject == null) {
                    injectObject = getInstance(injectClassType);
                }
                ((LazyInitializing) initObject).inject(injectObject);

                Object earlySingletonObject = earlySingletonObjects.remove(beanName);
                earlySingletonObject = initObject;
                singletonObjects.put(beanName, earlySingletonObject);
                return initObject;
            }
        } else {
            synchronized (extensionInstanceMap) {
                object = extensionInstanceMap.get(extensionMetadata);
                if (object == null) {
                    object = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
                    extensionInstanceMap.put(extensionMetadata, object);
                }
            }
        }
        return object;
    }

    private boolean internalClass(Class<?> clazz) {
        return clazz.getName().contains(INTERNAL_CLASS_PATH);
    }

    private <T> T resolveDependencyBean(Class<T> clazz) {
        Object object = singletonObjects.get(clazz.getName());
        if (object == null) {
            for (Object value : singletonObjects.values()) {
                if (clazz.isInstance(value)) {
                    object = value;
                }
            }
        }
        return (T) object;
    }
}
