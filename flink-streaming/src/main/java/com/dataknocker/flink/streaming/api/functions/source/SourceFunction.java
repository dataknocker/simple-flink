package com.dataknocker.flink.streaming.api.functions.source;

import com.dataknocker.flink.api.common.functions.Function;

/**
 * source类型的function
 * @param <T>
 */
public interface SourceFunction<T> extends Function {

    void run(SourceContext<T> ctx) throws Exception;

    void cancel();

    interface SourceContext<T> {
        void collect(T t);
    }
}
