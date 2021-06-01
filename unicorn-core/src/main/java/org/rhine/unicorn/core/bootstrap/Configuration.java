package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.interceptor.ProxyFactory;
import org.rhine.unicorn.core.metadata.DefaultScanner;
import org.rhine.unicorn.core.metadata.Scanner;
import org.rhine.unicorn.core.serialize.Serialization;
import org.rhine.unicorn.core.store.Storage;

/**
 * config info
 */
public class Configuration {

    private Config config;
    private ExpressionEngine expressionEngine;
    private Storage storage;
    private Scanner scanner;
    private ProxyFactory proxyFactory;
    private Serialization serialization;

    public Config getConfig() {
        return config;
    }

    public ExpressionEngine getExpressionEngine() {
        return expressionEngine;
    }

    public Storage getStorage() {
        return storage;
    }

    public Serialization getSerialization() {
        return serialization;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public <T> T getValue(String key, Class<T> targetType) {
        return this.config.getValue(key, targetType);
    }

    public String getStringValue(String key) {
        return this.config.getStringValue(key);
    }

    public Object getProxyObject(Object targetObject) {
        return proxyFactory.createProxy(targetObject, this);
    }

    public Configuration() {
        this.config = ExtensionFactory.INSTANCE.getInstance(Config.class);
        this.scanner = new DefaultScanner();
        this.scanner.scan(this.config.getPackageNames());
        this.expressionEngine = ExtensionFactory.INSTANCE.getInstance(ExpressionEngine.class, this.config.getElParseEngine());
        this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, this.config.getStoreType());
        this.proxyFactory = ExtensionFactory.INSTANCE.getInstance(ProxyFactory.class);
    }

    public Configuration(Config config) {
        this.config = config;
        this.scanner = new DefaultScanner();
        this.scanner.scan(this.config.getPackageNames());
        this.expressionEngine = ExtensionFactory.INSTANCE.getInstance(ExpressionEngine.class, this.config.getElParseEngine());
        this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, this.config.getStoreType());
        this.serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class, this.config.getSerialization());
        this.proxyFactory = ExtensionFactory.INSTANCE.getInstance(ProxyFactory.class);
    }
}