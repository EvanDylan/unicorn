package org.rhine.unicorn.core.config;

import java.util.List;
import java.util.Properties;

/**
 * config info
 */
public interface Configuration {

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
     * location of been scan
     * @return the collection of location
     */
    List<String> getScanLocations();
}