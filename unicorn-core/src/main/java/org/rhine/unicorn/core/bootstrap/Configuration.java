package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.expression.ExpressionEngine;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.interceptor.ProxyFactory;
import org.rhine.unicorn.core.metadata.DefaultScanner;
import org.rhine.unicorn.core.metadata.Scanner;
import org.rhine.unicorn.core.serialize.Serialization;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.utils.StringUtils;

/**
 * config info
 */
public class Configuration {

    private final Config config;
    private final ExpressionEngine expressionEngine;
    private final Storage storage;
    private final Scanner scanner = new DefaultScanner();
    private final Serialization serialization;

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

    public Configuration() {
        this.config = ExtensionFactory.INSTANCE.getInstance(Config.class);
        this.scanner.scan(this.config.getPackageNames());
        if (StringUtils.isEmpty(this.config.getElParseEngine())) {
            this.expressionEngine = ExtensionFactory.INSTANCE.getInstance(ExpressionEngine.class);
        } else {
            this.expressionEngine = ExtensionFactory.INSTANCE.getInstance(ExpressionEngine.class, this.config.getElParseEngine());
        }
        if (StringUtils.isEmpty(this.config.getStoreType())) {
            this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class);
        } else {
            this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, this.config.getStoreType());
        }
        if (StringUtils.isEmpty(this.config.getSerialization())) {
            this.serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class);
        } else {
            this.serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class, this.config.getSerialization());
        }
    }

    public Configuration(Config config) {
        this.config = config;
        this.scanner.scan(this.config.getPackageNames());
        if (StringUtils.isEmpty(this.config.getElParseEngine())) {
            this.expressionEngine = ExtensionFactory.INSTANCE.getInstance(ExpressionEngine.class);
        } else {
            this.expressionEngine = ExtensionFactory.INSTANCE.getInstance(ExpressionEngine.class, this.config.getElParseEngine());
        }
        if (StringUtils.isEmpty(this.config.getStoreType())) {
            this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class);
        } else {
            this.storage = ExtensionFactory.INSTANCE.getInstance(Storage.class, this.config.getStoreType());
        }
        if (StringUtils.isEmpty(this.config.getSerialization())) {
            this.serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class);
        } else {
            this.serialization = ExtensionFactory.INSTANCE.getInstance(Serialization.class, this.config.getSerialization());
        }
    }
}