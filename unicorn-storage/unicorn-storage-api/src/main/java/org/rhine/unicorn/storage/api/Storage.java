package org.rhine.unicorn.storage.api;

import java.util.concurrent.TimeUnit;

public interface Storage {

    void write(String key, int duration, TimeUnit timeUnit) throws WriteExcetion;

}
