package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.streaming.api.graph.StreamConfig;
import com.dataknocker.flink.streaming.api.graph.StreamEdge;
import com.dataknocker.flink.streaming.api.operators.*;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 链式op处理器
 * 负责将operatoryFactory反序列化回来，并得到mainOperator
 * @param <OUT>
 * @param <OP>
 */
public class OperatorChain<OUT, OP extends StreamOperator<OUT>> {

    private static final Logger logger = LoggerFactory.getLogger(OperatorChain.class);

    private StreamTask<OUT, OP> containingTask;

    private StreamOperatorWrapper<OUT, OP> mainOperatorWrapper;

    private Output<StreamRecord<OUT>> mainOperatorOutput;


    public OperatorChain(StreamTask<OUT, OP> containingTask) {
        this.containingTask = containingTask;
        ClassLoader classLoader = containingTask.getClass().getClassLoader();
        StreamConfig config = containingTask.getConfiguration();

        Map<Integer, StreamConfig> chainedConfigs = config.getChainedConfigsWithSelf(classLoader);
        mainOperatorOutput = createOutputCollector(containingTask, config, chainedConfigs, classLoader);
        StreamOperatorFactory<OUT> operatorFactory = config.getStreamOperatorFactory(classLoader);
        mainOperatorWrapper = new StreamOperatorWrapper<>(
                operatorFactory.createStreamOperator(new StreamOperatorParameters<>(containingTask, mainOperatorOutput))
        , true);
    }

    public <T> Output<StreamRecord<T>> createOutputCollector(StreamTask<?, ?> containingTask, StreamConfig config, Map<Integer, StreamConfig> chainedConfigs, ClassLoader classLoader) {
        List<StreamEdge> chainableOutEdges = config.getChainableOutEdges(classLoader);
        List<Output<StreamRecord<T>>> outputs = new ArrayList<>();
        for (StreamEdge edge : chainableOutEdges) {
            StreamConfig chainedConfig = chainedConfigs.get(edge.getTargetId());
            outputs.add(createOperatorChain(containingTask, chainedConfig, chainedConfigs, classLoader));
        }
        if (outputs.size() == 1) {
            return outputs.get(0);
        }

        if (chainableOutEdges.isEmpty()) {
            //TODO 先写死叶子节点的output
            return new Output<StreamRecord<T>>() {
                @Override
                public void collect(StreamRecord<T> record) {
                    System.out.println("final collect record:" + record.getValue());
                }

                @Override
                public void close() {

                }
            };

        }
        //TODO 多个输出的暂不处理
        return null;
    }

    /**
     * 创建chain中的一层
     * 1、创建output: 获得子节点的output
     * 2、创建input=operator
     * 3、创建当前output=ChainingOutput
     * @param containingTask
     * @param config
     * @param classLoader
     * @return
     * @param <IN>
     * @param <OUT>
     */
    public <IN, OUT> Output<StreamRecord<IN>> createOperatorChain(StreamTask<OUT, ?> containingTask, StreamConfig config, Map<Integer, StreamConfig> chainedConfigs, ClassLoader classLoader) {
        Output<StreamRecord<OUT>> output = createOutputCollector(containingTask, config, chainedConfigs, classLoader);
        StreamOperatorFactory<OUT> operatorFactory = config.getStreamOperatorFactory(classLoader);
        //返回结果泛型<T extends StreamOperator<OUT>>根据这里声明的，即T=OneInputStreamOperator<IN, OUT>
        OneInputStreamOperator<IN, OUT> operator = operatorFactory.createStreamOperator(new StreamOperatorParameters<>(containingTask, output));
        //TODO 先在这里显式调用open
        try {
            operator.open();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ChainingOutput<>(operator);
    }


    public OP getMainOperator() {
        return mainOperatorWrapper != null ? mainOperatorWrapper.getStreamOperator() : null;
    }


}
