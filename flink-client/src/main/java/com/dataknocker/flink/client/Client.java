package com.dataknocker.flink.client;

import com.dataknocker.flink.api.common.Collector;
import com.dataknocker.flink.api.common.functions.FlatMapFunction;
import com.dataknocker.flink.streaming.api.datastream.DataStream;
import com.dataknocker.flink.streaming.api.environment.StreamExecutionEnvironment;
import com.dataknocker.flink.streaming.api.functions.source.SourceFunction;

import java.util.Arrays;
import java.util.List;

public class Client {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = new StreamExecutionEnvironment();
        DataStream<String> dataStream = env.addSource(new SourceFunction<String>() {
            private boolean stop;

            private List<String> data = Arrays.asList("hello", "world");

            private int index = 0;

            @Override
            public void run(SourceContext<String> ctx) throws Exception {
                while (!stop && index <= data.size()) {
                    ctx.collect(data.get(index));
                    index++;
                }
            }

            @Override
            public void cancel() {
                stop = true;
            }
        });
        dataStream.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                for (int i = 0; i < value.length(); i++) {
                    out.collect(value.substring(i, i + 1));
                }
            }
        }).filter(t -> "h".compareToIgnoreCase(t) >= 0);
        env.execute("test");
    }
}
