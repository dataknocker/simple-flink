package com.dataknocker.flink.streaming.api.transformations;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.operators.OneInputStreamOperator;
import com.dataknocker.flink.streaming.api.operators.SimpleStreamOperatorFactory;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;

import java.util.Collections;
import java.util.List;

/**
 * 单输入转换器
 * 记录了上一个transformation以及本次operator
 * @param <IN>
 * @param <OUT>
 */
public class OneInputTransformation<IN, OUT> extends Transformation<OUT> {

    private Transformation<IN> input;  //记录了上个操作算子

    private StreamOperatorFactory<OUT> operatorFactory; //本次操作算子

    public OneInputTransformation(String name, Transformation<IN> input, OneInputStreamOperator<IN, OUT> operator) {
        this(name, input, SimpleStreamOperatorFactory.of(operator));
    }

    public OneInputTransformation(String name, Transformation<IN> input, StreamOperatorFactory<OUT> operatorFactory) {
        super(name);
        this.input = input;
        this.operatorFactory = operatorFactory;
    }

    public List<Transformation<?>> getInputs() {
        return Collections.singletonList(input);
    }

    public StreamOperatorFactory<OUT> getOperatorFactory() {
        return operatorFactory;
    }
}
