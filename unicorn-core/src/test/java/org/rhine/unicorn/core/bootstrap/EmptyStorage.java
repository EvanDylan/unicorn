package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.Message;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.store.WriteException;

@SPI(name = "empty")
public class EmptyStorage implements Storage {

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
