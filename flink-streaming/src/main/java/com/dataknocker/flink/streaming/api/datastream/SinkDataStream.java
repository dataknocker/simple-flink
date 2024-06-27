package com.dataknocker.flink.streaming.api.datastream;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.operators.StreamSink;
import com.dataknocker.flink.streaming.api.transformations.LegacySinkTransformation;

public class SinkDataStream<T> {
    private Transformation<T> transformation;
    public SinkDataStream(DataStream<T> dataStream, StreamSink<T> operator) {
        transformation = (LegacySinkTransformation<T>)new LegacySinkTransformation("Unamed Sink", operator, dataStream.getTransformation());
    }

    public Transformation<T> getTransformation() {
        return transformation;
    }
}
