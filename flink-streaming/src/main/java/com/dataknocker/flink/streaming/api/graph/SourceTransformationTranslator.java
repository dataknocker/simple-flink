package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.streaming.api.transformations.SourceTransformation;

public class SourceTransformationTranslator<OUT, T extends SourceTransformation<OUT>> extends SimpleTransformationTranslator<OUT, T>{
    @Override
    public void translateForStreaming(T transformation, Context context) {
        translateInternal(transformation, context);
    }

    private void translateInternal(T sourceTransformation, Context context) {
        StreamGraph streamGraph = context.getStreamGraph();
        streamGraph.addSource(sourceTransformation.getId(), sourceTransformation.getName(), sourceTransformation.getOperatorFactory());
    }
}
