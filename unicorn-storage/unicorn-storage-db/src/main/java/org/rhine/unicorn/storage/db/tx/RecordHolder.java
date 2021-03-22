package org.rhine.unicorn.storage.db.tx;

import org.rhine.unicorn.core.store.Record;

public class RecordHolder {

    public static final RecordHolder INSTANCE = new RecordHolder();

    private ThreadLocal<Record> recordThreadLocal = new ThreadLocal<>();

    public Record getRecord() {
        return recordThreadLocal.get();
    }

    public void storeMessage(Record record) {
        recordThreadLocal.set(record);
    }

    public void remove() {
        recordThreadLocal.remove();
    }
}
