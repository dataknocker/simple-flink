package com.dataknocker.flink.streaming.api.operators;

public interface OneInputStreamOperator<IN, OUT> extends StreamOperator<OUT>, Input<IN> {
}
