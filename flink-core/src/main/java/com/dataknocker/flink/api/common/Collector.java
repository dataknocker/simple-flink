package com.dataknocker.flink.api.common;

public interface Collector<T> {

    void collect(T record);

    void close();
}
