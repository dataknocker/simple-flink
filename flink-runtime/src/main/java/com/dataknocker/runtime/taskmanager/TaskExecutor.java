package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.rpc.RpcEndpoint;
import com.dataknocker.runtime.rpc.RpcService;

public class TaskExecutor extends RpcEndpoint implements TaskExecutorGateway{

    public TaskExecutor(RpcService rpcService) {
        super( rpcService, "taskmanager_0");

    }
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
