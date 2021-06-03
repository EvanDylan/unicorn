package org.rhine.unicorn.storage.api.tx;

public class TransactionException extends RuntimeException {

    private String message;

    public TransactionException(String message) {
        this.message = message;
    }

}
