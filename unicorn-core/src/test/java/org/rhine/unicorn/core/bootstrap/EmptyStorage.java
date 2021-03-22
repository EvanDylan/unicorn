package org.rhine.unicorn.core.bootstrap;

import org.rhine.unicorn.core.extension.SPI;
import org.rhine.unicorn.core.store.Record;
import org.rhine.unicorn.core.store.ReadException;
import org.rhine.unicorn.core.store.Storage;
import org.rhine.unicorn.core.store.WriteException;

@SPI(name = "empty")
public class EmptyStorage implements Storage {

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
