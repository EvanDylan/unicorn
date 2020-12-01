package org.rhine.unicorn.storage.db;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MessagePO implements Serializable {

    private static final long serialVersionUID = -3959232781480959314L;

    private Long id;

    private String system;

    private String name;

    private String key;

    private byte[] response;

    private long createdTimestamp;

    private long expiredTimestamp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getExpiredTimestamp() {
        return expiredTimestamp;
    }

    public void setExpiredTimestamp(long expiredTimestamp) {
        this.expiredTimestamp = expiredTimestamp;
    }

    @Override
    public String toString() {
        return "MessagePO{" +
                "id=" + id +
                ", system='" + system + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", response=" + Arrays.toString(response) +
                ", createdTimestamp=" + createdTimestamp +
                ", expiredTimestamp=" + expiredTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessagePO messagePO = (MessagePO) o;
        return Objects.equals(id, messagePO.id) &&
                Objects.equals(system, messagePO.system) &&
                Objects.equals(name, messagePO.name) &&
                Objects.equals(key, messagePO.key) &&
                Arrays.equals(response, messagePO.response) &&
                Objects.equals(createdTimestamp, messagePO.createdTimestamp) &&
                Objects.equals(expiredTimestamp, messagePO.expiredTimestamp);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, system, name, key, createdTimestamp, expiredTimestamp);
        result = 31 * result + Arrays.hashCode(response);
        return result;
    }
}
