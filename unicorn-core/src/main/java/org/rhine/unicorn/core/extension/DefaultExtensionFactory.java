package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SPI
public class DefaultExtensionFactory implements ExtensionFactory {

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
        if (internalClass(clazz)) {
            ExtensionMetadata extensionMetadata;
            if (clazz.isInterface()) {
                Class<?> matchedExtensionClass = ExtensionLoader.loadExtensionClass(clazz, spiName);
                extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(matchedExtensionClass);
            } else {
                extensionMetadata = ExtensionDefinitionRegistry.getExtensionDefinition(clazz);
            }
            return (T) doGetInstance(extensionMetadata, extensionMetadata.isSingleton());
        } else {
            return (T) singletonObjects.get(clazz.getName());
        }
    }

    @Override
    public <T> T getInstance(final Class<T> clazz) {
        return this.getInstance(clazz, "default");
    }

    @Override
    public void register(Object o) {
        String beanName = o.getClass().getName();
        singletonObjects.put(beanName, o);
    }

    private Object doGetInstance(final ExtensionMetadata extensionMetadata, boolean singleton) {
        Object object;
        String beanName = extensionMetadata.getExtensionClass().getName();
        if (singleton) {
            // 没有依赖关系可直接初始化
            if (!extensionMetadata.shouldInitializing()) {
                if (singletonObjects.get(beanName) == null) {
                    object = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
                    singletonObjects.put(beanName, object);
                }
                return singletonObjects.get(beanName);
            }
            // 发生循环依赖
            if (singletonInCreation.containsKey(beanName)) {
                object = singletonInCreation.remove(beanName);
                earlySingletonObjects.put(beanName, object);
                return object;
            }
            object = ReflectUtils.newInstance(extensionMetadata.getExtensionClass());
            singletonInCreation.put(beanName, object);
            Method injectMethod = extensionMetadata.getExtensionClassDeclaredMethods().stream()
                    .filter(method -> method.getName().equals("inject")).findFirst()
                    .orElseThrow(() -> new RuntimeException("[" + "beanName" + "] implements Initializing interface, but without inject method"));
            Class<?> injectClassType = injectMethod.getParameterTypes()[0];
            Object injectObject = singletonObjects.get(injectClassType.getName());
            if (injectObject == null) {
                injectObject = getInstance(injectClassType);
            }
            ((Initializing) object).inject(injectObject);
            if (earlySingletonObjects.containsKey(beanName)) {
                Object earlySingletonObject = earlySingletonObjects.remove(beanName);
                earlySingletonObject = object;
                singletonObjects.put(beanName, earlySingletonObject);
            }
            return object;
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
}
