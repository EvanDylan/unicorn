package org.rhine.unicorn.springboot.properties;

import org.rhine.unicorn.spring.config.SpringConfig;
import org.rhine.unicorn.springboot.autoconfigure.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = Constants.UNICORN_CONFIG_PREFIX)
public class UnicornConfigurationProperties extends SpringConfig{


}
