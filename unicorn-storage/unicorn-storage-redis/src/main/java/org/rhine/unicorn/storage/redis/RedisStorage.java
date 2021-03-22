package org.rhine.unicorn.storage.redis;

import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.store.WriteException;

public class RedisStorage implements Storage {

    @Override
    public long write(Record record) throws WriteException {
        return 0;
    }

    @Override
    public Record read(String serviceName, String name, String key) throws ReadException {
        return null;
    }

    @Override
    public long update(Record record) throws WriteException, ReadException {
        return 0;
    }
}
