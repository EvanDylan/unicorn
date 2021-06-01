package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SPI
@SuppressWarnings("unchecked")
public class DefaultExtensionFactory implements ExtensionFactory {

    private BeanContainer singletonObjectsContainer;
    private final ConcurrentHashMap<Class<?>, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    @Override
    public <T> T getInstance(final Class<T> clazz, final String spiName) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz parameter can't be null");
        }
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("[" +  clazz.getName() + "]" + " must be interface");
        }
        Class<?> matchedExtensionClass = ExtensionLoader.loadExtensionClass(clazz, spiName);
        ExtensionMetadata extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(matchedExtensionClass);
        return (T) doGetInstance(extensionMetadata);
    }

    @Override
    public <T> T getInstance(final Class<T> clazz) {
        Class<?> matchedExtensionClass = ExtensionLoader.loadFirstPriorityExtensionClass(clazz);
        ExtensionMetadata extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(matchedExtensionClass);
        return (T) doGetInstance(extensionMetadata);
    }

    @Override
    public <T> List<T> getAllInstance(Class<T> clazz) {
        List<Class<?>> classes = ExtensionLoader.loadAllExtensionClass(clazz);
        List<T> list = new ArrayList<>();
        for (Class<?> aClass : classes) {
            ExtensionMetadata extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(aClass);
            list.add((T) doGetInstance(extensionMetadata));
        }
        return list;
    }

    private Object doGetInstance(final ExtensionMetadata metadata) {
        Object object;
        Class<?> clazz = metadata.getExtensionClass();
        if (metadata.isSingleton()) {
            object = getContainer().getBean(clazz);
            if (object != null) {
                return object;
            }
            // return early bean to avoid cyclic dependency
            if (isSingletonCurrentlyInCreation(clazz)) {
                return this.earlySingletonObjects.get(clazz);
            }
            synchronized (this.earlySingletonObjects) {
                object = this.earlySingletonObjects.get(clazz);
                if (object == null) {
                    object = ReflectUtils.newInstance(clazz);
                    earlySingletonObjects.put(clazz, object);
                }
            }
        } else {
            object = ReflectUtils.newInstance(clazz);
        }

        if (metadata.shouldInitializing()) {
            Method injectMethod = metadata.getInjectMethod();
            if (injectMethod == null) {
                throw new RuntimeException("[" + "beanName" + "] implements Initializing interface, but without inject method");
            }

            // inject object
            Class<?> injectClassType = injectMethod.getParameterTypes()[0];
            Object injectObject = getContainer().getBean(injectClassType);
            if (injectObject == null) {
                injectObject = getInstance(injectClassType);
            }
            ((LazyInitializing) object).inject(injectObject);
        }

        earlySingletonObjects.remove(clazz);
        if (metadata.isSingleton()) {
            getContainer().register(clazz, object);
        }
        return object;
    }

    private boolean isSingletonCurrentlyInCreation(Class<?> clazz) {
        return earlySingletonObjects.get(clazz) != null;
    }

    @Override
    public BeanContainer getContainer() {
        if (singletonObjectsContainer == null) {
            Class<?> matchedExtensionClass = ExtensionLoader.loadFirstPriorityExtensionClass(BeanContainer.class);
            singletonObjectsContainer = (BeanContainer) ReflectUtils.newInstance(matchedExtensionClass);
        }
        return singletonObjectsContainer;
    }
}
