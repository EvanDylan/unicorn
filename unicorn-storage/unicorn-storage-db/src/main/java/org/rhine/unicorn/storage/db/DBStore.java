package org.rhine.unicorn.storage.db;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Store;
import org.rhine.unicorn.core.store.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI(name = "db")
public class DBStore implements Store {

    private static final Logger logger = LoggerFactory.getLogger(DBStore.class);

    private DatabaseOperationTemplate databaseOperationTemplate;

    @Override
    public void init(Configuration configuration) {
        String jdbcUrl = configuration.getStringValue("unicorn.datasource.jdbcUrl");
        String username = configuration.getStringValue("unicorn.datasource.username");
        String password = configuration.getStringValue("unicorn.datasource.password");
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        databaseOperationTemplate = new DatabaseOperationTemplate(new HikariDataSource(hikariConfig));
    }

    @Override
    public long write(Message message) throws WriteException {
        if (message == null) {
            throw new WriteException("message can't be null");
        }
        MessagePO messagePO = new MessagePO();
        messagePO.setServiceName(message.getServiceName());
        messagePO.setName(message.getName());
        messagePO.setKey(message.getKey());
        long now = System.currentTimeMillis();
        messagePO.setCreatedTimestamp(now);
        messagePO.setExpiredTimestamp(now + message.getExpireMillis());
        if (logger.isDebugEnabled()) {
            logger.debug("store " + message.toString());
        }
        boolean result = databaseOperationTemplate.insertMessage(messagePO);
        if (!result) {
            throw new WriteException("store message failed," + message.toString());
        }
        return messagePO.getId();
    }

    @Override
    public Message randomAccess(String serviceName, String name, String key, Long expireMillis) throws ReadException {
        Message message = new Message();
        MessagePO messagePO = databaseOperationTemplate.findMessage(serviceName, name, key);
        if (messagePO != null) {
            message.setServiceName(serviceName);
            message.setKey(key);
            message.setName(name);
            message.setExpireMillis(expireMillis);
            message.setStoreTimestamp(messagePO.getCreatedTimestamp());
        }
        return messagePO == null ? null : message;
    }
}
