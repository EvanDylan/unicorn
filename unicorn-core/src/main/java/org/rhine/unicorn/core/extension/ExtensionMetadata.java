package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ExtensionMetadata {

    private Collection<Class<?>> interfaceClass;

    private Class<?> extensionClass;

    private Collection<Method> extensionClassDeclaredMethods;

    private String extensionName;

    private boolean singleton;

    private int order;

    public Collection<Class<?>> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?>[] interfaceClass) {
        this.interfaceClass = Arrays.asList(interfaceClass);
    }

    public Class<?> getExtensionClass() {
        return extensionClass;
    }

    public void setExtensionClass(Class<?> extensionClass) {
        this.extensionClass = extensionClass;
    }

    public Collection<Method> getExtensionClassDeclaredMethods() {
        return extensionClassDeclaredMethods;
    }

    public void setExtensionClassDeclaredMethods(Collection<Method> extensionClassDeclaredMethods) {
        this.extensionClassDeclaredMethods = extensionClassDeclaredMethods;
    }

    public String getExtensionName() {
        return extensionName;
    }

    public void setExtensionName(String extensionName) {
        this.extensionName = extensionName;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean shouldInitializing() {
        return interfaceClass.contains(LazyInitializing.class);
    }

    public Method getInjectMethod() {
        return ReflectUtils.getFirstMatchedMethod(getExtensionClassDeclaredMethods(), "inject");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionMetadata that = (ExtensionMetadata) o;

        return Objects.equals(interfaceClass, that.interfaceClass) &&
                Objects.equals(extensionClass, that.extensionClass) &&
                Objects.equals(extensionName, that.extensionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interfaceClass, extensionClass, extensionName);
    }
}
