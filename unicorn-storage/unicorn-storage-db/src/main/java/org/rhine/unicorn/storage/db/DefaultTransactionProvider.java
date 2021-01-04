package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.extension.Initializing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DefaultTransactionProvider implements TransactionProvider, Initializing<DataSource> {

    private DataSource dataSource;

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void closeConnection(Connection connection) throws SQLException {

    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void inject(DataSource object) {

    }
}
