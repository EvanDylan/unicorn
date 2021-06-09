package org.rhine.unicorn.springboot.autoconfigure;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.utils.StringUtils;
import org.rhine.unicorn.spring.proxy.IdempotentAdvisor;
import org.rhine.unicorn.springboot.properties.ApplicationProperties;
import org.rhine.unicorn.springboot.properties.UnicornConfigurationProperties;
import org.rhine.unicorn.storage.db.tx.DataSourceProxy;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(prefix = Constants.UNICORN_PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(UnicornConfigurationProperties.class)
@Import({UnicornComponentScanRegistrar.class, ApplicationProperties.class})
public class UnicornAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnMissingBean(Config.class)
    @ConditionalOnProperty(name = Constants.UNICORN_CONFIG_STORE_TYPE, matchIfMissing = true)
    public Config config(DataSource dataSource, UnicornConfigurationProperties config, ApplicationProperties applicationProperties) {
        config.setDataSource(dataSource);
        if (StringUtils.isEmpty(config.getApplicationName())) {
            config.setApplicationName(applicationProperties.getName());
        }
        return config;
    }
}
