package org.rhine.unicorn.core.config;

import org.rhine.unicorn.core.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConfigurationFactory {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurationFactory.class);

    public Configuration resolve(Properties properties) {
        Configuration configuration = new Configuration(properties);
        String packageLocations = properties.getProperty("packageLocations");
        if (StringUtils.isEmpty(packageLocations)) {
            logger.error("properties packageLocations can't be null or empty");
        }
        return configuration;
    }

}
