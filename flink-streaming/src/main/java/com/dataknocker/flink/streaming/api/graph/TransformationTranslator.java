package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.api.dag.Transformation;

/**
 * 根据Transformation类型将其变成相应的StreamNode并找加到StreamGraph中
 */
public interface TransformationTranslator<OUT, T extends Transformation<OUT>> {
    default void translateForBatch(T transformation, Context context) {}

    void translateForStreaming(T transformation, Context context);

    interface Context {
        StreamGraph getStreamGraph();
    }
}
