package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.transformations.PartitionTransformation;

/**
 * 添加虚拟节点
 * @param <OUT>
 * @param <T>
 */
public class PartitionTransformationTranslator<OUT, T extends PartitionTransformation<OUT>> extends SimpleTransformationTranslator<OUT, T>{
    @Override
    public void translateForStreaming(T transformation, Context context) {
        StreamGraph streamGraph = context.getStreamGraph();
        for(Transformation<?> input : transformation.getInputs()) {
            streamGraph.addVirtualPartitionNode(transformation.getId(), input.getId(), transformation.getPartitioner());
        }
    }
}
