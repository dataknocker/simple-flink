package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.jobmanager.JobMasterGateway;
import com.dataknocker.runtime.rpc.RpcEndpoint;
import com.dataknocker.runtime.rpc.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskExecutor extends RpcEndpoint implements TaskExecutorGateway {

    private JobMasterGateway jobMasterGateway;


    public TaskExecutor(RpcService rpcService) {
        super(rpcService, "taskmanager_0");
        String jobMasterAddress = "akka.tcp://flink@localhost:2550/user/rpc/jobmaster_0";
        register(jobMasterAddress);
    }

    private void register(String jobMasterAddress) {
        rpcService.connect("akka.tcp://flink@localhost:2550/user/rpc/jobmaster_0", JobMasterGateway.class)
                .handle((gateway, throwable) -> {
                    if (throwable != null) {
                        logger.error("Connect to JobMaster {} error.", jobMasterAddress, throwable);
                        return null;
                    }
                    jobMasterGateway = gateway;
                    logger.info("actor path: {}.", rpcServer.getActorRef().path());
                    jobMasterGateway.registerTaskExecutor(endpointId, "akka.tcp://flink@localhost:2552/user/rpc/taskmanager_0");
                    return gateway;
                });
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
        logger.info("get message SubmitTask:{}.", taskId);
        return null;
    }
}
