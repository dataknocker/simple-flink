package com.dataknocker.flink.streaming.runtime.partitioner;

public class RebalancePartitioner<T> extends StreamPartitioner<T>{

    public String toString() {
        return "REBALANCE";
    }
}
