package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.runtime.jobgraph.tasks.AbstractInvokable;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * graph结点
 */
public class StreamNode {

    private int id;

    private int parallelism;

    private String operatorName;
    private StreamOperatorFactory<?> operatorFactory;

    //对应的StreamTask是什么
    private Class<? extends AbstractInvokable> vertexClass;

    private List<StreamEdge> inEdges = new ArrayList<>();

    private List<StreamEdge> outEdges = new ArrayList<>();

    public StreamNode(int id, String operatorName, StreamOperatorFactory<?> operatorFactory, Class<? extends AbstractInvokable> vertexClass) {
        this.id = id;
        this.operatorName = operatorName;
        this.operatorFactory = operatorFactory;
        this.vertexClass = vertexClass;
    }

    public void addInEdge(StreamEdge edge) {
        inEdges.add(edge);
    }

    public void addOutEdge(StreamEdge edge) {
        outEdges.add(edge);
    }

    public int getId() {
        return id;
    }

    public List<StreamEdge> getInEdges() {
        return inEdges;
    }

    public List<StreamEdge> getOutEdges() {
        return outEdges;
    }

    public StreamOperatorFactory<?> getOperatorFactory() {
        return operatorFactory;
    }

    public Class<? extends AbstractInvokable> getVertexClass() {
        return vertexClass;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String toString() {
        return operatorName + "-" + id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StreamNode that = (StreamNode) o;
        return id == that.id;

    }

    public int hashCode() {
        return id;
    }
}
