package org.rhine.unicorn.core.extension;

import java.util.Objects;

public class ExtensionDefinition {

    private Class<?> clazz;

    private SPI spi;

    private String className;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public SPI getSpi() {
        return spi;
    }

    public void setSpi(SPI spi) {
        this.spi = spi;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, spi, className);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionDefinition that = (ExtensionDefinition) o;
        if (that.getSpi() != null) {
            return clazz.equals(that.clazz) && className.equals(that.className) &&
                    spi.equals(that.spi);
        }
        return clazz.equals(that.clazz) && className.equals(that.className);
    }
}
