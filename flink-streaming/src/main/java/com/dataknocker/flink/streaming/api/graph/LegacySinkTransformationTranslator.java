package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.streaming.api.transformations.LegacySinkTransformation;

public class LegacySinkTransformationTranslator<T> extends SimpleTransformationTranslator<T, LegacySinkTransformation<T>>{


    @Override
    public void translateForStreaming(LegacySinkTransformation<T> transformation, Context context) {
        StreamGraph streamGraph = context.getStreamGraph();
        streamGraph.addLegacySink(transformation.getId(), transformation.getName(), transformation.getOperatorFactory());
        transformation.getInputs().forEach(input -> {
            streamGraph.addEdge(input.getId(), transformation.getId());
        });
    }
}
