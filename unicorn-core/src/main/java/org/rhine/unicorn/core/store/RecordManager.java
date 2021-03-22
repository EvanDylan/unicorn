package org.rhine.unicorn.core.store;

public class RecordManager {



    public Record generate(String serviceName, Object value, String name, long remainingExpireMillis) {
        Record record = new Record();
        record.setServiceName(serviceName);
        record.setName(name);
        record.setKey(value.toString());
        long now = System.currentTimeMillis();
        record.setStoreTimestamp(now);
        record.setExpireMillis(now + remainingExpireMillis);
        return record;
    }
}
