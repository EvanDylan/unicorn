package org.rhine.unicorn.spring.config;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.extension.SPI;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SPI(name = "spring", order = 1)
public class SpringConfig implements Config, EnvironmentAware {

    private Environment environment;

    private Object dataSource;

    private String applicationName;

    private String elParseEngine;

    private String storeType;

    private String serialization;

    private String[] packageNames;

    @Override
    public void init() {

    }

    @Override
    public Object getDataSource() {
        return dataSource;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public String getElParseEngine() {
        return elParseEngine;
    }

    @Override
    public String getStoreType() {
        return storeType;
    }

    @Override
    public String getSerialization() {
        return serialization;
    }

    @Override
    public List<String> getPackageNames() {
        if (packageNames == null) {
            return new ArrayList<>();
        }
        return Arrays.asList(packageNames);
    }

    @Override
    public <T> T getValue(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    @Override
    public String getStringValue(String key) {
        return environment.getProperty(key);
    }

    public void setDataSource(Object dataSource) {
        this.dataSource = dataSource;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setElParseEngine(String elParseEngine) {
        this.elParseEngine = elParseEngine;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public void setSerialization(String serialization) {
        this.serialization = serialization;
    }

    public void setPackageNames(String[] packageNames) {
        this.packageNames = packageNames;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
