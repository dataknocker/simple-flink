package com.dataknocker.flink.runtime.jobgraph;

import java.io.Serializable;

public class JobEdge implements Serializable {
    private static final long serialVersionUID = 1L;

    private IntermediateDataset source;
    private JobVertex target;

    public JobEdge(IntermediateDataset source, JobVertex target) {
        this.source = source;
        this.target = target;
        source.addConsumer(this);
    }
}
