package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.runtime.execution.Environment;
import com.dataknocker.flink.streaming.api.operators.Output;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

import java.util.ArrayList;
import java.util.List;

public class TestOneInputStreamTask extends OneInputStreamTask<String, String>{

    public TestOneInputStreamTask(Environment environment) {
        super(environment);
        List<String> data = new ArrayList<>();
        for(int i = 0;i<=10;i ++) {
            data.add("msg_" + i);
        }
        setData(data);
    }

    @Override
    public Output<StreamRecord<String>> getOutput() {
        return new Output<StreamRecord<String>>() {
            private List<StreamRecord<String>> data = new ArrayList<>();
            @Override
            public void collect(StreamRecord<String> record) {
                System.out.println("Collect record: " + record.getValue() + ".");
                data.add(record);
            }

            @Override
            public void close() {
                data.clear();
            }
        };
    }
}
