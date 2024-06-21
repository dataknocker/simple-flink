package com.dataknocker.flink.runtime.jobmanager;

import com.dataknocker.flink.runtime.rpc.RpcGateway;

public interface JobMasterGateway extends RpcGateway {
    public boolean registerTaskManager(String executorAddress);
}
