package com.dataknocker.flink.streaming.api.operators;

public class SimpleStreamOperatorFactory<OUT> implements StreamOperatorFactory<OUT>{

    private StreamOperator<OUT> operator;

    public static <OUT> StreamOperatorFactory<OUT> of(StreamOperator<OUT> operator) {
        return new SimpleStreamOperatorFactory<>(operator);
    }

    public SimpleStreamOperatorFactory(StreamOperator<OUT> operator) {
        this.operator = operator;
    }
    @Override
    public <T extends StreamOperator<OUT>> T createStreamOperator(StreamOperatorParameters<OUT> parameters) {
        return (T) operator;
    }

    public StreamOperator<OUT> getOperator() {
        return operator;
    }
}
