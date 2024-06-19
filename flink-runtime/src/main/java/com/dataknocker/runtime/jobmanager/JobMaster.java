package com.dataknocker.runtime.jobmanager;

import com.dataknocker.runtime.rpc.RpcEndpoint;
import com.dataknocker.runtime.rpc.RpcService;
import com.dataknocker.runtime.taskmanager.TaskExecutorGateway;

import java.util.HashMap;
import java.util.Map;

public class JobMaster extends RpcEndpoint implements JobMasterGateway {
    private Map<String, TaskExecutorGateway> executorGatewayMap = new HashMap<>();

    public JobMaster(RpcService rpcService) {
        super(rpcService, "jobmaster_0");
    }

    @Override
    public boolean registerTaskManager(String executorAddress) {
        rpcService.connect(executorAddress, TaskExecutorGateway.class)
                .handle((gateway, throwable) -> {
                    if (throwable != null) {
                        logger.error("Register TaskManager address {} error.", executorAddress, throwable);
                        return null;
                    }
                    executorGatewayMap.put(executorAddress, gateway);
                    logger.info("Success register taskmanager: {}.", executorAddress);
                    return gateway;
                });

        return true;
    }

    public void submitTask(int taskId) {
        if(!executorGatewayMap.isEmpty()) {
            executorGatewayMap.values().iterator().next().submitTask(taskId, "com.dataknocker.runtime.tasks.StreamTask");
        }
    }
}
