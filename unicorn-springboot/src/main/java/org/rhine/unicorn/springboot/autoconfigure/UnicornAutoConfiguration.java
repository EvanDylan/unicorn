package org.rhine.unicorn.springboot.autoconfigure;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.springboot.properties.UnicornConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = Constants.UNICORN_PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(UnicornConfigurationProperties.class)
@Import(UnicornComponentScanRegistrar.class)
public class UnicornAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(Config.class)
    @ConditionalOnProperty(value = Constants.UNICORN_CONFIG_STORE_TYPE, havingValue = "db")
    public Config config(DataSource dataSource, UnicornConfigurationProperties config) {
        config.setDataSource(dataSource);
        return config;
    }
}
