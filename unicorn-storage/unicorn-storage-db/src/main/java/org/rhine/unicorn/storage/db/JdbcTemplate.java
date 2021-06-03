package org.rhine.unicorn.storage.db;


import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.RecordLog;
import org.rhine.unicorn.core.store.WriteException;

public interface JdbcTemplate {

    RecordLog query(Object... args) throws ReadException;

    long insert(RecordLog record) throws WriteException;

    long update(Object... args) throws WriteException;

}
