package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.config.DefaultConfig;
import org.rhine.unicorn.core.expression.ExpressionParser;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.meta.ClassMetadata;
import org.rhine.unicorn.core.meta.DefaultScanner;
import org.rhine.unicorn.core.meta.Scanner;
import org.rhine.unicorn.core.proxy.IdempotentAnnotationInterceptor;
import org.rhine.unicorn.core.proxy.ProxyFactory;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.utils.ClassUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * config info
 */
public class Configuration {

    private static final String CONFIGURATION_LOCATION = "unicorn.properties";

    private final Properties properties;
    private Config config;
    private ExpressionParser expressionParser;
    private Storage storage;
    private Scanner scanner;

    public Object getValue(String key) {
        return this.config.getValue(key);
    }

    public String getStringValue(String key) {
        return this.config.getStringValue(key);
    }

    public Object getProxyObject(Class<?> clazz) {
        ClassMetadata classMetadata = this.scanner.getClassMetadataReader().getClassMetadata(clazz);
        try {
            return ProxyFactory.createProxy(clazz,
                    new IdempotentAnnotationInterceptor(clazz.newInstance(), this.config.getServiceName(), this.storage, this.expressionParser, classMetadata));
        } catch (Exception e) {
            throw new RuntimeException("new instance of [" + clazz.getName() + "] error", e);
        }
    }

    public void register(String beanName, Object o) {
        ExtensionFactory.INSTANCE.register(o);
    }

    public Configuration() {
        this.properties = new Properties();
        try {
            properties.load(ClassUtils.getClassLoader().getResourceAsStream(CONFIGURATION_LOCATION));
        } catch (IOException e) {
            throw new IllegalArgumentException("can't find properties config file with location [" + CONFIGURATION_LOCATION + "]");
        }

    }

    public Configuration(Properties properties) {
        this.properties = properties;
    }

    public void init() {
        this.config = new DefaultConfig(properties);
        this.scanner = new DefaultScanner();
        this.scanner.scan(this.config.getPackageNames());
        this.expressionParser = ExtensionFactory.INSTANCE.getInstance(ExpressionParser.class, this.config.getExpressionEngineType());
        this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, this.config.getStoreType());
    }
}