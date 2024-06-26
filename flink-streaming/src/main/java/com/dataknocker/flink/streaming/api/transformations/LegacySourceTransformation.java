package com.dataknocker.flink.streaming.api.transformations;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.operators.SimpleStreamOperatorFactory;
import com.dataknocker.flink.streaming.api.operators.StreamOperator;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;

import java.util.Collections;
import java.util.List;

/**
 * 无InputSource的Source。如果是env.fromSource(source)的则走SourceTransformation
 * @param <T>
 */
public class LegacySourceTransformation<T> extends Transformation<T> {
    private StreamOperatorFactory<T> operatorFactory;
    public LegacySourceTransformation(String name, StreamOperator<T> operator) {
        this(name, SimpleStreamOperatorFactory.of(operator));
    }

    public LegacySourceTransformation(String name, StreamOperatorFactory<T> operatorFactory) {
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
