package com.dataknocker.runtime.taskmanager;

import com.dataknocker.runtime.rpc.AkkaRpcService;
import com.dataknocker.runtime.rpc.AkkaRpcServiceUtils;
import com.dataknocker.runtime.rpc.RpcService;

public class TaskManagerRunner {
    public static void main(String[] args) {
        startTaskExecutor();
    }

    public static void startTaskExecutor() {
        RpcService rpcService = AkkaRpcServiceUtils.newLocalBuilder()
                .withAddress("localhost")
                .withPort(2552)
                .createAndStart(AkkaRpcService::new);
        TaskExecutor taskExecutor = new TaskExecutor(rpcService);

    }
}
