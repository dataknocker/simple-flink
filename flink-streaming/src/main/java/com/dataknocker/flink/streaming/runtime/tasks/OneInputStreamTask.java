package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.runtime.execution.Environment;
import com.dataknocker.flink.streaming.api.operators.OneInputStreamOperator;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

import java.util.List;

/**
 * 单输入型的StreamTask
 */
public class OneInputStreamTask<IN, OUT> extends StreamTask<OUT, OneInputStreamOperator<IN, OUT>>{

    private List<IN> data;

    public OneInputStreamTask(Environment environment) {
        super(environment);
    }

    @Override
    public void invoke() throws Exception {
        for(IN input : data) {
            mainOperator.processElement(new StreamRecord<>(input));
        }

    }

    public void setData(List<IN> data) {
        this.data = data;
    }
}
