package com.dataknocker.runtime.rpc;

import akka.actor.ActorRef;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 将所有请求通过proxy封装成RpcInvocation(method, args)转发向AkkaRpcActor，该actor负责找到Endpoint对应方法并执行
 */
public class AkkaInvocationHandler implements InvocationHandler {

    private ActorRef rpcEndpoint;

    public AkkaInvocationHandler(ActorRef rpcEndpoint) {
        this.rpcEndpoint = rpcEndpoint;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation(method.getName(), method.getParameterTypes(), args);
        tell(rpcInvocation);
        return null;
    }

    private void tell(RpcInvocation rpcInvocation) {
        rpcEndpoint.tell(rpcInvocation, ActorRef.noSender());
    }
}
