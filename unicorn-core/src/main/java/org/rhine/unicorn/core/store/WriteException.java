package org.rhine.unicorn.core.store;

public class WriteException extends RuntimeException {

    private String message;

    public WriteException(String message) {
        this.message = message;
    }

}
