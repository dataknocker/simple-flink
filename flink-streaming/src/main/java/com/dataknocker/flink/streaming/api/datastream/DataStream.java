package com.dataknocker.flink.streaming.api.datastream;

import com.dataknocker.flink.streaming.api.common.FlatMapFunction;
import com.dataknocker.flink.streaming.api.operators.StreamFlatMap;

/**
 * 数据流操作对象
 * @param <T>
 */
public class DataStream<T> {

    public <R> DataStream<R> flatMap(FlatMapFunction<T, R> flatMapper) {
        new StreamFlatMap<>(flatMapper);
        return null;
    }
}
