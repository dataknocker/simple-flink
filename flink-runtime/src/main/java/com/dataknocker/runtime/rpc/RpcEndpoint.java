package com.dataknocker.runtime.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rpc主服务
 */
public abstract class RpcEndpoint implements RpcGateway{

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected RpcService rpcService;

    protected RpcServer rpcServer;
    protected String endpointId;

    public RpcEndpoint(RpcService rpcService, String endpointId) {
        this.endpointId = endpointId;
        this.rpcService = rpcService;
        this.rpcServer = this.rpcService.startServer(this);
    }

    public String getEndpointId() {
        return endpointId;
    }

}
