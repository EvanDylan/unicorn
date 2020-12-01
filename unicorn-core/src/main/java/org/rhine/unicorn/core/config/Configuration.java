package org.rhine.unicorn.core.config;

import java.util.List;
import java.util.Properties;

/**
 * config info
 */
public interface Configuration {

    Object getValue(String key);

    String getStringValue(String key);

    /**
     * get properties
     * @return Properties
     */
    Properties getProperties();

    void setProperties(Properties properties);

    /**
     * get store type
     * @return store type
     */
    String getStoreType();

    void setStoreType(String storeType);

    /**
     * location of will be scan
     * @return the collection of location
     */
    List<String> getScanLocations();

    void setScanLocations(List<String> locations);
}