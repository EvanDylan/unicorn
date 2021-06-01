package org.rhine.unicorn.core.config;

import javax.sql.DataSource;
import java.util.List;

public interface Config {

    void init();

    DataSource getDataSource();

    String getApplicationName();

    String getElParseEngine();

    String getStoreType();

    String getSerialization();

    List<String> getPackageNames();

    <T> T getValue(String key, Class<T> targetType);

    String getStringValue(String key);
}
