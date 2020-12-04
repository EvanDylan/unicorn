package org.rhine.unicorn.core.config;

import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.bootstrap.GenericConfiguration;

public class CustomizeConfiguration extends GenericConfiguration {

    private String customizeConfiguration;

    public String getCustomizeConfiguration() {
        return customizeConfiguration;
    }

    public void setCustomizeConfiguration(String customizeConfiguration) {
        this.customizeConfiguration = customizeConfiguration;
    }

    public CustomizeConfiguration(Configuration configuration) {

    }
}
