package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.streaming.api.transformations.OneInputTransformation;

public class OneInputTransformationTranslator<IN, OUT, T extends OneInputTransformation<IN, OUT>> extends AbstractOneInputTransformationTranslator<IN, OUT, T>{

    @Override
    public void translateForStreaming(T transformation, Context context) {
        translateInternal(transformation, transformation.getOperatorFactory(), context);
    }
}
