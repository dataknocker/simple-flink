package com.dataknocker.flink.streaming.api.operators;

import com.dataknocker.flink.streaming.api.functions.source.SourceFunction;
import com.dataknocker.flink.streaming.runtime.streamrecord.StreamRecord;

/**
 * 根据eventtime等类型获得相应的sourcecontext
 */
public class StreamSourceContexts {

    public static <OUT> SourceFunction.SourceContext<OUT> getSourceContext(Output<StreamRecord<OUT>> output) {
        return new NonTimestampContext<>(output);
    }

    private static class NonTimestampContext<T> implements SourceFunction.SourceContext<T> {

        private Output<StreamRecord<T>> output;

        private StreamRecord<T> reuse;

        public NonTimestampContext(Output<StreamRecord<T>> output) {
            this.output = output;
            this.reuse = new StreamRecord<>(null);
        }

        public void collect(T element) {
            //TODO 源码这里为什么要加锁?
            output.collect(reuse.replace(element));
        }


   }
}
