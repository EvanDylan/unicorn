package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.WriteException;

import java.util.Objects;

public interface JdbcTemplate {

    Record query(Object... args) throws ReadException;

    long insert(Record record) throws WriteException;

    long update(Object... args) throws WriteException;

}
