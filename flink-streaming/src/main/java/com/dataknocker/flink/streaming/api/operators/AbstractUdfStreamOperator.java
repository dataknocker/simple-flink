package com.dataknocker.flink.streaming.api.operators;


import com.dataknocker.flink.api.common.functions.Function;

/**
 * 带用户自定义函数的流操作符
 * @param <OUT>
 * @param <F>
 */
public abstract class AbstractUdfStreamOperator<OUT, F extends Function> extends AbstractStreamOperator<OUT> {

    private static final long serialVersionUID = 1L;
    protected F userFunction;

    public AbstractUdfStreamOperator(F userFunction) {
        this.userFunction = userFunction;
    }

    public F getUserFunction() {
        return userFunction;
    }

    @Override
    public void open() throws Exception {
    }

    @Override
    public void close() throws Exception {

    }
}
