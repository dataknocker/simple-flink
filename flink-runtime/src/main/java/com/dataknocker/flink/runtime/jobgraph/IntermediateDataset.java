package com.dataknocker.flink.runtime.jobgraph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntermediateDataset implements Serializable {
    private static final long serialVersionUID = 1L;

    private JobVertex producer;

    private List<JobEdge> consumers = new ArrayList<>();

    public IntermediateDataset(JobVertex producer) {
        this.producer = producer;
    }

    public void addConsumer(JobEdge consumer) {
        consumers.add(consumer);
    }
}
