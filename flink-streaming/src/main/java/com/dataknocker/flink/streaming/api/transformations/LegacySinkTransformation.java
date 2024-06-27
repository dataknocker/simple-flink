package com.dataknocker.flink.streaming.api.transformations;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.operators.SimpleStreamOperatorFactory;
import com.dataknocker.flink.streaming.api.operators.StreamOperator;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;

import java.util.Collections;
import java.util.List;

/**
 * 无Sink的sink
 * @param <T>
 */
public class LegacySinkTransformation<T> extends Transformation<T> {

    private Transformation<T> input;
    private StreamOperatorFactory<T> operatorFactory;
    public LegacySinkTransformation(String name, StreamOperator<T> operator, Transformation<T> input) {
        this(name, SimpleStreamOperatorFactory.of(operator), input);
    }

    public LegacySinkTransformation(String name, StreamOperatorFactory<T> operatorFactory, Transformation<T> input) {
        super(name);
        this.operatorFactory = operatorFactory;
        this.input = input;
    }

    @Override
    public List<Transformation<?>> getInputs() {
        return Collections.singletonList(input);
    }

    public StreamOperatorFactory<T> getOperatorFactory() {
        return operatorFactory;
    }


}
