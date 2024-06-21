package com.dataknocker.flink.runtime.rpc;

import java.io.Serializable;

/**
 * Rpc调用消息, 包含请求方法以及方法参数
 * 因为要序列化，所以不能直接用Method
 */
public class RpcInvocation implements Serializable {
    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] args;

    public RpcInvocation(String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }
}
