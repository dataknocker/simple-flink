package com.dataknocker.runtime.jobmanager;

import com.dataknocker.runtime.rpc.RpcGateway;

public interface JobMasterGateway extends RpcGateway {
    public boolean registTaskExecutor(String executorId);
}
