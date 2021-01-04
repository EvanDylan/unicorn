package org.rhine.unicorn.storage.db;

import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.WriteException;

public interface JdbcTemplate {

    long insert(Object... args) throws WriteException;

    Message query(Object... args) throws ReadException;

    long delete(Object... args) throws WriteException;

}
