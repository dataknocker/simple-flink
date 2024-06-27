package com.dataknocker.flink.streaming.api.functions.sink;


import com.dataknocker.flink.api.common.functions.Function;

public interface SinkFunction<T> extends Function {

    public void invoke(T value) throws Exception;
}
