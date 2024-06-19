package com.dataknocker.runtime.jobgraph.tasks;

/**
 * 用户实现的 Invokable基类
 */
public abstract class AbstractInvokable {

    public abstract void invoke() throws Exception;
}
