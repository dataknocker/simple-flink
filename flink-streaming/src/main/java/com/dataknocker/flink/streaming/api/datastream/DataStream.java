package com.dataknocker.flink.streaming.api.datastream;

import com.dataknocker.flink.api.common.functions.FilterFunction;
import com.dataknocker.flink.api.common.functions.FlatMapFunction;
import com.dataknocker.flink.api.common.functions.MapFunction;
import com.dataknocker.flink.api.dag.Transformation;
import com.dataknocker.flink.streaming.api.environment.StreamExecutionEnvironment;
import com.dataknocker.flink.streaming.api.functions.sink.SinkFunction;
import com.dataknocker.flink.streaming.api.operators.*;
import com.dataknocker.flink.streaming.api.transformations.OneInputTransformation;
import com.dataknocker.flink.streaming.api.transformations.PartitionTransformation;
import com.dataknocker.flink.streaming.runtime.partitioner.RebalancePartitioner;
import com.dataknocker.flink.streaming.runtime.partitioner.StreamPartitioner;

/**
 * 数据流操作对象
 * transformation 就是当前操作转化，如果该DataStream同加一个操作，该transformation就作为returnTransformation的input transformation
 * @param <T>
 */
public class DataStream<T> {
    private StreamExecutionEnvironment environment;

    protected Transformation<T> transformation;
    public DataStream(StreamExecutionEnvironment environment, Transformation<T> transformation) {
        this.environment = environment;
        this.transformation = transformation;
    }

    public <R> DataStream<R> flatMap(FlatMapFunction<T, R> flatMapper) {
        return doTransform("FlatMap", SimpleStreamOperatorFactory.of(new StreamFlatMap<>(flatMapper)));
    }

    public DataStream<T> filter(FilterFunction<T> filterFunction) {
        return doTransform("Filter", SimpleStreamOperatorFactory.of(new StreamFilter<>(filterFunction)));
    }

    public <R> DataStream<R> map(MapFunction<T, R> mapFunction) {
        return doTransform("Map", SimpleStreamOperatorFactory.of(new StreamMap<>(mapFunction)));
    }

    /**
     * 将要加入的operator变成returnTransformation, 其input是当前DataStream记录的transformation，这样就形成了算子关系
     * @param operatorName
     * @param operatorFactory
     * @return
     * @param <R>
     */
    protected <R> SingleOutputDataStream<R> doTransform(String operatorName, StreamOperatorFactory<R> operatorFactory) {
        OneInputTransformation<T, R> returnTransform = new OneInputTransformation<>(operatorName, this.transformation, operatorFactory);
        environment.addOperator(returnTransform);
        return new SingleOutputDataStream<>(environment, returnTransform);
    }

    public SinkDataStream<T> addSink(SinkFunction<T> sinkFunction) {
        SinkDataStream<T> sink = new SinkDataStream(this, new StreamSink<>(sinkFunction));
        environment.addOperator(sink.getTransformation());
        return sink;
    }

    public DataStream<T> setConnectionType(StreamPartitioner<T> partitioner) {
        return new DataStream<>(environment, new PartitionTransformation<>(partitioner.toString(), transformation, partitioner));
    }

    public DataStream<T> rebalance() {
        return setConnectionType(new RebalancePartitioner<>());
    }

    public Transformation<T> getTransformation() {
        return transformation;
    }
}
