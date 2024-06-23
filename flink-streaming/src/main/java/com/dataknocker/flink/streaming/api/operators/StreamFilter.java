package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.api.common.functions.FilterFunction;
import com.dataknocker.flink.api.common.functions.FlatMapFunction;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

/**
 * flatmap的operator
 * @param <IN>
 */
public class StreamFilter<IN> extends AbstractUdfStreamOperator<IN, FilterFunction<IN>> implements OneInputStreamOperator<IN, IN>{

    private static final long serialVersionUID = 1L;
    public StreamFilter(FilterFunction<IN> filterMapper) {
        super(filterMapper);
    }


    @Override
    public void processElement(StreamRecord<IN> element) throws Exception {
        if (userFunction.filter(element.getValue())) {
            output.collect(element);
        }
    }


}
