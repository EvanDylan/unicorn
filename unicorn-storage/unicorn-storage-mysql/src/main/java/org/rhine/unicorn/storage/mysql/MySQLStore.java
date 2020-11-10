package org.rhine.unicorn.storage.mysql;


import org.rhine.unicorn.storage.api.Message;
import org.rhine.unicorn.storage.api.ReadException;
import org.rhine.unicorn.storage.api.Store;
import org.rhine.unicorn.storage.api.WriteExcetion;

public class MySQLStore implements Store {

    @Override
    public int write(Message message) throws WriteExcetion {
        return 0;
    }

    @Override
    public Message randomAccess(Message message) throws ReadException {
        return null;
    }
}
