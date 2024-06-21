package com.dataknocker.flink.streaming.runtime.streamrecord;

/**
 * 流里元素，record和watermark
 */
public abstract class StreamElement {
    public boolean isWatermark() {
        return false;
    }

    public boolean isRecord() {
        return getClass() == StreamRecord.class;
    }
}
