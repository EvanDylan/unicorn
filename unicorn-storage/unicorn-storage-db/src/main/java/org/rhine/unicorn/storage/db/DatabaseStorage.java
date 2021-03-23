package org.rhine.unicorn.storage.db;


import org.rhine.unicorn.core.extension.LazyInitializing;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

@SPI(name = "db")
public class DatabaseStorage implements Storage, LazyInitializing<JdbcTemplate> {

    private static final String VOID_KEY = "default";

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStorage.class);

    private JdbcTemplate jdbcTemplate;

    @Override
    public long write(Record record) throws WriteException {
        if (record == null) {
            throw new WriteException("record can't be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("store " + record.toString());
        }
        return jdbcTemplate.insert(record);
    }

    @Override
    public Record read(String applicationName, String name, String key) throws ReadException {
        return jdbcTemplate.query(applicationName, name, key == null ? VOID_KEY : key);
    }

    @Override
    public long update(Record record) throws WriteException, ReadException {
        if (RecordFlag.isVoidReturnTypeFlag(record.getFlag())) {
            record.setKey(VOID_KEY);
        }
        return jdbcTemplate.update(record.getExpireMillis(), record.getOffset());
    }

    @Override
    public void inject(JdbcTemplate object) {
        this.jdbcTemplate = object;
    }
}
