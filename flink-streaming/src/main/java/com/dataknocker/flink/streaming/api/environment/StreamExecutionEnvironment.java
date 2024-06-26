package com.dataknocker.flink.streaming.api.environment;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.runtime.jobgraph.JobGraph;
import com.dataknocker.flink.runtime.jobgraph.JobVertex;
import com.dataknocker.flink.runtime.jobmanager.JobManagerRunner;
import com.dataknocker.flink.runtime.jobmanager.JobMaster;
import com.dataknocker.flink.streaming.api.datastream.SourceDataStream;
import com.dataknocker.flink.streaming.api.functions.source.SourceFunction;
import com.dataknocker.flink.streaming.api.graph.StreamGraph;
import com.dataknocker.flink.streaming.api.graph.StreamGraphGenerator;
import com.dataknocker.flink.streaming.api.operators.StreamSource;
import com.dataknocker.flink.util.InstantiationUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StreamExecutionEnvironment {

    public static final String DEFAULT_JOB_NAME = "Flink Streaming Job";

    private List<Transformation<?>> transformations = new ArrayList<>();

    private Configuration configuration;

    public StreamExecutionEnvironment() {
        configuration = new Configuration();
    }

    public StreamExecutionEnvironment(Configuration configuration) {
        this.configuration = configuration;
    }

    public void execute() {
        execute(DEFAULT_JOB_NAME);
    }

    public void execute(String jobName) {
        execute(new StreamGraphGenerator(transformations, jobName).generate());
    }

    public void execute(StreamGraph streamGraph) {
        JobGraph jobGraph = streamGraph.createJobGraph();
        //TODO 这里先简化，负责启jobmaster，以及把jobvertex发给taskmanager
        JobMaster jobMaster = JobManagerRunner.startJobManager();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Map<Integer, JobVertex> taskVertices = jobGraph.getTaskVertices();
        taskVertices.forEach((key, value) -> jobMaster.submitTask(key, value.getConfiguration()));

    }

    public <T> SourceDataStream<T> addSource(SourceFunction<T> sourceFunction, String sourceName) {
        StreamSource<T, ?> operator = new StreamSource<>(sourceFunction);
        SourceDataStream<T> sourceDataStream = new SourceDataStream<>(this, operator, sourceName);
        //TODO 源码中为什么会没有这一步，那source transformation在哪里添加的
        addOperator(sourceDataStream.getTransformation());
        return sourceDataStream;
    }

    public <T> SourceDataStream<T> addSource(SourceFunction<T> sourceFunction) {
        return addSource(sourceFunction, "Custom Source");
    }

    public void addOperator(Transformation<?> transformation) {
        transformations.add(transformation);
    }
}
