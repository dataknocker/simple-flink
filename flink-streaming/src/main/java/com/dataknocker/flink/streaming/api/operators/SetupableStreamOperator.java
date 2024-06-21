package com.dataknocker.flink.streaming.api.operators;

public interface SetupableStreamOperator<OUT> {
    void setup(Output<StreamOperator<OUT>> output);
}
