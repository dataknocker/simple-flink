package com.dataknocker.flink.streaming.api.common;

/**
 * flatmap function接口
 * @param <T>
 * @param <O>
 */
public interface FlatMapFunction<T, O> extends Function{

    void flatMap(T value, Collector<O> out) throws Exception;
}
