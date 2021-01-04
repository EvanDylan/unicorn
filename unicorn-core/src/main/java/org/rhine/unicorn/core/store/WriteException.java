package org.rhine.unicorn.core.store;

import java.io.Writer;

public class WriteException extends RuntimeException {

    private String message;

    public WriteException(String message) {
        this.message = message;
    }

    public WriteException(String message, Exception e) {
        super(e);
        this.message = message;
    }

}
