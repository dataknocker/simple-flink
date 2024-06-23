package com.dataknocker.flink.streaming.api.environment;

import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.streaming.api.datastream.SourceDataStream;
import com.dataknocker.flink.streaming.api.functions.source.SourceFunction;
import com.dataknocker.flink.streaming.api.graph.StreamGraph;
import com.dataknocker.flink.streaming.api.graph.StreamGraphGenerator;
import com.dataknocker.flink.streaming.api.operators.StreamSource;

import java.util.ArrayList;
import java.util.List;

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
        StreamGraphGenerator generator = new StreamGraphGenerator(transformations, jobName);
        StreamGraph streamGraph = generator.generate();
        System.out.println(streamGraph);
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
