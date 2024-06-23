package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.runtime.execution.Environment;
import com.dataknocker.flink.streaming.api.functions.source.SourceFunction;
import com.dataknocker.flink.streaming.api.operators.StreamSource;

/**
 * source的task
 * @param <OUT>
 * @param <SRC>
 * @param <OP>
 */
public class SourceStreamTask<OUT, SRC extends SourceFunction<OUT>, OP extends StreamSource<OUT, SRC>> extends StreamTask<OUT, OP>{
    public SourceStreamTask(Environment environment) {
        super(environment);
    }

    @Override
    public void invoke() throws Exception {
        mainOperator.run();
    }
}
