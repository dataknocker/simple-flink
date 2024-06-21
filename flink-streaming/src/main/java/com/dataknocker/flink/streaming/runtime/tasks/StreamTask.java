package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.runtime.execution.Environment;
import com.dataknocker.flink.runtime.jobgraph.tasks.AbstractInvokable;
import com.dataknocker.flink.streaming.api.graph.StreamConfig;
import com.dataknocker.flink.streaming.api.operators.StreamOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * transform相关task
 */
public class StreamTask<OUT, OP extends StreamOperator<OUT>> extends AbstractInvokable {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected OP mainOperator;

    private OperatorChain<OUT, OP> operatorChain;

    protected StreamConfig configuration;

    public StreamTask(Environment environment) {
        super(environment);
        configuration = new StreamConfig(environment.getTaskConfiguration());
    }

    @Override
    public void invoke() throws Exception {

    }

    @Override
    public void init()throws Exception  {
        mainOperator.open();
    }

    public void restore() throws Exception {
        operatorChain = new OperatorChain<>(this);
        mainOperator = operatorChain.getMainOperator();
        init();
    }

    public StreamConfig getConfiguration() {
        return configuration;
    }


}
