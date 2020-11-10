package org.rhine.unicorn.core.config;

import java.util.List;
import java.util.Properties;

public class Configuration {

    private Properties properties;

    private List<String> packageLocations;

    public List<String> getPackageLocations() {
        return packageLocations;
    }

    public Configuration(Properties properties) {
        this.properties = properties;
    }
}