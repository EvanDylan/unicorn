package org.rhine.unicorn.core.config;

import java.util.List;
import java.util.Properties;

public class GenericConfiguration implements Configuration{

    private Properties properties;

    private String storeType;

    private List<String> scanLocations;

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public String getStoreType() {
        return this.storeType;
    }

    @Override
    public List<String> getScanLocations() {
        return this.scanLocations;
    }
}
