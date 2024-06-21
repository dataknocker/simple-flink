package com.dataknocker.flink.runtime.taskmanager;

import com.dataknocker.flink.runtime.rpc.RpcEndpoint;
import com.dataknocker.flink.runtime.rpc.RpcService;

import com.dataknocker.flink.configuration.Configuration;


public class TaskExecutor extends RpcEndpoint implements TaskExecutorGateway {

    private JobLeaderService jobLeaderService;


    public TaskExecutor(RpcService rpcService) {
        super(rpcService, "taskmanager_0");
        jobLeaderService = new DefaultJobLeaderService(getAddress(), rpcService);
        startTaskExecutorServices();
    }

    private void startTaskExecutorServices() {
        //向resourcemanager注册slot
        //向jobmaster注册taskmanager
        jobLeaderService.start();

    }


    @Override
    public String submitTask(int taskId, Configuration configuration) {
        logger.info("get message SubmitTask:{}.", taskId);
        Task task = new Task(taskId, configuration);
        new Thread(task).start();
        return null;
    }
}
