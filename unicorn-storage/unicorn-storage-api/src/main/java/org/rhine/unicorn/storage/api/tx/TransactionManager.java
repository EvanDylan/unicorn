package org.rhine.unicorn.storage.api.tx;

public interface TransactionManager {

    Transaction getCurrentTransaction();

    boolean isTransactionActive();

    Transaction beginTransaction(Resource resource) throws Exception;

    void endTransaction() throws Exception;

    void commit() throws Exception;

    void rollback() throws Exception;

}
