package org.rhine.unicorn.core.config;

import java.util.List;

public interface Config {

    String SERVICE_NAME = "unicorn.servicename";
    String STORE_TYPE_STRING = "unicorn.storetype";
    String SCAN_LOCATIONS_STRING = "unicorn.scanlocations";
    String EXPRESSION_ENGINE_TYPE_STRING = "unicorn.expressionengine";
    String JDBC_URL = "unicorn.datasource.jdbcUrl";
    String JDBC_USERNAME = "unicorn.datasource.username";
    String JDBC_PASSWORD = "unicorn.datasource.password";
    String JDBC_MAX_POOL_SIZE = "unicorn.datasource.maxPoolSize";
    String JDBC_CONNECTION_TIMEOUT = "unicorn.datasource.connectionTimeout";

    void setConfigProperties(Object o);

    String getServiceName();

    String getExpressionEngineType();

    /**
     * get store type
     *
     * @return store type
     */
    String getStoreType();

    /**
     * location of will be scan
     *
     * @return the collection of package name
     */
    List<String> getPackageNames();

    Object getValue(String key);

    String getStringValue(String key);
}
