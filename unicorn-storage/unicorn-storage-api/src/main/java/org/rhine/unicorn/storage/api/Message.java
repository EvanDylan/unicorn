package org.rhine.unicorn.storage.api;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Message implements Serializable {

    private static final long serialVersionUID = 2112380405948142230L;

    private String value;

    private int duration;

    private TimeUnit timeUnit;

    @Override
    public String toString() {
        return "Message{" +
                "value='" + value + '\'' +
                ", duration=" + duration +
                ", timeUnit=" + timeUnit +
                '}';
    }
}
