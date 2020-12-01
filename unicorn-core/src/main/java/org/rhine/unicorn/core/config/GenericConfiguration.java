package org.rhine.unicorn.core.config;

import java.util.List;
import java.util.Properties;

public class GenericConfiguration implements Configuration{

    private Properties properties;

    private String storeType;

    private List<String> scanLocations;

    @Override
    public Object getValue(String key) {
        if (properties != null) {
            return properties.get(key);
        }
        return null;
    }

    @Override
    public String getStringValue(String key) {
        if (properties != null) {
            return properties.getProperty(key);
        }
        return null;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getStoreType() {
        return this.storeType;
    }

    @Override
    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    @Override
    public List<String> getScanLocations() {
        return this.scanLocations;
    }

    @Override
    public void setScanLocations(List<String> locations) {
        this.scanLocations = locations;
    }
}
