package com.dataknocker.runtime.rpc;

public abstract class RpcEndpoint implements RpcGateway{

    private RpcService rpcService;

    private RpcServer rpcServer;
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
