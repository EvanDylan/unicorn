package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.AnnotationUtils;
import org.rhine.unicorn.core.utils.ReflectUtils;

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
        extensionMetadata.setInterfaceClass(ReflectUtils.getInterfaces(clazz));
        return extensionMetadata;
    }

}
