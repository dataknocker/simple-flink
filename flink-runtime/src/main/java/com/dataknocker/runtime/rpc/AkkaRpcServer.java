package com.dataknocker.runtime.rpc;

import akka.actor.ActorRef;

public class AkkaRpcServer implements RpcServer{
    private ActorRef actorRef;

    public AkkaRpcServer(ActorRef actorRef) {
        this.actorRef = actorRef;
    }

    public ActorRef getActorRef() {
        return actorRef;
    }
}
