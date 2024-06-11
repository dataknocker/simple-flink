package com.dataknocker.runtime.rpc;

import akka.actor.ActorSystem;

import java.util.concurrent.CompletableFuture;

public class AkkaRpcService implements RpcService {
    private ActorSystem actorSystem;

    public AkkaRpcService(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public <C extends RpcGateway> CompletableFuture<C> connect(String address, Class<C> clazz) {
        return null;
    }

    @Override
    public <C extends RpcEndpoint> RpcServer startServer(C rpcEndpoint) {
        return null;
    }
}
