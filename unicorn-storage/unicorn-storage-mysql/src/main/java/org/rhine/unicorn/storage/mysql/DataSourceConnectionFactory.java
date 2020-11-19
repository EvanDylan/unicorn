package org.rhine.unicorn.storage.mysql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectionFactory {

    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public DataSourceConnectionFactory(DataSource dataSource) {
        dataSource = dataSource;
    }
}