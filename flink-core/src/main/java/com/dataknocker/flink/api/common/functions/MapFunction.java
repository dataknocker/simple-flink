package com.dataknocker.flink.api.common.functions;

import com.dataknocker.flink.api.common.Collector;

public interface MapFunction<T, O> extends Function{
    public void map(T value, Collector<O> out) throws Exception;
}
