package com.dataknocker.flink.runtime.taskmanager;

import com.dataknocker.flink.runtime.rpc.AkkaRpcServiceUtils;
import com.dataknocker.flink.runtime.rpc.AkkaRpcService;
import com.dataknocker.flink.runtime.rpc.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManagerRunner {
    static Logger LOG = LoggerFactory.getLogger(TaskManagerRunner.class);
    public static void main(String[] args) {
        startTaskExecutor();
    }

    public static void startTaskExecutor() {
        LOG.info("start");
        RpcService rpcService = AkkaRpcServiceUtils.newRemoteBuilder("localhost", 2552)
                .createAndStart(AkkaRpcService::new);
        TaskExecutor taskExecutor = new TaskExecutor(rpcService);

    }
}
