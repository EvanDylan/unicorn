package org.rhine.unicorn.storage.db;

public class DefaultSqlStatement {

    private static final String TABLE_NAME = "record";
    private static final String INSERT_SQL = String.format("INSERT INTO %s (`flag`, `application_name`, `name`, `key`, `class_name`, `response`, `created_time`, `expired_time`) VALUES (?, ?, ?, ?, ?, ?, ?, ?);", TABLE_NAME);
    private static final String UPDATE_SQL = String.format("UPDATE %s SET `expired_time` = ? WHERE `id` = ? ;", TABLE_NAME);
    private static final String QUERY_SQL = String.format("SELECT * FROM %s WHERE `application_name` = ? and `name` = ? and `key` = ? ;", TABLE_NAME);
    private static final String DELETE_SQL = String.format("DELETE FROM %s WHERE `application_name` = ? and `name` = ? and `key` = ? ;", TABLE_NAME);
    private static final String DELETE_SQL_BY_ID = String.format("DELETE FROM %s WHERE `id` = ? ;", TABLE_NAME);

    public String getInsertSql() {
        return INSERT_SQL;
    }

    public String getUpdateSql() {
        return UPDATE_SQL;
    }

    public String getQuerySql() {
        return QUERY_SQL;
    }

    public String getDeleteSql() {
        return DELETE_SQL_BY_ID;
    }

}
