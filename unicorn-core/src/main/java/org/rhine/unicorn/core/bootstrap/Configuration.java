package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.expression.ExpressionParser;
import org.rhine.unicorn.core.extension.DefaultExtensionFactory;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.extension.ObjectFactoryRegister;
import org.rhine.unicorn.core.meta.ClassMetadata;
import org.rhine.unicorn.core.meta.DefaultScanner;
import org.rhine.unicorn.core.meta.Scanner;
import org.rhine.unicorn.core.proxy.IdempotentAnnotationInterceptor;
import org.rhine.unicorn.core.proxy.ProxyFactory;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.utils.ClassUtils;
import org.rhine.unicorn.core.utils.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * config info
 */
public class Configuration {

    private static final String CONFIGURATION_LOCATION = "unicorn.properties";

    private static final String SERVICE_NAME = "unicorn.servicename";
    private static final String STORE_TYPE_STRING = "unicorn.storetype";
    private static final String SCAN_LOCATIONS_STRING = "unicorn.scanlocations";
    private static final String EXPRESSION_ENGINE_TYPE_STRING = "unicorn.expressionengine";
    private static final String OBJECT_FACTORY_TYPE = "unicorn.objectfactorytype";

    private final Properties properties;
    private final String serviceName;
    private final String storeType;
    private final List<String> packageNames;
    private final String expressionEngineType;
    private final ExpressionParser expressionParser;
    private final Storage storage;
    private final Scanner scanner;
    private final String objectFactoryType;
    private final ObjectFactoryRegister objectFactoryRegister;

    public Object getValue(String key) {
        if (properties != null) {
            return properties.get(key);
        }
        return null;
    }

    public String getStringValue(String key) {
        if (properties != null) {
            return properties.getProperty(key);
        }
        return null;
    }

    public String getStringValueWithDefault(String key, String defaultValue) {
        String value = getStringValue(key);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * get properties
     * @return Properties
     */
    public Properties getProperties() {
        return this.properties;
    }

    /**
     * get store type
     * @return store type
     */
    public String getStoreType() {
        return this.storeType;
    }

    /**
     * location of will be scan
     * @return the collection of package name
     */
    public List<String> getPackageNames() {
        return this.packageNames;
    }

    public Object getProxyObject(Class<?> clazz) {
        ClassMetadata classMetadata = this.scanner.getClassMetadataReader().getClassMetadata(clazz);
        try {
            return ProxyFactory.createProxy(clazz,
                    new IdempotentAnnotationInterceptor(clazz.newInstance(), this.serviceName, this.storage, this.expressionParser, classMetadata));
        } catch (Exception e) {
            throw new RuntimeException("new instance of [" + clazz.getName() + "] error", e);
        }
    }

    public void registerObject(Object object) {
        objectFactoryRegister.register(object.getClass().getName(), object);
    }

    public ObjectFactoryRegister getObjectFactoryRegister() {
        return this.objectFactoryRegister;
    }

    public Configuration() {
        Properties properties = new Properties();
        try {
            properties.load(ClassUtils.getClassLoader().getResourceAsStream(CONFIGURATION_LOCATION));
        } catch (IOException e) {
            throw new IllegalArgumentException("can't find properties config file with location [" + CONFIGURATION_LOCATION + "]");
        }

        this.properties = properties;
        this.serviceName = getStringValue(SERVICE_NAME);
        this.scanner = new DefaultScanner();
        this.storeType = getStringValue(STORE_TYPE_STRING);
        this.packageNames = Arrays.asList(StringUtils
                .splitWithCommaSeparator(getStringValue(SCAN_LOCATIONS_STRING)));
        this.scanner.scan(packageNames);
        this.expressionEngineType = getStringValue(EXPRESSION_ENGINE_TYPE_STRING);
        this.expressionParser = ExtensionFactory.INSTANCE.getInstance(ExpressionParser.class, expressionEngineType);
        this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, storeType);
        this.objectFactoryType = getStringValueWithDefault(OBJECT_FACTORY_TYPE, "default");
        this.objectFactoryRegister = ExtensionFactory.INSTANCE.getInstance(ObjectFactoryRegister.class, objectFactoryType);
    }

    public Configuration(Properties properties) {
        this.properties = properties;
        this.serviceName = getStringValue(SERVICE_NAME);
        this.scanner = new DefaultScanner();
        this.storeType = getStringValue(STORE_TYPE_STRING);
        this.packageNames = Arrays.asList(StringUtils
                .splitWithCommaSeparator(getStringValue(SCAN_LOCATIONS_STRING)));
        this.scanner.scan(packageNames);
        this.expressionEngineType = getStringValue(EXPRESSION_ENGINE_TYPE_STRING);
        this.expressionParser = ExtensionFactory.INSTANCE.getInstance(ExpressionParser.class, expressionEngineType);
        this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, storeType);
        this.objectFactoryType = getStringValueWithDefault(OBJECT_FACTORY_TYPE, "default");
        this.objectFactoryRegister = ExtensionFactory.INSTANCE.getInstance(ObjectFactoryRegister.class, objectFactoryType);
    }
}