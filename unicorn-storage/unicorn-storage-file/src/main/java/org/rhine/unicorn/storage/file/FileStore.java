package org.rhine.unicorn.storage.file;

import org.rhine.unicorn.core.bootstrap.Configuration;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Store;
import org.rhine.unicorn.core.store.WriteException;

public class FileStore implements Store {

    @Override
    public void init(Configuration configuration) {

    }

    @Override
    public long write(Message message) throws WriteException {
        return 0;
    }

    @Override
    public Message randomAccess(String serviceName, String name, String key, Long expireMillis) throws ReadException {
        return null;
    }
}
