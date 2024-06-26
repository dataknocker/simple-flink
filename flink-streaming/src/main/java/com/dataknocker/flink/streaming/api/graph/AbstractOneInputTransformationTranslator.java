package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;

import java.util.List;

/**
 * 专门用于处理有一个输入的算子的转换器
 * 在生成StreamGraph的时候，会通过其input来建立edge
 *
 * @param <IN>
 * @param <OUT>
 * @param <T>
 */
public abstract class AbstractOneInputTransformationTranslator<IN, OUT, T extends Transformation<OUT>> extends SimpleTransformationTranslator<OUT, T> {

    /**
     * 添加当前算子节点以及和input的边
     *
     * @param transformation
     * @param operatorFactory
     * @param context
     */
    protected void translateInternal(T transformation, StreamOperatorFactory<OUT> operatorFactory, Context context) {
        StreamGraph streamGraph = context.getStreamGraph();
        streamGraph.addOperator(transformation.getId(), transformation.getName(), operatorFactory);
        List<Transformation<?>> inputs = transformation.getInputs();
        for (Transformation<?> input : inputs) {
            streamGraph.addEdge(input.getId(), transformation.getId());
        }
    }


}
