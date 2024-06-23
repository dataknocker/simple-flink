package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

import java.util.ArrayList;
import java.util.List;

public class SimpleOutput<OUT> implements Output<OUT>{

    private Output<StreamRecord<OUT>> output;

    private StreamRecord<OUT> reuse;

    public SimpleOutput(Output<StreamRecord<OUT>> output) {
        this.output = output;
        this.reuse = new StreamRecord<>(null);
    }

    @Override
    public void collect(OUT record) {
        output.collect(reuse.replace(record));
    }

    @Override
    public void close() {

    }
}
