package com.dataknocker.runtime.rpc;

import akka.actor.ActorRef;

/**
 * 获得具体actor server对象
 */
public interface RpcServer extends RpcGateway {

    ActorRef getActorRef();
}
