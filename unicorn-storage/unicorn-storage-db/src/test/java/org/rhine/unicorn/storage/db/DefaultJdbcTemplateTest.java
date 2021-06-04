package org.rhine.unicorn.storage.db;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class DefaultJdbcTemplateTest {

    private static DataSource dataSource;

    @BeforeClass
    public static void initDataSource() throws SQLException {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:unicorn;DB_CLOSE_DELAY=-1;MODE=MYSQL");
        jdbcDataSource.setUser("sa");
        jdbcDataSource.setPassword("password");
        Connection conn = jdbcDataSource.getConnection();
        conn.createStatement().execute("CREATE TABLE `message`\n" +
                "(\n" +
                "    `id`           bigint(20)   NOT NULL AUTO_INCREMENT,\n" +
                "    `service_name` varchar(128) DEFAULT '',\n" +
                "    `name`         varchar(128) NOT NULL,\n" +
                "    `key`          varchar(128) NOT NULL,\n" +
                "    `response`     blob,\n" +
                "    `created_time` datetime(3)  NOT NULL,\n" +
                "    `expired_time` datetime(3)  NOT NULL,\n" +
                "    PRIMARY KEY (`id`),\n" +
                "    KEY `domain_value_index` (`service_name`, `name`, `key`, `expired_time`) USING BTREE\n" +
                ") ENGINE = InnoDB\n" +
                "  AUTO_INCREMENT = 8\n" +
                "  DEFAULT CHARSET = utf8mb4;");
        conn.close();
        dataSource = jdbcDataSource;
    }

    @Test
    public void query() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void update() {
    }
}