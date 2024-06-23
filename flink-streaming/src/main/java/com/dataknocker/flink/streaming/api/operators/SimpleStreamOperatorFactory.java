package com.dataknocker.flink.streaming.api.operators;

public class SimpleStreamOperatorFactory<OUT> implements StreamOperatorFactory<OUT> {

    private StreamOperator<OUT> operator;

    public static <OUT> StreamOperatorFactory<OUT> of(StreamOperator<OUT> operator) {
        return new SimpleStreamOperatorFactory<>(operator);
    }

    public SimpleStreamOperatorFactory(StreamOperator<OUT> operator) {
        this.operator = operator;
    }

    /**
     * 这里反序列后获得对应的StreamOperator,并对StreamOperator进行初始化
     *
     * @param parameters
     * @param <T>
     * @return
     */
    @Override
    public <T extends StreamOperator<OUT>> T createStreamOperator(StreamOperatorParameters<OUT> parameters) {
        if (operator instanceof SetupableStreamOperator) {
            ((SetupableStreamOperator) operator).setup(parameters.getContainingTask(), parameters.getOutput());
        }
        return (T) operator;
    }

    public boolean isStreamSource() {
        return operator instanceof StreamSource;
    }

    public StreamOperator<OUT> getOperator() {
        return operator;
    }
}
