package org.rhine.unicorn.core.extension;

import org.rhine.unicorn.core.utils.AnnotationUtils;

public class ExtensionDefinitionUtils {

    public static ExtensionDefinition getExtensionDefinition(Class<?> clazz) {
        ExtensionDefinition extensionDefinition = new ExtensionDefinition();
        extensionDefinition.setClazz(clazz);
        extensionDefinition.setClassName(clazz.getName());
        extensionDefinition.setSpi(AnnotationUtils.getAnnotation(clazz, SPI.class));
        return extensionDefinition;
    }

}
