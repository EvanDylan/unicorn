package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.extension.ExtensionFactory;

public class JdbcConfig {

    public Config config = ExtensionFactory.INSTANCE.getInstance(Config.class);

    private String url;

    private String username;

    private String password;

    private int maxPoolSize = 10;

    private int connectionTimeout = 10000;

    public String getUrl() {
        return config.getStringValue(config.JDBC_URL);
    }

    public String getUsername() {
        return config.getStringValue(config.JDBC_USERNAME);
    }

    public String getPassword() {
        return config.getStringValue(config.JDBC_PASSWORD);
    }

    public int getMaxPoolSize() {
        Object maxPoolSizeObj = config.getValue(config.JDBC_MAX_POOL_SIZE);
        return maxPoolSizeObj == null ? maxPoolSize : (int) maxPoolSizeObj;
    }

    public int getConnectionTimeout() {
        Object connectionTimeoutObj = config.getValue(config.JDBC_CONNECTION_TIMEOUT);
        return connectionTimeoutObj == null ? connectionTimeout : (int) connectionTimeoutObj;
    }
}
