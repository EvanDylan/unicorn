package org.rhine.unicorn.storage.redis;

import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.store.WriteException;

public class RedisStorage implements Storage {

    @Override
    public long write(Message message) throws WriteException {
        return 0;
    }

    @Override
    public Message read(String serviceName, String name, String key, Long expireMillis) throws ReadException {
        return null;
    }

    @Override
    public long writeIfAbsent(Message message) throws WriteException, ReadException {
        return 0;
    }
}
