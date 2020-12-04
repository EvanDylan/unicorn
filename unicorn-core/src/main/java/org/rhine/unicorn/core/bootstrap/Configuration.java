package org.rhine.unicorn.core.bootstrap;

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

    /**
     * get store type
     * @return store type
     */
    String getStoreType();

    /**
     * location of will be scan
     * @return the collection of location
     */
    List<String> getScanLocations();
}