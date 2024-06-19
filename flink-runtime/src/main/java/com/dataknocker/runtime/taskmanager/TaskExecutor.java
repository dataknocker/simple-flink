package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.rpc.RpcEndpoint;
import com.dataknocker.runtime.rpc.RpcService;

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
    public String submitTask(int taskId, String invokableClassName) {
        logger.info("get message SubmitTask:{}.", taskId);
        Task task = new Task(taskId, invokableClassName);
        new Thread(task).start();
        return null;
    }
}
