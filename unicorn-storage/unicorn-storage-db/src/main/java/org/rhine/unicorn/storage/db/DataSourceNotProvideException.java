package org.rhine.unicorn.storage.db;

public class DataSourceNotProvideException extends RuntimeException{

    private String message;

    public DataSourceNotProvideException(String message) {
        this.message = message;
    }
}
