package org.rhine.unicorn.storage.db;

import java.sql.Connection;
import java.sql.SQLException;

interface TransactionProvider {

    Connection getConnection() throws SQLException;

    void closeConnection(Connection connection) throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

}
