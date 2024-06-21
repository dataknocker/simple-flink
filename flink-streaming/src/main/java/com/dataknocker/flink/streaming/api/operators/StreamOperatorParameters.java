package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;
import com.dataknocker.flink.streaming.runtime.tasks.StreamTask;

/**
 * 生成streamOperator的参数
 * @param <OUT>
 */
public class StreamOperatorParameters<OUT> {

    private StreamTask<?, ?> containingTask;

    private Output<StreamRecord<OUT>> output;

    public StreamOperatorParameters(StreamTask<?, ?> containingTask, Output<StreamRecord<OUT>> output) {
        this.containingTask = containingTask;
        this.output = output;
    }

    public StreamTask<?, ?> getContainingTask() {
        return containingTask;
    }
    public Output<StreamRecord<OUT>> getOutput() {
        return output;
    }
}
