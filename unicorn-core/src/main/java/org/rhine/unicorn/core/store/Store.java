package org.rhine.unicorn.core.store;

import org.rhine.unicorn.core.bootstrap.Configuration;

public interface Store {

    void init(Configuration configuration);

    long write(Message message) throws WriteException;

    Message randomAccess(String serviceName, String name, String key, Long expireMillis) throws ReadException;

}
