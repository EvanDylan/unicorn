package org.rhine.unicorn.storage.file;

import org.rhine.unicorn.storage.api.Message;
import org.rhine.unicorn.storage.api.ReadException;
import org.rhine.unicorn.storage.api.Store;
import org.rhine.unicorn.storage.api.WriteExcetion;

public class FileStore implements Store {

    @Override
    public long write(Message message) throws WriteExcetion {
        return 0;
    }

    @Override
    public Message randomAccess(Message message) throws ReadException {
        return null;
    }
}
