package org.rhine.unicorn.core.store;

import org.rhine.unicorn.core.utils.TimeUtils;

import java.io.Serializable;
import java.util.Objects;

public class RecordLog implements Serializable {

    private static final long serialVersionUID = 2112380405948142230L;

    /**
     * 消息在存储引擎中偏移地址
     */
    private long offset = -1;

    /**
     * 消息标志位
     * <p>
     *     1. 记录被拦截方式是否{@link java.lang.Void} 返回类型
     *     2. 记录方法返回值序列化类型{@link org.rhine.unicorn.core.serialize.Serialization}
     * </p>
     */
    private long flag;

    private String applicationName;

    private String name;

    private String key;

    /**
     * 过期时间
     */
    private long expireMillis;

    /**
     * 存储时间戳
     */
    private long storeTimestamp;

    /**
     * 被拦截方法返回类的全限定名
     */
    private String className;

    /**
     * 被拦截方法返回值序列化二进制数据
     */
    private byte[] response;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
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

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public boolean hasOffset() {
        return offset != -1;
    }

    public boolean hasExpired() {
        return this.getExpireMillis() < TimeUtils.getNow();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecordLog record = (RecordLog) o;
        return offset == record.offset &&
                flag == record.flag &&
                expireMillis == record.expireMillis &&
                storeTimestamp == record.storeTimestamp &&
                Objects.equals(applicationName, record.applicationName) &&
                Objects.equals(name, record.name) &&
                Objects.equals(key, record.key) &&
                Objects.equals(className, record.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offset, flag, applicationName, name, key, expireMillis, storeTimestamp, className);
    }

    @Override
    public String toString() {
        return "Record{" +
                "offset=" + offset +
                ", flag=" + flag +
                ", serviceName='" + applicationName + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                ", expireMillis=" + expireMillis +
                ", storeTimestamp=" + storeTimestamp +
                ", className='" + className + '\'' +
                '}';
    }
}
