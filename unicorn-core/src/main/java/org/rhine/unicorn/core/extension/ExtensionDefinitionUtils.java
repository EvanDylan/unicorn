package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.AnnotationUtils;

public class ExtensionDefinitionUtils {

    public static ExtensionDefinition getExtensionDefinition(Class<?> clazz) {
        ExtensionDefinition extensionDefinition = new ExtensionDefinition();
        extensionDefinition.setExtensionClass(clazz);
        SPI spi = AnnotationUtils.getAnnotation(clazz, SPI.class);
        if (spi == null) {
            throw new IllegalArgumentException("extension [" + clazz.getName() + "] can't without annotation SPI");
        }
        extensionDefinition.setExtensionName(spi.name());
        extensionDefinition.setSingleton(spi.singleton());
        return extensionDefinition;
    }

}
