package org.rhine.unicorn.core.store;

public interface Storage {

    long write(RecordLog record) throws WriteException;

    RecordLog read(String applicationName, String name, String key) throws ReadException;

}
