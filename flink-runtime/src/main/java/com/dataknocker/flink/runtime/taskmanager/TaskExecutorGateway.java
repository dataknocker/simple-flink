package com.dataknocker.flink.runtime.taskmanager;

import com.dataknocker.flink.configuration.Configuration;
import com.dataknocker.flink.runtime.rpc.RpcGateway;


public interface TaskExecutorGateway extends RpcGateway {
    public String submitTask(int taskId, Configuration configuration);
}
