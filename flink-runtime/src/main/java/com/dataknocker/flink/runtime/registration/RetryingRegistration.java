package com.dataknocker.flink.runtime.registration;

import com.dataknocker.flink.runtime.rpc.RpcGateway;
import com.dataknocker.flink.runtime.rpc.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 可重试的和远端服务进行连接注册。
 * 主要调用rpcservice的connect功能
 * TODO 需要声明state
 */
public abstract class RetryingRegistration<G extends RpcGateway> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final static int RETRY_WAIT_TIME = 10;

    private RpcService rpcService;

    private String targetName;

    private String targetAddress;

    private Class<G> targetType;


    public RetryingRegistration(RpcService rpcService, String targetName, Class<G> targetType, String targetAddress) {
        this.rpcService = rpcService;
        this.targetName = targetName;
        this.targetType = targetType;
        this.targetAddress = targetAddress;
    }

    public void startRegistration() {
        rpcService.connect(targetAddress, targetType)
                .whenCompleteAsync((gateway, throwable) -> {
                            if (throwable != null) {
                                logger.error("Could not resolve {} address {}. Retry later after {}s.", targetName, targetAddress, RETRY_WAIT_TIME);
                                startRegistrationLater();
                            } else {
                                logger.info("Resolved {} address {}, beginning registration.", targetName, targetAddress);
                                invokeRegistration(gateway);
                            }
                        }
                        //TODO 为什么要使用akka的executor?
                        , rpcService.getExecutor());

    }

    public abstract void invokeRegistration(G gateway);



    public void startRegistrationLater() {
        rpcService.getExecutorService().schedule(this::startRegistration, RETRY_WAIT_TIME, TimeUnit.SECONDS);
    }


}
