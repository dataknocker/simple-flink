package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.api.dag.Pipeline;
import com.dataknocker.flink.runtime.jobgraph.tasks.AbstractInvokable;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;
import com.dataknocker.flink.streaming.runtime.tasks.OneInputStreamTask;
import com.dataknocker.flink.streaming.runtime.tasks.SourceStreamTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * graph, 记录的是点和边，不是通过树型结构来记录的
 */
public class StreamGraph implements Pipeline {
    private static final Logger logger = LoggerFactory.getLogger(StreamGraph.class);

    private String jobName;

    private Map<Integer, StreamNode> streamNodes;

    private Set<Integer> sources;

    private Set<Integer> sinks;

    public StreamGraph() {
        clear();
    }


    public void clear() {
        streamNodes = new HashMap<>();
        sources = new HashSet<>();
        sinks = new HashSet<>();
    }

    public <OUT> void addSource(int vertexId, String sourceName, StreamOperatorFactory<OUT> operatorFactory) {
        addOperator(vertexId, sourceName, operatorFactory);
        sources.add(vertexId);
    }

    public <IN, OUT> void addOperator(int vertexId, String operatorName, StreamOperatorFactory<OUT> operatorFactory) {
        //根据operatorFactory.isStreamSource()来决定是sourceTask还是普通的Task
        addNode(vertexId, operatorName, operatorFactory, operatorFactory.isStreamSource() ? SourceStreamTask.class : OneInputStreamTask.class);
    }

    private void addOperator(int vertexId, String operatorName, StreamOperatorFactory<?> operatorFactory, Class<? extends AbstractInvokable> invokableClass) {
        addNode(vertexId, operatorName, operatorFactory, invokableClass);
    }

    private void addNode(int vertexId, String operatorName, StreamOperatorFactory<?> operatorFactory, Class<? extends AbstractInvokable> invokableClass) {
        if(streamNodes.containsKey(vertexId)) {
            throw new RuntimeException("Duplicate vertexId " + vertexId);
        }
        StreamNode node = new StreamNode(vertexId, operatorName, operatorFactory, invokableClass);
        streamNodes.put(vertexId, node);
    }

    public void addEdge(int sourceId, int targetId) {
        StreamNode sourceNode = getStreamNode(sourceId);
        StreamNode targetNode = getStreamNode(targetId);
        StreamEdge edge = new StreamEdge(sourceNode, targetNode);
        sourceNode.addOutEdge(edge);
        targetNode.addInEdge(edge);
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public StreamNode getStreamNode(int vertexId) {
        return streamNodes.get(vertexId);
    }
}
