package com.dataknocker.flink.streaming.api.operators;

import java.util.ArrayList;
import java.util.List;

public class SimpleOutput<OUT> implements Output<OUT>{

    private List<OUT> containers = new ArrayList<>();

    @Override
    public void collect(OUT record) {
        System.out.println("collect record:" + record);
        containers.add(record);
    }

    @Override
    public void close() {
        containers.clear();
    }
}
