package org.rhine.unicorn.core.interceptor;

public class IdempotentException extends RuntimeException {

    private String message;

    public IdempotentException(String message) {
        this.message = message;
    }

}
