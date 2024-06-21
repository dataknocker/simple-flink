package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.streaming.api.graph.StreamConfig;
import com.dataknocker.flink.streaming.api.operators.StreamOperator;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 链式op处理器
 * @param <OUT>
 * @param <OP>
 */
public class OperatorChain<OUT, OP extends StreamOperator<OUT>> {

    private static final Logger LOG = LoggerFactory.getLogger(OperatorChain.class);

    private StreamTask<OUT, OP> containingTask;

    private StreamOperatorWrapper<OUT, OP> mainOperatorWrapper;


    public OperatorChain(StreamTask<OUT, OP> containingTask) {
        this.containingTask = containingTask;
        ClassLoader classLoader = containingTask.getClass().getClassLoader();
        StreamConfig configuration = containingTask.getConfiguration();
        StreamOperatorFactory<OUT> operatorFactory = configuration.getStreamOperatorFactory(classLoader);
        mainOperatorWrapper = new StreamOperatorWrapper<>(
                operatorFactory.createStreamOperator(new StreamOperatorParameters<>(containingTask, null))
        , true);
    }

    public OP getMainOperator() {
        return mainOperatorWrapper != null ? mainOperatorWrapper.getStreamOperator() : null;
    }

}
