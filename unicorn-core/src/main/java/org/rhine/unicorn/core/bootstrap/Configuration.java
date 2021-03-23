package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.metadata.ClassMetadataReader;
import org.rhine.unicorn.core.metadata.DefaultScanner;
import org.rhine.unicorn.core.metadata.Scanner;
import org.rhine.unicorn.core.interceptor.ProxyFactory;
import org.rhine.unicorn.core.serialize.Serialization;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.utils.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * config info
 */
public class Configuration {

    private static final String CONFIGURATION_LOCATION = "unicorn.properties";

    public static Configuration INSTANCE;

    private Config config;
    private ExpressionEngine expressionEngine;
    private Storage storage;
    private Scanner scanner;
    private Serialization serialization;
    private ClassMetadataReader classMetadataReader;
    private ProxyFactory proxyFactory;

    public Config getConfig() {
        return config;
    }

    public ExpressionEngine getExpressionParser() {
        return expressionEngine;
    }

    public Storage getStorage() {
        return storage;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public Object getValue(String key) {
        return this.config.getValue(key);
    }

    public String getStringValue(String key) {
        return this.config.getStringValue(key);
    }

    public Object getProxyObject(Class<?> clazz) {
        return proxyFactory.createProxy(clazz, this);
    }

    public Serialization getSerialization() {
        return serialization;
    }

    public Configuration() {
        init(loadDefaultProperties());
    }

    public Configuration(Properties properties) {
        init(properties);
    }

    private Properties loadDefaultProperties() {
        Properties properties = new Properties();
        try {
            InputStream is = ClassUtils.getClassLoader().getResourceAsStream(CONFIGURATION_LOCATION);
            if (is != null) {
                properties.load(is);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("can't find properties config file with location [" + CONFIGURATION_LOCATION + "]");
        }
        return properties;
    }

    private void init(Properties properties) {
        this.config = ExtensionFactory.INSTANCE.getInstance(Config.class);
        this.config.setConfigProperties(properties);
        this.scanner = new DefaultScanner();
        this.scanner.scan(this.config.getPackageNames());
        this.classMetadataReader = this.scanner.getClassMetadataReader();
        this.expressionEngine = ExtensionFactory.INSTANCE.getInstance(ExpressionEngine.class, this.config.getExpressionEngineType());
        this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, this.config.getStoreType());
        this.serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class);
        this.proxyFactory = ExtensionFactory.INSTANCE.getInstance(ProxyFactory.class);
    }
}