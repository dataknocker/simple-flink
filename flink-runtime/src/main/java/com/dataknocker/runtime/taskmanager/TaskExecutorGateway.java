package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.rpc.RpcGateway;

public interface TaskExecutorGateway extends RpcGateway {
    public String submitTask(int taskId);
}
