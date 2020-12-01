package org.rhine.unicorn.core.store;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Message implements Serializable {

    private static final long serialVersionUID = 2112380405948142230L;

    private String system;

    private String name;

    private String key;

    private int duration;

    private TimeUnit timeUnit;

    private long storeTimestamp;

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
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
        return duration == message.duration &&
                storeTimestamp == message.storeTimestamp &&
                Objects.equals(system, message.system) &&
                Objects.equals(name, message.name) &&
                Objects.equals(key, message.key) &&
                timeUnit == message.timeUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(system, name, key, duration, storeTimestamp, timeUnit);
    }

    @Override
    public String toString() {
        return "Message{" +
                "system='" + system + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", duration=" + duration +
                ", storeTimestamp=" + storeTimestamp +
                ", timeUnit=" + timeUnit +
                '}';
    }
}
