package org.rhine.unicorn.storage.api.tx;

import org.rhine.unicorn.core.store.Resource;

public interface Transaction {

    int PREPARE = 0;

    int COMMIT = 1;

    int ROLL_BACK = 2;

    int getStatus();

    Resource getResource();

}
