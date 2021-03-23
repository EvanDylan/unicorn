package org.rhine.unicorn.core.store;

public interface Storage {

    long write(Record record) throws WriteException;

    Record read(String applicationName, String name, String key) throws ReadException;

    long update(Record record) throws WriteException, ReadException;

}
