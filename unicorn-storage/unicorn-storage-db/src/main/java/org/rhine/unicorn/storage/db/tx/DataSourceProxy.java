package org.rhine.unicorn.storage.db.tx;

import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.storage.api.tx.TransactionManager;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DataSourceProxy implements DataSource {

    private DataSource targetDataSource;
    private TransactionManager transactionManager = ExtensionFactory.INSTANCE.getInstance(TransactionManager.class);

    public DataSource getPlainDataSource() {
        return targetDataSource;
    }

    public Connection getPlainConnection() throws SQLException {
        return targetDataSource.getConnection();
    }

    public Connection getPoxyConnection() throws SQLException {
        if (transactionManager.isTransactionActive()) {
            return (Connection) transactionManager.getCurrentTransaction().getResource();
        }
        return new ConnectionProxy(targetDataSource.getConnection(), true);
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (transactionManager.isTransactionActive()) {
            return (Connection) transactionManager.getCurrentTransaction().getResource();
        }
        return new ConnectionProxy(targetDataSource.getConnection());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        if (transactionManager.isTransactionActive()) {
            return (Connection) transactionManager.getCurrentTransaction().getResource();
        }
        return new ConnectionProxy(targetDataSource.getConnection(username, password));
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return targetDataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return targetDataSource.isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return targetDataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        targetDataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        targetDataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return targetDataSource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return targetDataSource.getParentLogger();
    }

    public DataSource getTargetDataSource() {
        return targetDataSource;
    }

    public void setTargetDataSource(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    public DataSourceProxy(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    public DataSourceProxy() {
    }
}
