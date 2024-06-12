package com.dataknocker.runtime.rpc;

import java.util.concurrent.CompletableFuture;

public interface RpcService {
    <C extends RpcGateway> CompletableFuture<C> connect(String address, Class<C> clazz);

    <C extends RpcEndpoint & RpcGateway> RpcServer startServer(C rpcEndpoint);
}
