package com.dataknocker.flink.runtime.jobgraph.tasks;

import com.dataknocker.flink.runtime.execution.Environment;

/**
 * 用户实现的 Invokable基类
 */
public abstract class AbstractInvokable {

    private Environment environment;

    public AbstractInvokable(Environment environment) {
        this.environment = environment;
    }

    public abstract void invoke() throws Exception;

    public abstract void init() throws Exception ;

    public void restore() throws Exception {}
}
