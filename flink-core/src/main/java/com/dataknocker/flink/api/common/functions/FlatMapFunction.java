package com.dataknocker.flink.api.common.functions;

import com.dataknocker.flink.api.common.Collector;

/**
 * flatmap function接口
 * @param <T>
 * @param <O>
 */
public interface FlatMapFunction<T, O> extends Function {

    void flatMap(T value, Collector<O> out) throws Exception;
}
