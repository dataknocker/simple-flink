package com.dataknocker.runtime.jobmanager;

import com.dataknocker.runtime.taskmanager.TaskExecutorGateway;

import java.util.HashMap;
import java.util.Map;

public class JobMaster implements JobMasterGateway{
    private Map<String, TaskExecutorGateway> executorGatewayMap = new HashMap<>();

    @Override
    public boolean registTaskExecutor(String executorId) {
        return false;
    }

    @Override
    public String getAddress() {
        return null;
    }

    @Override
    public String getHostName() {
        return null;
    }
}
