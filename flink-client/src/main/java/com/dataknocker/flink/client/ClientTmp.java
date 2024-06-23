package com.dataknocker.flink.client;

import com.dataknocker.flink.api.common.Collector;
import com.dataknocker.flink.api.common.functions.FlatMapFunction;
import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.runtime.jobmanager.JobManagerRunner;
import com.dataknocker.flink.runtime.jobmanager.JobMaster;
import com.dataknocker.flink.streaming.api.operators.OneInputStreamOperator;
import com.dataknocker.flink.streaming.api.operators.SimpleStreamOperatorFactory;
import com.dataknocker.flink.streaming.api.operators.StreamFlatMap;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;
import com.dataknocker.flink.util.InstantiationUtil;

public class ClientTmp {

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        configuration.setString("invokableClassName", "com.dataknocker.flink.streaming.runtime.tasks.TestOneInputStreamTask");
        FlatMapFunction<String, String> flatMapper = new FlatMapFunction<String, String>() {

            @Override
            public void flatMap(String value, Collector<String> out) throws Exception {
                System.out.println("process: " + value);
                out.collect(value + "_flatmap");
            }
        };
        OneInputStreamOperator<String, String> operator = new StreamFlatMap<>(flatMapper);
        StreamOperatorFactory<String> operatorFactory =  SimpleStreamOperatorFactory.of(operator);
        configuration.putBytes("operatorFactory", InstantiationUtil.serializeObject(operatorFactory, ClientTmp.class.getClassLoader()));
        JobMaster jobMaster = JobManagerRunner.startJobManager();
        Thread.sleep(10000);
        jobMaster.submitTask(configuration);
    }
}
