package org.rhine.unicorn.core.store;

public interface Store {

    long write(Message message) throws WriteException;

    Message randomAccess(Message message) throws ReadException;

}
