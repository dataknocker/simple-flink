package com.dataknocker.flink.streaming.api.graph;

import java.io.Serializable;
import java.util.Objects;

/**
 * graph边
 */
public class StreamEdge implements Serializable {
    private static final long serialVersionUID = 1L;
    private String edgeId;
    private int sourceId;
    private int targetId;

    public StreamEdge(StreamNode sourceVertex, StreamNode targetVertex) {
        this.sourceId = sourceVertex.getId();
        this.targetId = targetVertex.getId();
        this.edgeId = sourceVertex + "_" + targetVertex;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getTargetId() {
        return targetId;
    }

    public String getEdgeId() {
        return edgeId;
    }

    public int hashCode() {
        return Objects.hashCode(edgeId);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StreamEdge that = (StreamEdge) o;
        return Objects.equals(edgeId, that.edgeId);
    }

    public String toString() {
        return edgeId;
    }
}
