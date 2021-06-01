package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.config.Config;
import org.rhine.unicorn.core.extension.ExtensionFactory;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.store.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

@SPI
public class DefaultJdbcTemplate implements JdbcTemplate {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJdbcTemplate.class);

    private final DefaultSqlStatement defaultSqlStatement;

    @Override
    public Record query(Object... args) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = preparedStatement(this.defaultSqlStatement.getQuerySql(), args);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Record record = new Record();
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
    public long insert(Record record) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet= null;
        try {
            Object[] args = {record.getFlag(), record.getApplicationName(), record.getName(), record.getKey(),
                record.getClassName(), record.getResponse(), new Timestamp(record.getStoreTimestamp()),
                new Timestamp(record.getExpireMillis())};
            preparedStatement = preparedStatement(this.defaultSqlStatement.getInsertSql(), true, args);
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
    public long update(Object... args) throws WriteException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet= null;
        try {
            preparedStatement = preparedStatement(this.defaultSqlStatement.getUpdateSql(), false, args);
            return preparedStatement.executeUpdate();
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

    private PreparedStatement preparedStatement(String sql, Object... args) throws SQLException {
        return preparedStatement(sql, false, args);
    }

    private PreparedStatement preparedStatement(String sql, boolean returnGeneratedKeys, Object... args) throws SQLException {
        PreparedStatement preparedStatement;
        if (returnGeneratedKeys) {
            preparedStatement = getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            preparedStatement = getDataSource().getConnection().prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            preparedStatement.setObject(i + 1, args[i]);
        }
        return preparedStatement;
    }

    public DefaultJdbcTemplate() {
        this.defaultSqlStatement = new DefaultSqlStatement();
    }

    private DataSource getDataSource() {
        return ExtensionFactory.INSTANCE.getInstance(Config.class).getDataSource();
    }
}
