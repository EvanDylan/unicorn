package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.extension.Initializing;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SPI
public class DefaultJdbcTemplate implements JdbcTemplate, Initializing<TransactionProvider> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJdbcTemplate.class);

    private final DefaultSqlStatement defaultSqlStatement;
    private TransactionProvider transactionProvider;

    @Override
    public long insert(Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet= null;
        try {
            preparedStatement = preparedStatement(this.defaultSqlStatement.getInsertSql(), args);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            return resultSet.next() ? resultSet.getLong(1) : 0;
        } catch (SQLException e) {
            throw new WriteException("write error", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.getConnection().close();
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                // ignore it
            }
        }
    }

    @Override
    public Message query(Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(this.defaultSqlStatement.getQuerySql(), args);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Message message = new Message();
                message.setOffset(resultSet.getLong(1));
                message.setServiceName(resultSet.getString(2));
                message.setName(resultSet.getString(3));
                message.setKey(resultSet.getString(4));
                message.setResponse(resultSet.getBytes(5));
                message.setStoreTimestamp(resultSet.getTimestamp(6).getTime());
                message.setExpireMillis(resultSet.getTimestamp(7).getTime());
                return message;
            }
        } catch (SQLException e) {
            throw new ReadException("read error", e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.getConnection().close();
                    preparedStatement.close();
                }
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e) {
                // ignore it
            }
        }
        return null;
    }

    @Override
    public long delete(Object... args) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = preparedStatement(this.defaultSqlStatement.getDeleteSql(), args);
            return preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            try {
                if (preparedStatement != null) {
                    preparedStatement.getConnection().close();
                    preparedStatement.close();
                }
            } catch (Exception e) {
                // ignore it
            }
        }
        return 0;
    }

    private PreparedStatement preparedStatement(String sql, Object... args) throws SQLException {
        PreparedStatement preparedStatement = this.transactionProvider.getConnection().prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }
        return preparedStatement;
    }

    @Override
    public void inject(TransactionProvider object) {
        if (transactionProvider == null) {
            transactionProvider = object;
        }
    }

    public DefaultJdbcTemplate() {
        this.defaultSqlStatement = new DefaultSqlStatement();
    }
}
