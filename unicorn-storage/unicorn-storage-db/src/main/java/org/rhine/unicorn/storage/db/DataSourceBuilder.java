package org.rhine.unicorn.storage.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceBuilder {

    public static DataSource builderDataSource(JdbcConfig jdbcConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcConfig.getUrl());
        config.setUsername(jdbcConfig.getUsername());
        config.setPassword(jdbcConfig.getPassword());
        config.setMaximumPoolSize(jdbcConfig.getMaxPoolSize());
        config.setConnectionTimeout(jdbcConfig.getConnectionTimeout());
        return new HikariDataSource(config);
    }

}
