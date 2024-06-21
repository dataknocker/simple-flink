package com.dataknocker.flink.streaming.runtime.tasks;

import com.dataknocker.flink.streaming.api.operators.StreamOperator;

/**
 * 专门用于链式operatorchain的op封装
 * @param <OUT>
 * @param <OP>
 */
public class StreamOperatorWrapper<OUT, OP extends StreamOperator<OUT>> {

    private OP wrapped;

    private boolean isHead;

    private StreamOperatorWrapper<?, ?> previous;

    private StreamOperatorWrapper<?, ?> next;



    public StreamOperatorWrapper(OP wrapped, boolean isHead) {
        this.wrapped = wrapped;
        this.isHead = isHead;
    }

    public OP getStreamOperator() {
        return wrapped;
    }

    public void setPrevious(StreamOperatorWrapper<?, ?> previous) {
        this.previous = previous;
    }

    public void setNext(StreamOperatorWrapper<?, ?> next) {
        this.next = next;
    }

    public StreamOperatorWrapper<?, ?> getPrevious() {
        return previous;
    }

    public StreamOperatorWrapper<?, ?> getNext() {
        return next;
    }

}
