package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.AnnotationUtils;

public class ExtensionDefinitionUtils {

    public static ExtensionMetadata getExtensionDefinition(Class<?> clazz) {
        ExtensionMetadata extensionMetadata = new ExtensionMetadata();
        extensionMetadata.setExtensionClass(clazz);
        SPI spi = AnnotationUtils.getAnnotation(clazz, SPI.class);
        if (spi == null) {
            throw new IllegalArgumentException("extension [" + clazz.getName() + "] can't without annotation SPI");
        }
        extensionMetadata.setExtensionName(spi.name());
        extensionMetadata.setSingleton(spi.singleton());
        return extensionMetadata;
    }

}
