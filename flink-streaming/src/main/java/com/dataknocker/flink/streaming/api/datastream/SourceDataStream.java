package com.dataknocker.flink.streaming.api.datastream;

import com.dataknocker.flink.streaming.api.environment.StreamExecutionEnvironment;
import com.dataknocker.flink.streaming.api.operators.StreamSource;
import com.dataknocker.flink.streaming.api.transformations.LegacySourceTransformation;

/**
 * source类型的datastraem
 * flink里叫DataSourceStream
 * @param <T>
 */
public class SourceDataStream<T> extends SingleOutputDataStream<T>{


    public SourceDataStream(StreamExecutionEnvironment environment, StreamSource<T, ?> operator, String sourceName) {
        super(environment, new LegacySourceTransformation<>(sourceName, operator));
    }
}
