package org.rhine.unicorn.core.extension;

import java.util.Objects;

public class ExtensionDefinition {

    private Class<?> clazz;

    private SPI spi;

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

    @Override
    public int hashCode() {
        return Objects.hash(clazz, spi);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionDefinition that = (ExtensionDefinition) o;
        return clazz.equals(that.clazz) &&
                spi.equals(that.spi);
    }
}
