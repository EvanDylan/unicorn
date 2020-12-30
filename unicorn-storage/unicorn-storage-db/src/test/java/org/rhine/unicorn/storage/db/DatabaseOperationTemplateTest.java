package org.rhine.unicorn.storage.db;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(JUnit4.class)
public class DatabaseOperationTemplateTest {

    private static DataSource dataSource;

    @BeforeClass
    public static void initDataSource() throws SQLException {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:unicorn;DB_CLOSE_DELAY=-1;MODE=MYSQL");
        jdbcDataSource.setUser("sa");
        jdbcDataSource.setPassword("password");
        Connection conn = jdbcDataSource.getConnection();
        conn.createStatement().execute(DatabaseOperationTemplate.CREATE_TABLE_MYSQL_DIALECT);
        conn.close();
        dataSource = jdbcDataSource;
    }

    @Before
    public void insertMessage() {
        DatabaseOperationTemplate template = new DatabaseOperationTemplate(dataSource);
        MessagePO messagePO = new MessagePO();
        messagePO.setExpiredTimestamp(System.currentTimeMillis());
        messagePO.setExpiredTimestamp(System.currentTimeMillis());
        messagePO.setResponse("META-INF/unicorn".getBytes());
        messagePO.setServiceName("META-INF/unicorn");
        messagePO.setName("2");
        messagePO.setKey("1");
        Assert.assertTrue(template.insertMessage(messagePO));
    }

    @Test
    public void deleteMessage() {
        DatabaseOperationTemplate template = new DatabaseOperationTemplate(dataSource);
        Assert.assertTrue(template.deleteMessage("META-INF/unicorn", "2", "1"));
    }

    @Test
    public void deleteMessageById() {
        DatabaseOperationTemplate template = new DatabaseOperationTemplate(dataSource);
        Assert.assertTrue(template.deleteMessageById(1L));
    }

    @Test
    public void findMessage() {
        DatabaseOperationTemplate template = new DatabaseOperationTemplate(dataSource);
        MessagePO messagePO = template.findMessage("META-INF/unicorn", "2", "1");
        Assert.assertNotNull(messagePO);
    }

}