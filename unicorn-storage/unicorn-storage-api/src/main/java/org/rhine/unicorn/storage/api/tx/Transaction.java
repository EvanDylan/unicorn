package org.rhine.unicorn.storage.api.tx;

public interface Transaction {

    int PREPARE = 0;

    int COMMIT = 1;

    int ROLL_BACK = 2;

    int getStatus();

    Resource getResource();

}
