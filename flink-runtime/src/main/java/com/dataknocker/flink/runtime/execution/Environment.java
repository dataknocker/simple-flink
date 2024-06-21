package com.dataknocker.flink.runtime.execution;

import com.dataknocker.flink.configuration.Configuration;

public interface Environment {

    Configuration getTaskConfiguration();
}
