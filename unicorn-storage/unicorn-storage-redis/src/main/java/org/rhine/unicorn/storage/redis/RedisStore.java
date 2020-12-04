package org.rhine.unicorn.storage.redis;

import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Store;
import org.rhine.unicorn.core.store.WriteException;

public class RedisStore implements Store {

    @Override
    public void init() {

    }

    @Override
    public long write(Message message) throws WriteException {
        return 0;
    }

    @Override
    public Message randomAccess(Message message) throws ReadException {
        return null;
    }
}
