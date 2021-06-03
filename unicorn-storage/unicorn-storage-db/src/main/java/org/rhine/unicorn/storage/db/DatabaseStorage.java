package org.rhine.unicorn.storage.db;


import org.rhine.unicorn.core.extension.LazyInitializing;
import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.RecordLog;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.store.WriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

@SPI(name = "db")
public class DatabaseStorage implements Storage, LazyInitializing<JdbcTemplate> {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseStorage.class);

    private JdbcTemplate jdbcTemplate;

    @Override
    public long write(RecordLog record) throws WriteException {
        if (record == null) {
            throw new WriteException("record can't be null");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("store " + record);
        }
        if (record.hasOffset()) {
            return jdbcTemplate.update(new Timestamp(record.getExpireMillis()), record.getOffset());
        } else {
            return jdbcTemplate.insert(record);
        }
    }

    @Override
    public RecordLog read(String applicationName, String name, String key) throws ReadException {
        return jdbcTemplate.query(applicationName, name, key);
    }

    @Override
    public void inject(JdbcTemplate object) {
        this.jdbcTemplate = object;
    }
}
