package com.dataknocker.flink.runtime.execution;

import com.dataknocker.flink.configuration.Configuration;

public class RuntimeEnvironment implements Environment{

    private Configuration configuration;

    public RuntimeEnvironment() {
        this.configuration = new Configuration();
    }

    public RuntimeEnvironment(Configuration configuration) {
        this.configuration = configuration;
    }
    @Override
    public Configuration getTaskConfiguration() {
        return configuration;
    }
}
