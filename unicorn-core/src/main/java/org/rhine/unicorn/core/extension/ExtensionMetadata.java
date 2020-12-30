package org.rhine.unicorn.core.extension;

import com.google.common.base.Objects;

public class ExtensionMetadata {

    private Class<?> interfaceClass;

    private Class<?> extensionClass;

    private String extensionName;

    private boolean singleton;

    public Class<?> getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Class<?> getExtensionClass() {
        return extensionClass;
    }

    public void setExtensionClass(Class<?> extensionClass) {
        this.extensionClass = extensionClass;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionMetadata that = (ExtensionMetadata) o;
        return Objects.equal(interfaceClass, that.interfaceClass) &&
                Objects.equal(extensionClass, that.extensionClass) &&
                Objects.equal(extensionName, that.extensionName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(interfaceClass, extensionClass, extensionName);
    }
}
