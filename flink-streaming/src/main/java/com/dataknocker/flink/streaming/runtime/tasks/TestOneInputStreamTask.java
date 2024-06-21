package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.runtime.execution.Environment;

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
}
