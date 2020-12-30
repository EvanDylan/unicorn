package org.rhine.unicorn.core.proxy;

public class IdempotentException extends RuntimeException {

    private String message;

    public IdempotentException(String message) {
        this.message = message;
    }

}
