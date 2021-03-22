package org.rhine.unicorn.storage.db.tx;

interface TransactionProvider {

    Object beginTransaction() throws Throwable;

    void endTransaction(Object o) throws Throwable;

    void commit() throws Throwable;

    void rollback() throws Throwable;

}
