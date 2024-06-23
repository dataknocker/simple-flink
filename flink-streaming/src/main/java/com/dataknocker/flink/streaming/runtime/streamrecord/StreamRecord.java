package com.dataknocker.flink.streaming.runtime.streamrecord;

/**
 * 数据记录表
 * @param <T>
 */
public class StreamRecord<T> extends StreamElement{

    private T value;
    private long timestamp;

    private boolean hasTimestamp;

    public StreamRecord(T value)
    {
        this.value = value;
        this.hasTimestamp = false;
    }

    public StreamRecord(T value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
        this.hasTimestamp = true;
    }

    public T getValue() {
        return value;
    }

    /**
     * 替换值，达到对象复用的效果
     * @param value
     * @return
     * @param <X>
     */
    public <X> StreamRecord<X> replace(X value) {
        this.value = (T)value;
        return (StreamRecord<X>) this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean equals(Object o) {
        if(o == this) {
            return true;
        } else if(o != null && getClass() == o.getClass()){
            StreamRecord<?> that = (StreamRecord<?>)o;
            return this.hasTimestamp == that.hasTimestamp
                    && (this.hasTimestamp && this.timestamp == that.timestamp)
                    && (this.value == null ? that.value == null : this.value.equals(that.value));

        }
        return false;
    }

    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (hasTimestamp ? (int)(timestamp ^ (timestamp >>> 32)) : 0);
        return result;
    }

    public String toString() {
        return "Record @ " + (hasTimestamp ? timestamp : "(undef)") + ":" +  value;
    }

}
