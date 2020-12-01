package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.utils.StringUtils;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.sql.*;

public class DatabaseOperationTemplate {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseOperationTemplate.class);

    private DataSource dataSource;

    private static final String TABLE_NAME = "message";

    public static final String CREATE_TABLE_MYSQL_DIALECT = "CREATE TABLE " + TABLE_NAME + "(\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `system` varchar(128) DEFAULT '',\n" +
            "  `name` varchar(128) NOT NULL,\n" +
            "  `key` varchar(128) NOT NULL,\n" +
            "  `response` blob,\n" +
            "  `created_time` datetime NOT NULL,\n" +
            "  `expired_time` datetime NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `domain_value_index` (`name`,`key`) USING BTREE\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

    private static final String INSERT_SQL = "INSERT INTO " + TABLE_NAME + " (`system`, `name`, `key`, `response`, `created_time`, `expired_time`) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String QUERY_SQL = "SELECT * FROM " + TABLE_NAME + " WHERE `system` = ? and `name` = ? and `key` = ? ;";

    private static final String DELETE_SQL = "DELETE FROM " + TABLE_NAME + " WHERE `system` = ? and `name` = ? and `key` = ? ;";

    private static final String DELETE_SQL_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE `id` = ? ;";

    public boolean insertMessage(final MessagePO messagePO) {
        return execute(new Execute<Boolean>() {
            @Override
            public Boolean doExecute(Connection connection) throws SQLException{
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, messagePO.getSystem());
                preparedStatement.setString(2, messagePO.getName());
                preparedStatement.setString(3, messagePO.getKey());
                preparedStatement.setBinaryStream(4, new ByteArrayInputStream(messagePO.getResponse()));
                preparedStatement.setTimestamp(5, new Timestamp(messagePO.getCreatedTimestamp()));
                preparedStatement.setTimestamp(6, new Timestamp(messagePO.getExpiredTimestamp()));
                if (preparedStatement.executeUpdate() == 0) {
                    throw new WriteException("create message failed");
                }
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    messagePO.setId(resultSet.getLong(1));
                } else {
                    throw new WriteException("create message failed, no id obtained.");
                }
                return true;
            }
        });
    }

    public MessagePO findMessage(String system, String name, String key) {
        return execute(new Execute<MessagePO>() {
            @Override
            public MessagePO doExecute(Connection connection) throws SQLException {
                if (StringUtils.isEmpty(system) || StringUtils.isEmpty(name) || StringUtils.isEmpty(key)) {
                    throw new ReadException("find message failed, " +
                            "system:[ + " + system + " +] or name:[" + name + "] or key:[" + key + "] is null");
                }
                PreparedStatement preparedStatement = connection.prepareStatement(QUERY_SQL);
                preparedStatement.setString(1, system);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, key);
                preparedStatement.executeQuery();
                ResultSet resultSet = preparedStatement.getResultSet();
                if (resultSet.next()) {
                    MessagePO messagePO = new MessagePO();
                    messagePO.setId(resultSet.getLong("id"));
                    messagePO.setSystem(resultSet.getString("system"));
                    messagePO.setName(resultSet.getString("name"));
                    messagePO.setKey(resultSet.getString("key"));
                    messagePO.setResponse(resultSet.getBytes("response"));
                    Timestamp createdTime = resultSet.getTimestamp("created_time");
                    if (createdTime != null) {
                        messagePO.setCreatedTimestamp(createdTime.getTime());
                    }
                    Timestamp expiredTime = resultSet.getTimestamp("expired_time");
                    if (expiredTime != null) {
                        messagePO.setExpiredTimestamp(expiredTime.getTime());
                    }
                    return messagePO;
                }
                return null;
            }
        });
    }

    public boolean deleteMessageById(Long id) {
        return execute(new Execute<Boolean>() {
            @Override
            public Boolean doExecute(Connection connection) throws SQLException{
                if (id == null) {
                    throw new ReadException("delete message failed, id is null");
                }
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL_BY_ID);
                preparedStatement.setLong(1, id);
                return preparedStatement.executeUpdate() > 0;
            }
        });
    }

    public boolean deleteMessage(String system, String name, String key) {
        return execute(new Execute<Boolean>() {
            @Override
            public Boolean doExecute(Connection connection) throws SQLException {
                if (StringUtils.isEmpty(system) || StringUtils.isEmpty(name) || StringUtils.isEmpty(key)) {
                    throw new ReadException("delete message failed, " +
                            "system:[ + " + system + " +] or name:[" + name + "] or key:[" + key + "] is null");
                }
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL);
                preparedStatement.setString(1, system);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, key);
                return preparedStatement.executeUpdate() > 0;
            }
        });
    }

    /**
     * provide sql execute template.
     * @param execute be execute sql.
     * @return execute result, success or failed
     */
    private <T> T execute(Execute<T> execute) throws RuntimeException {
        Connection connection = null;
        try {
            if (dataSource == null) {
                throw new DataSourceNotProvideException("datasource not provide");
            }
            connection = dataSource.getConnection();
            return execute.doExecute(connection);
        } catch (Exception e) {
            throw new RuntimeException("execute sql occur error", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException exception) {
                    logger.warn("database connection close occur exception");
                }
            }
        }
    }

    public DatabaseOperationTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private interface Execute<T> {

        T doExecute(Connection connection) throws Exception;

    }
}
