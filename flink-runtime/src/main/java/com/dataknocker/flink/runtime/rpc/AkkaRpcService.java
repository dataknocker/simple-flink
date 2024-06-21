package com.dataknocker.flink.runtime.rpc;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.concurrent.*;

/**
 * 1、用于启动supervisor(/rpc)以及用supervisor注册各endpoint rpc actor
 * 2、通过address连接目标服务，得到actorSelection。通过proxy调用目标服务接口时转成由actorSelection.tell(RpcInvocation)传给目标服务
 * 3、做一些线程池调度
 */
public class AkkaRpcService implements RpcService {
    private static final Logger LOG = LoggerFactory.getLogger(AkkaRpcService.class);

    private static Duration TIMEOUT_DURATION = Duration.ofSeconds(5);

    private ActorSystem actorSystem;
    private Supervisor supervisor;

    private ScheduledExecutorService executorService;

    public AkkaRpcService(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
        this.supervisor = Supervisor.create(this.actorSystem);
        executorService = Executors.newScheduledThreadPool(3);
    }

    @Override
    public <C extends RpcGateway> CompletableFuture<C> connect(String address, Class<C> clazz) {
        return actorSystem.actorSelection(address)
                .resolveOne(TIMEOUT_DURATION)
                .toCompletableFuture()
                .exceptionally(error -> {
                    throw new RuntimeException(String.format("Could not connect to rpc endpoint of %s under address %s.", clazz.getName(), address), error);
                })
                .thenApply(actorRef -> {
                    InvocationHandler invocationHandler = new AkkaInvocationHandler(actorRef);
                    return (C) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{clazz}, invocationHandler);
                });
    }

    @Override
    public <C extends RpcEndpoint & RpcGateway> RpcServer startServer(C rpcEndpoint) {
        Supervisor.ActorRegistration registration = registerAkkaRpcActor(rpcEndpoint);
        ActorRef actorRef = registration.getActorRef();
        String address = AkkaUtils.getAkkaURL(actorSystem, actorRef);
        LOG.info("Starting RPC endpoint for {} at {} ", rpcEndpoint.getClass().getName(), address);
        return new AkkaRpcServer(actorRef, address);
    }

    @Override
    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public Executor getExecutor() {
        return actorSystem.dispatcher();
    }

    private <C extends RpcEndpoint & RpcGateway> Supervisor.ActorRegistration registerAkkaRpcActor(C rpcEndpoint) {
        //通过propsFactory将endpoint对象传给akkaRpcActor对象
        Supervisor.StartAkkaRpcActorResponse response = supervisor.startAkkaRpcActor(
                ()-> Props.create(AkkaRpcActor.class, rpcEndpoint)
        , rpcEndpoint.getEndpointId());
        return response.orElseThrow(cause ->
                new AkkaRpcRuntimeException(
                        String.format("could not create akka rpc server for %s", rpcEndpoint.getEndpointId()),
                        cause));
    }

    public static class AkkaRpcRuntimeException extends RuntimeException {

        public AkkaRpcRuntimeException(String message) {
            super(message);
        }

        public AkkaRpcRuntimeException(Throwable cause) {
            super(cause);
        }

        public AkkaRpcRuntimeException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
