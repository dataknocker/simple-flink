package com.dataknocker.flink.runtime.jobgraph;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * StreamGraph的chain(多个点)组成JobGraph的一个点
 */
public class JobGraph implements Serializable {
    private static final long serialVersionUID = 1L;

    private String jobName;

    private Map<Integer, JobVertex> taskVertices = new LinkedHashMap<>();

    public JobGraph(String jobName) {
        this.jobName = jobName;
    }

    public void addVertex(JobVertex vertex) {
        taskVertices.put(vertex.getID(), vertex);
    }

    public Map<Integer, JobVertex> getTaskVertices() {
        return taskVertices;
    }
}
