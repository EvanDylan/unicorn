package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.RecordLog;
import org.rhine.unicorn.core.store.WriteException;
import org.rhine.unicorn.storage.api.tx.Resource;
import org.rhine.unicorn.storage.api.tx.TransactionManager;
import org.rhine.unicorn.storage.db.tx.DataSourceProxy;
import org.rhine.unicorn.storage.db.tx.JdbcTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

@SPI
public class DefaultJdbcTemplate implements JdbcTemplate {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJdbcTemplate.class);

    private final DefaultSqlStatement defaultSqlStatement;

    private final TransactionManager transactionManager;

    @Override
    public RecordLog query(Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = preparedStatement(connection, this.defaultSqlStatement.getQuerySql(), args);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                RecordLog record = new RecordLog();
                record.setOffset(resultSet.getLong(1));
                record.setFlag(resultSet.getLong(2));
                record.setApplicationName(resultSet.getString(3));
                record.setName(resultSet.getString(4));
                record.setKey(resultSet.getString(5));
                record.setClassName(resultSet.getString(6));
                record.setResponse(resultSet.getBytes(7));
                record.setStoreTimestamp(resultSet.getTimestamp(8).getTime());
                record.setExpireMillis(resultSet.getTimestamp(9).getTime());
                return record;
            }
        } catch (SQLException e) {
            logger.error("read record log error", e);
            throw new ReadException("read error", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                releaseConnection(connection);
            } catch (Exception e) {
                // ignore it
            }
        }
        return null;
    }

    @Override
    public long insert(RecordLog record) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet= null;
        try {
            connection = getConnection();
            transactionManager.beginTransaction((Resource) connection);
            Object[] args = {record.getFlag(), record.getApplicationName(), record.getName(), record.getKey(),
                record.getClassName(), record.getResponse(), new Timestamp(record.getStoreTimestamp()),
                new Timestamp(record.getExpireMillis())};
            preparedStatement = preparedStatement(connection, this.defaultSqlStatement.getInsertSql(), true, args);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            long offset = resultSet.next() ? resultSet.getLong(1) : -1;
            if (offset < 0) {
                logger.error("write record log failed recordLog[{}]", record.toString());
                transactionManager.rollback();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("write record log complete");
                }
                transactionManager.commit();
            }
            return offset;
        } catch (Exception e) {
            logger.error("write record log error recordLog[{}]", record.toString(), e);
            try {
                transactionManager.rollback();
            } catch (Exception exception) {
                logger.error("transaction rollback error recordLog[{}]", record.toString(), e);
            }
            throw new WriteException("write error", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
                transactionManager.endTransaction();
                releaseConnection(connection);
            } catch (Exception e) {
                // ignore it
            }
        }
    }

    @Override
    public long update(Object... args) throws WriteException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            transactionManager.beginTransaction((Resource) connection);
            preparedStatement = preparedStatement(connection, this.defaultSqlStatement.getUpdateSql(), false, args);
            int rows = preparedStatement.executeUpdate();
            if (rows <= 0) {
                logger.error("write record log failed recordLog args[]", args);
                transactionManager.rollback();
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("write record log complete");
                }
                transactionManager.commit();
            }
            return (long) args[1];
        } catch (Exception e) {
            logger.error("write record log failed recordLog args[]", args);
            try {
                transactionManager.rollback();
            } catch (Exception exception) {
                logger.error("write record log failed recordLog args[]", args);
            }
            throw new WriteException("write error", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                transactionManager.endTransaction();
                releaseConnection(connection);
            } catch (Exception e) {
                // ignore it
            }
        }
    }

    private PreparedStatement preparedStatement(Connection connection, String sql, Object... args) throws SQLException {
        return preparedStatement(connection, sql, false, args);
    }

    private PreparedStatement preparedStatement(Connection connection, String sql, boolean returnGeneratedKeys, Object... args) throws SQLException {
        PreparedStatement preparedStatement;
        if (returnGeneratedKeys) {
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            preparedStatement = connection.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }
        return preparedStatement;
    }

    public DefaultJdbcTemplate() {
        this.defaultSqlStatement = new DefaultSqlStatement();
        this.transactionManager = new JdbcTransactionManager();
        ExtensionFactory.INSTANCE.getContainer().register(TransactionManager.class, transactionManager);
    }

    private Connection getConnection() throws SQLException {
        DataSource dataSource = ExtensionFactory.INSTANCE.getInstance(Config.class).getDataSource();
        if (dataSource == null) {
            throw new DataSourceNotProvideException();
        }
        if (!(dataSource instanceof DataSourceProxy)) {
            dataSource = new DataSourceProxy(dataSource);
        }
        Connection connection = ((DataSourceProxy) dataSource).getPoxyConnection();
        if (!connection.getAutoCommit()) {
            connection.setAutoCommit(false);
        }
        return connection;
    }

    private void releaseConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
