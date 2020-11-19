package org.rhine.unicorn.storage.api;

public interface Store {

    long write(Message message) throws WriteExcetion;

    Message randomAccess(Message message) throws ReadException;

}
