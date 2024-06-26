package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.streaming.api.operators.Input;
import com.dataknocker.flink.streaming.api.operators.Output;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

/**
 * 链式output
 * @param <T>
 */
public class ChainingOutput<T> implements Output<StreamRecord<T>> {

    private Input<T> input;


    public ChainingOutput(Input<T> input) {
        this.input = input;
    }
    @Override
    public void collect(StreamRecord<T> record) {
        try {
            input.processElement(record);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }
}
