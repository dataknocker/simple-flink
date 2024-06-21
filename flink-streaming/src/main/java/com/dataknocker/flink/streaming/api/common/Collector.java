package com.dataknocker.flink.streaming.api.common;

public interface Collector<T> {

    void collect(T record);

    void close();
}
