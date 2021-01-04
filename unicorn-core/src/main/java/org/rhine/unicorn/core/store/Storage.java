package org.rhine.unicorn.core.store;

public interface Storage {

    long write(Message message) throws WriteException;

    Message read(String serviceName, String name, String key, Long expireMillis) throws ReadException;

    long writeIfAbsent(Message message) throws WriteException, ReadException;

}
