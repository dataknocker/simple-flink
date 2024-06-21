package com.dataknocker.flink.runtime.rpc;

import akka.actor.ActorRef;

public class AkkaRpcServer implements RpcServer{
    private ActorRef actorRef;

    private String address;

    public AkkaRpcServer(ActorRef actorRef, String address) {
        this.actorRef = actorRef;
        this.address = address;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String getHostName() {
        return null;
    }
}
