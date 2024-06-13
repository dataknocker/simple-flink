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
    public boolean registerTaskExecutor(String executorId, String executorAddress) {
        rpcService.connect(executorAddress, TaskExecutorGateway.class)
                .handle((gateway, throwable) -> {
                    if (throwable != null) {
                        System.out.println(throwable);
                        return null;
                    }
                    executorGatewayMap.put(executorId, gateway);
                    logger.info("Success register taskmanager: {}.", executorAddress);
                    return gateway;
                });

        return true;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getHostName() {
        return null;
    }

    public void submitTask(int taskId, String endpointId) {
        if(executorGatewayMap.containsKey(endpointId)) {
            executorGatewayMap.get(endpointId).submitTask(taskId);
        }
    }
}
