package org.rhine.unicorn.core.config;

import java.util.List;

public interface Config {

    void init();

    Object getDataSource();

    String getApplicationName();

    String getElParseEngine();

    String getStoreType();

    String getSerialization();

    List<String> getPackageNames();

    <T> T getValue(String key, Class<T> targetType);

    String getStringValue(String key);
}
