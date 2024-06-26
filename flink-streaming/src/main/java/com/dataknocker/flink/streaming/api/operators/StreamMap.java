package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.api.common.functions.MapFunction;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

public class StreamMap<IN, OUT> extends AbstractUdfStreamOperator<OUT, MapFunction<IN, OUT>> implements OneInputStreamOperator<IN, OUT>{

    private static final long serialVersionUID = 1L;

    private Output<OUT> collector;

    public StreamMap(MapFunction<IN, OUT> userFunction) {
        super(userFunction);
    }

    @Override
    public void processElement(StreamRecord<IN> element) throws Exception {
        this.userFunction.map(element.getValue(), collector);
    }

    public void open() throws Exception {
        collector = new SimpleOutput<>(output);
    }
}
