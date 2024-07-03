package com.dataknocker.flink.streaming.api.transformations;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.runtime.partitioner.StreamPartitioner;

import java.util.Collections;
import java.util.List;

public class PartitionTransformation<T> extends Transformation<T> {
    private Transformation<T> input;

    private StreamPartitioner<T> partitioner;
    public PartitionTransformation(String name, Transformation<T> input, StreamPartitioner<T> partitioner) {
        super(name);
        this.input = input;
        this.partitioner = partitioner;
    }

    @Override
    public List<Transformation<?>> getInputs() {
        return Collections.singletonList(input);
    }

    public StreamPartitioner<T> getPartitioner() {
        return partitioner;
    }
}
