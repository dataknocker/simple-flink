package com.dataknocker.runtime.jobmanager;

import com.dataknocker.runtime.rpc.AkkaRpcServiceUtils;
import com.dataknocker.runtime.rpc.RpcService;

import java.util.Random;
import java.util.concurrent.ExecutionException;

public class JobManagerRunner {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RpcService rpcService = AkkaRpcServiceUtils.newRemoteBuilder("localhost", 2550)
                .createAndStart();
        JobMaster jobMaster = new JobMaster(rpcService);
        Random random = new Random();
        while(true) {
            jobMaster.submitTask(random.nextInt(1000));
            Thread.sleep(5000);
        }

    }
}
