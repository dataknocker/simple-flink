package com.dataknocker.runtime.tasks;

import java.io.Serializable;

public interface StreamOperator<Out> extends Serializable {
    void open() throws Exception;

    void close() throws Exception;

}
