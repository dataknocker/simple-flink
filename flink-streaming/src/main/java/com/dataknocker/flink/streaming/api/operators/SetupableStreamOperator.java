package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;
import com.dataknocker.flink.streaming.runtime.tasks.StreamTask;

public interface SetupableStreamOperator<OUT> {
    void setup(StreamTask<?, ?> containingTask, Output<StreamRecord<OUT>> output);
}
