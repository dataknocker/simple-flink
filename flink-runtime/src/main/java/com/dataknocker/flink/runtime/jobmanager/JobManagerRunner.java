package com.dataknocker.flink.runtime.jobmanager;

import com.dataknocker.flink.runtime.rpc.AkkaRpcServiceUtils;
import com.dataknocker.flink.runtime.rpc.RpcService;

import java.util.concurrent.ExecutionException;

public class JobManagerRunner {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

    }

    public static JobMaster startJobManager() {
        RpcService rpcService = AkkaRpcServiceUtils.newRemoteBuilder("localhost", 2550)
                .createAndStart();
        return new JobMaster(rpcService);
    }
}
