package com.dataknocker.flink.streaming.api.transformations;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.operators.SimpleStreamOperatorFactory;
import com.dataknocker.flink.streaming.api.operators.StreamOperator;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;

import java.util.Collections;
import java.util.List;

public class SourceTransformation<T> extends Transformation<T> {
    private StreamOperatorFactory<T> operatorFactory;
    public SourceTransformation(String name, StreamOperator<T> operator) {
        this(name, SimpleStreamOperatorFactory.of(operator));
    }

    public SourceTransformation(String name, StreamOperatorFactory<T> operatorFactory) {
        super(name);
        this.operatorFactory = operatorFactory;
    }

    @Override
    public List<Transformation<?>> getInputs() {
        return Collections.emptyList();
    }

    public StreamOperatorFactory<T> getOperatorFactory() {
        return operatorFactory;
    }


}
