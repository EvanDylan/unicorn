package org.rhine.unicorn.storage.db;


import org.rhine.unicorn.core.extension.Initializing;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.store.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;

@SPI(name = "db")
public class DatabaseStorage implements Storage, Initializing<JdbcTemplate> {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStorage.class);

    private JdbcTemplate jdbcTemplate;

    @Override
    public long write(Message message) throws WriteException {
        if (message == null) {
            throw new WriteException("message can't be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("store " + message.toString());
        }
        return jdbcTemplate.insert(message.getServiceName(), message.getName(), message.getKey(),
                message.getResponse(), new Time(message.getStoreTimestamp()), new Timestamp(message.getExpireMillis()));
    }

    @Override
    public Message read(String serviceName, String name, String key, Long expireMillis) throws ReadException {
        return jdbcTemplate.query(serviceName, name, key, new Timestamp(expireMillis));
    }

    @Override
    public long writeIfAbsent(Message message) throws WriteException, ReadException {
        message = this.read(message.getServiceName(), message.getName(), message.getKey(), message.getExpireMillis());
        return message == null ? 0 : this.write(message);
    }

    @Override
    public void inject(JdbcTemplate object) {
        this.jdbcTemplate = object;
    }
}
