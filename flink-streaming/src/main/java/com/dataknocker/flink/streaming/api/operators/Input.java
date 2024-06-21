package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

public interface Input<IN> {
    void processElement(StreamRecord<IN> element) throws Exception;
}
