package com.dataknocker.flink.runtime.rpc;

import akka.actor.AbstractActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 处理给endpoint服务的方法调用，根据请求方法名找到endpoint服务对应方法，并调用。
 * @param <T>
 */
public class AkkaRpcActor<T extends RpcEndpoint & RpcGateway> extends AbstractActor {
    private Logger LOG = LoggerFactory.getLogger(getClass());

    private T rpcEndpoint;

    public AkkaRpcActor(T rpcEndpoint) {
        this.rpcEndpoint = rpcEndpoint;
    }
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(this::handleMessage)
                .build();
    }

    private void handleMessage(Object message) {
        if (message instanceof RpcInvocation) {
            handleRpcMessage((RpcInvocation) message);
        }
    }

    private void handleRpcMessage(RpcInvocation rpcInvocation) {
        try {
            Method rpcMethod = lookupRpcMethod(rpcInvocation);
            rpcMethod.invoke(rpcEndpoint, rpcInvocation.getArgs());
        } catch (NoSuchMethodException e) {
            LOG.error("could not find the method {} from {}",rpcInvocation.getMethodName(), rpcEndpoint.getClass().getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Method lookupRpcMethod(RpcInvocation rpcInvocation) throws NoSuchMethodException {
        return rpcEndpoint.getClass().getMethod(rpcInvocation.getMethodName(), rpcInvocation.getParameterTypes());
    }
}
