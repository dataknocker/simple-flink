package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.rpc.RpcEndpoint;

public class TaskExecutor extends RpcEndpoint implements TaskExecutorGateway{
    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getHostName() {
        return null;
    }

    @Override
    public String submitTask(int taskId) {
        return null;
    }
}
