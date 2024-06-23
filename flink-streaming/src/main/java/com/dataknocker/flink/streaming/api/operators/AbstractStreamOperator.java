package com.dataknocker.flink.streaming.api.operators;


import java.io.Serializable;

import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;
import com.dataknocker.flink.streaming.runtime.tasks.StreamTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStreamOperator<OUT> implements StreamOperator<OUT>, SetupableStreamOperator<OUT>, Serializable {
    private static final long serialVersionUID = 1L;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected transient StreamTask<?, ?> containingTask;

    protected transient Output<StreamRecord<OUT>> output;

    public void setup(StreamTask<?, ?> containingTask, Output<StreamRecord<OUT>> output) {
        this.containingTask = containingTask;
        this.output = output;
    }
}
