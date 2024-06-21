package com.dataknocker.flink.streaming.api.graph;

import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.streaming.api.operators.StreamOperatorFactory;
import com.dataknocker.flink.util.InstantiationUtil;

import java.io.IOException;
import java.io.Serializable;

/**
 * stream相关配置
 */
public class StreamConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private Configuration config;

    public StreamConfig(Configuration configuration) {
        this.config = configuration;
    }

    public <T extends StreamOperatorFactory<?>> T getStreamOperatorFactory(ClassLoader classLoader) {
        try {
            return (T) InstantiationUtil.readObjectFromConfig(config, "operatorFactory", classLoader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
