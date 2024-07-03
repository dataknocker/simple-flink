package com.dataknocker.flink.streaming.runtime.partitioner;

public class ForwardPartitioner<T> extends StreamPartitioner<T>{

    public String toString() {
        return "FORWARD";
    }
}
