package org.rhine.unicorn.storage.db.tx;

import org.rhine.unicorn.storage.api.tx.Resource;
import org.rhine.unicorn.storage.api.tx.Transaction;

public class JdbcTransaction implements Transaction {

    private Resource resource;

    private int status;

    @Override
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
