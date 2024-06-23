package com.dataknocker.flink.api.common.functions;

public interface FilterFunction<T> extends Function{
    public boolean filter(T value) throws Exception;
}
