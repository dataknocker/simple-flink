package com.dataknocker.runtime.rpc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

public interface RpcService {
    /**
     * 连接目标服务。
     * @param address
     * @param clazz 目标服务接口
     * @return 代理。其invokehandler负责将请求通过actorselector转给目标服务
     * @param <C>
     */
    <C extends RpcGateway> CompletableFuture<C> connect(String address, Class<C> clazz);

    <C extends RpcEndpoint & RpcGateway> RpcServer startServer(C rpcEndpoint);

    //TODO flink用的是ActorSystem.schedule相关方法，需要实现
    ScheduledExecutorService getExecutorService();

    Executor getExecutor();
}
