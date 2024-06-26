package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.api.common.functions.FlatMapFunction;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

/**
 * flatmap的operator
 * @param <IN>
 * @param <OUT>
 */
public class StreamFlatMap<IN, OUT> extends AbstractUdfStreamOperator<OUT, FlatMapFunction<IN, OUT>> implements OneInputStreamOperator<IN, OUT>{
    private static final long serialVersionUID = 1L;
    private transient Output<OUT> collector;
    public StreamFlatMap(FlatMapFunction<IN, OUT> flatMapper) {
        super(flatMapper);
    }


    @Override
    public void processElement(StreamRecord<IN> element) throws Exception {
        userFunction.flatMap(element.getValue(), collector);
    }

    @Override
    public void open() throws Exception {
        collector = new SimpleOutput<>(output);
    }

    @Override
    public void close() throws Exception {

    }
}
