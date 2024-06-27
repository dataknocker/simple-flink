package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.streaming.api.functions.sink.SinkFunction;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

public class StreamSink<IN> extends AbstractUdfStreamOperator<Object, SinkFunction<IN>> implements OneInputStreamOperator<IN, Object>{
    public StreamSink(SinkFunction<IN> userFunction) {
        super(userFunction);
    }

    @Override
    public void processElement(StreamRecord<IN> element) throws Exception {
        userFunction.invoke(element.getValue());
    }
}
