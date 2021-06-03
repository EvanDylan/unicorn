package org.rhine.unicorn.storage.db.tx;

import org.rhine.unicorn.storage.api.tx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcTransactionManager implements TransactionManager {

    private static Logger logger = LoggerFactory.getLogger(JdbcTransactionManager.class);

    @Override
    public Transaction getCurrentTransaction() {
        return TransactionHolder.getCurrentTransaction();
    }

    @Override
    public boolean isTransactionActive() {
        return TransactionHolder.getCurrentTransaction() != null;
    }

    @Override
    public Transaction beginTransaction(Resource resource) throws Exception {
        JdbcTransaction transaction = new JdbcTransaction();
        transaction.setResource(resource);
        TransactionHolder.setTransaction(transaction);
        return transaction;
    }

    @Override
    public void endTransaction() throws Exception {
        TransactionHolder.remove();
    }

    @Override
    public void commit() throws Exception {
        Transaction transaction = TransactionHolder.getCurrentTransaction();
        if (transaction == null) {
            logger.error("transaction is null, should never happened");
            throw new TransactionException("transaction not exist");
        }
        ConnectionProxy connection = (ConnectionProxy) transaction.getResource();
        if (connection.hasRollback()) {
            if (logger.isDebugEnabled()) {
                logger.debug("transaction already rollback, unicorn refuse commit record log");
            }
        } else {
            connection.plainCommit();
            if (logger.isDebugEnabled()) {
                logger.debug("current transaction has been commit");
            }
        }

    }

    @Override
    public void rollback() throws Exception {
        Transaction transaction = TransactionHolder.getCurrentTransaction();
        if (transaction == null) {
            logger.error("transaction is null, should never happened");
            throw new TransactionException("transaction not exist");
        }
        ((ConnectionProxy) transaction.getResource()).plainRollback();
        if (logger.isDebugEnabled()) {
            logger.debug("current transaction has been rollback");
        }
    }
}
