package org.rhine.unicorn.core.store;

import java.io.Serializable;
import java.util.Objects;

public class Message implements Serializable {

    private static final long serialVersionUID = 2112380405948142230L;

    private String serviceName;

    private String name;

    private String key;

    private long expireMillis;

    private long storeTimestamp;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getExpireMillis() {
        return expireMillis;
    }

    public void setExpireMillis(long expireMillis) {
        this.expireMillis = expireMillis;
    }

    public long getStoreTimestamp() {
        return storeTimestamp;
    }

    public void setStoreTimestamp(long storeTimestamp) {
        this.storeTimestamp = storeTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return expireMillis == message.expireMillis &&
                storeTimestamp == message.storeTimestamp &&
                Objects.equals(serviceName, message.serviceName) &&
                Objects.equals(name, message.name) &&
                Objects.equals(key, message.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, name, key, expireMillis, storeTimestamp);
    }

    @Override
    public String toString() {
        return "Message{" +
                "system='" + serviceName + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", duration=" + expireMillis +
                ", storeTimestamp=" + storeTimestamp +
                '}';
    }
}
