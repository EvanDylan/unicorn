package org.rhine.unicorn.storage.db;

public class DefaultSqlStatement {

    private static final String TABLE_NAME = "message";
    private static final String INSERT_SQL = String.format("INSERT INTO %s (`service_name`, `name`, `key`, `response`, `created_time`, `expired_time`) VALUES (?, ?, ?, ?, ?, ?);", TABLE_NAME);
    private static final String QUERY_SQL = String.format("SELECT * FROM %s WHERE `service_name` = ? and `name` = ? and `key` = ? and `expired_time` >= ? ;", TABLE_NAME);
    private static final String DELETE_SQL = String.format("DELETE FROM %s WHERE `service_name` = ? and `name` = ? and `key` = ? ;", TABLE_NAME);
    private static final String DELETE_SQL_BY_ID = String.format("DELETE FROM %s WHERE `id` = ? ;", TABLE_NAME);

    public String getInsertSql() {
        return INSERT_SQL;
    }

    public String getQuerySql() {
        return QUERY_SQL;
    }

    public String getDeleteSql() {
        return DELETE_SQL_BY_ID;
    }

}
