package com.dataknocker.flink.streaming.api.datastream;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * flink里叫SingleOutputStreamOperator
 * @param <T>
 */
public class SingleOutputDataStream<T> extends DataStream<T>{

    public SingleOutputDataStream(StreamExecutionEnvironment environment, Transformation<T> transformation) {
        super(environment, transformation);
    }

    public SingleOutputDataStream<T> uid(String uid) {
        this.transformation.setUid(uid);
        return this;
    }
}
