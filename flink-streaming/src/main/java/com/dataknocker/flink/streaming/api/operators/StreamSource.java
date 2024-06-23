package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.streaming.api.functions.source.SourceFunction;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

/**
 * source类型的operator
 * @param <OUT>
 * @param <SRC>
 */
public class StreamSource<OUT, SRC extends SourceFunction<OUT>> extends AbstractUdfStreamOperator<OUT, SRC>{
    private transient SourceFunction.SourceContext<OUT> ctx;

    public StreamSource(SRC userFunction) {
        super(userFunction);
    }

    public void run() {
        this.ctx = StreamSourceContexts.getSourceContext(output);
        try {
            this.userFunction.run(this.ctx);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public void close() throws Exception {

    }
}
