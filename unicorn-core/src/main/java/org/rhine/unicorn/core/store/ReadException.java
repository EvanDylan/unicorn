package org.rhine.unicorn.core.store;

public class ReadException extends RuntimeException {

    private String message;

    public ReadException(String message) {
        this.message = message;
    }

}
