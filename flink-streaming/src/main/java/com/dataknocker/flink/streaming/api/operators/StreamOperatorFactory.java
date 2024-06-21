package com.dataknocker.flink.streaming.api.operators;

import java.io.Serializable;

public interface StreamOperatorFactory<OUT> extends Serializable {

    <T extends StreamOperator<OUT>> T createStreamOperator(StreamOperatorParameters<OUT> parameters);
}
