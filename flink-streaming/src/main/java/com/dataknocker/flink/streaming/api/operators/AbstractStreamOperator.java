package com.dataknocker.flink.streaming.api.operators;


import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractStreamOperator<OUT> implements StreamOperator<OUT>, SetupableStreamOperator<OUT>, Serializable {
    private static final long serialVersionUID = 1L;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected transient Output<StreamOperator<OUT>> output;

    public void setup(Output<StreamOperator<OUT>> output) {

    }
}
