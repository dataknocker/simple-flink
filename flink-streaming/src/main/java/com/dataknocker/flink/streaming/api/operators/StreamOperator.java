package com.dataknocker.flink.streaming.api.operators;

import java.io.Serializable;

/**
 * 算子操作接口
 * @param <OUT>
 */
public interface StreamOperator<OUT> extends Serializable {

    void open() throws Exception;

    void close() throws Exception;
}
